package com.bitlworks.intlib_music_base.common.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bitlworks.intlib_music_base.common.StaticValues;


public class DBOpenHelper extends SQLiteOpenHelper {
	private final String LOG_TAG_NAVI = "<DBOpenHelper.class>";

	public DBOpenHelper(Context context) {
		super(context, "WA.db", null, 50);
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
        db.execSQL("DROP TABLE IF EXISTS WA_Couple ;");  
        db.execSQL("DROP TABLE IF EXISTS WA_Theme ;");  
        db.execSQL("DROP TABLE IF EXISTS WA_Photo ;");  
        db.execSQL("DROP TABLE IF EXISTS WA_Comment ;");  
        db.execSQL("DROP TABLE IF EXISTS WA_Photo2Like ;");        
        db.execSQL("DROP TABLE IF EXISTS WA_Theme_Dynamic ;");
        db.execSQL("DROP TABLE IF EXISTS WA_FriendsShot ;");
        createTableSet(db);
	}
	
	private void createTableSet(SQLiteDatabase db) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " createTableSet()");
		String sql = "CREATE TABLE WA_Couple("
				+ "couple_id INTEGER primary key,"
				+ "couple_state INTEGER,"
                + "couple_mr TEXT,"
                + "couple_mrs TEXT,"
                + "couple_time TEXT,"
                + "couple_path TEXT,"
                + "couple_video TEXT,"
                + "couple_image INTEGER,"
                + "couple_photo TEXT,"
                + "couple_dirsize TEXT,"
                + "couple_mr_en TEXT,"
                + "couple_mrs_en TEXT,"
                + "hall_name_kr TEXT,"
                + "hall_name_en TEXT,"
                + "hall_address TEXT,"
                + "hall_way1 TEXT,"
                + "hall_way2 TEXT,"
                + "hall_mapx TEXT,"
                + "hall_mapy TEXT,"
                + "hall_tel TEXT,"
                + "couple_update INTEGER,"  
				+ "couple_like INTEGER,"
				+ "couple_music TEXT,"
				+ "music_inpath TEXT,"
				+ "visible INTEGER,"
				+ "album_title TEXT,"
				+ "album_msg TEXT"
                + ");";  
	    db.execSQL(sql);
	    
	    sql = "CREATE TABLE WA_Theme("
	    		+ "theme_id INTEGER primary key,"
	            + "couple_id INTEGER,"
	            + "theme_path TEXT,"
	            + "theme_name TEXT,"
	            + "theme_description TEXT,"
	            + "theme_effect INTEGER,"
	            + "theme_sequence INTEGER,"
	            + "theme_isHide INTEGER,"
	    		+ "theme_type INTEGER);";
	    db.execSQL(sql);
	    
	    sql = "CREATE TABLE WA_Photo("
	    		+ "photo_id INTEGER primary key,"
	            + "theme_id INTEGER,"
	            + "dtheme_id INTEGER,"
	            + "couple_id INTEGER,"
	            + "photo_path TEXT,"
	            + "photo_isPublic INTEGER,"
	            + "photo_isSelect INTEGER,"
	            + "photo_isHeightrLarge INTEGER,"
	            + "photo_time TEXT,"
	            + "photo_sequence INTEGER,"
	            + "photo_url TEXT,"
	    		+ "photo_inpath TEXT);";
	    db.execSQL(sql);
	    
	    sql = "CREATE TABLE WA_Comment("
	    		+ "comment_id INTEGER primary key,"
	    		+ "photo_id INTEGER,"
	    		+ "couple_id INTEGER,"        		
	    		+ "user_id INTEGER,"
	    		+ "user_name TEXT,"
	    		+ "comment_time TEXT,"
	    		+ "comment_contents TEXT);";
	    db.execSQL(sql);
	
	    sql = "CREATE TABLE WA_Photo2Like("
	    		+ "photo_id INTEGER,"
	    		+ "user_id INTEGER);";
	    db.execSQL(sql);
	    
	    sql = "CREATE TABLE WA_Theme_Dynamic("
	    		+ "dtheme_id INTEGER primary key,"
	    		+ "couple_id INTEGER,"
	    		+ "dtheme_type INTEGER,"
	    		+ "dtheme_isHide INTEGER,"
	    		+ "dtheme_sequence INTEGER,"
	    		+ "dtheme_name TEXT,"
	    		+ "dtheme_description TEXT);";
	    db.execSQL(sql);
	    
	    sql = "CREATE TABLE WA_FriendsShot("
	    		+ "fs_id INTEGER primary key,"
	    		+ "photo_id INTEGER,"
	    		+ "couple_id INTEGER,"
	    		+ "user_id INTEGER,"
	    		+ "user_name TEXT,"
	    		+ "fs_time TEXT,"
	    		+ "fs_comment TEXT,"
	    		+ "count_like INTEGER,"
	    		+ "count_comment INTEGER);";
	    db.execSQL(sql);
	    ///////////////////////////////////////////////////////////////

		sql = "CREATE TABLE album("
				+ "album_id INTEGER primary key,"
				+ "album_name TEXT,"
				+ "singer_id_list TEXT,"
		        + "album_type TEXT," +
				"album_genre TEXT," +
				"album_company TEXT," +
				"album_intro TEXT," +
				"album_time TEXT);";
		db.execSQL(sql);

		sql = "CREATE TABLE disk("
				+ "disk_id INTEGER primary key,"
				+ "disk_name TEXT,"
				+ "album_id INTEGER);";

		db.execSQL(sql);

		sql = "CREATE TABLE photo("
				+"photo_id INTEGER primary key,"
				+"disk_id INTEGER ,"
				+"song_video_id INTEGER ,"
				+"type INTEGER ,"
				+"photo_file_name TEXT,"
				+"album_id INTEGER,"
				+"photo_order INTEGER);";

		db.execSQL(sql);

		sql = "CREATE TABLE singer("
				+"singer_id INTEGER primary key,"
				+"singer_name TEXT);";


		db.execSQL(sql);

		sql = "CREATE TABLE song("
				+"song_id INTEGER primary key,"
				+"song_name TEXT ,"
				+"disk_id INTEGER ,"
				+"song_file_name TEXT,"
				+"photo_id INTEGER,"
				+"song_order INTEGER,"
				+"msg1 TEXT,"
				+"msg2 TEXT,"
				+"song_lyric TEXT);";


		db.execSQL(sql);

		sql = "CREATE TABLE user("
				+"user_id INTEGER primary key,"
				+"user_name TEXT ,"
				+"album_id INTEGER ,"
				+"user_phone_number TEXT,"
				+"user_appver TEXT,"
				+"user_uuid TEXT,"
				+"user_regid TEXT,"
				+"os_type INTEGER,"
		        +"user_level INTEGER);";



		db.execSQL(sql);

		sql = "CREATE TABLE video("
				+"video_id INTEGER primary key,"
				+"song_id INTEGER ,"
				+"album_id INTEGER ,"
				+"video_file_name TEXT,"
				+"video_name TEXT,"
				+"photo_id INTEGER);";




		db.execSQL(sql);

		sql = "CREATE TABLE comment("
				+"comment_id INTEGER primary key,"
				+"user_id INTEGER ,"
				+"user_name TEXT,"
				+"comment_time TEXT,"
				+"comment_contents TEXT,"
				+"album_id INTEGER );";





		db.execSQL(sql);

///////////////////////////////////

		sql = "CREATE TABLE new_info("
				+"info_id INTEGER primary key,"
				+"album_id INTEGER ,"
				+"main_subject TEXT,"
				+"time TEXT,"
				+"image_data TEXT,"
				+"contents TEXT,"
				+"link_url TEXT );";
				//+"mobile_music_id INTEGER );";





		db.execSQL(sql);
	}

}
