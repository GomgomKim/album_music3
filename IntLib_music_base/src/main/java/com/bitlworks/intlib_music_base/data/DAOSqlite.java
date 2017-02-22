package com.bitlworks.intlib_music_base.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.bitlworks.intlib_music_base.StaticValues;

import org.json.JSONArray;
import org.json.JSONObject;

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

	/** 커플리스트의 존재 여부를 확인합니다 **/
	synchronized public boolean isExistsCoupleList() {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " isExistsCoupleList()");
		boolean result = false;

		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT couple_id FROM WA_Couple ;", null);
		if (cursor.getCount() != 0)
			result = true;
		cursor.close();
		db.close();

		return result;
	} // END isExistsCoupleList();

	/** 커플id 리스트를 가져옵니다 **/
	synchronized public String getCoupleIdList() {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getCoupleIdList()");
		String list = "-1";

		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT couple_id FROM WA_Couple", null);

		while (cursor.moveToNext()) {
			list += "," + cursor.getInt(0);
		}
		cursor.close();
		db.close();

		return list;
	} // END getCoupleIdList();

	/** 한 커플의 정보를 가져오는 메소드 **/
	synchronized public VOCouple getCouple(int coupleId) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getCouple()");
		VOCouple result = null;

		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT couple_id, couple_state, couple_mr, couple_mrs, couple_time, "
								+ "couple_path, couple_video, couple_image, couple_photo, couple_dirsize, couple_mr_en, couple_mrs_en, "
								+ "hall_name_kr, hall_name_en, hall_address, hall_way1, hall_way2, hall_mapx, hall_mapy, hall_tel, "
								+ "couple_update, couple_like, couple_music, music_inpath, visible,album_title, album_msg "
								+ "FROM WA_Couple WHERE couple_id=" + coupleId
								+ " ;", null);
		while (cursor.moveToNext()) {
			int i = 0;
			result = new VOCouple(cursor.getInt(i++), cursor.getInt(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++), cursor.getInt(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getInt(i++), cursor.getInt(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getInt(i++),cursor.getString(i++),cursor.getString(i++));
		}
		cursor.close();
		db.close();

		return result;
	} // END getCouple();

	/** 커플 목록을 가져오는 메소드 **/
	synchronized public ArrayList<VOCouple> getCoupleList(boolean isVisibleOnly) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getCouple()");
		ArrayList<VOCouple> result = new ArrayList<VOCouple>();

		final String QUERY_WHERE = (isVisibleOnly) ? " WHERE visible=1 " : "";

		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT couple_id, couple_state, couple_mr, couple_mrs, couple_time, "
								+ "couple_path, couple_video, couple_image, couple_photo, couple_dirsize, couple_mr_en, couple_mrs_en, "
								+ "hall_name_kr, hall_name_en, hall_address, hall_way1, hall_way2, "
								+ "hall_mapx, hall_mapy, hall_tel, couple_update, couple_like, couple_music, music_inpath, visible,album_title,album_msg "
								+ "FROM WA_Couple " + QUERY_WHERE
								+ " ORDER BY couple_time DESC ;", null);
		VOCouple vo;
		while (cursor.moveToNext()) {
			int i = 0;
			vo = new VOCouple(cursor.getInt(i++), cursor.getInt(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++), cursor.getInt(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getInt(i++), cursor.getInt(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getInt(i++),cursor.getString(i++),cursor.getString(i++)   );
			result.add(vo);
		}
		vo = null;
		cursor.close();
		db.close();

		return result;
	} // END getCouple();

	/** 테마 정보를 반환하는 메소드 **/
	synchronized public VOTheme getTheme(int themeId) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getPhotos()");
		VOTheme result = null;

		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT theme_id, couple_id, theme_path, theme_name, "
						+ "theme_description, theme_effect, theme_sequence, "
						+ "theme_isHide, theme_type FROM WA_Theme "
						+ "WHERE theme_id=" + themeId
						+ " ORDER BY theme_sequence ASC ;", null);
		while (cursor.moveToNext()) {
			int i = 0;
			result = new VOTheme(cursor.getInt(i++), cursor.getInt(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++), cursor.getInt(i++),
					cursor.getInt(i++), cursor.getInt(i++), cursor.getInt(i++));
		}
		cursor.close();
		db.close();

		return result;
	} // END getPhotos();
////////////////////////////
	/** 테마 정보를 반환하는 메소드 **/
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

				public int user_id;
    public String user_name;
    public int mobile_music_id;
    public String user_phone_number;
    public String user_appver;
    public String user_uuid;
    public String user_regid;

    int user_id, String user_name, int album_id, String user_phone_number,String user_appver,
                  String user_uuid, String user_regid, int user_level
	*/
	synchronized public VOUser getUser() {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getUser()");
		VOUser result = null;

		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT user_id, user_name, album_id, user_phone_number, "
						+ "user_appver, user_uuid, user_regid, user_level"
						+ " FROM user;"	, null);
		while (cursor.moveToNext()) {
			int i = 0;
			result = new VOUser(cursor.getInt(i++), cursor.getString(i++),
					cursor.getInt(i++), cursor.getString(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++),cursor.getInt(i++));
		}
		cursor.close();
		db.close();

		return result;
	} // END getPhotos();

	synchronized public VOAlbum getalbum() {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getalbum()");
		VOAlbum result = null;

		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT album_id, album_name, singer_id_list, album_type, "
						+ "album_genre, album_company, album_intro,album_time, album_invitemsg, album_inviteurl"
						+ " FROM album;"	, null);
		while (cursor.moveToNext()) {
			int i = 0;
			result = new VOAlbum(
					cursor.getInt(i++),
					cursor.getString(i++), cursor.getString(i++), cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++), cursor.getString(i++), cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++));
		}
		cursor.close();
		db.close();

		return result;
	} // END getPhotos();
	/////////////////////////////

	/** 테마 목록을 반환하는 메소드 **/
	synchronized public ArrayList<VOPhotoM> getphotoList(int disk_id, int album_id) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getphotoList()");
		ArrayList<VOPhotoM> result = new ArrayList<VOPhotoM>();


		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT photo_id, disk_id, song_video_id, type, "
						+ " photo_file_name, album_id, photo_order FROM photo "
						+ " WHERE disk_id="+disk_id+" OR (album_id="+album_id+" AND type=3) ORDER BY photo_order ASC ;", null);
		while (cursor.moveToNext()) {
			int i = 0;
			VOPhotoM vo = new VOPhotoM(cursor.getInt(i++), cursor.getInt(i++), cursor.getInt(i++), cursor.getInt(i++),
					cursor.getString(i++),  cursor.getInt(i++),cursor.getInt(i++));
			result.add(vo);
		}
		cursor.close();
		db.close();

		return result;
	} // END getThemeList();
	/** 테마 목록을 반환하는 메소드 **/
	/*
	"CREATE TABLE singer("
				+"singer_id INTEGER primary key,"
				+"singer_name TEXT);";

				 public int id;
    public String name;
	*/
	synchronized public ArrayList<VOSinger> getsingerList() {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getsingerList()");
		ArrayList<VOSinger> result = new ArrayList<VOSinger>();


		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT singer_id, singer_name"
						+ " FROM singer "
						+ "  ;", null);
		while (cursor.moveToNext()) {
			int i = 0;
			VOSinger vo = new VOSinger(cursor.getInt(i++),cursor.getString(i++));
			result.add(vo);
		}
		cursor.close();
		db.close();

		return result;
	} // END getThemeList();

	synchronized public ArrayList<VOdisk> getdiskList() {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getdiskList()");
		ArrayList<VOdisk> result = new ArrayList<VOdisk>();


		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT disk_id, disk_name, album_id"
						+ " FROM disk "
						+ "  ;", null);
		//int disk_id, String disk_name, int album_id
		while (cursor.moveToNext()) {
			int i = 0;
			VOdisk vo = new VOdisk(cursor.getInt(i++),cursor.getString(i++),cursor.getInt(i++));
			result.add(vo);
		}
		cursor.close();
		db.close();

		return result;
	} // END getThemeList();
/*
"CREATE TABLE song("
				+"song_id INTEGER primary key,"
				+"song_name TEXT ,"
				+"mobile_music_id INTEGER ,"
				+"song_file_name TEXT,"
				+"photo_id INTEGER,"
				+"song_lyric TEXT);";

				 public int song_id;
    public String song_name;
    public int mobile_music_id;
    public String song_file_name;
    public int photo_id;
    public String song_lyric;

    int song_id, String song_name, int disk_id, String song_file_name, int photo_id,
                    String song_lyric, String msg1, String msg2, int song_order
	*/
	synchronized public ArrayList<VOSong> getsongList(int disk_id) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getsongList()");
		ArrayList<VOSong> result = new ArrayList<VOSong>();


		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT song_id, song_name, disk_id, song_file_name, "
						+ " photo_id, song_lyric, msg1, msg2, song_order FROM song "
						+ " WHERE disk_id="+disk_id+" ORDER BY song_id ASC ;", null);
		while (cursor.moveToNext()) {
			int i = 0;
			VOSong vo = new VOSong(cursor.getInt(i++), cursor.getString(i++), cursor.getInt(i++), cursor.getString(i++),
					cursor.getInt(i++),  cursor.getString(i++), cursor.getString(i++), cursor.getString(i++),cursor.getInt(i++));
			result.add(vo);
		}
		cursor.close();
		db.close();

		return result;
	} // END getThemeList();
/*
"CREATE TABLE video("
				+"video_id INTEGER primary key,"
				+"song_id INTEGER ,"
				+"mobile_music_id INTEGER ,"
				+"video_file_name TEXT,"
				+"photo_id INTEGER);";

				 public int video_id;
    public int song_id;
    public int mobile_music_id;
    public String video_file_name;
    public int photo_id;

    sql = "CREATE TABLE video("
				+"video_id INTEGER primary key,"
				+"song_id INTEGER ,"
				+"album_id INTEGER ,"
				+"video_file_name TEXT,"
				+"video_name TEXT,"
				+"photo_id INTEGER);";
	*/
	synchronized public ArrayList<VOVideo> getvideoList(int album_id) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getvideoList()");
		ArrayList<VOVideo> result = new ArrayList<VOVideo>();


		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT video_id, song_id, album_id, video_file_name, "
						+ " video_name,photo_id FROM video "
						+ " WHERE album_id="+album_id+" ORDER BY video_id ASC;", null);
		while (cursor.moveToNext()) {
			int i = 0;
			VOVideo vo = new VOVideo(cursor.getInt(i++), cursor.getInt(i++), cursor.getInt(i++), cursor.getString(i++),cursor.getString(i++),
					cursor.getInt(i++));
			result.add(vo);
		}
		cursor.close();
		db.close();

		return result;
	} // END getThemeList();
/*
 "CREATE TABLE comment("
				+"comment_id INTEGER primary key,"
				+"user_id INTEGER ,"
				+"user_name TEXT,"
				+"comment_time TEXT,"
				+"comment_contents TEXT,"
				+"mobile_music_id INTEGER );";

				 public int comment_id;
    public int user_id;
    public String user_name;
    public String comment_time;
    public String comment_contents;
    public int mobile_music_id;


    sql = "CREATE TABLE comment("
				+"comment_id INTEGER primary key,"
				+"user_id INTEGER ,"
				+"user_name TEXT,"
				+"comment_time TEXT,"
				+"comment_contents TEXT,"
				+"album_id INTEGER );";
	*/
	synchronized public ArrayList<VOComment> getcommentList(int album_id) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getcommentList()");
		ArrayList<VOComment> result = new ArrayList<VOComment>();


		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT comment_id, user_id, user_name, comment_time,comment_contents, "
						+ " album_id FROM comment "
						+ " WHERE album_id="+album_id+" ORDER BY comment_id ASC ;", null);
		while (cursor.moveToNext()) {
			int i = 0;
			VOComment vo = new VOComment(cursor.getInt(i++), cursor.getInt(i++), cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++),cursor.getInt(i++));
			result.add(vo);
		}
		cursor.close();
		db.close();

		return result;
	} // END getThemeList();

	/*
	sql = "CREATE TABLE new_info("
				+"info_id INTEGER primary key,"
				+"album_id INTEGER ,"
				+"main_subject TEXT,"
				+"time TEXT,"
				+"image_data TEXT,"
				+"contents TEXT,"
				+"link_url TEXT );";
	*/
	synchronized public ArrayList<VONewInfo> getnewInfoList(int album_id) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getnewInfoList()");
		ArrayList<VONewInfo> result = new ArrayList<VONewInfo>();


		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT info_id, album_id, main_subject, time,image_data, "
						+ " contents,link_url FROM new_info "
						+ " WHERE album_id="+album_id +" ORDER BY info_id DESC ;", null);
		while (cursor.moveToNext()) {
			int i = 0;
			VONewInfo vo = new VONewInfo(cursor.getInt(i++), cursor.getInt(i++), cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++),cursor.getString(i++),cursor.getString(i++));
			result.add(vo);
		}
		cursor.close();
		db.close();

		return result;
	} // END getThemeList();
	//////////////////////////
	/** 테마 목록을 반환하는 메소드 **/
	synchronized public ArrayList<VOTheme> getThemeList(int coupleId,
																											boolean withoutHideTheme) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getThemeList()");
		ArrayList<VOTheme> result = new ArrayList<VOTheme>();

		String query_where_hide = "";
		if (withoutHideTheme)
			query_where_hide = " AND theme_isHide = 0 ";

		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT theme_id, couple_id, theme_path, theme_name, "
						+ " theme_description, theme_effect, theme_sequence, "
						+ " theme_isHide, theme_type FROM WA_Theme "
						+ " WHERE couple_id=" + coupleId + query_where_hide
						+ " ORDER BY theme_sequence ASC ;", null);
		while (cursor.moveToNext()) {
			int i = 0;
			VOTheme vo = new VOTheme(cursor.getInt(i++), cursor.getInt(i++),
					cursor.getString(i++), cursor.getString(i++),
					cursor.getString(i++), cursor.getInt(i++),
					cursor.getInt(i++), cursor.getInt(i++), cursor.getInt(i++));
			result.add(vo);
		}
		cursor.close();
		db.close();

		return result;
	} // END getThemeList();

	/** 앨범의 사진 목록을 반환하는 메소드 **/
	synchronized public ArrayList<VOPhoto> getPhotoList(int coupleId,
																											boolean visibleImageOnly, boolean isHideGangi) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getPhotoList()");
		ArrayList<VOPhoto> result = new ArrayList<VOPhoto>();

		String queryWhere = "";
		if (visibleImageOnly)
			queryWhere += " AND photo_isPublic=1 ";
		if (isHideGangi)
			queryWhere += " AND photo_sequence>0 ";

		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT photo_id, WA_Photo.theme_id, dtheme_id, WA_Photo.couple_id, photo_path, "
								+ "photo_isPublic, photo_isSelect, photo_isHeightrLarge, photo_time, photo_sequence, photo_url, photo_inpath "
								+ "FROM WA_Photo, WA_Theme WHERE WA_Photo.theme_id = WA_Theme.theme_id AND WA_Photo.couple_id="
								+ coupleId
								+ queryWhere
								+ " ORDER BY theme_sequence ASC, photo_sequence ASC, photo_time DESC, photo_path ASC ;",
						null);
		while (cursor.moveToNext()) {
			int i = 0;
			VOPhoto vo = new VOPhoto(cursor.getInt(i++), cursor.getInt(i++),
					cursor.getInt(i++), cursor.getInt(i++),
					cursor.getString(i++), cursor.getInt(i++),
					cursor.getInt(i++), cursor.getInt(i++),
					cursor.getString(i++), cursor.getInt(i++));
			vo.setPhoto_url(cursor.getString(i++));
			vo.setPhoto_inpath(cursor.getString(i++));
			result.add(vo);
		}
		cursor.close();
		db.close();

		return result;
	} // END getPhotoList();

	/** 앨범의 사진 목록을 반환하는 메소드 **/
	synchronized public ArrayList<VOPhoto> getDownPhotoList(int coupleId) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getPhotoList()");
		ArrayList<VOPhoto> result = new ArrayList<VOPhoto>();

		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT photo_id, theme_id, dtheme_id, couple_id, photo_path, "
								+ "photo_isPublic, photo_isSelect, photo_isHeightrLarge, photo_time, photo_sequence, photo_url, photo_inpath "
								+ "FROM WA_Photo WHERE couple_id=" + coupleId
								+ " ;", null);
		while (cursor.moveToNext()) {
			int i = 0;
			VOPhoto vo = new VOPhoto(cursor.getInt(i++), cursor.getInt(i++),
					cursor.getInt(i++), cursor.getInt(i++),
					cursor.getString(i++), cursor.getInt(i++),
					cursor.getInt(i++), cursor.getInt(i++),
					cursor.getString(i++), cursor.getInt(i++));
			vo.setPhoto_url(cursor.getString(i++));
			vo.setPhoto_inpath(cursor.getString(i++));
			result.add(vo);
		}
		cursor.close();
		db.close();

		return result;
	} // END getPhotoList();

	/** 사진 정보를 반환하는 메소드 **/
	synchronized public VOPhoto getPhoto(int photoId) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getPhoto()");
		VOPhoto result = null;

		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT photo_id, theme_id, dtheme_id, couple_id, photo_path, "
								+ "photo_isPublic, photo_isSelect, photo_isHeightrLarge, photo_time, photo_sequence, photo_url, photo_inpath "
								+ "FROM WA_Photo WHERE photo_id=" + photoId
								+ " ;", null);
		while (cursor.moveToNext()) {
			int i = 0;
			result = new VOPhoto(cursor.getInt(i++), cursor.getInt(i++),
					cursor.getInt(i++), cursor.getInt(i++),
					cursor.getString(i++), cursor.getInt(i++),
					cursor.getInt(i++), cursor.getInt(i++),
					cursor.getString(i++), cursor.getInt(i++));
			result.setPhoto_url(cursor.getString(i++));
			result.setPhoto_inpath(cursor.getString(i++));
		}
		cursor.close();
		db.close();

		return result;
	} // END getPhoto();


	/** 테마의 사진 목록을 반환하는 메소드 **/
	synchronized public ArrayList<VOPhoto> getThemePhotoList(int themeId,
																													 boolean visibleImageOnly, boolean isHideGangi) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getThemePhotoList()");
		ArrayList<VOPhoto> result = new ArrayList<VOPhoto>();

		String queryWhere = "";
		if (visibleImageOnly)
			queryWhere += " AND photo_isPublic=1 ";
		if (isHideGangi)
			queryWhere += " AND photo_sequence>0 ";

		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT photo_id, theme_id, dtheme_id, couple_id, photo_path, "
								+ "photo_isPublic, photo_isSelect, photo_isHeightrLarge, photo_time, photo_sequence, photo_url, photo_inpath "
								+ "FROM WA_Photo WHERE theme_id="
								+ themeId
								+ queryWhere
								+ " ORDER BY photo_sequence ASC, photo_time DESC, photo_path ASC ;",
						null);
		while (cursor.moveToNext()) {
			int i = 0;
			VOPhoto vo = new VOPhoto(cursor.getInt(i++), cursor.getInt(i++),
					cursor.getInt(i++), cursor.getInt(i++),
					cursor.getString(i++), cursor.getInt(i++),
					cursor.getInt(i++), cursor.getInt(i++),
					cursor.getString(i++), cursor.getInt(i++));
			vo.setPhoto_url(cursor.getString(i++));
			vo.setPhoto_inpath(cursor.getString(i++));
			result.add(vo);
		}
		cursor.close();
		db.close();

		return result;
	} // END getThemePhotoList();

	/** 동적테마의 사진 목록을 반환하는 메소드 **/
	synchronized public ArrayList<VOPhoto> getDynamicThemePhotoList(
			int dthemeId, boolean visibleImageOnly, boolean isHideGangi) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI
				+ " getDynamicThemePhotoList()");
		ArrayList<VOPhoto> result = new ArrayList<VOPhoto>();

		String queryWhere = "";
		if (visibleImageOnly)
			queryWhere += " AND photo_isPublic=1 ";
		if (isHideGangi)
			queryWhere += " AND photo_sequence>0 ";

		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT photo_id, theme_id, dtheme_id, couple_id, photo_path, "
								+ "photo_isPublic, photo_isSelect, photo_isHeightrLarge, photo_time, photo_sequence, photo_url, photo_inpath "
								+ "FROM WA_Photo WHERE dtheme_id="
								+ dthemeId
								+ queryWhere
								+ " ORDER BY photo_sequence ASC, photo_time ASC, photo_path ASC ;",
						null);
		while (cursor.moveToNext()) {
			int i = 0;
			VOPhoto vo = new VOPhoto(cursor.getInt(i++), cursor.getInt(i++),
					cursor.getInt(i++), cursor.getInt(i++),
					cursor.getString(i++), cursor.getInt(i++),
					cursor.getInt(i++), cursor.getInt(i++),
					cursor.getString(i++), cursor.getInt(i++));
			vo.setPhoto_url(cursor.getString(i++));
			vo.setPhoto_inpath(cursor.getString(i++));
			result.add(vo);
		}
		cursor.close();
		db.close();

		return result;
	} // END getDynamicThemePhotoList();


	/** 커플 데이터를 DB에 넣습니다 (가급적 insertCoupleList로 사용을 권장) **/
	@Deprecated
	synchronized public void insertCouple(VOCouple voCouple) {
		Log.i(StaticValues.LOG_TAG,
				LOG_TAG_NAVI + " insertCouple (" + voCouple.getCouple_id()
						+ ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		String sql = "INSERT OR REPLACE INTO WA_Couple(couple_id, couple_state, couple_mr, couple_mrs, couple_time,"
				+ "couple_path, couple_video, couple_image, couple_photo, couple_dirsize, couple_mr_en, couple_mrs_en, "
				+ "hall_name_kr, hall_name_en, hall_address, hall_way1, hall_way2, hall_mapx, hall_mapy, "
				+ "hall_tel, couple_update, couple_like, visible, album_title, album_msg) VALUES ("
				+ voCouple.getCouple_id()
				+ ", "
				+ voCouple.getCouple_state()
				+ ", '"
				+ voCouple.getCouple_mr()
				+ "', '"
				+ voCouple.getCouple_mrs()
				+ "', '"
				+ voCouple.getCouple_time()
				+ "', '"
				+ voCouple.getCouple_path()
				+ "', '"
				+ voCouple.getCouple_video()
				+ "', "
				+ voCouple.getCouple_image()
				+ ", '"
				+ voCouple.getCouple_photo()
				+ "', '"
				+ voCouple.getCouple_dirszie()
				+ "', '"
				+ voCouple.getCouple_mr_en()
				+ "', '"
				+ voCouple.getCouple_mrs_en()
				+ "', '"
				+ voCouple.getHall_name_kr()
				+ "', '"
				+ voCouple.getHall_name_en()
				+ "', '"
				+ voCouple.getHall_address()
				+ "', '"
				+ voCouple.getHall_way1()
				+ "', '"
				+ voCouple.getHall_way2()
				+ "', '"
				+ voCouple.getHall_mapx()
				+ "', '"
				+ voCouple.getHall_mapy()
				+ "', '"
				+ voCouple.getHall_tel()
				+ "', "
				+ voCouple.getCouple_update()
				+ ", "
				+ voCouple.getCouple_like()
				+ ", "
				+ voCouple.getVisible()
				+", '"
				+ voCouple.album_title
				+"','"
				+ voCouple.album_msg
				+ "' );";
		db.execSQL(sql);

		db.close();
	} // END insertCouple();

	/** 커플 데이터를 DB에 넣습니다 (Transection 처리됨) **/
	synchronized public void insertCoupleList(ArrayList<VOCouple> coupleList) {
		for (int i = 0; i < coupleList.size(); i++) {
			Log.e("ab1233", "couple id added="
					+ coupleList.get(i).getCouple_id());
		}
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " insertCoupleList ("
				+ coupleList.size() + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		db.beginTransaction();
		try {
			db.execSQL("DELETE FROM WA_Couple ;");
			for (VOCouple voCouple : coupleList) {
				String sql = "INSERT INTO WA_Couple(couple_id, couple_state, couple_mr, couple_mrs, couple_time,"
						+ "couple_path, couple_video, couple_image, couple_photo, couple_dirsize, couple_mr_en, couple_mrs_en, "
						+ "hall_name_kr, hall_name_en, hall_address, hall_way1, hall_way2, hall_mapx, hall_mapy, "
						+ "hall_tel, couple_update, couple_like, couple_music, visible, album_title, album_msg) VALUES ("
						+ voCouple.getCouple_id()
						+ ", "
						+ voCouple.getCouple_state()
						+ ", '"
						+ voCouple.getCouple_mr()
						+ "', '"
						+ voCouple.getCouple_mrs()
						+ "', '"
						+ voCouple.getCouple_time()
						+ "', '"
						+ voCouple.getCouple_path()
						+ "', '"
						+ voCouple.getCouple_video()
						+ "', "
						+ voCouple.getCouple_image()
						+ ", '"
						+ voCouple.getCouple_photo()
						+ "', '"
						+ voCouple.getCouple_dirszie()
						+ "', '"
						+ voCouple.getCouple_mr_en()
						+ "', '"
						+ voCouple.getCouple_mrs_en()
						+ "', '"
						+ voCouple.getHall_name_kr()
						+ "', '"
						+ voCouple.getHall_name_en()
						+ "', '"
						+ voCouple.getHall_address()
						+ "', '"
						+ voCouple.getHall_way1()
						+ "', '"
						+ voCouple.getHall_way2()
						+ "', '"
						+ voCouple.getHall_mapx()
						+ "', '"
						+ voCouple.getHall_mapy()
						+ "', '"
						+ voCouple.getHall_tel()
						+ "', "
						+ voCouple.getCouple_update()
						+ ", "
						+ voCouple.getCouple_like()
						+ ", '"
						+ voCouple.getCouple_music()
						+ "', "
						+ voCouple.getVisible()
						+", '"
						+ voCouple.album_title
						+"','"
						+ voCouple.album_msg
						+ "' );";
				db.execSQL(sql);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	} // END insertCoupleList();

	/** 테마 데이터를 DB에 넣습니다 (가급적 insertThemeList 사용을 권장) **/
	@Deprecated
	synchronized public void insertTheme(VOTheme voTheme) {
		Log.i(StaticValues.LOG_TAG,
				LOG_TAG_NAVI + " insertTheme (" + voTheme.getTheme_id() + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		String sql = "INSERT OR REPLACE INTO WA_Theme(theme_id, couple_id, theme_path,"
				+ "theme_name, theme_description, theme_effect, theme_sequence, "
				+ "theme_isHide, theme_type) VALUES ("
				+ voTheme.getTheme_id()
				+ ", "
				+ voTheme.getCouple_id()
				+ ", '"
				+ voTheme.getTheme_path()
				+ "', '"
				+ voTheme.getTheme_name()
				+ "', '"
				+ voTheme.getTheme_description().replace("'", "")
				+ "', "
				+ voTheme.getTheme_effect()
				+ ", "
				+ voTheme.getTheme_sequence()
				+ ", "
				+ voTheme.getTheme_isHide()
				+ ", "
				+ voTheme.getTheme_type()
				+ " );";
		db.execSQL(sql);

		db.close();
	} // END insertTheme();

	/** 테마 데이터를 DB에 넣습니다 (Transection 처리됨) **/
	synchronized public void insertThemeList(ArrayList<VOTheme> themeList) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " insertThemeList ("
				+ themeList.size() + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		db.beginTransaction();
		try {
			db.execSQL("DELETE FROM WA_Theme ;");
			for (VOTheme voTheme : themeList) {
				String sql = "INSERT INTO WA_Theme(theme_id, couple_id, theme_path,"
						+ "theme_name, theme_description, theme_effect, theme_sequence, "
						+ "theme_isHide, theme_type) VALUES ("
						+ voTheme.getTheme_id()
						+ ", "
						+ voTheme.getCouple_id()
						+ ", '"
						+ voTheme.getTheme_path()
						+ "', '"
						+ voTheme.getTheme_name()
						+ "', '"
						+ voTheme.getTheme_description().replace("'", "")
						+ "', "
						+ voTheme.getTheme_effect()
						+ ", "
						+ voTheme.getTheme_sequence()
						+ ", "
						+ voTheme.getTheme_isHide()
						+ ", "
						+ voTheme.getTheme_type() + " );";
				db.execSQL(sql);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	} // END insertThemeList();

	/** 사진 데이터를 DB에 넣습니다 (가급적 insertPhotoList 사용을 권장) **/
	@Deprecated
	synchronized public void insertPhoto(VOPhoto voPhoto) {
		Log.i(StaticValues.LOG_TAG,
				LOG_TAG_NAVI + " insertPhoto (" + voPhoto.getPhoto_id() + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		String sql = "INSERT OR REPLACE INTO WA_Photo(photo_id, theme_id, dtheme_id, couple_id, photo_path, "
				+ "photo_isPublic, photo_isHeightrLarge, photo_time, photo_sequence) VALUES ("
				+ voPhoto.getPhoto_id()
				+ ", "
				+ voPhoto.getTheme_id()
				+ ", "
				+ voPhoto.getDtheme_id()
				+ ", "
				+ voPhoto.getCouple_id()
				+ ", '"
				+ voPhoto.getPhoto_path()
				+ "', "
				+ voPhoto.getPhoto_isPublic()
				+ ", "
				+ voPhoto.getPhoto_isHeightrLarge()
				+ ", '"
				+ voPhoto.getPhoto_time()
				+ "', "
				+ voPhoto.getPhoto_sequence()
				+ " );";
		db.execSQL(sql);

		db.close();
	} // END insertPhoto();
/////////////////////////////

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
} // END insertPhoto();

	////////////////////////////

	/////////////////////////////
	synchronized public void insertalbum(VOAlbum voalbum) {
		Log.i(StaticValues.LOG_TAG,
				LOG_TAG_NAVI + " insertmobile_music (" + voalbum.album_id + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		db.execSQL("DELETE FROM album ;");
/*
CREATE TABLE mobile_music("
				+ "mobile_music_id INTEGER primary key,"
				+ "mobile_music_name TEXT,"
				+ "singer_id_list TEXT,"
		        + "album_type TEXT," +
				"album_genre TEXT," +
				"album_company TEXT," +
				"album_intro TEXT," +
				"album_time TEXT);";
	*/
		String sql = "INSERT OR REPLACE INTO album(album_id, album_name, singer_id_list, album_type, album_genre,"
				+ "album_company, album_intro, album_time) VALUES ("
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
				+ "' );";
		db.execSQL(sql);

		db.close();
	} // END insertPhoto();


	////////////////////////

	/////////////////////
	/** 사진 데이터를 DB에 넣습니다 (Transection 처리됨) **/
	synchronized public void insertPhotoMList(ArrayList<VOPhotoM> photoList, int disk_id, int album_id) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " insertPhotoList ("
				+ photoList.size() + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();
/*
"CREATE TABLE photo("
				+"photo_id INTEGER primary key,"
				+"mobile_music_id INTEGER ,"
				+"song_video_id INTEGER ,"
				+"type INTEGER ,"
				+"photo_file_name TEXT,"
				+"photo_order INTEGER);"

				sql = "CREATE TABLE photo("
				+"photo_id INTEGER primary key,"
				+"disk_id INTEGER ,"
				+"song_video_id INTEGER ,"
				+"type INTEGER ,"
				+"photo_file_name TEXT,"
				+"album_id INTEGER,"
				+"photo_order INTEGER);";
		*/
		db.beginTransaction();
		try {
			db.execSQL("DELETE FROM photo WHERE disk_id="+disk_id+" OR (album_id="+album_id+" AND type=3);");
			for (VOPhotoM voPhoto : photoList) {
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

	/////////////////////
	/** 사진 데이터를 DB에 넣습니다 (Transection 처리됨) **/
	synchronized public void insertSingerList(ArrayList<VOSinger> singerList) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " insertSingerList ("
				+ singerList.size() + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();
/*
CREATE TABLE singer("
				+"singer_id INTEGER primary key,"
				+"singer_name TEXT);";
		*/
		db.beginTransaction();
		try {
			db.execSQL("DELETE FROM singer ;");
			for (VOSinger vosinger : singerList) {
				String sql = "INSERT INTO singer(singer_id, singer_name)"
						+ "VALUES ("
						+ vosinger.id
						+ ", '"
						+ vosinger.name
						+ "' );";
				db.execSQL(sql);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	synchronized public void insertDiskList(ArrayList<VOdisk> diskList) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " insertSingerList ("
				+ diskList.size() + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();
/*
CREATE TABLE singer("
				+"singer_id INTEGER primary key,"
				+"singer_name TEXT);";

				sql = "CREATE TABLE disk("
				+ "disk_id INTEGER primary key,"
				+ "disk_name TEXT,"
				+ "album_id INTEGER);";

		*/
		db.beginTransaction();
		try {
			db.execSQL("DELETE FROM disk ;");
			for (VOdisk vodisk : diskList) {
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
	/////////////////////
	/** 사진 데이터를 DB에 넣습니다 (Transection 처리됨) **/
	synchronized public void insertsongList(ArrayList<VOSong> songList, int disk_id) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " insertsongList ("
				+ songList.size() + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();
/*
CREATE TABLE song("
				+"song_id INTEGER primary key,"
				+"song_name TEXT ,"
				+"mobile_music_id INTEGER ,"
				+"song_file_name TEXT,"
				+"photo_id INTEGER,"
				+"song_lyric TEXT);";

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
		*/
		db.beginTransaction();
		try {
			db.execSQL("DELETE FROM song WHERE disk_id="+disk_id+";");
			for (VOSong voSong : songList) {

				String new_lyrics =  voSong.song_lyric.replaceAll("'","");
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

	/////////////////////
	/////////////////////
	/** 사진 데이터를 DB에 넣습니다 (Transection 처리됨) **/
	synchronized public void insertvideoList(ArrayList<VOVideo> videoList) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " insertvideoList ("
				+ videoList.size() + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();
/*
"CREATE TABLE video("
				+"video_id INTEGER primary key,"
				+"song_id INTEGER ,"
				+"mobile_music_id INTEGER ,"
				+"video_file_name TEXT,"
				+"photo_id INTEGER);";


				sql = "CREATE TABLE video("
				+"video_id INTEGER primary key,"
				+"song_id INTEGER ,"
				+"album_id INTEGER ,"
				+"video_file_name TEXT,"
				+"video_name TEXT,"
				+"photo_id INTEGER);";
		*/
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

	/////////////////////
	/////////////////////
	/** 사진 데이터를 DB에 넣습니다 (Transection 처리됨) **/
	synchronized public void insertcommentList(ArrayList<VOComment> commentList) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " insertcommentList ("
				+ commentList.size() + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();
/*
"CREATE TABLE comment("
				+"comment_id INTEGER primary key,"
				+"user_id INTEGER ,"
				+"user_name TEXT,"
				+"comment_time TEXT,"
				+"comment_contents TEXT,"
				+"mobile_music_id INTEGER );";

				sql = "CREATE TABLE comment("
				+"comment_id INTEGER primary key,"
				+"user_id INTEGER ,"
				+"user_name TEXT,"
				+"comment_time TEXT,"
				+"comment_contents TEXT,"
				+"album_id INTEGER );";
		*/
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
						+"',"
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
////////////////////////
synchronized public void insertnewInfoList(ArrayList<VONewInfo> newInfoList) {
	Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " insertnewInfoList ("
			+ newInfoList.size() + ")");
	SQLiteDatabase db = DBHelper.getWritableDatabase();
/*
"CREATE TABLE comment("
				+"comment_id INTEGER primary key,"
				+"user_id INTEGER ,"
				+"user_name TEXT,"
				+"comment_time TEXT,"
				+"comment_contents TEXT,"
				+"mobile_music_id INTEGER );";


				sql = "CREATE TABLE new_info("
				+"info_id INTEGER primary key,"
				+"album_id INTEGER ,"
				+"main_subject TEXT,"
				+"time TEXT,"
				+"image_data TEXT,"
				+"contents TEXT,"
				+"link_url TEXT );";
		*/
	db.beginTransaction();
	try {
		db.execSQL("DELETE FROM new_info ;");
		for (VONewInfo voNewInfo : newInfoList) {
			String sql = "INSERT INTO new_info(info_id, album_id, main_subject, time, image_data, contents,link_url) "
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
					+"','"
					+ voNewInfo.contents
					+"','"
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

	/////////////////////
	/////////////////////
	/** 사진 데이터를 DB에 넣습니다 (Transection 처리됨) **/
	synchronized public void insertPhotoList(ArrayList<VOPhoto> photoList) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " insertPhotoList ("
				+ photoList.size() + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		db.beginTransaction();
		try {
			db.execSQL("DELETE FROM WA_Photo ;");
			for (VOPhoto voPhoto : photoList) {
				String sql = "INSERT INTO WA_Photo(photo_id, theme_id, dtheme_id, couple_id, photo_path, "
						+ "photo_isPublic, photo_isHeightrLarge, photo_time, photo_sequence) VALUES ("
						+ voPhoto.getPhoto_id()
						+ ", "
						+ voPhoto.getTheme_id()
						+ ", "
						+ voPhoto.getDtheme_id()
						+ ", "
						+ voPhoto.getCouple_id()
						+ ", '"
						+ voPhoto.getPhoto_path()
						+ "', "
						+ voPhoto.getPhoto_isPublic()
						+ ", "
						+ voPhoto.getPhoto_isHeightrLarge()
						+ ", '"
						+ voPhoto.getPhoto_time()
						+ "', "
						+ voPhoto.getPhoto_sequence() + " );";
				db.execSQL(sql);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	} // END insertPhotoList();


	
	/** 사진의 경로 데이터를 DB에 반영합니다 (트랜잭션 처리) **/
	synchronized public void updateBGMPath(int coupleId, String inpath) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " updateBGMPath ()");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

			String sql = "UPDATE WA_Couple SET music_inpath='"+inpath+"' "
					+ "WHERE couple_id=" + coupleId +" ;";
			db.execSQL(sql);
			
		db.close();
	} // END updateBGMPath();

	/** 커플의 댓글 플래그를 갱신합니다 **/
	synchronized public void updateCoupleUpdateFlag(int coupleId, int flag) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " updateCoupleUpdateFlag ("
				+ coupleId + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		String sql = "UPDATE WA_Couple SET couple_update=" + flag
				+ " WHERE couple_id=" + coupleId + " ;";
		db.execSQL(sql);

		db.close();
	} // END updateCoupleUpdateFlag();

	/** 커플의 정보를 갱신합니다 **/
	synchronized public void updateCoupleInfo(VOCouple coupleInfo) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " updateCoupleInfo ("
				+ coupleInfo.getCouple_id() + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		String sql = "UPDATE WA_Couple SET hall_name_kr='"
				+ coupleInfo.getHall_name_kr() + "', couple_mr='"
				+ coupleInfo.getCouple_mr() + "', couple_mrs='"
				+ coupleInfo.getCouple_mrs() + "', couple_mr_en='"
				+ coupleInfo.getCouple_mr_en() + "', couple_mrs_en='"
				+ coupleInfo.getCouple_mrs_en() + "', couple_time='"
				+ coupleInfo.getCouple_time() + "', couple_path='"
				+ coupleInfo.getCouple_path() + "', couple_image='"
				+ coupleInfo.getCouple_image() + "', couple_music='" 
				+ coupleInfo.getCouple_music() + "' WHERE couple_id="
				+ coupleInfo.getCouple_id() + " ;";
		db.execSQL(sql);

		db.close();
	} // END updateCoupleInfo();

	/** 커플의 댓글 플래그를 갱신합니다 (auto +1) **/
	synchronized public void updateCoupleUpdateFlag(int coupleId) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " updateCoupleUpdateFlag ("
				+ coupleId + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		String sql = "UPDATE WA_Couple SET couple_update=couple_update+1 WHERE couple_id="
				+ coupleId + " ;";
		db.execSQL(sql);

		db.close();
	} // END updateCoupleUpdateFlag();

	/** 커플의 좋아요 플래그를 갱신합니다 **/
	synchronized public void updateCoupleLikeFlag(int coupleId, int flag) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " updateCoupleUpdateFlag ("
				+ coupleId + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		String sql = "UPDATE WA_Couple SET couple_like=" + flag
				+ " WHERE couple_id=" + coupleId + " ;";
		db.execSQL(sql);

		db.close();
	} // END updateCoupleLikeFlag();

	/** 커플의 좋아요 플래그를 갱신합니다 (auto +1) **/
	synchronized public void updateCoupleLikeFlag(int coupleId) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " updateCoupleUpdateFlag ("
				+ coupleId + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		String sql = "UPDATE WA_Couple SET couple_like=couple_like+1 WHERE couple_id="
				+ coupleId + " ;";
		db.execSQL(sql);

		db.close();
	} // END updateCoupleLikeFlag();

	/** 좋아요를 삭제합니다 (local) **/
	synchronized public void deletePhotoLike(int photoId, int userId) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " deletePhotoLike ()");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		db.execSQL("DELETE FROM WA_Photo2Like WHERE photo_id=" + photoId
				+ " AND user_id=" + userId + " ;");

		db.close();
	} // END deletePhotoLike();

	/** 댓글을 삭제합니다 (local) **/
	synchronized public void deleteComment(int commentId) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " deleteComment ("
				+ commentId + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		db.execSQL("DELETE FROM WA_Comment WHERE comment_id=" + commentId
				+ " ;");

		db.close();
	} // END deleteComment();

	/** 사진 공개 여부를 업데이트 합니다. **/
	public void updatePhotoState(JSONArray jPhotoState) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " updatePhotoState ("
				+ jPhotoState.length() + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		db.beginTransaction();
		try {
			JSONObject obj;
			for (int i = 0; i < jPhotoState.length(); i++) {
				obj = (JSONObject) jPhotoState.get(i);
				String sql = "UPDATE WA_Photo SET " + "photo_isPublic="
						+ obj.getInt("pub") + " " + "WHERE photo_id="
						+ obj.getInt("pid") + " ;";
				db.execSQL(sql);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	/** 동적테마 공개 여부를 업데이트 합니다. **/
	public void updateDynamicThemeHide(String dtheme_ids) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " updateDynamicThemeHide ("
				+ dtheme_ids + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		db.beginTransaction();
		try {
			String sql = "UPDATE WA_Theme_Dynamic SET dtheme_isHide=1 ;";
			db.execSQL(sql);
			sql = "UPDATE WA_Theme_Dynamic SET dtheme_isHide=0 "
					+ " WHERE dtheme_id IN (" + dtheme_ids + ") ;";
			db.execSQL(sql);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	/** 테마 공개 여부를 업데이트 합니다. **/
	public void updateThemeHide(String theme_ids) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " updateThemeHide ("
				+ theme_ids + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		db.beginTransaction();
		try {
			String sql = "UPDATE WA_Theme SET theme_isHide=1 ;";
			db.execSQL(sql);
			sql = "UPDATE WA_Theme SET theme_isHide=0 "
					+ " WHERE theme_id IN (" + theme_ids + ") ;";
			db.execSQL(sql);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	/** 앨범 공개 여부를 업데이트 합니다. **/
	public void updateCoupleAlbumVisible(int coupleId, boolean isVisible) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI
				+ " updateCoupleAlbumVisible (" + coupleId + ")");
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		try {
			String sql = "UPDATE WA_Couple SET visible=" + (isVisible ? 1 : 0)
					+ " WHERE couple_id=" + coupleId + " ;";
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	/** 내가 사진의 좋아요를 눌렀는지 여부를 반환하는 메소드 **/
	synchronized public boolean isPhotoLike(int photoId) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getPhotoLike()");
		boolean result = false;

		final int userId = DataNetUtils.getMyId(context);

		Log.d("test hyuk----->","p_id>>"+photoId+" u_id>>"+userId);
		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT photo_id, user_id FROM WA_Photo2Like "
						+ "WHERE photo_id=" + photoId + " AND user_id="
						+ userId + " ;", null);
		while (cursor.moveToNext())
			result = true;
		cursor.close();
		db.close();

		return result;
	} // END isPhotoLike();

	public void PrinitPhotoLike() {
		//Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getPhotoLike()");
		boolean result = false;

		//final int userId = DataNetUtils.getMyId(context);

		//Log.d("test hyuk----->","p_id>>"+photoId+" u_id>>"+userId);
		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT photo_id, user_id FROM WA_Photo2Like; "
						, null);
		while (cursor.moveToNext()) {
			//result = true;
			Log.d("print photo like,,,","p_id>>"+cursor.getInt(0)+" u_id>>"+cursor.getInt(1));
		}
		cursor.close();
		db.close();

		//return result;
	} // END isPhotoLike();


	synchronized public void deleteCommentTable() {
		SQLiteDatabase db = DBHelper.getWritableDatabase();
		db.execSQL("DELETE FROM WA_Comment ;");
		db.close();
	}

}
