package com.bitlworks.intlib_bitlworks;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Environment;
import android.telephony.TelephonyManager;

import java.util.Calendar;
import java.util.UUID;


/**
 * 공통적으로 사용할 유용한 메소드 모음
 **/
public class CommonUtils {

  /**
   * Function to convert milliseconds time to
   * Timer Format
   * Hours:Minutes:Seconds
   */
  public static String milliSecondsToTimer(long milliseconds) {
    String finalTimerString = "";
    String secondsString = "";

    // Convert total duration into time
    int hours = (int) (milliseconds / (1000 * 60 * 60));
    int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
    int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
    // Add hours if there
    if (hours > 0) {
      finalTimerString = hours + ":";
    }

    // Prepending 0 to seconds if it is one digit
    if (seconds < 10) {
      secondsString = "0" + seconds;
    } else {
      secondsString = "" + seconds;
    }

    finalTimerString = finalTimerString + minutes + ":" + secondsString;

    // return timer string
    return finalTimerString;
  }

  /**
   * Function to get Progress percentage
   *
   * @param currentDuration
   * @param totalDuration
   */
  public static int getProgressPercentage(long currentDuration, long totalDuration) {
    Double percentage = (double) 0;

    long currentSeconds = (int) (currentDuration / 1000);
    long totalSeconds = (int) (totalDuration / 1000);

    // calculating percentage
    percentage = (((double) currentSeconds) / totalSeconds) * 100;

    // return percentage
    return percentage.intValue();
  }

  /**
   * Function to change progress to timer
   *
   * @param progress      -
   * @param totalDuration returns current duration in milliseconds
   */
  public static int progressToTimer(int progress, int totalDuration) {
    int currentDuration = 0;
    totalDuration = (int) (totalDuration / 1000);
    currentDuration = (int) ((((double) progress) / 100) * totalDuration);

    // return current duration in milliseconds
    return currentDuration * 1000;
  }


  /**
   * Mysql 날짜 변환 함수 (YYYY-MM-DD HH:MM:SS)
   **/
  public static String timestampToText(String timestamp) {
    Calendar now = Calendar.getInstance();
    String result = "";

    timestamp = timestamp.trim();

    final int YEAR = Integer.parseInt(timestamp.substring(0, 4));
    final int MONTH = Integer.parseInt(timestamp.substring(5, 7));
    final int DAY_OF_MONTH = Integer.parseInt(timestamp.substring(8, 10));
    final int HOUR_OF_DAY = Integer.parseInt(timestamp.substring(11, 13));
    final int MINUTE = Integer.parseInt(timestamp.substring(14, 16));
    final int SECOND = Integer.parseInt(timestamp.substring(17, 19));

    Calendar inputTime = Calendar.getInstance();
    inputTime.set(Calendar.YEAR, YEAR);
    inputTime.set(Calendar.MONTH, MONTH - 1);
    inputTime.set(Calendar.DAY_OF_MONTH, DAY_OF_MONTH);
    inputTime.set(Calendar.HOUR_OF_DAY, HOUR_OF_DAY);
    inputTime.set(Calendar.MINUTE, MINUTE);
    inputTime.set(Calendar.SECOND, SECOND);
    inputTime.set(Calendar.MILLISECOND, 0);

    long gap = (now.getTimeInMillis() - inputTime.getTimeInMillis());

    if (gap < 24 * 60 * 60 * 1000) {
      // 시간 전
      result = ((int) (gap / 60 / 60 / 1000)) + "시간 전";
      // 분 전
      if (gap < 60 * 60 * 1000)
        result = ((int) (gap / 60 / 1000)) + "분 전";
      // 초 전
      if (gap < 5 * 60 * 1000)
        result = "방금";
    } else
      // 월-일
      result = timestamp.substring(5, 10).replace("-", "/");

    // inputTime.get(Calendar.YEAR)

    return result;
  }


  /**
   * 프리퍼런스 getter
   **/
  public static String getStringPref(Context context, String name, String def) {
    SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    return prefs.getString(name, def);
  }

  /**
   * 프리퍼런스 setter
   **/
  public static void setStringPref(Context context, String name, String value) {
    SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    Editor ed = prefs.edit();
    ed.putString(name, value);
    ed.commit();
  }

  /**
   * SharedPreferences에 내 아이디 getter
   **/
  public static int getMyID(Context context) {
    return Integer.parseInt(
        getStringPref(context, "USER_ID", "-1"));
  }

  /**
   * SharedPreferences에 내 아이디 setter
   **/
  public static void setMyID(Context context, int no) {
    setStringPref(context, "USER_ID", no + "");
  }


  /**
   * 내부 저장 경로 자동으로 만들어주는 메소드
   **/
  public static String getInternalStorePath() {
    // 경로 정하기
    String rootPath = "/mnt/sdcard/";
    // 내부 저장소 경로 구하기
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    }
    // 폴더 존재 확인 및 생성
    rootPath += "bitlworks/studiogallery";
    return rootPath;
  }

  /**
   * 인터넷에 연결되어 있나?
   **/
  public static boolean isNetworkConnect(Activity a) {
    ConnectivityManager connect = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) == null) {
      if (connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == State.CONNECTED) {
        return true;
      } else {
        return false;
      }
    } else {
      if (connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == State.CONNECTED ||
          connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == State.CONNECTED) {
        return true;
      } else {
        return false;
      }
    }
  }

  /**
   * 유니크한 ID 맹글기
   **/
  public static String getDevicesUUID(Context mContext) {
    final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

    final String tmDevice, tmSerial, androidId;
    tmDevice = "" + tm.getDeviceId();
    tmSerial = "" + tm.getSimSerialNumber();
    androidId = "" + android.provider.Settings.Secure.getString(mContext.getContentResolver(),
        android.provider.Settings.Secure.ANDROID_ID);

    UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
    String deviceId = deviceUuid.toString();

    return deviceId;
  }

  public static String getMyNumber(Context context, String string) {
    String res = "";
    TelephonyManager telephone = (TelephonyManager) context.getSystemService("phone");
    if (telephone.getLine1Number() == null) { // 에뮬일때를 위해
      res = "";
    } else {
      res = telephone.getLine1Number();
    }

    if (res.contains("+82")) {
      res = res.replace("+82", "0");
    }

    if (res == null)
      res = string;

    return res;
  }

  public static void setLatestInstallableVersion(Context context,
                                                 String version) {
    setStringPref(context, "version", version);
  }

  public static String getLatestInstallableVersion(Context context) {
    return getStringPref(context, "version", "1.0");
  }

  public static String getAppVersionName(Context context) {
    PackageInfo pInfo;
    try {
      pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      return pInfo.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return "1.0";
    }
  }

  public static int getAppVersion(Context context) {
    try {
      PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      return packageInfo.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      throw new RuntimeException(e);
    }
  }


  /**
   * API KEY
   **/
  public static String getApikey(Context c) {
    return getDevicesUUID(c);
  }

}
