package com.changxiao.themeskin.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * File util used for application. Help to read and save files.
 * 
 * @author Midas.
 * @version 1.0
 * @createDate 2015-11-03
 * @lastUpdate 2016-01-09
 */
public class FileUtils {

	private static String TAG = "ZRFileUtils";

	private static final String PACKAGE_NAME = "com.midas/";
	private static final String DATA_DIRECTORY = "/data/" + PACKAGE_NAME;
	private static final String SDCARD_DIRECTORY = "/Android" + DATA_DIRECTORY;

	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";

	public static final int PATH_SDCARD = 0;
	public static final int PATH_DATA = 1;

	public static String getWorkFolder() {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			return getWorkFolder(PATH_SDCARD);
		} else {
			return getWorkFolder(PATH_DATA);
		}
	}

	public static String getWorkFolder(int path) {
		if (PATH_DATA == path) {
			return Environment.getDataDirectory() + DATA_DIRECTORY;
		} else {
			return Environment.getExternalStorageDirectory() + SDCARD_DIRECTORY;
		}
	}

	public static String getImageFolder(String imageFloderName) {
		String imageFolder = getWorkFolder() + imageFloderName;
		File file = new File(imageFolder);
		if (!file.exists())
			file.mkdirs();
		return imageFolder;
	}

	/**
	 * Get free size in target path.
	 * 
	 * @param path
	 *            should be {@link #PATH_DATA} or {@link #PATH_SDCARD}.
	 * @return <b>long</b> available size of the specified directory.
	 */
	public static long getStorageFreeSize(int path) {
		File directory = null;
		if (PATH_DATA == path) {
			directory = Environment.getDataDirectory();
		} else {
			directory = Environment.getExternalStorageDirectory();
		}
		return getStorageFreeSize(directory.getPath());
	}

	/**
	 * Get free size in target path.
	 * 
	 * @return <b>long</b> available size of the specified directory.
	 */
	public static long getStorageFreeSize(String path) {
		File f = new File(path);
		if (!f.isDirectory() || !f.exists()) {
			return 0;
		}
		StatFs stat = new StatFs(path);
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	public static File getCaptureFile() {
		File captureFile = new File(FileUtils.getImageFolder("image/"),
				SystemClock.elapsedRealtime() + "_capture.jpg");
		return captureFile;
	}

	/**
	 * Read file.
	 * 
	 * @param file
	 * @return <b>String</b> String with file content
	 * @throws IOException
	 */
	public static String readFile(File file) throws IOException {
		return readStream(new FileInputStream(file));
	}

	/**
	 * Read InputStream.
	 * 
	 * @param is
	 * @return <b>String</b> String with InputStream content
	 * @throws IOException
	 */
	public static String readStream(InputStream is) throws IOException {
		InputStreamReader isr = null;
		StringWriter sw = new StringWriter();
		try {
			isr = new InputStreamReader(is, "UTF-8");
			char[] buffer = new char[1024];
			int length = -1;
			while (-1 != (length = isr.read(buffer))) {
				sw.write(buffer, 0, length);
			}
			return sw.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != isr) {
				isr.close();
			}
			sw.close();
		}
	}

	/**
	 * Save file.
	 * 
	 * @param content
	 *            File content
	 * @param file
	 *            Target file
	 * @return true if success.
	 */
	public static boolean saveFile(String content, File file) {
		try {
			return saveFile(
					new ByteArrayInputStream(content.getBytes("UTF-8")), file);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Save file.
	 * 
	 * @param is
	 *            File content
	 * @param file
	 *            Target file
	 * @return true if success.
	 */
	public static boolean saveFile(InputStream is, File file) {
		if (!checkFile(file)) {
			return false;
		}

		OutputStreamWriter osw = null;
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(is, "UTF-8");
			osw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			char[] buffer = new char[1024];
			int length = -1;
			while (-1 != (length = isr.read(buffer))) {
				osw.write(buffer, 0, length);
			}
			osw.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != osw) {
				try {
					osw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != isr) {
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static boolean deleteFile(File file) {
		return file.delete();
	}

	public static void delFile(String path) {
		File file = new File(path);
		if (file.isFile()) {
			file.delete();
		}
		file.exists();
	}

	public static void deleteDir(String path) {
		File dir = new File(path);
		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			return;
		}
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File _dir = dir.getParentFile();
				if (_dir == null || !_dir.exists() || !_dir.isDirectory()) {
					return;
				}
			}
		}
		dir.delete();
	}

	public static boolean checkFile(File file) {
		File parent = file.getParentFile();
		if (null != parent && !parent.exists() && !parent.mkdirs()) {
			return false;
		}

		if (file.exists() && !file.delete()) {
			return false;
		}

		try {
			return file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Uncompress zip file to target folder.
	 * 
	 * @param zipFilePath
	 *            The path of zip file.
	 * @param folderPath
	 *            The path of target folder.
	 * @throws IOException
	 */
	public static void upZipFile(String zipFilePath, String folderPath)
			throws IOException {
		File zipFile = new File(zipFilePath);
		File desDir = new File(folderPath);
		if (!desDir.exists()) {
			desDir.mkdirs();
		}
		ZipFile zf = null;

		OutputStream out = null;
		InputStream in = null;
		try {
			zf = new ZipFile(zipFile);
			for (Enumeration<?> entries = zf.entries(); entries
					.hasMoreElements();) {
				ZipEntry entry = ((ZipEntry) entries.nextElement());
				in = zf.getInputStream(entry);
				String str = folderPath + File.separator + entry.getName();
				str = new String(str.getBytes("8859_1"), "GB2312");
				File desFile = new File(str);
				if (desFile.exists()) {
					desFile.delete();
				}
				File fileParentDir = desFile.getParentFile();
				if (!fileParentDir.exists()) {
					fileParentDir.mkdirs();
				}
				desFile.createNewFile();

				out = new FileOutputStream(desFile);
				byte buffer[] = new byte[1024];
				int realLength;
				while ((realLength = in.read(buffer)) > 0) {
					out.write(buffer, 0, realLength);
				}
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (null != zf) {
				zf.close();
			}
			if (null != in) {
				in.close();
			}
			if (null != out) {
				out.close();
			}
		}
	}

	public static boolean saveBitmap(String path, Bitmap bm) {
		ZRLog.d(TAG, " start save bitmap...");
		FileOutputStream out = null;
		try {
			File f = new File(path);
			checkFile(f);
			if (f.exists()) {
				f.delete();
			}
			out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 100, out);
			ZRLog.d(TAG, "save bitmap success!");
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != out) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 获取圆角位图的方法
	 * 
	 * @param bitmap
	 *            需要转化成圆角的位图
	 * @param pixels
	 *            圆角的度数，数值越大，圆角越大
	 * @return 处理后的圆角位图
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = pixels;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 * {@link ZRAsyncImageLoader class}
	 * 
	 * @param path
	 * @return
	 */
	public static byte[] decodeBitmap(String path) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
		BitmapFactory.decodeFile(path, opts);
		opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);
		opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		opts.inDither = false;
		opts.inPurgeable = true;
		opts.inTempStorage = new byte[16 * 1024];
		FileInputStream is = null;
		Bitmap bmp = null;
		Bitmap bmp2 = null;
		ByteArrayOutputStream baos = null;
		try {
			is = new FileInputStream(path);
			bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
			double scale = getScaling(opts.outWidth * opts.outHeight,
					1024 * 600);
			bmp2 = Bitmap.createScaledBitmap(bmp,
					(int) (opts.outWidth * scale),
					(int) (opts.outHeight * scale), true);
			// bmp.recycle();
			baos = new ByteArrayOutputStream();
			bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			// bmp2.recycle();
			return baos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (!bmp.isRecycled())
					bmp.recycle();
				if (!bmp2.isRecycled())
					bmp2.recycle();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.gc();
		}
		return baos.toByteArray();
	}

	private static double getScaling(int src, int des) {
		/**
		 * 48 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比 49
		 */
		double scale = Math.sqrt((double) des / (double) src);
		return scale;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static File createTmpFile(Context context) throws IOException {
		File dir = null;
		if (TextUtils.equals(Environment.getExternalStorageState(),
				Environment.MEDIA_MOUNTED)) {
			dir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
			if (!dir.exists()) {
				dir = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM
								+ "/Camera");
				if (!dir.exists()) {
					dir = getCacheDirectory(context, true);
				}
			}
		} else {
			dir = getCacheDirectory(context, true);
		}
		return File.createTempFile(JPEG_FILE_PREFIX, JPEG_FILE_SUFFIX, dir);
	}

	private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

	/**
	 * Returns application cache directory. Cache directory will be created on
	 * SD card <i>("/Android/data/[app_package_name]/cache")</i> if card is
	 * mounted and app has appropriate permission. Else - Android defines cache
	 * directory on device's file system.
	 *
	 * @param context
	 *            Application context
	 * @return Cache {@link File directory}.<br />
	 *         <b>NOTE:</b> Can be null in some unpredictable cases (if SD card
	 *         is unmounted and {@link Context#getCacheDir()
	 *         Context.getCacheDir()} returns null).
	 */
	public static File getCacheDirectory(Context context) {
		return getCacheDirectory(context, true);
	}

	/**
	 * Returns application cache directory. Cache directory will be created on
	 * SD card <i>("/Android/data/[app_package_name]/cache")</i> (if card is
	 * mounted and app has appropriate permission) or on device's file system
	 * depending incoming parameters.
	 *
	 * @param context
	 *            Application context
	 * @param preferExternal
	 *            Whether prefer external location for cache
	 * @return Cache {@link File directory}.<br />
	 *         <b>NOTE:</b> Can be null in some unpredictable cases (if SD card
	 *         is unmounted and {@link Context#getCacheDir()
	 *         Context.getCacheDir()} returns null).
	 */
	public static File getCacheDirectory(Context context, boolean preferExternal) {
		File appCacheDir = null;
		String externalStorageState;
		try {
			externalStorageState = Environment.getExternalStorageState();
		} catch (NullPointerException e) { // (sh)it happens (Issue #660)
			externalStorageState = "";
		} catch (IncompatibleClassChangeError e) { // (sh)it happens too (Issue
			// #989)
			externalStorageState = "";
		}
		if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState)
				&& hasExternalStoragePermission(context)) {
			appCacheDir = getExternalCacheDir(context);
		}
		if (appCacheDir == null) {
			appCacheDir = context.getCacheDir();
		}
		if (appCacheDir == null) {
			String cacheDirPath = "/data/data/" + context.getPackageName()
					+ "/cache/";
			appCacheDir = new File(cacheDirPath);
		}
		return appCacheDir;
	}

	private static File getExternalCacheDir(Context context) {
		File dataDir = new File(new File(
				Environment.getExternalStorageDirectory(), "Android"), "data");
		File appCacheDir = new File(
				new File(dataDir, context.getPackageName()), "cache");
		if (!appCacheDir.exists()) {
			if (!appCacheDir.mkdirs()) {
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();
			} catch (IOException e) {
			}
		}
		return appCacheDir;
	}

	private static boolean hasExternalStoragePermission(Context context) {
		int perm = context
				.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
		return perm == PackageManager.PERMISSION_GRANTED;
	}


}
