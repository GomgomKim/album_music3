package com.bitlworks.intlib_music_base;

import com.bitlworks.intlib_music_base.data.VOAlbum;
import com.bitlworks.intlib_music_base.data.VOComment;
import com.bitlworks.intlib_music_base.data.VOMetadata;
import com.bitlworks.intlib_music_base.data.VONewInfo;
import com.bitlworks.intlib_music_base.data.VOPhoto;
import com.bitlworks.intlib_music_base.data.VOSong;
import com.bitlworks.intlib_music_base.data.VOUser;
import com.bitlworks.intlib_music_base.data.VOVideo;
import com.bitlworks.intlib_music_base.data.VODisk;
import com.bitlworks.intlib_music_base.source.PagerMainActivity;

import java.util.ArrayList;

public class StaticValues {
  public static final String LOG_TAG = "비틀웍스";
  public static final int NOTIFICATION_ID = 1;

  // 앱 실행 중 쓰이는 값들
  public static int playIndex = -1;
  public static VOUser user;
  public static VOAlbum album;
  public static VOMetadata metadata;
  public static ArrayList<VODisk> disks = new ArrayList<>();
  public static ArrayList<VOVideo> videos = new ArrayList<>();
  public static ArrayList<VONewInfo> newInfos = new ArrayList<>();
  public static ArrayList<VOComment> comments = new ArrayList<>();
  public static VODisk selectedDisk;
  public static ArrayList<VOSong> songs = new ArrayList<>();
  public static ArrayList<VOPhoto> photos = new ArrayList<>();

  // Reference
  public static PagerMainActivity pagerMainActivity;
  public static String NOTIFICATION_MESSAGE = "";
  public static int NOTIFICATION_FLAG = -1;
  public static int unread_count = 0;
  public static int first_check = 100;

  // Key
  public static final String METADATA_VERSION = "METADATA_VERSION";
  public static final String SONG_VERSION = "SONG_VERSION";
  public static final String PHOTO_VERSION = "PHOTO_VERSION";
}
