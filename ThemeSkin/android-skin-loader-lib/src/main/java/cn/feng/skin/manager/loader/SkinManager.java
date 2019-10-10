package cn.feng.skin.manager.loader;

import android.content.res.TypedArray;
import android.os.Environment;
import cn.feng.skin.manager.util.SkinPreferencesUtils;
import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import cn.feng.skin.manager.config.SkinConfig;
import cn.feng.skin.manager.listener.ILoaderListener;
import cn.feng.skin.manager.listener.ISkinLoader;
import cn.feng.skin.manager.listener.ISkinUpdate;
import cn.feng.skin.manager.util.L;

/**
 * Skin Manager Instance
 *
 * 
 * <ul>
 * <strong>global init skin manager, MUST BE CALLED FIRST ! </strong>
 * <li> {@link #init()} </li>
 * </ul>
 * <ul>
 * <strong>get single runtime instance</strong>
 * <li> {@link #getInstance()} </li>
 * </ul>
 * <ul>
 * <strong>attach a listener (Activity or fragment) to SkinManager</strong>
 * <li> {@link #onAttach(ISkinUpdate observer)} </li>
 * </ul>
 * <ul>
 * <strong>detach a listener (Activity or fragment) to SkinManager</strong>
 * <li> {@link #detach(ISkinUpdate observer)} </li>
 * </ul>
 * <ul>
 * <strong>load latest theme </strong>
 * <li> {@link #load()} </li>
 * <li> {@link #load(ILoaderListener callback)} </li>
 * </ul>
 * <ul>
 * <strong>load new theme with the giving skinPackagePath</strong>
 * <li> {@link #load(String skinPackagePath,ILoaderListener callback)} </li>
 * </ul>
 * 
 * 
 * @author fengjun
 */
public class SkinManager implements ISkinLoader{
	
	private static final String NOT_INIT_ERROR = "SkinManager MUST init with Context first";
	private static Object synchronizedLock = new Object();
	private static SkinManager instance;

	private List<ISkinUpdate> skinObservers;
	private Context context;
	private String skinPackageName;
	private Resources mResources;
	private String skinPath;
	private boolean isDefaultSkin = false;
	private String mSkinUrl;

	private static boolean mIsTestSkin = true;
	public static boolean isTestSkin(){
		return mIsTestSkin;
	}
	private static String mTestSkinFilePath = Environment.getExternalStorageDirectory().getPath() + "/SkinPack.skin";

	/**
	 * whether the skin being used is from external .skin file 
	 * @return is external skin = true
	 */
	public boolean isExternalSkin(){
		return !isDefaultSkin && mResources != null && System.currentTimeMillis() < SkinConfig.getSkinExpire(context);
	}
	
	/**
	 * get current skin path
	 * @return current skin path
	 */
	public String getSkinPath() {
		return skinPath;
	}

	/**
	 * return a global static instance of {@link SkinManager}
	 * @return
	 */
	public static SkinManager getInstance() {
		if (instance == null) {
			synchronized (synchronizedLock) {
				if (instance == null){
					instance = new SkinManager();
				}
			}
		}
		return instance;
	}
	
	public String getSkinPackageName() {
		return skinPackageName;
	}
	
	public Resources getResources(){
		return mResources;
	}
	
	private SkinManager() { }
	
	public void init(Context ctx) {
		if (null == ctx) {
			throw new NullPointerException(NOT_INIT_ERROR);
		}
		context = ctx.getApplicationContext();
	}
	
	public void restoreDefaultTheme(){
		SkinConfig.saveSkinPath(context, SkinConfig.DEFALT_SKIN);
		isDefaultSkin = true;
		mResources = context.getResources();
		notifySkinUpdate();
	}

	public void checkCurSkin() {
		if (System.currentTimeMillis() >= SkinConfig.getSkinExpire(context)) {
			restoreDefaultTheme();
		}
	}

	public void clearSkin() {
		SkinConfig.clearSkin(context);
		restoreDefaultTheme();
	}

	public void load() {
		if(isTestSkin()) {
			long expireTime = System.currentTimeMillis() + 10000000;
			SkinConfig.saveSkinExpire(context, expireTime);
			load(mTestSkinFilePath, expireTime, null);
		} else {
			if(SkinPreferencesUtils.getBoolean(context, SkinPreferencesUtils.PREF_KEY_HAS_SKIN, false)) {
				if (System.currentTimeMillis() >= SkinConfig.getSkinExpire(context)) {
					restoreDefaultTheme();
				}
				String skin = SkinConfig.getCustomSkinPath(context);
				load(skin, SkinConfig.getSkinExpire(context), null);
			}
		}
	}
	
	/**
	 * Load resources from apk in asyc task
	 *
	 * @param skinPackagePath path of skin apk
	 * @param expireTime
	 * @param callback        callback to notify user
	 */
	public void load(final String skinPackagePath, final long expireTime, final ILoaderListener callback) {
//		if (skinPackagePath != null && skinPackagePath.equals(mSkinUrl)) {
//			return;
//		}

		new LoadTask(this, context, expireTime, skinPackagePath, callback).execute();
	}

	private static class LoadTask extends AsyncTask<String, Void, Resources> {
		private SkinManager mSkinManager;
		private WeakReference<Context> mContextRef;
		private long mExpireTime;
		private String mSkinPackagePath;
		private ILoaderListener mCallback;


		LoadTask(SkinManager skinManager, Context context, long expireTime,
				String skinPackagePath, ILoaderListener callback) {
			mSkinManager = skinManager;
			mContextRef = new WeakReference<>(context);
			mExpireTime = expireTime;
			mSkinPackagePath = skinPackagePath;
			mCallback = callback;
		}

		@Override
		protected void onPreExecute() {
			if (mCallback != null) {
				mCallback.onStart();
			}
		}

		@Override
		protected Resources doInBackground(String... params) {
			Context context = mContextRef.get();
			if (context == null) {
				return null;
			}

			try {
				File file = new File(mSkinPackagePath);
				if(file == null || !file.exists()){
					return null;
				}

				PackageManager mPm = context.getPackageManager();
				PackageInfo mInfo = mPm.getPackageArchiveInfo(mSkinPackagePath, PackageManager.GET_ACTIVITIES);
				mSkinManager.skinPackageName = mInfo.packageName;

				AssetManager assetManager = AssetManager.class.newInstance();
				Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
				addAssetPath.invoke(assetManager, mSkinPackagePath);

				Resources superRes = context.getResources();
				Resources skinResource = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());

				SkinConfig.saveSkinPath(context, mSkinPackagePath);
				SkinConfig.saveSkinExpire(context, mExpireTime);

				mSkinManager.skinPath = mSkinPackagePath;
				mSkinManager.isDefaultSkin = false;
				return skinResource;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		protected void onPostExecute(Resources result) {
			mSkinManager.mResources = result;

			if (mSkinManager.mResources != null) {
				mSkinManager.mSkinUrl = mSkinPackagePath;
				if (mCallback != null) mCallback.onSuccess();
				mSkinManager.notifySkinUpdate();
			} else {
				mSkinManager.isDefaultSkin = true;
				if (mCallback != null) mCallback.onFailed();
			}
		}
	}
	
	@Override
	public void attach(ISkinUpdate observer) {
		if(skinObservers == null){
			skinObservers = new ArrayList<ISkinUpdate>();
		}
		if(!skinObservers.contains(skinObservers)){
			skinObservers.add(observer);
		}
	}

	@Override
	public void detach(ISkinUpdate observer) {
		if(skinObservers == null) return;
		if(skinObservers.contains(observer)){
			skinObservers.remove(observer);
		}
	}

	@Override
	public void notifySkinUpdate() {
		if(skinObservers == null) return;
		for(ISkinUpdate observer : skinObservers){
			observer.onThemeUpdate();
		}
	}
	
	public int getColor(int resId){
		int originColor = 0;
		try {
			originColor = context.getResources().getColor(resId);
		} catch (Exception e) {
			e.printStackTrace();

//			Activity activity = BaseApplication.getTopActivity(); // 获取失败时尝试通过topActivity获取
//			if (activity != null) {
//				try {
//					originColor = activity.getResources().getColor(resId);
//				} catch (Exception e2) {
//					e2.printStackTrace();
//				}
//			}
		}

		if(mResources == null || isDefaultSkin){
			return originColor;
		}
		
		String resName = context.getResources().getResourceEntryName(resId);
		
		int trueResId = mResources.getIdentifier(resName, "color", skinPackageName);
		int trueColor = 0;
		
		try{
			trueColor = mResources.getColor(trueResId);
		}catch(NotFoundException e){
			e.printStackTrace();
			trueColor = originColor;
		}
		
		return trueColor;
	}

	public int getDimen(int resId) {
		int originDimen = 0;
		try {
			originDimen = context.getResources().getDimensionPixelSize(resId);
		}catch (Exception e){
			e.printStackTrace();

//			Activity activity = BaseApplication.getTopActivity();   //获取失败时尝试通过topActivity获取
//			if(activity != null){
//				try {
//					originDimen = activity.getResources().getDimensionPixelSize(resId);
//				}catch (Exception e2){
//					e2.printStackTrace();
//				}
//			}
		}

		if (mResources == null || isDefaultSkin) {
			return originDimen;
		}

		String resName = context.getResources().getResourceEntryName(resId);

		int trueResId = mResources.getIdentifier(resName, "dimen", skinPackageName);
		try {
			return mResources.getDimensionPixelSize(trueResId);
		} catch(NotFoundException e) {
			e.printStackTrace();
		}
		return context.getResources().getDimensionPixelSize(resId);
	}

	public boolean getBoolean(int resId) {
		boolean originBoolean = false;

		try {
			originBoolean = context.getResources().getBoolean(resId);
		} catch (Exception e) {
			e.printStackTrace();

//			Activity activity = BaseApplication.getTopActivity();   //获取失败时尝试通过topActivity获取
//			if(activity != null){
//				try {
//					originBoolean = activity.getResources().getBoolean(resId);
//				}catch (Exception e2){
//					e2.printStackTrace();
//				}
//			}
		}

		if (mResources == null || isDefaultSkin) {
			return originBoolean;
		}

		String resName = context.getResources().getResourceEntryName(resId);
		int trueResId = mResources.getIdentifier(resName, "bool", skinPackageName);
		try {
			return mResources.getBoolean(trueResId);
		} catch(NotFoundException e) {
			e.printStackTrace();
		}
		return context.getResources().getBoolean(resId);
	}

	public int getInt(int resId) {
		int originInt = 0;

		try {
			originInt = context.getResources().getInteger(resId);
		} catch (Exception e) {
			e.printStackTrace();

//			Activity activity = BaseApplication.getTopActivity();   //获取失败时尝试通过topActivity获取
//			if(activity != null){
//				try {
//					originInt = activity.getResources().getInteger(resId);
//				}catch (Exception e2){
//					e2.printStackTrace();
//				}
//			}
		}

		if (mResources == null || isDefaultSkin) {
			return originInt;
		}

		String resName = context.getResources().getResourceEntryName(resId);
		int trueResId = mResources.getIdentifier(resName, "integer", skinPackageName);
		try {
			return mResources.getInteger(trueResId);
		} catch(NotFoundException e) {
			e.printStackTrace();
		}
		return context.getResources().getInteger(resId);
	}

	public int[] getDrawableRefArray(int resId) {
		if (mResources == null || isDefaultSkin) {
			try {
				TypedArray array = context.getResources().obtainTypedArray(resId);
				if (array.length() > 0) {
					int[] refs = new int[array.length()];
					for (int i = 0; i < array.length(); i++) {
						refs[i] = array.getResourceId(i, 0);
					}
					array.recycle();
					return refs;
				}
				array.recycle();
			} catch (NotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}

		if (mResources == null || isDefaultSkin) {
			return null;
		}
		String resName = context.getResources().getResourceEntryName(resId);
		int trueResId = mResources.getIdentifier(resName, "array", skinPackageName);
		try {
			TypedArray array = mResources.obtainTypedArray(trueResId);
			if (array.length() > 0) {
				int[] refs = new int[array.length()];
				for (int i = 0; i < array.length(); i++) {
					refs[i] = array.getResourceId(i, 0);
				}
				array.recycle();
				return refs;
			}
			array.recycle();
		} catch(NotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressLint("NewApi")
	public Drawable getDrawable(int resId){
		Drawable originDrawable = null;
		try {
			originDrawable = context.getResources().getDrawable(resId);
		} catch (Exception e) {
			e.printStackTrace();

//			Activity activity = BaseApplication.getTopActivity();   //获取失败时尝试通过topActivity获取
//			if(activity != null){
//				try {
//					originDrawable = activity.getResources().getDrawable(resId);
//				}catch (Exception e2){
//					e2.printStackTrace();
//				}
//			}
		}

		if(mResources == null || isDefaultSkin){
			return originDrawable;
		}
		String resName = context.getResources().getResourceEntryName(resId);
		
		int trueResId = mResources.getIdentifier(resName, "drawable", skinPackageName);
		
		Drawable trueDrawable = null;
		try{
			L.e("ttgg", "SDK_INT = " + android.os.Build.VERSION.SDK_INT);
			if(android.os.Build.VERSION.SDK_INT < 22){
				trueDrawable = mResources.getDrawable(trueResId);
			}else{
				trueDrawable = mResources.getDrawable(trueResId, null);
			}
		}catch(NotFoundException e){
			e.printStackTrace();
			trueDrawable = originDrawable;
		}
		if(trueDrawable == null) {
			trueDrawable = originDrawable;
		}
		
		return trueDrawable;
	}

	public Drawable getDrawableFromSkinPack(int resId) {
		if (mResources != null) {
			try {
				if (android.os.Build.VERSION.SDK_INT < 22){
					return mResources.getDrawable(resId);
				} else {
					return mResources.getDrawable(resId, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 加载指定资源颜色drawable,转化为ColorStateList，保证selector类型的Color也能被转换。</br>
	 * 无皮肤包资源返回默认主题颜色
	 * @author pinotao
	 * @param resId 资源id
	 * @return 资源id转化成的ColorStateList
	 */
	public ColorStateList convertToColorStateList(int resId) {
		L.e("attr1", "convertToColorStateList");
		
		boolean isExtendSkin = true;
		
		if (mResources == null || isDefaultSkin) {
			isExtendSkin = false;
		}
		
		String resName = context.getResources().getResourceEntryName(resId);
		L.e("attr1", "resName = " + resName);
		if (isExtendSkin) {
			L.e("attr1", "isExtendSkin");
			int trueResId = mResources.getIdentifier(resName, "color", skinPackageName);
			L.e("attr1", "trueResId = " + trueResId);
			ColorStateList trueColorList = null;
			if (trueResId == 0) { // 如果皮肤包没有复写该资源，但是需要判断是否是ColorStateList
				try {
					ColorStateList originColorList = context.getResources().getColorStateList(resId);
					return originColorList;
				} catch (NotFoundException e) {
					e.printStackTrace();
					L.e("resName = " + resName + " NotFoundException : "+ e.getMessage());
				}
			} else {
				try {
					trueColorList = mResources.getColorStateList(trueResId);
					L.e("attr1", "trueColorList = " + trueColorList);
					return trueColorList;
				} catch (NotFoundException e) {
					e.printStackTrace();
					L.w("resName = " + resName + " NotFoundException :" + e.getMessage());
				}
			}
		} else {
			try {
				ColorStateList originColorList = context.getResources().getColorStateList(resId);
				return originColorList;
			} catch (NotFoundException e) {
				e.printStackTrace();
				L.w("resName = " + resName + " NotFoundException :" + e.getMessage());
			}

		}

		int[][] states = new int[1][1];
		return new ColorStateList(states, new int[] { context.getResources().getColor(resId) });
	}
}