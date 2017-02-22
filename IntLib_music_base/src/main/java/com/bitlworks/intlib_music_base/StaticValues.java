package com.bitlworks.intlib_music_base;

import android.content.Context;

import com.bitlworks.intlib_music_base.data.VOAlbum;
import com.bitlworks.intlib_music_base.data.VOComment;
import com.bitlworks.intlib_music_base.data.VONewInfo;
import com.bitlworks.intlib_music_base.data.VOPhotoM;
import com.bitlworks.intlib_music_base.data.VOSinger;
import com.bitlworks.intlib_music_base.data.VOSong;
import com.bitlworks.intlib_music_base.data.VOUser;
import com.bitlworks.intlib_music_base.data.VOVideo;
import com.bitlworks.intlib_music_base.data.VOdisk;
import com.bitlworks.intlib_music_base.source.PagerMainActivity;


import java.util.ArrayList;

public class StaticValues {

  public static final String LOG_TAG = "비틀웍스";
  private static final String SERVICE_URL = "http://music.bitlworks.co.kr/API/";
  public static final int NOTIFICATION_ID = 1;

  // 앱 실행 중 쓰이는 값들
  public static int playIndex = -1;
  public static VOUser user;
  public static VOAlbum album;
  public static ArrayList<VOdisk> diskList = new ArrayList<>();
  public static VOdisk selectedDisk;
  public static ArrayList<VOSong> songList = new ArrayList<>();
  public static ArrayList<VOComment> commentList = new ArrayList<>();
  public static ArrayList<VOPhotoM> photoList = new ArrayList<>();
  public static ArrayList<VOVideo> videoList = new ArrayList<>();
  public static ArrayList<VOSinger> singerList = new ArrayList<>();
  public static ArrayList<VONewInfo> newinfoList = new ArrayList<>();
  public static int unread_count=0;

  // Reference
  public static PagerMainActivity pagerMainActivity;



  public static final String DOMAIN_THIRDMIND = "http://thethirdmind.bitlworks.co.kr";
  public static final String DOMAIN_SOMETHING = "http://something.bitlworks.co.kr";
  public static final String DOMAIN_HEBA = "http://heba.bitlworks.co.kr";

  public static final String PREFERENCE_NAME_SELECTED_ALBUM = "selectedAlbum";
  public static final String PREFERENCE_NAME_SELECTED_ALBUM_DATE = "selectedAlbumDate";
  public static final String PREFERENCE_NAME_SELECTED_USER_ID = "selectedUserId";
  public static final String PREFERENCE_NAME_SELECTED_USER_NAME = "selectedUserName";
  public static final String PREFERENCE_NAME_SELECTED_USER_MOBILE = "selectedUserMobile";

  private static String getDomain(Context context) {
    context = context.getApplicationContext();
    String className = context.getClass().getCanonicalName();
    if (className.startsWith("com.bitlworks.weddingalbum3_thirdmind")) {
      return DOMAIN_THIRDMIND;
    } else if (className.startsWith("com.bitlworks.weddingalbum3_may")) {
      return DOMAIN_SOMETHING;
    } else if (className.startsWith("com.bitlworks.weddingalbum3_heba")) {
      return DOMAIN_HEBA;
    }
    return "http://music.bitlworks.co.kr";
    //return "http://14.63.160.237";
  }

  public static String getServiceUrl(Context context) {
    return getDomain(context) + "/mobilemusic/API/";
  }



  public static int first_check = 100;



  public static String youtube_key = "AIzaSyC2jENm1Uc7VnYo0tfhaWH9Iosoh8tRZPk";

  public static String y_data = "";

  public static final String PREFERENCE_NAME_USER_NO = "user.no";





}
