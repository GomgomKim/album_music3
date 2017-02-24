package com.bitlworks.intlib_music_base.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.bitlworks.intlib_music_base.StaticValues;

import java.util.ArrayList;

public class DAOSqlite {
  private final static String LOG_TAG_NAVI = "<DAO.class>";

  private static DAOSqlite instance;
  private DBOpenHelper DBHelper;
  private final Context context;

  private DAOSqlite(Context c) {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " DAO()");
    DBHelper = new DBOpenHelper(c); // DBHelper 초기화
    this.context = c;
  }

  public static DAOSqlite getInstance(Context c) {
    if (instance == null)
      instance = new DAOSqlite(c);
    return instance;
  }

  synchronized public void insertUser(VOUser voUser) {
    Log.i(StaticValues.LOG_TAG,
        LOG_TAG_NAVI + " insertUser (" + voUser.user_id + ")");
    SQLiteDatabase db = DBHelper.getWritableDatabase();

    db.execSQL("DELETE FROM user ;");
/*
"CREATE TABLE user("
				+"user_id INTEGER primary key,"
				+"user_name TEXT ,"
				+"mobile_music_id INTEGER ,"
				+"user_phone_number TEXT,"
				+"user_appver TEXT,"
				+"user_uuid TEXT,"
				+"user_regid TEXT,"
				+"os_type INTEGER);";
	*/
    String sql = "INSERT OR REPLACE INTO user(user_id, user_name, album_id, user_phone_number, user_appver,"
        + "user_uuid, user_regid, os_type, user_level) VALUES ("
        + voUser.user_id
        + ", '"
        + voUser.user_name
        + "', "
        + voUser.album_id
        + ", '"
        + voUser.user_phone_number
        + "', '"
        + voUser.user_appver
        + "', '"
        + voUser.user_uuid
        + "', '"
        + voUser.user_regid
        + "', "
        + 0
        + ","
        + voUser.user_level
        + " );";
    db.execSQL(sql);

    db.close();
  }

  synchronized public VOUser getUser() {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getUser()");
    VOUser result = null;

    SQLiteDatabase db = DBHelper.getReadableDatabase();
    Cursor cursor = db.rawQuery(
        "SELECT user_id, user_name, album_id, user_phone_number, "
            + "user_appver, user_uuid, user_regid, user_level"
            + " FROM user;", null);
    while (cursor.moveToNext()) {
      int i = 0;
      result = new VOUser(cursor.getInt(i++), cursor.getString(i++),
          cursor.getInt(i++), cursor.getString(i++),
          cursor.getString(i++), cursor.getString(i++),
          cursor.getString(i++), cursor.getInt(i++));
    }
    cursor.close();
    db.close();

    return result;
  }

  synchronized public void insertAlbum(VOAlbum voalbum) {
    Log.i(StaticValues.LOG_TAG,
        LOG_TAG_NAVI + " insertmobile_music (" + voalbum.album_id + ")");
    SQLiteDatabase db = DBHelper.getWritableDatabase();

    db.execSQL("DELETE FROM album ;");
    String sql = "INSERT OR REPLACE INTO album(album_id, album_name, singer_id_list, album_type, album_genre,"
        + "album_company, album_intro, album_time, album_invitemsg, album_inviteurl) VALUES ("
        + voalbum.album_id
        + ", '"
        + voalbum.album_name
        + "','"
        + voalbum.singer_id_list
        + "', '"
        + voalbum.album_type
        + "', '"
        + voalbum.album_genre
        + "', '"
        + voalbum.album_company
        + "', '"
        + voalbum.album_intro
        + "', '"
        + voalbum.album_time
        + "', '"
        + voalbum.album_invitemsg
        + "', '"
        + voalbum.album_inviteurl
        + "' );";
    db.execSQL(sql);

    db.close();
  }

  synchronized public VOAlbum getAlbum() {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getAlbum()");
    VOAlbum result = null;

    SQLiteDatabase db = DBHelper.getReadableDatabase();
    Cursor cursor = db.rawQuery(
        "SELECT album_id, album_name, singer_id_list, album_type, "
            + "album_genre, album_company, album_intro,album_time, album_invitemsg, album_inviteurl"
            + " FROM album;", null);
    while (cursor.moveToNext()) {
      int i = 0;
      result = new VOAlbum(
          cursor.getInt(i++),
          cursor.getString(i++),
          cursor.getString(i++),
          cursor.getString(i++),
          cursor.getString(i++),
          cursor.getString(i++),
          cursor.getString(i++),
          cursor.getString(i++),
          cursor.getString(i++),
          cursor.getString(i++));
    }
    cursor.close();
    db.close();

    return result;
  }

  synchronized public void insertDisks(ArrayList<VODisk> diskList) {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " insertSingerList ("
        + diskList.size() + ")");
    SQLiteDatabase db = DBHelper.getWritableDatabase();
    db.beginTransaction();
    try {
      db.execSQL("DELETE FROM disk ;");
      for (VODisk vodisk : diskList) {
        String sql = "INSERT INTO disk(disk_id,album_id, disk_name)"
            + "VALUES ("
            + vodisk.disk_id
            + ", "
            + vodisk.album_id
            + ", '"
            + vodisk.disk_name
            + "' );";
        db.execSQL(sql);
      }
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
      db.close();
    }
  }

  synchronized public ArrayList<VODisk> getDisks() {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getDisks()");
    ArrayList<VODisk> result = new ArrayList<VODisk>();


    SQLiteDatabase db = DBHelper.getReadableDatabase();
    Cursor cursor = db.rawQuery(
        "SELECT disk_id, disk_name, album_id"
            + " FROM disk "
            + "  ;", null);
    //int disk_id, String disk_name, int album_id
    while (cursor.moveToNext()) {
      int i = 0;
      VODisk vo = new VODisk(cursor.getInt(i++), cursor.getString(i++), cursor.getInt(i++));
      result.add(vo);
    }
    cursor.close();
    db.close();

    return result;
  }

  synchronized public void insertSongs(ArrayList<VOSong> songList, int disk_id) {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " insertSongs ("
        + songList.size() + ")");
    SQLiteDatabase db = DBHelper.getWritableDatabase();
    db.beginTransaction();
    try {
      db.execSQL("DELETE FROM song WHERE disk_id=" + disk_id + ";");
      for (VOSong voSong : songList) {

        String new_lyrics = voSong.song_lyric.replaceAll("'", "");
        String sql = "INSERT INTO song(song_id, song_name, disk_id, song_file_name, photo_id, song_order, msg1, msg2,"
            + "song_lyric) VALUES ("
            + voSong.song_id
            + ", '"
            + voSong.song_name
            + "', "
            + voSong.disk_id
            + ", '"
            + voSong.song_file_name
            + "', "
            + voSong.photo_id
            + ", "
            + voSong.song_order
            + ", '"
            + voSong.msg1
            + "', '"
            + voSong.msg2
            + "', '"
            + new_lyrics /*voSong.song_lyric.replaceAll("'","")*/
            + "' );";
        db.execSQL(sql);
      }
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
      db.close();
    }
  }

  synchronized public ArrayList<VOSong> getSongs(int disk_id) {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getSongs()");
    ArrayList<VOSong> result = new ArrayList<VOSong>();


    SQLiteDatabase db = DBHelper.getReadableDatabase();
    Cursor cursor = db.rawQuery(
        "SELECT song_id, song_name, disk_id, song_file_name, "
            + " photo_id, song_lyric, msg1, msg2, song_order FROM song "
            + " WHERE disk_id=" + disk_id + " ORDER BY song_id ASC ;", null);
    while (cursor.moveToNext()) {
      int i = 0;
      VOSong vo = new VOSong(cursor.getInt(i++), cursor.getString(i++), cursor.getInt(i++), cursor.getString(i++),
          cursor.getInt(i++), cursor.getString(i++), cursor.getString(i++), cursor.getString(i++), cursor.getInt(i++));
      result.add(vo);
    }
    cursor.close();
    db.close();

    return result;
  }

  synchronized public void insertPhotos(ArrayList<VOPhoto> photoList, int disk_id, int album_id) {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " insertPhotoList ("
        + photoList.size() + ")");
    SQLiteDatabase db = DBHelper.getWritableDatabase();
    db.beginTransaction();
    try {
      db.execSQL("DELETE FROM photo WHERE disk_id=" + disk_id + " OR (album_id=" + album_id + " AND type=3);");
      for (VOPhoto voPhoto : photoList) {
        String sql = "INSERT INTO photo(photo_id, disk_id, song_video_id, type, photo_file_name "
            + ", album_id, photo_order) VALUES ("
            + voPhoto.photo_id
            + ", "
            + voPhoto.disk_id
            + ", "
            + voPhoto.song_video_id
            + ", "
            + voPhoto.type
            + ", '"
            + voPhoto.photo_file_name
            + "', "
            + voPhoto.album_id
            + ", "
            + voPhoto.photo_order
            + " );";
        db.execSQL(sql);
      }
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
      db.close();
    }
  }

  synchronized public ArrayList<VOPhoto> getPhotos(int disk_id, int album_id) {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getPhotos()");
    ArrayList<VOPhoto> result = new ArrayList<VOPhoto>();


    SQLiteDatabase db = DBHelper.getReadableDatabase();
    Cursor cursor = db.rawQuery(
        "SELECT photo_id, disk_id, song_video_id, type, "
            + " photo_file_name, album_id, photo_order FROM photo "
            + " WHERE disk_id=" + disk_id + " OR (album_id=" + album_id + " AND type=3) ORDER BY photo_order ASC ;", null);
    while (cursor.moveToNext()) {
      int i = 0;
      VOPhoto vo = new VOPhoto(cursor.getInt(i++), cursor.getInt(i++), cursor.getInt(i++), cursor.getInt(i++),
          cursor.getString(i++), cursor.getInt(i++), cursor.getInt(i++));
      result.add(vo);
    }
    cursor.close();
    db.close();

    return result;
  }

  synchronized public void insertVideos(ArrayList<VOVideo> videoList) {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " insertVideos ("
        + videoList.size() + ")");
    SQLiteDatabase db = DBHelper.getWritableDatabase();
    db.beginTransaction();
    try {
      db.execSQL("DELETE FROM video ;");
      for (VOVideo voVideo : videoList) {
        String sql = "INSERT INTO video(video_id, song_id, album_id, video_file_name,video_name, photo_id) "
            + " VALUES ("
            + voVideo.video_id
            + ", "
            + voVideo.song_id
            + ", "
            + voVideo.album_id
            + ", '"
            + voVideo.video_file_name
            + "', '"
            + voVideo.video_name
            + "', "
            + voVideo.photo_id
            + " );";
        db.execSQL(sql);
      }
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
      db.close();
    }
  }

  synchronized public ArrayList<VOVideo> getVideos(int album_id) {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getVideos()");
    ArrayList<VOVideo> result = new ArrayList<VOVideo>();


    SQLiteDatabase db = DBHelper.getReadableDatabase();
    Cursor cursor = db.rawQuery(
        "SELECT video_id, song_id, album_id, video_file_name, "
            + " video_name,photo_id FROM video "
            + " WHERE album_id=" + album_id + " ORDER BY video_id ASC;", null);
    while (cursor.moveToNext()) {
      int i = 0;
      VOVideo vo = new VOVideo(cursor.getInt(i++), cursor.getInt(i++), cursor.getInt(i++), cursor.getString(i++), cursor.getString(i++),
          cursor.getInt(i++));
      result.add(vo);
    }
    cursor.close();
    db.close();

    return result;
  }

  synchronized public void insertComments(ArrayList<VOComment> commentList) {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " insertComments ("
        + commentList.size() + ")");
    SQLiteDatabase db = DBHelper.getWritableDatabase();
    db.beginTransaction();
    try {
      db.execSQL("DELETE FROM comment ;");
      for (VOComment voComment : commentList) {
        String sql = "INSERT INTO comment(comment_id, user_id, user_name, comment_time, comment_contents, album_id) "
            + " VALUES ("
            + voComment.comment_id
            + ", "
            + voComment.user_id
            + ",'"
            + voComment.user_name
            + "', '"
            + voComment.comment_time
            + "', '"
            + voComment.comment_contents
            + "',"
            + voComment.album_id
            + " );";
        db.execSQL(sql);
      }
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
      db.close();
    }
  }

  synchronized public ArrayList<VOComment> getComments(int album_id) {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getComments()");
    ArrayList<VOComment> result = new ArrayList<VOComment>();


    SQLiteDatabase db = DBHelper.getReadableDatabase();
    Cursor cursor = db.rawQuery(
        "SELECT comment_id, user_id, user_name, comment_time,comment_contents, "
            + " album_id FROM comment "
            + " WHERE album_id=" + album_id + " ORDER BY comment_id ASC ;", null);
    while (cursor.moveToNext()) {
      int i = 0;
      VOComment vo = new VOComment(cursor.getInt(i++), cursor.getInt(i++), cursor.getString(i++), cursor.getString(i++),
          cursor.getString(i++), cursor.getInt(i++));
      result.add(vo);
    }
    cursor.close();
    db.close();

    return result;
  }

  synchronized public void insertNewInofs(ArrayList<VONewInfo> newInfoList) {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " insertNewInofs ("
        + newInfoList.size() + ")");
    SQLiteDatabase db = DBHelper.getWritableDatabase();
    db.beginTransaction();
    try {
      db.execSQL("DELETE FROM new_info ;");
      for (VONewInfo voNewInfo : newInfoList) {
        String sql = "INSERT INTO new_info(info_id, album_id, mainSubjectText, timeText, subjectImage, contentText,link_url) "
            + " VALUES ("
            + voNewInfo.info_id
            + ", "
            + voNewInfo.album_id
            + ",'"
            + voNewInfo.main_subject
            + "', '"
            + voNewInfo.time
            + "', '"
            + voNewInfo.image_data
            + "','"
            + voNewInfo.contents
            + "','"
            + voNewInfo.link_url
            + "' );";
        db.execSQL(sql);
      }
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
      db.close();
    }
  }

  synchronized public ArrayList<VONewInfo> getNewInfos(int album_id) {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getNewInfos()");
    ArrayList<VONewInfo> result = new ArrayList<VONewInfo>();


    SQLiteDatabase db = DBHelper.getReadableDatabase();
    Cursor cursor = db.rawQuery(
        "SELECT info_id, album_id, mainSubjectText, timeText,subjectImage, "
            + " contentText,link_url FROM new_info "
            + " WHERE album_id=" + album_id + " ORDER BY info_id DESC ;", null);
    while (cursor.moveToNext()) {
      int i = 0;
      VONewInfo vo = new VONewInfo(cursor.getInt(i++), cursor.getInt(i++), cursor.getString(i++), cursor.getString(i++),
          cursor.getString(i++), cursor.getString(i++), cursor.getString(i++));
      result.add(vo);
    }
    cursor.close();
    db.close();

    return result;
  }
}
