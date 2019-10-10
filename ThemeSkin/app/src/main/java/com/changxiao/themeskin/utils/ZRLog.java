package com.changxiao.themeskin.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Log util used for application.
 * 
 * @author gufei
 * @version 1.0
 * @createDate 2014-07-23
 * @lastUpdate 2014-07-23
 */
public class ZRLog {

	private static final String TAG = "quickframe";
	private static final String LOG_FILE_PREFIX = "quickframe_" + DeviceInfo.getClientVersionName() + "_log";
	private static final String CRASH_FILE_PREFIX = "quickframe_" + DeviceInfo.getClientVersionName() + "_crash";

	private static final int LOG_FILE_SIZE = 1024 * 1024 * 5;
	private static final int LOG_FILE_MAX_COUNT = 8;

	private static final int FILE_TYPE_LOG = 0;
	private static final int FILE_TYPE_CRASH = 1;

	private static File sCurrentLogFile = null;
	private static File sCurrentCrashFile = null;

	public static final void v(String tag, String msg) {
		doLog(Log.VERBOSE, tag, msg, null);
	}

	public static final void d(String tag, String msg) {
		doLog(Log.DEBUG, tag, msg, null);
	}

	public static final void i(String tag, String msg) {
		doLog(Log.INFO, tag, msg, null);
	}

	public static final void w(String tag, String msg) {
		doLog(Log.WARN, tag, msg, null);
	}

	public static final void e(String tag, String msg) {
		doLog(Log.ERROR, tag, msg, null);
	}

	public static final void v(String tag, String msg, Throwable ex) {
		doLog(Log.VERBOSE, tag, msg, ex);
	}

	public static final void d(String tag, String msg, Throwable ex) {
		doLog(Log.DEBUG, tag, msg, ex);
	}

	public static final void i(String tag, String msg, Throwable ex) {
		doLog(Log.INFO, tag, msg, ex);
	}

	public static final void w(String tag, String msg, Throwable ex) {
		doLog(Log.WARN, tag, msg, ex);
	}

	public static final void e(String tag, String msg, Throwable ex) {
		doLog(Log.ERROR, tag, msg, ex);
	}

	public static final void v(String msg) {
		v(TAG, msg, null);
	}

	public static final void d(String msg) {
		d(TAG, msg, null);
	}

	public static final void i(String msg) {
		i(TAG, msg, null);
	}

	public static final void w(String msg) {
		w(TAG, msg, null);
	}

	public static final void e(String msg) {
		e(TAG, msg, null);
	}

	public static final void v(String msg, Throwable ex) {
		v(TAG, msg, ex);
	}

	public static final void d(String msg, Throwable ex) {
		d(TAG, msg, ex);
	}

	public static final void i(String msg, Throwable ex) {
		i(TAG, msg, ex);
	}

	public static final void w(String msg, Throwable ex) {
		w(TAG, msg, ex);
	}

	public static final void e(String msg, Throwable ex) {
		e(TAG, msg, ex);
	}

	private static final void doLog(int level, String tag, String msg,
                                    Throwable ex) {
		if (AppConfig.DEBUG) {
			switch (level) {
			case Log.VERBOSE:
				Log.v(tag, msg, ex);
				break;
			case Log.DEBUG:
				Log.d(tag, msg, ex);
				break;
			case Log.INFO:
				Log.i(tag, msg, ex);
				break;
			case Log.WARN:
				Log.w(tag, msg, ex);
				break;
			case Log.ERROR:
				Log.e(tag, msg, ex);
				break;
			default:
				break;
			}
		}

		if (AppConfig.LOG_TO_FILE) {
			writeLogFile(tag, msg, ex);
		}
	}

	public static void writeLogFile(String tag, String msg, Throwable ex) {
		try {
			if (!checkCurrentFile(FILE_TYPE_LOG)) {
				return;
			}

			msg = DateUtils.getCurrentTimeStr(DateUtils.TIME_FORMAT2) + " --- " + tag
					+ " --- " + msg + "\n";

			if (null != ex) {
				msg += ex.getClass().getName() + "(" + ex.getMessage() + ")\n";
				StackTraceElement[] stes = ex.getStackTrace();
				for (StackTraceElement ste : stes) {
					msg += ste.getClassName() + ":" + ste.getMethodName() + "("
							+ ste.getLineNumber() + ") \n";
				}
			}

			synchronized (ZRLog.class) {
				FileOutputStream outputStream = new FileOutputStream(
						sCurrentLogFile, true);
				outputStream.write(msg.getBytes());
				outputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeCrashFile(Throwable ex) {
		try {
			if (!checkCurrentFile(FILE_TYPE_CRASH)) {
				return;
			}

			String msg = DateUtils.getCurrentTimeStr(DateUtils.TIME_FORMAT2) + "\r\n";

			if (null != ex) {
				msg += ex.getClass().getName() + "(" + ex.getMessage() + ")\n";
				StackTraceElement[] stes = ex.getStackTrace();
				for (StackTraceElement ste : stes) {
					msg += ste.getClassName() + ":" + ste.getMethodName() + "("
							+ ste.getLineNumber() + ") \n";
				}
			}

			synchronized (ZRLog.class) {
				FileOutputStream outputStream = new FileOutputStream(
						sCurrentCrashFile, true);
				outputStream.write(msg.getBytes());
				outputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static synchronized boolean checkCurrentFile(int type)
			throws IOException {
		File current = null;
		String path = null;

		String currdate = DateUtils.getCurrentTimeStr(DateUtils.TIME_FORMAT4);

		if (FILE_TYPE_LOG == type) {
			current = sCurrentLogFile;
			path = String.format(AppConfig.APP_LOG_DIR, currdate);
		} else {
			current = sCurrentCrashFile;
			path = String.format(AppConfig.APP_CRASH_LOG_DIR, currdate);
		}

		/*
		 * If this is the first call, or current file size is larger than
		 * #LOG_FILE_SIZE, we should get a file to write to.
		 */
		if (null == current || LOG_FILE_SIZE <= current.length()) {
			File logfileDictionary = new File(path);

			if (!logfileDictionary.exists()) {
				boolean success = logfileDictionary.mkdirs();
				if (!success) {
					return false;
				}
			}

			File[] allFiles = logfileDictionary.listFiles();
			ArrayList<File> logFileList = new ArrayList<File>();
			for (File f : allFiles) {
				if (!f.isDirectory()) {
					logFileList.add(f);
				}
			}
			File[] logFiles = logFileList.toArray(new File[logFileList.size()]);

			if (null != logFiles && 0 != logFiles.length) {
				/*
				 * Creation time is saved in file name, So sort according to
				 * file name is equal to sort them according to creation time.
				 */
				Arrays.sort(logFiles, new Comparator<File>() {
					@Override
					public int compare(File lhs, File rhs) {
						return rhs.getName().compareTo(lhs.getName());
					}
				});

				if (LOG_FILE_MAX_COUNT < logFiles.length) {
					/*
					 * If file count is greater then #LOG_FILE_MAX_COUNT, remove
					 * the extra.
					 */
					int deleteCount = logFiles.length - LOG_FILE_MAX_COUNT;
					for (int i = 0; i < deleteCount; ++i) {
						logFiles[logFiles.length - 1 - i].delete();
					}
				} else {
					/*
					 * Get the lastest one, and check the file size.
					 */
					File last = logFiles[0];
					if (LOG_FILE_SIZE > last.length()) {
						current = last;
					}
				}
			}

			/*
			 * Create a new file.
			 */
			if (null == current) {
				String currentTime = DateUtils
						.getCurrentTimeStr(DateUtils.TIME_FORMAT);
				if (FILE_TYPE_LOG == type) {
					current = new File(logfileDictionary, LOG_FILE_PREFIX + "_"
							+ currentTime);
					current.createNewFile();
				} else if (FILE_TYPE_CRASH == type) {
					current = new File(logfileDictionary, CRASH_FILE_PREFIX
							+ "_" + currentTime);
					current.createNewFile();
				}
			}
		}

		/*
		 * We still can't get a file to write to. Something abnormal happened,
		 * oh~ give up.
		 */
		if (null == current) {
			return false;
		}

		if (FILE_TYPE_LOG == type) {
			sCurrentLogFile = current;
		} else {
			sCurrentCrashFile = current;
		}

		return true;
	}
}
