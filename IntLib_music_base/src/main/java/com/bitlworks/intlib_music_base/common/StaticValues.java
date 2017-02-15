package com.bitlworks.intlib_music_base.common;

import android.content.Context;

import com.bitlworks.intlib_music_base.common.source.PagerMainActivity;
import com.bitlworks.music._common.data.VOAlbum;
import com.bitlworks.music._common.data.VOComment2;
import com.bitlworks.music._common.data.VONewInfo;
import com.bitlworks.music._common.data.VOPhotoM;
import com.bitlworks.music._common.data.VOSinger;
import com.bitlworks.music._common.data.VOSong;
import com.bitlworks.music._common.data.VOUser;
import com.bitlworks.music._common.data.VOVideo;
import com.bitlworks.music._common.data.VOdisk;

import java.util.ArrayList;

public class StaticValues {
  public static final String DOMAIN_THIRDMIND = "http://thethirdmind.bitlworks.co.kr";
  public static final String DOMAIN_SOMETHING = "http://something.bitlworks.co.kr";
  public static final String DOMAIN_HEBA = "http://heba.bitlworks.co.kr";

  public static final String LOG_TAG = "비틀웍스";
  public static String SERVICE_URL;
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

  public static int album_id;
  public static int disk_id;

  public static String disk_name;

  public static int first_check = 100;



  public static String youtube_key = "AIzaSyC2jENm1Uc7VnYo0tfhaWH9Iosoh8tRZPk";

  public static String y_data = "";

  public static final String PREFERENCE_NAME_USER_NO = "user.no";

  public static int nework_check;//100: network ok,,,,0: network fail,,


  // 새로 정리한 것
  public static int playIndex = 0;
  public static VOUser myInfo;
  public static VOAlbum album;
  public static ArrayList<VOdisk> diskList;
  public static ArrayList<VOSong> songList;
  public static ArrayList<VOComment2> commentList;
  public static ArrayList<VOPhotoM> photoList;
  public static ArrayList<VOVideo> videoList;
  public static ArrayList<VOSinger> singerList;
  public static ArrayList<VONewInfo> newinfoList;
  public static PagerMainActivity pagerMainActivity;
}
