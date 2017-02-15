package com.bitlworks.intlib_music_base.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class AlbumUtils {
	public final static String PREF_BGM_MUSIC_ID = "bgm_id";
	public final static String PREF_BGM_MUSIC = "bgm";
	public static final int ACTION_CALL = 1;
	public static final int ACTION_EMAIL = 2;
	public static final int ACTION_WEB = 3;

	public static final String DOMAIN_THIRDMIND = "http://thethirdmind.bitlworks.co.kr";
	public static final String DOMAIN_SOMETHING = "http://something.bitlworks.co.kr";
	public static final String DOMAIN_HEBA = "http://heba.bitlworks.co.kr";
	public static final int EXTENTION_IMAGE = 100;
	public static final int EXTENTION_VIDEO = 101;
	public static final int EXTENTION_UNKNOWN = 102;
	public static final int STUDIO_ID_HEBA = 1;
	public static final int STUDIO_ID_SOHO = 2;
	public static final int STUDIO_ID_THIRDMIND = 3;
	public static final int STUDIO_ID_LUCE = 4;
	public static final int STUDIO_ID_MIN = 5;
	public static final int STUDIO_ID_RARI = 6;
	public static final int STUDIO_ID_MAY = 7;
	public static final int STUDIO_ID_LEBLANC = 8;
	public static final int STUDIO_ID_VIZU = 9;
	public static final int SDF_OPT_yyyyMMddHHmmss = 1;
	public static final int SDF_OPT_DEFAULT = 2;
	public static final int SDF_OPT_MM_BAR_DD = 3;
	public static final int SDF_OPT_HH_COLON_mm = 4;
	public static final int SDF_OPT_DEFAULT_WITHOUT_SECOND = 5;
	public static final int SDF_OPT_yyyyDOTMMDOTdd = 6;
	public static final int SDF_OPT_yyyyMMdd = 7;
	public static final int SDF_OPT_yyyyMMddKOR = 8;
	public static final int SDF_OPT_yyyyMMddStep = 9;
	static SimpleDateFormat defaultTimeFmt1 = new SimpleDateFormat(
			"yyyyMMddHHmmss", Locale.KOREA);
	static SimpleDateFormat defaultTimeFmt2 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.KOREA);
	static SimpleDateFormat defaultTimeFmt3 = new SimpleDateFormat("MM-dd",
			Locale.KOREA);
	static SimpleDateFormat defaultTimeFmt4 = new SimpleDateFormat("HH:mm",
			Locale.KOREA);
	static SimpleDateFormat defaultTimeFmt5 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm", Locale.KOREA);
	static SimpleDateFormat defaultTimeFmt6 = new SimpleDateFormat(
			"yyyy.MM.dd", Locale.KOREA);
	static SimpleDateFormat defaultTimeFmt7 = new SimpleDateFormat("yyyyMMdd",
			Locale.KOREA);
	static SimpleDateFormat defaultTimeFmt8 = new SimpleDateFormat(
			"yyyy/MM/dd HH시 mm분", Locale.KOREA);
	static SimpleDateFormat defaultTimeFmt9 = new SimpleDateFormat(
			"yyyy년 MM월 dd일", Locale.KOREA);

	public static String formatTime(Date d, int opt) {
		switch (opt) {
		case SDF_OPT_yyyyMMddHHmmss:
			return defaultTimeFmt1.format(d);
		case SDF_OPT_DEFAULT:
			return defaultTimeFmt2.format(d);
		case SDF_OPT_MM_BAR_DD:
			return defaultTimeFmt3.format(d);
		case SDF_OPT_HH_COLON_mm:
			return defaultTimeFmt4.format(d);
		case SDF_OPT_DEFAULT_WITHOUT_SECOND:
			return defaultTimeFmt5.format(d);
		case SDF_OPT_yyyyDOTMMDOTdd:
			return defaultTimeFmt6.format(d);
		case SDF_OPT_yyyyMMdd:
			return defaultTimeFmt7.format(d);
		case SDF_OPT_yyyyMMddKOR:
			return defaultTimeFmt8.format(d);
		case SDF_OPT_yyyyMMddStep:
			return defaultTimeFmt9.format(d);
		default:
			break;
		}
		return null;
	}

	public static Date parseSDF(String str, int opt) {
		try {
			switch (opt) {
			case SDF_OPT_yyyyMMddHHmmss:
				return defaultTimeFmt1.parse(str);
			case SDF_OPT_DEFAULT:
				return defaultTimeFmt2.parse(str);
			case SDF_OPT_MM_BAR_DD:
				return defaultTimeFmt3.parse(str);
			case SDF_OPT_HH_COLON_mm:
				return defaultTimeFmt4.parse(str);
			case SDF_OPT_DEFAULT_WITHOUT_SECOND:
				return defaultTimeFmt5.parse(str);
			case SDF_OPT_yyyyDOTMMDOTdd:
				return defaultTimeFmt6.parse(str);
			case SDF_OPT_yyyyMMddStep:
				return defaultTimeFmt9.parse(str);

			default:
				return null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int getDDay(String yyyyMMddHHmmss, Date currentDate) {
		long gap = currentDate.getTime()
				- parseSDF(yyyyMMddHHmmss, SDF_OPT_yyyyMMddHHmmss).getTime();

		return (int) (gap / (1000 * 60 * 60 * 24));

	}

	public static void openShareIntent(final Activity activity, Uri uri) {
		try {
			Log.e("share", uri.toString());
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("image/jpeg");
			// i.putExtra(android.content.Intent.EXTRA_SUBJECT,
			// activity.getString(R.string.share_subject));
			// i.putExtra(android.content.Intent.EXTRA_TEXT,
			// activity.getString(R.string.share_content));
			i.addCategory("android.intent.category.DEFAULT");
			i.putExtra(Intent.EXTRA_STREAM, uri);
			// Intent createChooser = Intent.createChooser(i,
			// activity.getString(R.string.share_title));
			activity.startActivity(i);
		} catch (final Exception e) {

		}
	}

	public static String makePhotoUrl(String serviceUrl, String dirName,
																		String name) {
		try {
			String url = serviceUrl + "/wedding" + StaticObjects.sCoupleDir
					+ "/"
					// + dirName + "/" + name;
					+ URLEncoder.encode(dirName, "UTF-8") + "/"
					+ URLEncoder.encode(name, "UTF-8");
			return url;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void openShareMessageIntent(final Activity activity,
			String message) {
		try {
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_TEXT, message);
			activity.startActivity(i);
		} catch (final Exception e) {

		}
	}

	public static String saveBitmapJPEG(Context context, Bitmap bitmap,
																			String dir, String name, int quality) throws IOException {
		if (!dir.endsWith("/")) {
			dir = dir + "/";
		}
		FileOutputStream fos = new FileOutputStream(new File(dir + name));
		Log.e("savePath", dir + name);
		// context.openFileOutput(name,
		// Context.MODE_PRIVATE);

		try {
			bitmap.compress(CompressFormat.JPEG, quality, fos);
		} catch (Exception e) {

		}
		fos.flush();
		fos.close();
		return dir + name;
	}

	public static String saveBitmap(Context context, Bitmap bitmap, String dir,
																	String name) throws IOException {
		if (!dir.endsWith("/")) {
			dir = dir + "/";
		}
		FileOutputStream fos = new FileOutputStream(new File(dir + name));
		Log.e("savePath", dir + name);
		// context.openFileOutput(name,
		// Context.MODE_PRIVATE);

		try {
			bitmap.compress(CompressFormat.PNG, 100, fos);
		} catch (Exception e) {

		}
		fos.flush();
		fos.close();
		return dir + name;
	}

	public int dpToPixel(Activity activity, int dp) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return (int) (dp * dm.density + 0.5f);
	}

	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				res.getDisplayMetrics());
	}

	public static DisplayMetrics getDisplayMetrics(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics;
	}

	public static String getLineNumber(Context context) {
		String mobile = AlbumUtils.getStringPref(context,
				AlbumConstants.PREF_NAME_USER_MOBILE, "");
		if (mobile.length() > 0) {
			return mobile;
		}
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String number = tm.getLine1Number();
		if (number == null || number.length() < 4) {
			return "null";
		}
		number = number.replace("+82", "0");
		return number;
	}

	public static int getRawContactId(Context context, long contactId) {
		Cursor c2 = context.getContentResolver().query(RawContacts.CONTENT_URI,
				new String[] { RawContacts._ID },
				RawContacts.CONTACT_ID + "=?",
				new String[] { String.valueOf(contactId) }, null);
		ArrayList<String> results2 = new ArrayList<String>();
		while (c2.moveToNext()) {
			results2.add(c2.getString(0));
		}
		if (results2.size() == 0) {
			return -1;
		}
		return Integer.valueOf(results2.get(0));
	}

	static String baseNumberString = "0123456789";

	public static String generateRandomNumber(int len) {
		Random random = new Random();
		int baseLen = baseNumberString.length();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			int idx = random.nextInt(baseLen);
			sb.append(baseNumberString.charAt(idx));
		}
		return sb.toString();
	}

	public static void sendSMS(Context context, String mobile, String message)
			throws IllegalArgumentException {
		android.telephony.SmsManager smsManager = android.telephony.SmsManager
				.getDefault();
		String sendTo = mobile;
		try {
			smsManager.sendTextMessage(sendTo, null, message, null, null);
		} catch (NullPointerException e) {

		}

	}

	public static boolean isAuthCreated(Context context) {
		String id = getStringPref(context, "User.id", null);
		if (id != null)
			return true;
		return false;
	}

	public static void createAuth(Context context, String id, String mobile) {
		setStringPref(context, "User.id", id);
		setStringPref(context, AlbumConstants.PREF_NAME_USER_MOBILE, mobile);
	}

	public static void removeAuth(Context context) {
		setStringPref(context, "User.id", null);
		setStringPref(context, AlbumConstants.PREF_NAME_USER_MOBILE, null);
	}

	public static String getStringPref(Context context, String name, String def) {
		SharedPreferences prefs = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		return prefs.getString(name, def);
	}

	public static void setStringPref(Context context, String name, String value) {
		SharedPreferences prefs = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		Editor ed = prefs.edit();
		ed.putString(name, value);
		ed.commit();
	}

	public static String notNullStr(String str) {
		if (str == null)
			return "";
		return str;
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		try {
			imm.hideSoftInputFromWindow(activity.getCurrentFocus()
					.getWindowToken(), 0);
		} catch (NullPointerException e) {

		}
	}

	public static String getDefaultAuthorName(Context context) {
		// String myNumber = getLineNumber(context);
		String tail4Number = "";
		// if (myNumber.length() > 4) {
		// tail4Number = getLineNumber(context).substring(
		// myNumber.length() - 4, myNumber.length());
		// }
		return getStringPref(context, AlbumConstants.PREF_NAME_DEFAULT_AUTHOR,
				tail4Number);
	}

	public static void saveDefaultAuthorName(Context context, String name) {
		setStringPref(context, AlbumConstants.PREF_NAME_DEFAULT_AUTHOR, name);
	}

	public static String getSessionId(Context context) {

		return getStringPref(context, AlbumConstants.PREF_NAME_SESSION_ID, "");
	}

	public static void saveSessionId(Context context, String sessionId) {
		setStringPref(context, AlbumConstants.PREF_NAME_SESSION_ID, sessionId);
	}

	public static String getVideoPath() {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Video/weddingAlbum";
		if (!new File(path).exists()) {
			new File(path).mkdirs();
		}
		return path;
	}

	public static boolean isVideoExist(String videoFileName) {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Video/weddingAlbum/" + videoFileName;
		if (!new File(path).exists()) {
			return false;
		}
		return true;
	}

	public static long getEnableMemory() {
		return Runtime.getRuntime().totalMemory();
	}

	public static String getOsVersion() {
		return Build.VERSION.RELEASE.equals("L")?"5.0": Build.VERSION.RELEASE;
	}

	public static double getOsVersionDouble() {
		String osVerStr = getOsVersion();
		Log.i("bitlworks", "osverstr>>"+osVerStr);
		String parseableStr = osVerStr.split("\\.")[0] + "."
				+ osVerStr.split("\\.")[1];
		return Double.parseDouble(parseableStr);
	}

	public static void processResult(String result) throws Exception {
		Log.e("ab1233", result);
		if (!result.equals("1")) {
			throw new Exception();
		}
	}

	public static boolean checkNetwork(Context c) {
//		if (!DataNetUtils.isNetworkConnect((Activity) c)) {
//			return false;
//		}
		return true;
	}

	public static String getWeekStr(Date weddingTimeDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(weddingTimeDate);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		String dayOfWeekStr = "";
		switch (dayOfWeek) {
		case Calendar.SUNDAY:
			dayOfWeekStr = "SUNDAY";
			break;
		case Calendar.MONDAY:
			dayOfWeekStr = "MONDAY";
			break;
		case Calendar.TUESDAY:
			dayOfWeekStr = "TUESDAY";
			break;
		case Calendar.WEDNESDAY:
			dayOfWeekStr = "WEDNESDAY";
			break;
		case Calendar.THURSDAY:
			dayOfWeekStr = "THURSDAY";
			break;
		case Calendar.FRIDAY:
			dayOfWeekStr = "FRIDAY";
			break;
		case Calendar.SATURDAY:
			dayOfWeekStr = "SATURDAY";
			break;
		default:
			break;
		}
		return dayOfWeekStr;
	}

	public static String getWeekStrKR(Date weddingTimeDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(weddingTimeDate);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		String dayOfWeekStr = "";
		switch (dayOfWeek) {
		case Calendar.SUNDAY:
			dayOfWeekStr = "일요일";
			break;
		case Calendar.MONDAY:
			dayOfWeekStr = "월요일";
			break;
		case Calendar.TUESDAY:
			dayOfWeekStr = "화요일";
			break;
		case Calendar.WEDNESDAY:
			dayOfWeekStr = "수요일";
			break;
		case Calendar.THURSDAY:
			dayOfWeekStr = "목요일";
			break;
		case Calendar.FRIDAY:
			dayOfWeekStr = "금요일";
			break;
		case Calendar.SATURDAY:
			dayOfWeekStr = "토요일";
			break;
		default:
			break;
		}
		return dayOfWeekStr;
	}

	public static String getMonthStr(Date weddingTimeDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(weddingTimeDate);
		int month = c.get(Calendar.MONTH);
		String monthStr = "";
		switch (month) {
		case Calendar.JANUARY:
			monthStr = "JANUARY";
			break;
		case Calendar.FEBRUARY:
			monthStr = "FEBRUARY";
			break;
		case Calendar.MARCH:
			monthStr = "MARCH";
			break;
		case Calendar.APRIL:
			monthStr = "APRIL";
			break;
		case Calendar.MAY:
			monthStr = "MAY";
			break;
		case Calendar.JUNE:
			monthStr = "JUNE";
			break;
		case Calendar.JULY:
			monthStr = "JULY";
			break;
		case Calendar.AUGUST:
			monthStr = "AUGUST";
			break;
		case Calendar.SEPTEMBER:
			monthStr = "SEPTEMBER";
			break;
		case Calendar.OCTOBER:
			monthStr = "OCTOBER";
			break;
		case Calendar.NOVEMBER:
			monthStr = "NOVEMBER";
			break;
		case Calendar.DECEMBER:
			monthStr = "DECEMBER";
			break;
		default:
			break;
		}
		return monthStr;
	}

	public static String getAM_PM_ClockStr(Date weddingTimeDate) {
		String weddingClockStr = AlbumUtils.formatTime(weddingTimeDate,
				AlbumUtils.SDF_OPT_HH_COLON_mm);
		int weddingHour = Integer.valueOf(weddingClockStr.split(":")[0]);
		int weddingMinute = Integer.valueOf(weddingClockStr.split(":")[1]);
		StringBuilder sbWeddingClock = new StringBuilder();

		if (weddingHour > 12) {
			sbWeddingClock.append("P M");
			weddingHour = weddingHour - 12;
		} else if ( weddingHour == 12) {
			sbWeddingClock.append("P M");
//			weddingHour = weddingHour; 
		} else {
			sbWeddingClock.append("A M");
		}
		sbWeddingClock.append(" ");
		sbWeddingClock.append(String.format("%02d", weddingHour));
		sbWeddingClock.append(" : ");
		sbWeddingClock.append(String.format("%02d", weddingMinute));
		return sbWeddingClock.toString();
	}

	public static Bitmap downloadBitmap(String url) {
		final HttpClient client = new DefaultHttpClient();
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("downloadBitmap", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				// BitmapFactory.Options options = new BitmapFactory.Options();
				// options.inSampleSize = 2;

				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory
							.decodeStream(new FlushedInputStream(inputStream));
					// final Bitmap bitmap = BitmapFactory.decodeStream(new
					// FlushedInputStream(inputStream), null, options);
					Log.i("Network", "Bitmap Download Complete");
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			getRequest.abort();
		}
		return null;
	}

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int bytes = read();
					if (bytes < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	public static int getStudioID(Context context) {
		// String packageName = context.getClass().getCanonicalName();
		// if (packageName.startsWith("com.bitlworks.weddingalbum3.thirdmind"))
		// {
		// return STUDIO_ID_THIRDMIND;
		// } else if
		// (packageName.startsWith("com.bitlworks.weddingalbum3.soho")) {
		// return STUDIO_ID_SOHO;
		// } else if
		// (packageName.startsWith("com.bitlworks.weddingalbum3.rari")) {
		// return STUDIO_ID_RARI;
		// } else if
		// (packageName.startsWith("com.bitlworks.weddingalbum3.luce")) {
		// return STUDIO_ID_LUCE;
		// } else if (packageName.startsWith("com.bitlworks.weddingalbum3.may"))
		// {
		// return STUDIO_ID_MAY;
		// } else if (packageName.startsWith("com.bitlworks.weddingalbum3.min"))
		// {
		// return STUDIO_ID_MIN;
		// }
		return getStudioIdFromApplicationContext(context
				.getApplicationContext());
	}

	public static int getStudioIdFromApplicationContext(Context context) {
		String packageName = context.getClass().getCanonicalName();
		if (packageName.startsWith("com.bitlworks.weddingalbum3_thirdmind")) {
			return STUDIO_ID_THIRDMIND;
		} else if (packageName.startsWith("com.bitlworks.weddingalbum3_soho")) {
			return STUDIO_ID_SOHO;
		} else if (packageName.startsWith("com.bitlworks.weddingalbum3_rari")) {
			return STUDIO_ID_RARI;
		} else if (packageName.startsWith("com.bitlworks.weddingalbum3_luce")) {
			return STUDIO_ID_LUCE;
		} else if (packageName.startsWith("com.bitlworks.weddingalbum3_may")) {
			return STUDIO_ID_MAY;
		} else if (packageName.startsWith("com.bitlworks.weddingalbum3_min")) {
			return STUDIO_ID_MIN;
		} else if (packageName.startsWith("com.bitlworks.weddingalbum3_heba")) {
			return STUDIO_ID_HEBA;
		} else if (packageName
				.startsWith("com.bitlworks.weddingalbum3_leblanc")) {
			return STUDIO_ID_LEBLANC;
		} else if (packageName.startsWith("com.bitlworks.weddingalbum3_vizu")) {
			return STUDIO_ID_VIZU;
		}
		return -1;
	}

	public static boolean isBrandAppContext(Context context) {
		String packageName = context.getApplicationContext().getPackageName();
		return packageName.endsWith("studio");

	}

	public static String getDomain(Context context) {
		return "http://music.bitlworks.co.kr";
	}

	public static String getServiceUrl(Context context) {
		return getDomain(context) + "/mobilemusic/API/";
	}

	public static String getFirstPhotoPath(Context context) {
		return getServiceUrl(context) + "/first_photo/";
	}

	public static void sendInviteMessage(final Activity activity, Uri uri,
																			 String mobile, String message) {
		Intent sendIntent = new Intent(Intent.ACTION_VIEW);
		
		sendIntent.putExtra("address", mobile);
		sendIntent.putExtra("sms_body", message);
		sendIntent.setType("vnd.android-dir/mms-sms");
		try {
			activity.startActivity(sendIntent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(activity, "문자메시지를 지원하지 않는 기기입니다", Toast.LENGTH_SHORT).show();
		}
		if(true){
			return ;
		}
		try {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setData(Uri.parse("mmsto:"));
			intent.setType("image/png");
			// intent.setType("vnd.android-dir/mms-sms");
			intent.putExtra("sms_body", message);
			// intent.setDataAndType(uri, "image/png");
			intent.addCategory("android.intent.category.DEFAULT");
			if (uri != null) {
				intent.putExtra(Intent.EXTRA_STREAM, uri);
			}
			// intent.setType("image/png");
			// intent.setComponent(new ComponentName("com.sec.mms",
			// "com.sec.mms.Mms"));
			intent.putExtra("address", mobile);
			activity.startActivity(intent);
			// new File(uri.getPath()).delete();
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	public static void fileCopy(String inFileName, String outFileName) {
		try {
			FileInputStream fis = new FileInputStream(inFileName);
			FileOutputStream fos = new FileOutputStream(outFileName);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}

			fis.close();
			fos.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int checkExtention(String path) {
		if (path.toLowerCase(Locale.US).endsWith(".png")
				|| path.toLowerCase(Locale.US).endsWith(".jpg")
				|| path.toLowerCase(Locale.US).endsWith(".jpeg")
				|| path.toLowerCase(Locale.US).endsWith(".gif")
				|| path.toLowerCase(Locale.US).endsWith(".bmp")) {
			return EXTENTION_IMAGE;
		} else if (path.toLowerCase(Locale.US).endsWith(".mp4")
				|| path.toLowerCase(Locale.US).endsWith(".avi")
				|| path.toLowerCase(Locale.US).endsWith(".mpeg")
				|| path.toLowerCase(Locale.US).endsWith(".asf")
				|| path.toLowerCase(Locale.US).endsWith(".swf")
				|| path.toLowerCase(Locale.US).endsWith(".3gp")) {
			return EXTENTION_VIDEO;
		}
		return EXTENTION_UNKNOWN;
	}

	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @author paulburke
	 */
	@TargetApi(19)
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
																		 String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/** 유니크한 ID 맹글기 **/
	public static String GetDevicesUUID(Context mContext) {
		final TelephonyManager tm = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						mContext.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String deviceId = deviceUuid.toString();

		return deviceId;
	}

	public static Bitmap loadBitmapFromView(View v) {
		if (v.getMeasuredHeight() <= 0) {
			v.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(),
					v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b);
			v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
			v.draw(c);
			return b;
		}
		Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width,
				v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
		v.draw(c);
		return b;
	}

	public static Bitmap getWholeListViewItemsToBitmap(ListView listview) {
		ListAdapter adapter = listview.getAdapter();
		int itemscount = adapter.getCount();
		int allitemsheight = 0;
		List<Bitmap> bmps = new ArrayList<Bitmap>();

		for (int i = 0; i < itemscount; i++) {

			View childView = adapter.getView(i, null, listview);
			childView.measure(MeasureSpec.makeMeasureSpec(listview.getWidth(),
					MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED));

			childView.layout(0, 0, childView.getMeasuredWidth(),
					childView.getMeasuredHeight());
			childView.setDrawingCacheEnabled(true);
			childView.buildDrawingCache();
			bmps.add(childView.getDrawingCache());
			allitemsheight += childView.getMeasuredHeight();
		}

		Bitmap bigbitmap = Bitmap.createBitmap(listview.getMeasuredWidth(),
				allitemsheight, Bitmap.Config.ARGB_8888);
		Canvas bigcanvas = new Canvas(bigbitmap);

		Paint paint = new Paint();
		int iHeight = 0;

		for (int i = 0; i < bmps.size(); i++) {
			Bitmap bmp = bmps.get(i);
			bigcanvas.drawBitmap(bmp, 0, iHeight, paint);
			iHeight += bmp.getHeight();

			bmp.recycle();
			bmp = null;
		}

		return bigbitmap;
	}

	public static void copyStream(InputStream input, OutputStream output)
			throws IOException {

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

	public static Bitmap getBitmap(Context context, Uri uri) {
		InputStream in = null;
		ContentResolver contentResolver = context.getContentResolver();
		try {
			final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
			in = contentResolver.openInputStream(uri);

			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, o);
			in.close();

			int scale = 1;
			while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
				scale++;
			}
			Log.d("ab1233", "scale = " + scale + ", orig-width: " + o.outWidth
					+ ", orig-height: " + o.outHeight);

			Bitmap b = null;
			in = contentResolver.openInputStream(uri);
			if (scale > 1) {
				scale--;
				// scale to max possible inSampleSize that still yields an image
				// larger than target
				o = new BitmapFactory.Options();
				o.inSampleSize = scale;
				b = BitmapFactory.decodeStream(in, null, o);

				// resize to desired dimensions
				int height = b.getHeight();
				int width = b.getWidth();
				Log.d("ab1233", "1th scale operation dimenions - width: "
						+ width + ",height: " + height);

				double y = Math.sqrt(IMAGE_MAX_SIZE
						/ (((double) width) / height));
				double x = (y / height) * width;

				Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
						(int) y, true);
				b.recycle();
				b = scaledBitmap;

				System.gc();
			} else {
				b = BitmapFactory.decodeStream(in);
			}
			in.close();

			Log.d("ab1233", "bitmap size - width: " + b.getWidth()
					+ ", height: " + b.getHeight());
			return b;
		} catch (IOException e) {
			Log.e("ab1233", e.getMessage(), e);
			return null;
		}
	}

	public static void setMaximumLines(EditText et, final int maxLines) {
		et.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_UP) {
					String text = ((EditText) v).getText().toString();
					if (text.endsWith("\n\n")) {
						int lastBreakIndex = text.lastIndexOf("\n");
						String newText = text.substring(0, lastBreakIndex);
						((EditText) v).setText("");
						((EditText) v).append(newText);
						return true;
					}
					int editTextRowCount = text.split("\n").length;
					if (editTextRowCount >= maxLines) {

						int lastBreakIndex = text.lastIndexOf("\n");
						String newText = text.substring(0, lastBreakIndex);
						((EditText) v).setText("");
						((EditText) v).append(newText);
					}
				}
				return false;
			}
		});
	}

	public static boolean isNumeric(String str) {
		try {
			Long.valueOf(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static void contactAction(Context context, int action, String link) {
		try {
			switch (action) {
			case ACTION_CALL:
				Intent i = new Intent(Intent.ACTION_DIAL);
				i.setData(Uri.parse("tel:" + link));
				context.startActivity(i);
				break;
			case ACTION_EMAIL:
				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL, new String[] { link });
				email.setType("message/rfc822");
				context.startActivity(Intent.createChooser(email,
						"Choose an Email client :"));
				break;
			case ACTION_WEB:
				Intent browserIntent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(link));
				context.startActivity(browserIntent);
				break;

			default:
				break;
			}
		} catch (ActivityNotFoundException e) {
			Toast.makeText(context, "기기에서 지원하지 않는 기능입니다", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public static String getAppVersionName(Context context) {
		PackageInfo pInfo;
		try {
			pInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "1.0";
		}
	}

	public static void setLatestTouchedNoticeTime(Context context, long time) {
		setStringPref(context, "notice", time + "");
	}

	public static long getLatestTouchedNoticeTime(Context context) {
		return Long.valueOf(getStringPref(context, "notice", "0"));
	}

	public static String getDefaultPassword() {
		return "1howtobiz";
	}

	public static void setLatestInstallableVersion(Context context,
			String version) {
		setStringPref(context, "version", version);
	}

	public static String getLatestInstallableVersion(Context context) {
		return getStringPref(context, "version", "1.0");
	}

	public static void setClearForm(Context context) {
		setStringPref(context, "clearForm", "Y");
	}

	public static void setCacheForm(Context context) {
		setStringPref(context, "clearForm", "N");
	}

	public static void setBackgroundMusicEnable(Context context,
			boolean isEnable) {
		setStringPref(context, "background_music", isEnable ? "Y" : "N");
	}

	public static boolean getBackgroundMusicEnable(Context context) {
		String s = getStringPref(context, "background_music", "");
		if (s.equals("N")) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isEmptyForm(Context context) {
		return getStringPref(context, "clearForm", "").equals("Y");
	}

	public static void executePackage(Context context, String packageName) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(
				packageName);
		if (intent == null) {
			Toast.makeText(context, "설치되어있지 않습니다.\n구글플레이에서 설치해주세요.",
					Toast.LENGTH_SHORT).show();
			intent = new Intent(Intent.ACTION_VIEW);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setData(Uri.parse("market://details?id=" + packageName));
			context.startActivity(intent);
			return;
		}
		intent.setAction(Intent.ACTION_MAIN);
		context.startActivity(intent);
	}

	public static String getBgmPath(Context context) {
		return getStringPref(context, PREF_BGM_MUSIC, "");

	}

	public static void setBgmPath(Context context, String sdcardPath) {
		setStringPref(context, PREF_BGM_MUSIC, sdcardPath);
	}

	public static String getBgmId(Context context) {
		return getStringPref(context, PREF_BGM_MUSIC_ID, "-1");

	}

	public static void setBgmId(Context context, int id) {
		setStringPref(context, PREF_BGM_MUSIC_ID, id + "");
	}

	public static String getInvitePicturePath(Context context, int albumId) {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/WeddingAlbum/Application/" + albumId + ".dat";
	}

	public static String getDisplayName(Context context, String mobile) {
		String[] arrPhoneProjection = { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(mobile));
		Cursor clsPhoneCursor = context.getContentResolver().query(uri,
				new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
		// context.getContentResolver().query(
		// ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
		// arrPhoneProjection,
		// ContactsContract.CommonDataKinds.Phone.NUMBER + " = " + mobile,
		// null, null);
		ArrayList<String> results = new ArrayList<String>();
		while (clsPhoneCursor.moveToNext()) {
			String name = clsPhoneCursor.getString(0);
			if (name.length() < 1) {
				continue;
			}
			results.add(name);
		}
		clsPhoneCursor.close();
		if (results.size() > 0) {
			return results.get(0);
		}
		return "";
	}
}
