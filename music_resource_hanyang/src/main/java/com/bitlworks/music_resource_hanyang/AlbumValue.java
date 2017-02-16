package com.bitlworks.music_resource_hanyang;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Html;
import android.text.Spanned;

public class AlbumValue {
  public static final int album_id = 7;//5;
  public static int disk_id = 44;











  public static final boolean isSingle = false;



  public static final String music_url = "http://www.bitlmusic.com/untitled-ki2ig";
  public static final boolean IS_SAMPLE_PROJECT = false;
  public static final String STUDIO_NAME = "DirectWedding Inc."; //""포레스트 힐";
  public static final String ALBUM_APP_NAME = STUDIO_NAME + "모바일 화보";
  public static final String APP_NAME_EN = "babyPastel"; //"foresthill";
  public static final int STUDIO_ID = 35;
  public static final int DEFAULT_ALBUM_ID = 737;
  public static final String DOMAIN = "http://wedding.bitlworks.co.kr";

  public static final float SLIDE_SHOW_PADDING_RATE = 0.f;

  // 설정 페이지 layout
  public static final String SETTING_STUDIO_TAG = "ⓒBlanc Bebe Studio"; //"ⓒForestHill Studio";

  private static final String PREF_NAME_EMAIL_CC = "email";
  public static final String DOWN_URL = "market://details?id=com.bitlworks.st_" + APP_NAME_EN;
  public static final String DOWN_URL_NAVER = "";

  public static final String BRAND_LINK_URL = "market://details?id=com.bitlworks.weddingalbum3_" + APP_NAME_EN + "_studio";
  public static final String BRAND_LINK_URL_NAVER = "";

  public static final String API_MAP_KEY = "";
  public static final String API_MAP_KEY_BRAND = "";


  public static final String ALBUM_APP_PACKAGE;
  public static final String INTENT_ACTION_SELECT_IMAGE;

  static {
    if (IS_SAMPLE_PROJECT) {
      ALBUM_APP_PACKAGE = "com.bitlworks.st_" + APP_NAME_EN + "_preview";
      INTENT_ACTION_SELECT_IMAGE = ALBUM_APP_PACKAGE + ".intent.action.SELECT_IMAGE";
    } else {
      ALBUM_APP_PACKAGE = "com.bitlworks.st_" + APP_NAME_EN;
      INTENT_ACTION_SELECT_IMAGE = ALBUM_APP_PACKAGE + ".intent.action.SELECT_IMAGE";
    }
  }

  public static String getMoveSampleAlbumDesc() {
    return "";
  }

  public static String getEventName(String event) {
    return "#\n" + event.replaceAll("_", " ");
  }

  public static Spanned getCoupleNamesEn(String mr, String mrs) {
    return Html.fromHtml("<font color=\"#a8a9ab\">" + mr + "</font>" + "<font color=\"#a8a9ab\"> & </font>"
        + "<font color=\"#a8a9ab\">" + mrs + "</font>");
  }

  public static Spanned getAlbumTitleEn(String mr) {
    return Html.fromHtml("<font color=\"#a8a9ab\">" + mr + "</font>");
  }


  public static String getAppAdminMailID(Context context) {
    return "bitlworks@gmail.com";
  }

  public static String getStudioAdminMailID(Context context) {
    return getStringPref(context, PREF_NAME_EMAIL_CC,
        "cs.bitlworks@gmail.com");
  }

  /**
   * 프리퍼런스 getter
   **/
  private static String getStringPref(Context context, String name, String def) {
    SharedPreferences prefs = context.getSharedPreferences(
        context.getPackageName(), Context.MODE_PRIVATE);
    return prefs.getString(name, def);
  }

  /**
   * 프리퍼런스 setter
   **/
  private static void setStringPref(Context context, String name, String value) {
    SharedPreferences prefs = context.getSharedPreferences(
        context.getPackageName(), Context.MODE_PRIVATE);
    Editor ed = prefs.edit();
    ed.putString(name, value);
    ed.commit();
  }

  public static void setStudioAdminMailID(Context context, String email) {
    setStringPref(context, PREF_NAME_EMAIL_CC, email);
  }
}
