package com.bitlworks.intlib_music_base.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bitlworks.intlib_music_base.StaticValues;


public class DBOpenHelper extends SQLiteOpenHelper {
  private final String LOG_TAG_NAVI = "<DBOpenHelper.class>";

  public DBOpenHelper(Context context) {
    super(context, "WA.db", null, 59);
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " DBOpenHelper()");
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " onCreate()");
    createTableSet(db);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " onUpgrade()");
    db.execSQL("DROP TABLE IF EXISTS user ;");
    db.execSQL("DROP TABLE IF EXISTS album ;");
    db.execSQL("DROP TABLE IF EXISTS metadata ;");
    db.execSQL("DROP TABLE IF EXISTS disk ;");
    db.execSQL("DROP TABLE IF EXISTS song ;");
    db.execSQL("DROP TABLE IF EXISTS photo ;");
    db.execSQL("DROP TABLE IF EXISTS comment ;");
    db.execSQL("DROP TABLE IF EXISTS video ;");
    db.execSQL("DROP TABLE IF EXISTS new_info ;");
    createTableSet(db);
  }

  private void createTableSet(SQLiteDatabase db) {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " createTableSet()");
    String sql = "";
    sql = "CREATE TABLE album("
        + "album_id INTEGER primary key,"
        + "album_name TEXT,"
        + "singer_id_list TEXT,"
        + "album_type TEXT,"
        + "image_type TEXT,"
        + "album_genre TEXT,"
        + "album_company TEXT,"
        + "album_intro TEXT,"
        + "album_time TEXT,"
        + "album_invitemsg TEXT,"
        + "album_inviteurl TEXT);";
    db.execSQL(sql);

    sql = "CREATE TABLE metadata("
        + "album_id INTEGER primary key,"
        + "album_cover TEXT,"
        + "disk_bg TEXT,"
        + "review_bg TEXT,"
        + "setting_bg TEXT,"
        + "color INTEGER,"
        + "title_image TEXT,"
        + "main_image TEXT,"
        + "music_player_bg TEXT,"
        + "song_play_icon TEXT,"
        + "song_pause_icon TEXT,"
        + "song_list_icon TEXT,"
        + "lyrics_icon TEXT,"
        + "disk_icon TEXT,"
        + "mini_icon TEXT"
        + ");";
    db.execSQL(sql);

    sql = "CREATE TABLE disk("
        + "disk_id INTEGER primary key,"
        + "disk_name TEXT,"
        + "disk_icon TEXT,"
        + "album_id INTEGER);";

    db.execSQL(sql);

    sql = "CREATE TABLE photo("
        + "photo_id INTEGER primary key,"
        + "disk_id INTEGER ,"
        + "photo_file_name TEXT,"
        + "photo_order INTEGER);";
    db.execSQL(sql);

    sql = "CREATE TABLE song("
        + "song_id INTEGER primary key,"
        + "song_name TEXT ,"
        + "disk_id INTEGER ,"
        + "song_file_name TEXT,"
        + "photo_id INTEGER,"
        + "song_order INTEGER,"
        + "msg1 TEXT,"
        + "msg2 TEXT,"
        + "song_lyric TEXT);";
    db.execSQL(sql);

    sql = "CREATE TABLE user("
        + "user_id INTEGER primary key,"
        + "user_name TEXT ,"
        + "album_id INTEGER ,"
        + "user_phone_number TEXT,"
        + "user_appver TEXT,"
        + "user_uuid TEXT,"
        + "user_regid TEXT,"
        + "os_type INTEGER,"
        + "user_level INTEGER);";
    db.execSQL(sql);

    sql = "CREATE TABLE video("
        + "video_id INTEGER primary key,"
        + "album_id INTEGER ,"
        + "video_file_name TEXT,"
        + "video_name TEXT,"
        + "photo_path TEXT);";
    db.execSQL(sql);

    sql = "CREATE TABLE comment("
        + "comment_id INTEGER primary key,"
        + "user_id INTEGER ,"
        + "user_name TEXT,"
        + "comment_time TEXT,"
        + "comment_contents TEXT,"
        + "album_id INTEGER );";
    db.execSQL(sql);

    sql = "CREATE TABLE new_info("
        + "info_id INTEGER primary key,"
        + "album_id INTEGER ,"
        + "mainSubjectText TEXT,"
        + "timeText TEXT,"
        + "subjectImage TEXT,"
        + "contentText TEXT,"
        + "link_url TEXT );";
    db.execSQL(sql);
  }

}
