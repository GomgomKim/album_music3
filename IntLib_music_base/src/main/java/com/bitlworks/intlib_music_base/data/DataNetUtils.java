package com.bitlworks.intlib_music_base.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Environment;

import com.bitlworks.intlib_music_base.StaticValues;


/** 웨딩 앨범에 필요한 유용한 메소드&변수 집합 **/
public class DataNetUtils {
	public static final String PREFERENCE_NAME_USER_MOBILE = "user";

	/** 인증 확인 : 프리퍼런스에 저장된 값을 찾아온다 **/
	public static boolean isAuthCreated(Context context) {
		String id = getStringPref(context, "User.id", null);
		if (id != null)
			return true;
		return false;
	}

	/** 인증 생성 : 프리퍼런스에 값을 저장한다 **/
	public static void createAuth(Context context, String id, String mobile) {
		setStringPref(context, "User.id", id);
		setStringPref(context, PREFERENCE_NAME_USER_MOBILE, mobile);
	}

	/** 프리퍼런스 getter **/
	public static String getStringPref(Context context, String name, String def) {
		SharedPreferences prefs = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		return prefs.getString(name, def);
	}

	/** 프리퍼런스 setter **/
	public static void setStringPref(Context context, String name, String value) {
		SharedPreferences prefs = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		Editor ed = prefs.edit();
		ed.putString(name, value);
		ed.commit();
	}

	/** 프리퍼런스에 저장된 커플 아이디(선택된 앨범)를 가져오는 메소드 **/
	public static int getSelectedCoupleId(Context c) {
		return Integer.parseInt(DataNetUtils.getStringPref(c,
				StaticValues.PREFERENCE_NAME_SELECTED_ALBUM, "-1"));
	}

	/** 프리퍼런스에 커플 아이디(선택할 앨범)를 설정하는 메소드 **/
	public static void setSelectedDiskId(Context c, int coupleId) {
		DataNetUtils.setStringPref(c,
				StaticValues.PREFERENCE_NAME_SELECTED_ALBUM, "" + coupleId);
		return;
	}
	
	public static String getSelectedCoupleDate(Context c){
		return DataNetUtils.getStringPref(c, StaticValues.PREFERENCE_NAME_SELECTED_ALBUM_DATE, "0");
	}
	
	public static void setSelectedCoupleDate(Context c, String date){
		DataNetUtils.setStringPref(c, StaticValues.PREFERENCE_NAME_SELECTED_ALBUM_DATE, date);
	}

	public static void removeSelectedCoupleId(Context c) {
		DataNetUtils.setStringPref(c,
				StaticValues.PREFERENCE_NAME_SELECTED_ALBUM, null);
		return;
	}

	/** 프리퍼런스에 저장된 내 아이디를 가져오는 메소드 **/
	public static int getMyId(Context c) {
		return Integer.parseInt(DataNetUtils.getStringPref(c,
				StaticValues.PREFERENCE_NAME_SELECTED_USER_ID, "-1"));
	}

	/** 프리퍼런스에 저장된 내 닉네임를 가져오는 메소드 **/
	public static String getMyName(Context c) {
		return DataNetUtils.getStringPref(c,
				StaticValues.PREFERENCE_NAME_SELECTED_USER_NAME, "null");
	}

	/** 내부 저장 경로 자동으로 만들어주는 메소드 **/
	public static String getInternalStorePath() {
		// 경로 정하기
		String rootPath = "/mnt/sdcard/";
		// 내부 저장소 경로 구하기
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			rootPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/";
		}
		// 폴더 존재 확인 및 생성
		rootPath += "bitlworks/wedding/";
		return rootPath;
	}

	/** 인터넷에 연결되어 있나? **/
	public static boolean isNetworkConnect(Activity a) {
		ConnectivityManager connect = (ConnectivityManager) a
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) == null) {
			if (connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.getState() == State.CONNECTED) {
				return true;
			} else {
				return false;
			}
		} else {
			if (connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
					.getState() == State.CONNECTED
					|| connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
							.getState() == State.CONNECTED) {
				return true;
			} else {
				return false;
			}
		}
	}

}
