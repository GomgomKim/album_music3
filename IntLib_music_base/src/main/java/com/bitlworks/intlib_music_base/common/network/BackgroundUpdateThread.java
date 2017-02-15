package com.bitlworks.intlib_music_base.common.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.bitlworks.WeddingAlbum.DataNet.StaticValues;
import com.bitlworks.music._common.data.DAOSqlite;
import com.bitlworks.music._common.data.DataNetUtils;
import com.bitlworks.music._common.data.VOCouple;
import com.bitlworks.music._common.data.VOPhoto;
import com.bitlworks.music._common.data.VOTheme;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/** 백그라운드에서 앨범 데이터 업데이트 클래스 **/
public class BackgroundUpdateThread extends Thread {

	// 전역 변수들
	private final Context context;
	private final DAOSqlite sqlDAO;
	private final ProgressDialog progressDialog;
	private final int studioNo;
	private Handler handler;

	public BackgroundUpdateThread(Context c, int studioNo,
																ProgressDialog pDialog) {
		this.context = c;
		this.sqlDAO = DAOSqlite.getInstance(context);
		this.progressDialog = pDialog;
		this.studioNo = studioNo;
		this.handler = null;
		this.start();
		if (progressDialog != null)
			progressDialog.show();
	}
	
	public BackgroundUpdateThread(Context c, int studioNo,
																ProgressDialog pDialog, Handler h) {
		this.context = c;
		this.sqlDAO = DAOSqlite.getInstance(context);
		this.progressDialog = pDialog;
		this.studioNo = studioNo;
		this.handler = h;
		this.start();
		if (progressDialog != null)
			progressDialog.show();
	}

	@Override
	public void run() {
		Log.w(StaticValues.LOG_TAG, "<<BackgroundUpdateThread Start>>");
		new NetThreadAthuCheck(context, studioNo, netHandlerAthuCheck);
		super.run();
	}

	/** 인증 체크 쓰레드 수행 후 **/
	Handler netHandlerAthuCheck = new Handler() {
		private final String LOG_TAG_NAVI = "<netHandlerAthuCheck.Handler>";

		public void handleMessage(Message msg) {
			Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " handleMessage()");

			if (msg.what == 3) { // httpHostConnectionException 날 경우
				Toast.makeText(context, "네트워크를 다시 확인해주세요.", Toast.LENGTH_LONG).show();
				return;
			}

			final String resultString = (String) msg.obj;

			// 메세지 분할 처리
			final String[] resultContents = resultString.split("<br>");
			final String myID = resultContents[0]; // id
			final String myName = resultContents[1]; // 이름

			// 프리퍼런스에 나의 정보를 저장
			DataNetUtils.setStringPref(context,
					StaticValues.PREFERENCE_NAME_SELECTED_USER_ID, myID);
			DataNetUtils.setStringPref(context,
					StaticValues.PREFERENCE_NAME_SELECTED_USER_NAME, myName);

			new NetThreadMetadataDownload(context, netHandlerMetadataDownload,
					resultContents[2]);
		};
	};

	/** 앨범의 메타 데이터 다운로드 후 **/
	private final Handler netHandlerMetadataDownload = new Handler() {
		private final String LOG_TAG_NAVI = "<netHandlerMetadataDownload.Handler>";

		public void handleMessage(Message msg) {
			Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " handleMessage()");
			
			if (msg.what == 3) { // httpHostConnectionException 날 경우
				Toast.makeText(context, "네트워크를 다시 확인해주세요.", Toast.LENGTH_LONG).show();
				return;
			}

			
			try {
				// 3개로 분할
				String[] responseArray = ((String) msg.obj).split("<br>");
				Log.i("bitlworks", "AAAAAAAAAAAA1");
				final JSONArray coupleJSONArray = new JSONArray(
						responseArray[0].trim());
				Log.i("bitlworks", "AAAAAAAAAAAA2");
				final JSONArray themeJSONArray = new JSONArray(
						responseArray[1].trim());
				Log.i("bitlworks", "AAAAAAAAAAAA3"+responseArray[2]);
				final JSONArray photoJSONArray = new JSONArray(
						responseArray[2].trim());
				responseArray = null;
				Log.i("bitlworks", "AAAAAAAAAAAA4"+coupleJSONArray.length());
				// 디비에 커플데이터 넣기
				ArrayList<VOCouple> coupleList = new ArrayList<VOCouple>();
				VOCouple voCouple;
				for (int i = 0; i < coupleJSONArray.length(); i++) {
					JSONObject obj = (JSONObject) coupleJSONArray.get(i);
					voCouple = new VOCouple(obj.getInt("couple_id"),
							obj.getInt("couple_state"),
							obj.getString("couple_mr"),
							obj.getString("couple_mrs"),
							obj.getString("couple_time"),
							obj.getString("couple_path"),
							obj.getString("couple_video"),
							obj.getInt("couple_image"),
							obj.getString("couple_photo"),
							obj.getString("couple_dirsize"),
							obj.getString("couple_mr_en"),
							obj.getString("couple_mrs_en"),
							obj.getString("hall_name_kr"),
							obj.getString("hall_name_en"),
							obj.getString("hall_address"),
							obj.getString("hall_way1"),
							obj.getString("hall_way2"),
							obj.getString("hall_mapx"),
							obj.getString("hall_mapy"),
							obj.getString("hall_tel"),
							obj.getInt("couple_update"),
							obj.getInt("couple_like"),
							obj.getString("couple_music"),
							"", 1, obj.getString("album_title"),obj.getString("album_msg"));
					coupleList.add(voCouple);
				}
				voCouple = null;
				sqlDAO.insertCoupleList(coupleList);

				// 디비에 데이터 넣기
				ArrayList<VOTheme> themeList = new ArrayList<VOTheme>();
				VOTheme voTheme = null;
				for (int i = 0; i < themeJSONArray.length(); i++) {
					JSONObject obj = (JSONObject) themeJSONArray.get(i);
					voTheme = new VOTheme(
							obj.getInt("theme_id"),
							obj.getInt("couple_id"),
							obj.getString("theme_path"),
							obj.getString("theme_name"),
							obj.getString("theme_description"),
							obj.getInt("theme_effect"),
							obj.getInt("theme_sequence"),
							obj.getInt("theme_isHide"),
							obj.getInt("theme_type")							
						);
					themeList.add(voTheme);
				}
				voTheme = null;
				sqlDAO.insertThemeList(themeList);

				// 디비에 사진 데이터 넣기
				ArrayList<VOPhoto> photoList = new ArrayList<VOPhoto>();
				VOPhoto voPhoto = null;
				for (int i = 0; i < photoJSONArray.length(); i++) {
					JSONObject obj = (JSONObject) photoJSONArray.get(i);
					voPhoto = new VOPhoto(
							obj.getInt("photo_id"),
							obj.getInt("theme_id"),
							obj.getInt("dtheme_id"),
							obj.getInt("couple_id"),
							obj.getString("photo_path"),
							obj.getInt("photo_isPublic"),
							obj.getInt("photo_isSelect"),
							obj.getInt("photo_isHeightrLarge"),
							obj.getString("photo_time"),
							obj.getInt("photo_sequence"));
					photoList.add(voPhoto);
					// sqlDAO.insertPhoto(voPhoto);
				}
				voPhoto = null;
				sqlDAO.insertPhotoList(photoList);

				final int couple_id = DataNetUtils.getSelectedCoupleId(context);
				// 프렌즈샷 데이터 다운로드 작업
				new NetThreadGetFriendsShotList(context,null, couple_id);
				// 동적 테마 리스트 다운로드 작업
				new NetThreadGetDynamicThemeList(context, null, couple_id);
				// 방명록 데이터 다운로드 작업
				new NetThreadGetCommentList(context, null, couple_id);
				// 좋아요 데이터 다운로드 작업
				new NetThreadGetPhotoLikeCount(context, null, couple_id);
				// 실제 사진 다운로드 작업
				new AlbumDataDownloader(context, couple_id, null);

			} catch (JSONException e) {
				Log.e(StaticValues.LOG_TAG, LOG_TAG_NAVI + e.toString());
			} finally {
				if (progressDialog != null)
					progressDialog.dismiss();
				if(handler != null) handler.sendEmptyMessage(0);
			} // END Try
		} // END handleMessage();
	}; // END netHandlerMetadataDownload
	
}
