package com.bitlworks.intlib_music_base;

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
  public static final int NOTIFICATION_ID = 1;
  public static final String PREFERENCE_NAME_SELECTED_ALBUM = "selectedAlbum";
  public static final String PREFERENCE_NAME_SELECTED_ALBUM_DATE = "selectedAlbumDate";
  public static final String PREFERENCE_NAME_SELECTED_USER_ID = "selectedUserId";
  public static final String PREFERENCE_NAME_SELECTED_USER_NAME = "selectedUserName";
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
  public static ArrayList<VONewInfo> newInfos = new ArrayList<>();
  // Reference
  public static PagerMainActivity pagerMainActivity;
  public static String NOTIFICATION_MESSAGE = "";
  public static int NOTIFICATION_FLAG = -1;
  public static int unread_count = 0;
  public static int first_check = 100;
}
