package cn.feng.skin.manager.loader;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.View;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.feng.skin.manager.config.SkinConfig;
import cn.feng.skin.manager.entity.AttrFactory;
import cn.feng.skin.manager.entity.DynamicAttr;
import cn.feng.skin.manager.entity.SkinAttr;
import cn.feng.skin.manager.entity.SkinItem;
import cn.feng.skin.manager.util.L;
import cn.feng.skin.manager.util.ListUtils;
import cn.feng.skin.manager.util.SkinPreferencesUtils;

/**
 * Supply {@link SkinInflaterFactory} to be called when inflating from a LayoutInflater.
 *
 * <p>Use this to collect the {skin:enable="true|false"} views availabled in our XML layout files.
 *
 * @author fengjun
 */
public class SkinInflaterFactory implements Factory {

	private static final boolean DEBUG = false;

	public static SkinInflaterFactory getLayoutInflaterFactory(Activity act) {
//		if(SkinPreferencesUtils.getBoolean(act, SkinPreferencesUtils.PREF_KEY_HAS_SKIN, false)) {
			try {
				Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
				field.setAccessible(true);
				field.setBoolean(act.getLayoutInflater(), false);
				SkinInflaterFactory factory = new SkinInflaterFactory();
				act.getLayoutInflater().setFactory(factory);
				return factory;
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			Log.i("SkinInflaterFactory", "mFactorySet is null");
//		}
		return null;
	}

	/**
	 * Store the view item that need skin changing in the activity
	 */
	private ArrayList<SkinItem> mSkinItems = new ArrayList<>();

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// if this is NOT enable to be skined , simplly skip it
		boolean isSkinEnable = attrs.getAttributeBooleanValue(SkinConfig.NAMESPACE, SkinConfig.ATTR_SKIN_ENABLE, false);
		if (!isSkinEnable){
			return null;
		}

		View view = createView(context, name, attrs);

		if (view == null){
			Log.i("SkinInflaterFactory", "onCreateView's createView return null");
			return null;
		}

		parseSkinAttr(context, attrs, view);

		return view;
	}

	/**
	 * Invoke low-level function for instantiating a view by name. This attempts to
	 * instantiate a view class of the given <var>name</var> found in this
	 * LayoutInflater's ClassLoader.
	 *
	 * @param context
	 * @param name The full name of the class to be instantiated.
	 * @param attrs The XML attributes supplied for this instance.
	 *
	 * @return View The newly instantiated view, or null.
	 */
	private View createView(Context context, String name, AttributeSet attrs) {
		View view = null;
		try {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			try {  //layoutInflater 内部的mConstructorArgs[0]可能为空，这将导致传给view的构造函数的context为空
				Field constructorArgs = LayoutInflater.class.getDeclaredField("mConstructorArgs");
				constructorArgs.setAccessible(true);
				Object[] args = (Object[]) constructorArgs.get(layoutInflater);
				if (args[0] == null) {
					args[0] = context;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (-1 == name.indexOf('.')){
				if ("View".equals(name)) {
					Log.i("SkinInflaterFactory", "createView method " + name);
					view = layoutInflater.createView(name, "android.view.", attrs);
				}
				if (view == null) {
					Log.i("SkinInflaterFactory", "createView view == null1 " + name);
					view = layoutInflater.createView(name, "android.widget.", attrs);
				}
				if (view == null) {
					Log.i("SkinInflaterFactory", "createView view == null2 " + name);
					view = layoutInflater.createView(name, "android.webkit.", attrs);
				}
			}else {
				Log.i("SkinInflaterFactory", "createView -1 != name.indexOf('.') " + name);
				view = layoutInflater.createView(name, null, attrs);
			}

			L.i("about to create " + name);

		} catch (Exception e) {
			L.e("error while create 【" + name + "】 : " + e.getMessage());
			view = null;
		}
		return view;
	}

	/**
	 * Collect skin able tag such as background , textColor and so on
	 *
	 * @param context
	 * @param attrs
	 * @param view
	 */
	private void parseSkinAttr(Context context, AttributeSet attrs, View view) {
		List<SkinAttr> viewAttrs = new ArrayList<SkinAttr>();

		for (int i = 0; i < attrs.getAttributeCount(); i++){
			String attrName = attrs.getAttributeName(i);
			String attrValue = attrs.getAttributeValue(i);

			if(!AttrFactory.isSupportedAttr(attrName)){
				continue;
			}

			if(attrValue.startsWith("@")){
				try {
					int id = Integer.parseInt(attrValue.substring(1));
					String entryName = context.getResources().getResourceEntryName(id);
					String typeName = context.getResources().getResourceTypeName(id);
					SkinAttr mSkinAttr = AttrFactory.get(attrName, id, entryName, typeName);
					if (mSkinAttr != null) {
						viewAttrs.add(mSkinAttr);
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (NotFoundException e) {
					e.printStackTrace();
				}
			}
		}

		if(!ListUtils.isEmpty(viewAttrs)){
			SkinItem skinItem = new SkinItem();
			skinItem.view = new WeakReference<View>(view);
			skinItem.attrs = viewAttrs;

			mSkinItems.add(skinItem);

			if(SkinManager.getInstance().isExternalSkin()){
				skinItem.apply();
			}
		}
	}

	public void applySkin(){
		if(ListUtils.isEmpty(mSkinItems)){
			return;
		}
		for (Iterator<SkinItem> ite = mSkinItems.iterator(); ite.hasNext(); ) {
			SkinItem si = ite.next();
			if (si.view == null || si.view.get() == null) {
				ite.remove();
				continue;
			}
			si.apply();
		}
	}

	public void dynamicAddSkinEnableView(Context context, View view, List<DynamicAttr> pDAttrs){
		List<SkinAttr> viewAttrs = new ArrayList<SkinAttr>();
		SkinItem skinItem = new SkinItem();
		skinItem.view = new WeakReference<View>(view);

		for(DynamicAttr dAttr : pDAttrs){
			int id = dAttr.refResId;
			String entryName = context.getResources().getResourceEntryName(id);
			String typeName = context.getResources().getResourceTypeName(id);
			SkinAttr mSkinAttr = AttrFactory.get(dAttr.attrName, id, entryName, typeName);
			viewAttrs.add(mSkinAttr);
		}

		skinItem.attrs = viewAttrs;
		addSkinView(skinItem);
	}

	public void dynamicAddSkinEnableView(Context context, View view, String attrName, int attrValueResId){
		int id = attrValueResId;
		String entryName = context.getResources().getResourceEntryName(id);
		String typeName = context.getResources().getResourceTypeName(id);
		SkinAttr mSkinAttr = AttrFactory.get(attrName, id, entryName, typeName);
		SkinItem skinItem = new SkinItem();
		skinItem.view = new WeakReference<View>(view);
		List<SkinAttr> viewAttrs = new ArrayList<SkinAttr>();
		viewAttrs.add(mSkinAttr);
		skinItem.attrs = viewAttrs;
		addSkinView(skinItem);
	}

	public void addSkinView(SkinItem item) {
		if (item == null || item.view == null || item.view.get() == null) {
			return;
		}

		Iterator<SkinItem> iterator = mSkinItems.iterator();
		while (iterator.hasNext()) {
			SkinItem i = iterator.next();
			if (i.view != null && i.view.get() != null && i.view.get() == item.view.get()) {
				item.attrs = mergeAttrs(item.attrs, i.attrs);
				iterator.remove();
				break;
			}
		}
		mSkinItems.add(item);
		item.apply();
	}

	private List<SkinAttr> mergeAttrs(List<SkinAttr> newAttrs, List<SkinAttr> originalAttrs) {
		for (SkinAttr newAttr : newAttrs) {
			SkinAttr originAttr = getAttrByName(originalAttrs, newAttr.attrName);
			if (originAttr != null) {
				originalAttrs.remove(originAttr);
			}
			originalAttrs.add(newAttr);
		}

		return originalAttrs;
	}

	private @Nullable
	SkinAttr getAttrByName(@NonNull List<SkinAttr> attrs, @NonNull String attrName) {
		for (SkinAttr attr : attrs) {
			if (attr.attrName.equals(attrName)) {
				return attr;
			}
		}

		return null;
	}

	public void clean(){
		if(ListUtils.isEmpty(mSkinItems)){
			return;
		}

		for(SkinItem si : mSkinItems){
			if(si.view == null){
				continue;
			}
			si.clean();
		}
		mSkinItems.clear();
	}
}
