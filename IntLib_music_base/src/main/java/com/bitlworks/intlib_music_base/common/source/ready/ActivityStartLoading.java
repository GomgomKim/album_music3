package com.bitlworks.intlib_music_base.common.source.ready;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitlworks.intlib_music_base.common.StaticValues;
import com.bitlworks.music.R;
import com.bitlworks.music._common.data.DAOSqlite;
import com.bitlworks.music._common.data.DataNetUtils;
import com.bitlworks.music._common.data.VOAlbum;
import com.bitlworks.music._common.data.VOComment2;
import com.bitlworks.music._common.data.VOCouple;
import com.bitlworks.music._common.data.VONewInfo;
import com.bitlworks.music._common.data.VOPhoto;
import com.bitlworks.music._common.data.VOPhotoM;
import com.bitlworks.music._common.data.VOSinger;
import com.bitlworks.music._common.data.VOSong;
import com.bitlworks.music._common.data.VOTheme;
import com.bitlworks.music._common.data.VOVideo;
import com.bitlworks.music._common.network.AlbumDataDownloader;
import com.bitlworks.music._common.network.BackgroundUpdateThread;
import com.bitlworks.music._common.network.DataDownloader;
import com.bitlworks.music._common.network.NETGetNewInfoList;
import com.bitlworks.music._common.network.NetGetCommentList;
import com.bitlworks.music._common.network.NetGetMobileMusic;
import com.bitlworks.music._common.network.NetGetPhotoList;
import com.bitlworks.music._common.network.NetGetSingerList;
import com.bitlworks.music._common.network.NetGetSongList;
import com.bitlworks.music._common.network.NetGetVideoList;
import com.bitlworks.music._common.network.NetThreadAthuCheck;
import com.bitlworks.music._common.network.NetThreadGetCommentList;
import com.bitlworks.music._common.network.NetThreadGetDynamicThemeList;
import com.bitlworks.music._common.network.NetThreadGetFriendsShotList;
import com.bitlworks.music._common.network.NetThreadGetPhotoLikeCount;
import com.bitlworks.music._common.network.NetThreadMetadataDownload;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 시작시 로딩 클래스 1. 가입 및 인증 작업 : 서버에 인증 요청 / 서버에서 id와 권한 받기 2. 앨범 메타 데이터 확인 및 다운로드
 * 3. wifi 상태 묻고 앨범 사진 & 동영상 데이터 다운로드 4. 오프라인 모드일 경우 처리
 */
@SuppressLint("HandlerLeak")
public class ActivityStartLoading extends Activity {

  private final String LOG_TAG_NAVI = "<ActivityStartLoading.class>";
  public static final String PARAM_DEFAULT_ALBUM_NO = "defaultAlbumNo";
  public static final String PARAM_STUDIO_NO = "STUDIO_NO";
  public static final String PARAM_NEXT_ACTIVITY = "NEXT_ACTIVITY";
  public static final String PARAM_IS_SAMPLE = "SAMPLE";
  public static final String PARAM_ORIENTATION = "ORIENTATION";

  public static final String PARAM_MUSIC_ID = "MUSIC_ID";
  TextView tvLog;
  DAOSqlite sqlDAO;
  Intent intent;
  Class<?> nextClass;
  ProgressBar progressBar;
  ScrollView mScroll;
  int couple_id;
  int defaultAlbumNo = 0;
  int studioNo;
  boolean isSample;
  boolean isCancel = false;

  int mobile_music_id;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " onCreate()");
    super.onCreate(savedInstanceState);
    StaticValues.SERVICE_URL = StaticValues.getServiceUrl(this);
    intent = getIntent();
    nextClass = (Class<?>) intent.getSerializableExtra(PARAM_NEXT_ACTIVITY);

    setContentView(R.layout.activity_startloading);
    tvLog = (TextView) findViewById(R.id.tv_log);
    progressBar = (ProgressBar) findViewById(R.id.progressbar);
    mScroll = (ScrollView) findViewById(R.id.scrollview);

    sqlDAO = DAOSqlite.getInstance(this);

    mobile_music_id = intent.getIntExtra(PARAM_MUSIC_ID, -1);

    if (DataNetUtils.isNetworkConnect(ActivityStartLoading.this) == false) {
      Log.i("bitlworks", "AA-----------------------------------------------");
      offlineMode2();
    } else { // 다운로드 된 앨범 확인
      new NetGetMobileMusic(StaticValues.album_id, netHandlerGetMobileMusic);
    }
  }

  Handler netHandlerGetMobileMusic = new Handler() {
    private final String LOG_TAG_NAVI = "<netHandlerGetSampleId.Handler>";

    public void handleMessage(Message msg) {

      final String resultString = (String) msg.obj;
      try {
        JSONObject jso = new JSONObject(resultString);

        StaticValues.album = new VOAlbum(jso.getInt("album_id"), jso.getString("album_name"),
            jso.getString("singer_id_list"),
            jso.getString("album_type"),
            jso.getString("album_genre"),
            jso.getString("album_company"),
            jso.getString("album_intro"),
            jso.getString("album_time"),
            jso.getString("album_invitemsg"),
            jso.getString("album_inviteurl")
        );

        sqlDAO.insertalbum(StaticValues.album);

        new NetGetPhotoList(StaticValues.album_id, mobile_music_id, netHandlerGetPhotoList);
      } catch (JSONException e) {
        Log.e(StaticValues.LOG_TAG, LOG_TAG_NAVI + e.toString());
      }
    }
  };

  Handler netHandlerGetPhotoList = new Handler() {
    private final String LOG_TAG_NAVI = "<netHandlerGetSampleId.Handler>";


    public void handleMessage(Message msg) {

      final String resultString = (String) msg.obj;
      try {
        JSONArray result = new JSONArray(resultString);
        ArrayList<VOPhotoM> photoList = new ArrayList<VOPhotoM>();
        VOPhotoM voPhoto = null;
        for (int i = 0; i < result.length(); i++) {
          JSONObject obj = (JSONObject) result.get(i);
          voPhoto = new VOPhotoM(
              obj.getInt("photo_id"),
              obj.getInt("disk_id"),
              obj.getInt("song_video_id"),
              obj.getInt("type"),
              obj.getString("photo_file_name"),
              obj.getInt("album_id"),
              obj.getInt("photo_order")

          );
          Log.d("hyuk2", "id>>" + voPhoto.photo_id + " name>>" + voPhoto.photo_file_name + " order>>" + voPhoto.photo_order);
          photoList.add(voPhoto);
        }

        StaticValues.photoList = photoList;
        sqlDAO.insertPhotoMList(StaticValues.photoList, StaticValues.disk_id, StaticValues.album_id);
        new NETGetNewInfoList(StaticValues.album_id, netHandlerGetNewInfoList);
      } catch (JSONException e) {
        Log.e(StaticValues.LOG_TAG, LOG_TAG_NAVI + e.toString());
      }
    }
  };

  Handler netHandlerGetNewInfoList = new Handler() {
    private final String LOG_TAG_NAVI = "<netHandlerGetSampleId.Handler>";


    public void handleMessage(Message msg) {

      final String resultString = (String) msg.obj;
      try {
        JSONArray result = new JSONArray(resultString);
        ArrayList<VONewInfo> photoList = new ArrayList<VONewInfo>();
        VONewInfo voPhoto = null;
        for (int i = 0; i < result.length(); i++) {
          JSONObject obj = (JSONObject) result.get(i);
          voPhoto = new VONewInfo(
              obj.getInt("info_id"),
              obj.getInt("album_id"),
              obj.getString("main_subject"),
              obj.getString("time"),
              obj.getString("image_data"),
              obj.getString("contents"),
              obj.getString("link_url")

          );
          photoList.add(voPhoto);
        }

       StaticValues.newinfoList = photoList;
        sqlDAO.insertnewInfoList(StaticValues.newinfoList);

        new NetGetSingerList(mobile_music_id, netHandlerGetSingerList);
      } catch (JSONException e) {
        Log.e(StaticValues.LOG_TAG, LOG_TAG_NAVI + e.toString());

        StaticValues.newinfoList = new ArrayList<VONewInfo>();
        new NetGetSingerList(mobile_music_id, netHandlerGetSingerList);
      }
    }
  };

  Handler netHandlerGetSingerList = new Handler() {
    private final String LOG_TAG_NAVI = "<netHandlerGetSampleId.Handler>";


    public void handleMessage(Message msg) {

      final String resultString = (String) msg.obj;
      try {
        JSONArray result = new JSONArray(resultString);
        ArrayList<VOSinger> singerList = new ArrayList<VOSinger>();
        VOSinger voSinger = null;
        for (int i = 0; i < result.length(); i++) {
          JSONObject obj = (JSONObject) result.get(i);
          voSinger = new VOSinger(
              obj.getInt("singer_id"),
              obj.getString("singer_name")

          );
          Log.d("hyuk2", "id>>" + voSinger.id + " name>>" + voSinger.name);
          singerList.add(voSinger);
        }

        StaticValues.singerList = singerList;

        sqlDAO.insertSingerList(StaticValues.singerList);
        new NetGetSongList(mobile_music_id, netHandlerGetSongList);
      } catch (JSONException e) {
        Log.e(StaticValues.LOG_TAG, LOG_TAG_NAVI + e.toString());
      }
    }
  };

  Handler netHandlerGetSongList = new Handler() {
    private final String LOG_TAG_NAVI = "<netHandlerGetSampleId.Handler>";


    public void handleMessage(Message msg) {

      final String resultString = (String) msg.obj;
      try {
        JSONArray result = new JSONArray(resultString);
        ArrayList<VOSong> songList = new ArrayList<VOSong>();
        VOSong voSong = null;

        for (int i = 0; i < result.length(); i++) {
          JSONObject obj = (JSONObject) result.get(i);
          voSong = new VOSong(
              obj.getInt("song_id"),
              obj.getString("song_name"),
              obj.getInt("disk_id"),
              obj.getString("song_file_name"),
              obj.getInt("photo_id"),
              obj.getString("song_lyric"),
              obj.getString("msg1"),
              obj.getString("msg2"), obj.getInt("song_order")

          );
          Log.d("hyuk2", "song name>>" + voSong.song_name + " song file name>>" + voSong.song_file_name);
          songList.add(voSong);
        }

        StaticValues.songList = songList;

        sqlDAO.insertsongList(StaticValues.songList, StaticValues.disk_id);
        new NetGetVideoList(StaticValues.album_id, netHandlerGetVideoList);
      } catch (JSONException e) {
        Log.e(StaticValues.LOG_TAG, LOG_TAG_NAVI + e.toString());
      }


    }
  };

  Handler netHandlerGetVideoList = new Handler() {
    private final String LOG_TAG_NAVI = "<netHandlerGetSampleId.Handler>";


    public void handleMessage(Message msg) {

      final String resultString = (String) msg.obj;
      try {

        JSONArray result = new JSONArray(resultString);
        // insert DB comment table
        ArrayList<VOVideo> videoList = new ArrayList<VOVideo>();
        VOVideo voVideo = null;
        //int video_id, int song_id, int album_id, String video_file_name, String video_name,int photo_id)
        for (int i = 0; i < result.length(); i++) {
          JSONObject obj = (JSONObject) result.get(i);
          voVideo = new VOVideo(
              obj.getInt("video_id"),
              obj.getInt("song_id"),
              obj.getInt("album_id"),
              obj.getString("video_file_name"),
              obj.getString("video_name"),
              obj.getInt("photo_id")

          );
          Log.d("hyuk2", "song id>>" + voVideo.song_id + " video file name>>" + voVideo.video_file_name);
          videoList.add(voVideo);
        }

        //StaticValues.songList = songList;

        StaticValues.videoList = videoList;

        sqlDAO.insertvideoList(StaticValues.videoList);
        new NetGetCommentList(StaticValues.album_id, netHandlerGetCommentList);

        //nextActivity();

        //finish();

        //new NetGetVideoList(mobile_music_id, netHandlerGetVideoList);
        //finish();
        //new NetGetSongList(mobile_music_id, netHandlerGetSongList);
        //finish();
        //StaticValues.photoList = photoList;

        //new NetGetSingerList(mobile_music_id, netHandlerGetSingerList);
        //finish();
        //StaticValues.album = new VOMobileMusic(jso.getInt("mobile_music_id"),jso.getString("mobile_music_name"), jso.getString("singer_id_list"));

        //Log.d("hyuk","id>>"+StaticValues.album.id+" name>>"+StaticValues.album.name+" singer>>"+StaticValues.album.singer_list);

        //new NetGetPhotoList(mobile_music_id, netHandlerGetPhotoList);
        //finish();
      } catch (JSONException e) {
        Log.e(StaticValues.LOG_TAG, LOG_TAG_NAVI + e.toString());
      }


    }
  };
  //////////////////
  Handler netHandlerGetCommentList = new Handler() {
    private final String LOG_TAG_NAVI = "<netHandlerGetSampleId.Handler>";


    public void handleMessage(Message msg) {

      final String resultString = (String) msg.obj;
      try {
        //JSONObject jso = new JSONObject(resultString);
        //defaultAlbumNo = jso.optInt("sample_album_id");

        JSONArray result = new JSONArray(resultString);
        // insert DB comment table
        ArrayList<VOComment2> songList = new ArrayList<VOComment2>();
        VOComment2 voSong = null;
        for (int i = 0; i < result.length(); i++) {
          JSONObject obj = (JSONObject) result.get(i);
          voSong = new VOComment2(
              obj.getInt("comment_id"),
              obj.getInt("user_id"),
              obj.getString("user_name"),
              obj.getString("comment_time"),
              obj.getString("comment_contents"),
              obj.getInt("album_id")

          );
          //Log.d("hyuk2","song name>>"+voSong.song_name+" song file name>>"+voSong.song_file_name);
          songList.add(voSong);
        }

        StaticValues.commentList = songList;


        sqlDAO.insertcommentList(StaticValues.commentList);
        //new NetGetVideoList(mobile_music_id, netHandlerGetVideoList);
        downloadedCount = 0;
        totalCount = 1;
        new DataDownloader(getApplicationContext(), netHandlerPhotoDonwAfter);
        //nextActivity();
        //finish();
        //new NetGetSongList(mobile_music_id, netHandlerGetSongList);
        //finish();
        //StaticValues.photoList = photoList;

        //new NetGetSingerList(mobile_music_id, netHandlerGetSingerList);
        //finish();
        //StaticValues.album = new VOMobileMusic(jso.getInt("mobile_music_id"),jso.getString("mobile_music_name"), jso.getString("singer_id_list"));

        //Log.d("hyuk","id>>"+StaticValues.album.id+" name>>"+StaticValues.album.name+" singer>>"+StaticValues.album.singer_list);

        //new NetGetPhotoList(mobile_music_id, netHandlerGetPhotoList);
        //finish();
      } catch (JSONException e) {
        Log.e(StaticValues.LOG_TAG, LOG_TAG_NAVI + e.toString());
        StaticValues.commentList = new ArrayList<VOComment2>();
        downloadedCount = 0;
        totalCount = 1;
        new DataDownloader(getApplicationContext(), netHandlerPhotoDonwAfter);
        //nextActivity();

      }


    }
  };

  //////////////////////////////
  Handler netHandlerGetSampleID = new Handler() {
    private final String LOG_TAG_NAVI = "<netHandlerGetSampleId.Handler>";


    public void handleMessage(Message msg) {

      final String resultString = (String) msg.obj;
      try {
        JSONObject jso = new JSONObject(resultString);
        defaultAlbumNo = jso.optInt("sample_album_id");
      } catch (JSONException e) {
        Log.e(StaticValues.LOG_TAG, LOG_TAG_NAVI + e.toString());
      }

      if (defaultAlbumNo < 0) {
        Toast.makeText(ActivityStartLoading.this, "앨범 정보를 불러올 수 없습니다", Toast.LENGTH_SHORT)
            .show();
        finish();
        return;
      }

      // 화면 세팅
      int orientation = intent.getIntExtra(PARAM_ORIENTATION,
          ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
        setRequestedOrientation(orientation);
      }
      setContentView(R.layout.activity_startloading);
      tvLog = (TextView) findViewById(R.id.tv_log);
      progressBar = (ProgressBar) findViewById(R.id.progressbar);
      mScroll = (ScrollView) findViewById(R.id.scrollview);

      // 인터넷 연결 여부 확인
      if (DataNetUtils.isNetworkConnect(ActivityStartLoading.this) == false) {
        Log.i("bitlworks", "AA-----------------------------------------------");
        offlineMode();
      } else { // 다운로드 된 앨범 확인
        new NetThreadAthuCheck(ActivityStartLoading.this, studioNo, netHandlerAthuCheck);
/*
        new NetThreadGetPhotoLikeCount(getApplicationContext(), netTEST,
						427);
				*/
      }
      // DAO 초기화
      sqlDAO = DAOSqlite.getInstance(ActivityStartLoading.this);
      Log.i("bitlworks", "StartLoading:END onCreate");
    }
  };
	/*
	Handler netTEST = new Handler() {
		//private final String LOG_TAG_NAVI = "<netHandlerAthuCheck.Handler>";

		public void handleMessage(Message msg) {
			new NetThreadAthuCheck(ActivityStartLoading.this, studioNo, netHandlerAthuCheck);
		}

	};*/

  /**
   * 인증 체크 쓰레드 수행 후
   **/
  Handler netHandlerAthuCheck = new Handler() {
    private final String LOG_TAG_NAVI = "<netHandlerAthuCheck.Handler>";

    public void handleMessage(Message msg) {
      Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " handleMessage()");

      if (msg.what == 3) {
        Toast.makeText(
            getApplicationContext(),
            "네트워크 상태가 원할하지 않습니다. " + "네트워크가 안정적인 곳에서 다시 실행하여 주십시오.",
            Toast.LENGTH_LONG).show();
        isCancel = true;
        finish();
        return;
      }

      final String resultString = (String) msg.obj;

      // 메세지 분할 처리
      final String[] resultContents = resultString.split("<br>");
      final String myID = resultContents[0]; // id
      final String myName = resultContents[1]; // 이름

      String[] albumNumbers;
      if (resultContents[2].contains(",")) {
        albumNumbers = resultContents[2].split(","); // 권한 있는 앨범 번호들이 된다
        // 다운로드 권한이 있는 앨범이 없는 경우...
        if (resultContents[2].length() < 2) {
          noAlbumMode();
          return;
        }

        // 항상 최근 초대받은 것 기준으로 설정
				/*DataNetUtils.setStringPref(getApplicationContext(),
						StaticValues.PREFERENCE_NAME_SELECTED_ALBUM,
						albumNumbers[1]);*/
      } else { // 볼 수 있는 앨범이 없는 경우 임의로 만들어줌
        albumNumbers = new String[2];
        albumNumbers[0] = "" + defaultAlbumNo;
        albumNumbers[1] = "" + defaultAlbumNo;
        resultContents[2] = defaultAlbumNo + "," + defaultAlbumNo;
      }

      // 오픈할 앨범이 프리퍼런스에 없는 경우
      int selno = DataNetUtils.getSelectedCoupleId(getApplicationContext());
      boolean isFirstAlbum = false;
      if (selno < 0) {
        // 첫번째 앨범을 프리퍼런스에 기억해줌
        // addLog(albumNumbers[1] + "번 앨범을 오픈 앨범으로 저장합니다.");
        DataNetUtils.setStringPref(getApplicationContext(),
            StaticValues.PREFERENCE_NAME_SELECTED_ALBUM,
            albumNumbers[1]);
        selno = defaultAlbumNo;
        isFirstAlbum = true;
      }
//////////////////////////added by hyuk

      //finish();
/*
couple_id = DataNetUtils
						.getSelectedCoupleId(getApplicationContext());
			new NetThreadGetPhotoLikeCount(getApplicationContext(), null,
					couple_id);
*/
/*
			new NetThreadGetPhotoLikeCount(getApplicationContext(), null,
					427);
*/

      /////////////////////
      Log.i("bitlworks", ">>" + selno);
      Log.i("bitlworks", ">>" + resultContents[2]);
      //Log.i("bitlworks", ">>" + resultContents[3]);
      if (resultContents.length == 4 && resultContents[3] != null && !isFirstAlbum) {
        Log.i("bitlworks", ">>" + resultContents[3]);
        Log.i("bitlworks", ">>selno:" + selno);
        String[] tmp = resultContents[3].split(",");
        Log.i("bitlworks", ">>selno:" + tmp.length);
        if (tmp.length == 2) {
          String selDate = DataNetUtils.getSelectedCoupleDate(getApplicationContext());
          Log.i("bitlworks", ">>sd:" + selDate);
          Log.i("bitlworks", ">>" + tmp[0] + "," + tmp[1]);
          if (selno != Integer.parseInt(tmp[0]) && selDate.compareTo(tmp[1]) < 0) {
            Log.i("bitlworks", ">>diff");
            DataNetUtils.setSelectedCoupleDate(getApplicationContext(), tmp[1]);
            newAlbumDialog(resultContents[2], albumNumbers[1]);
            return;
          } else {
            Log.i("bitlworks", ">>same");
          }
        }
      }

      // 프리퍼런스에 앨범이 볼수 있는 앨범 목록에 없는 경우오 새로 설정
      boolean isExistAlbum = false;

      for (String item : albumNumbers) {
        if (item.trim().equals(selno + ""))
          isExistAlbum = true;
      }
      if (isExistAlbum) {
        Log.i("bitlworks", "StartLoading:isExist");
        if (AlbumDataDownloader.isExsistAlbumData(
            DataNetUtils.getSelectedCoupleId(getApplicationContext()),
            getApplicationContext())) {
          Log.i("bitlworks", "StartLoading:isExist2");
          final Handler h = new Handler() {

            @Override
            public void handleMessage(Message msg) {
              // TODO Auto-generated method stub
              nextActivity();
              finish();
            }

          };
          new BackgroundUpdateThread(getApplicationContext(), studioNo, null, h);

          return;
        }
      } else {
        DataNetUtils.setStringPref(getApplicationContext(),
            StaticValues.PREFERENCE_NAME_SELECTED_ALBUM,
            albumNumbers[1]);
      }
      Log.e(StaticValues.LOG_TAG, "couple id : " + DataNetUtils.getSelectedCoupleId(getApplicationContext()));
      // 프리퍼런스에 나의 정보를 저장
      DataNetUtils.setStringPref(getApplicationContext(),
          StaticValues.PREFERENCE_NAME_SELECTED_USER_ID, myID);
      DataNetUtils.setStringPref(getApplicationContext(),
          StaticValues.PREFERENCE_NAME_SELECTED_USER_NAME, myName);

      // 정렬 로그
      // addLog("내 아이디 : " + myID);
      // addLog("내 이름 : " + myName);
      // addLog("접근 가능한 앨범 : " + resultContents[2]);

      // 앨범의 메타 데이터 존재 여부 확인
      // addLog("앨범 메타 데이터 존재 여부 : "
      // + ((sqlDAO.isExistsCoupleList()) ? "Y" : "N"));

      // addLog("# Wifi접속을 확인 합니다");
      dialogDownload(resultContents[2]);

    }

    ;
  };

  /**
   * 앨범 다운로드 전 경고 다이얼로그
   **/
  private void dialogDownload(final String albumList) {
    Log.i("bitlworks", "AA" + albumList);
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " dialogDownload()");
    Log.e(StaticValues.LOG_TAG, "couple id : " + DataNetUtils.getSelectedCoupleId(getApplicationContext()));
    Calendar c = Calendar.getInstance();
    String s = String.format("%04d%02d%02d%02d%02d%02d", c.get(Calendar.YEAR), (c.get(Calendar.MONTH) + 1), c.get(Calendar.DAY_OF_MONTH),
        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
    Log.i("bitlworks", ">>now:" + s);
    DataNetUtils.setSelectedCoupleDate(getApplicationContext(), s);
    Builder dlg = new Builder(this);
    dlg.setTitle("안내")
        .setCancelable(false)
        .setMessage(
            "모바일 화보 사진을 보시려면 다운로드가 필요합니다. wifi 접속이 아닐 시 데이터 통화료가 부과 될 수 있습니다. 다운로드 하시겠습니까?")
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setPositiveButton("Yes",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog,
                                  int which) {
                new NetThreadMetadataDownload(
                    getApplicationContext(),
                    netHandlerMetadataDownload, albumList);
              }
            })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            // addLog("[Download Stop]");
            offlineMode();
          }
        }).show();
  }

  /**
   * 앨범의 메타 데이터 다운로드 후
   **/
  Handler netHandlerMetadataDownload = new Handler() {
    private final String LOG_TAG_NAVI = "<netHandlerMetadataDownload.Handler>";

    public void handleMessage(Message msg) {
      Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " handleMessage()");
      Log.e(StaticValues.LOG_TAG, "couple id : " + couple_id);
      if (msg.what == 3) { // httpHostConnectionException 날 경우
        // 취소 하기 살려주기
        Toast.makeText(getApplicationContext(), "네트워크를 다시 확인해주세요.",
            Toast.LENGTH_LONG).show();
        isCancel = true;
        finish();
        return;
      }
      try {
        // 3개로 분할
        String[] responseArray = ((String) msg.obj).split("<br>");

        final JSONArray coupleJSONArray = new JSONArray(
            responseArray[0].trim());
        final JSONArray themeJSONArray = new JSONArray(
            responseArray[1].trim());
        final JSONArray photoJSONArray = new JSONArray(
            responseArray[2].trim());
        responseArray = null;
        couple_id = DataNetUtils
            .getSelectedCoupleId(getApplicationContext());
        Log.e(StaticValues.LOG_TAG, "couple id : " + couple_id);
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
              "",
              1,
              obj.getString("album_title"),
              obj.getString("album_msg"));
          coupleList.add(voCouple);

          // addLog(voCouple.getCouple_mr() + "님과 "
          // + voCouple.getCouple_mrs() + "님의 앨범을 저장했습니다");
        }
        voCouple = null;
        sqlDAO.insertCoupleList(coupleList);

        // 디비에 데이터 넣기
        ArrayList<VOTheme> themeList = new ArrayList<VOTheme>();
        VOTheme voTheme = null;
        for (int i = 0; i < themeJSONArray.length(); i++) {
          JSONObject obj = (JSONObject) themeJSONArray.get(i);
          voTheme = new VOTheme(obj.getInt("theme_id"),
              obj.getInt("couple_id"),
              obj.getString("theme_path"),
              obj.getString("theme_name"),
              obj.getString("theme_description"),
              obj.getInt("theme_effect"),
              obj.getInt("theme_sequence"),
              obj.getInt("theme_isHide"),
              obj.getInt("theme_type"));
          themeList.add(voTheme);
          // addLog(voTheme.getTheme_name() + " 테마 메타데이터를 저장했습니다");
        }
        voTheme = null;
        ///////////////////////////////////////////////////
				/*
				AlbumThemeInfo.sAlbumThemeList = new int[new_page_array.size()];
				for( int i = 0; i < themeList.size(); i++ ){
					String CurrentString = themeList.get(i).getTheme_name();
					String[] separated = CurrentString.split("_");

					if(separated[0].equals("11")){

					}
				}*/
        //////////////////////////////////////////////////
        sqlDAO.insertThemeList(themeList);

        // 디비에 사진 데이터 넣기
        ArrayList<VOPhoto> photoList = new ArrayList<VOPhoto>();
        VOPhoto voPhoto = null;
        for (int i = 0; i < photoJSONArray.length(); i++) {
          JSONObject obj = (JSONObject) photoJSONArray.get(i);
          voPhoto = new VOPhoto(obj.getInt("photo_id"),
              obj.getInt("theme_id"), obj.getInt("dtheme_id"),
              obj.getInt("couple_id"),
              obj.getString("photo_path"),
              obj.getInt("photo_isPublic"),
              obj.getInt("photo_isSelect"),
              obj.getInt("photo_isHeightrLarge"),
              obj.getString("photo_time"),
              obj.getInt("photo_sequence"));
          photoList.add(voPhoto);
          // addLog(voPhoto.getPhoto_path() + " 사진 메타데이터를 저장했습니다");
        }
        voPhoto = null;
        sqlDAO.insertPhotoList(photoList);

        // 동적 테마 리스트 다운로드 작업
        new NetThreadGetDynamicThemeList(getApplicationContext(),
            netHandlerDynamicThemeDonwAfter, couple_id);
      } catch (JSONException e) {
        Log.e(StaticValues.LOG_TAG, LOG_TAG_NAVI + e.toString());
      } // END Try
    } // END handleMessage();
  }; // END netHandlerMetadataDownload

  /**
   * 동적 테마의 메타 데이터 다운로드 후
   */
  Handler netHandlerDynamicThemeDonwAfter = new Handler() {
    public void handleMessage(Message msg) {
      if (msg.what == 1) {
        // 프렌즈샷 데이터 다운로드 작업
        new NetThreadGetFriendsShotList(getApplicationContext(),
            netHandlerFriendsShotDonwAfter, couple_id);
      }
    }
  };

  /**
   * 프렌즈샷 데이터 다운로드 후
   */
  Handler netHandlerFriendsShotDonwAfter = new Handler() {
    public void handleMessage(Message msg) {
      if (msg.what == 1) {
        // 방명록 데이터 다운로드 작업
        new NetThreadGetCommentList(getApplicationContext(), null,
            couple_id);
        // 좋아요 데이터 다운로드 작업
        //finish();
        new NetThreadGetPhotoLikeCount(getApplicationContext(), null,
            couple_id);
        // 실제 사진 다운로드 작업
        new AlbumDataDownloader(getApplicationContext(), couple_id,
            netHandlerPhotoDonwAfter);
      }
    }
  };

  public int downloadedCount = 0;
  public int totalCount = 1;


  /**
   * 사진 데이터 다운로드 완료 후
   **/
  Handler netHandlerPhotoDonwAfter = new Handler() {
    private final String LOG_TAG_NAVI = "<netHandlerPhotoDonwAfter.Handler>";


    public void handleMessage(Message msg) {
      Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " handleMessage()");

      if (msg.what == 0) { // 사진 파일 카운트 초기화
        // totalCount = ( msg.arg1 < totalCount)?msg.arg1 : totalCount;
        totalCount = msg.arg1;
        progressBar.setMax(totalCount);
        if (totalCount == 0) {
          Toast.makeText(getApplicationContext(),
              "앨범 로딩에 문제가 있습니다. 제작사에 문의해 주세요", Toast.LENGTH_SHORT)
              .show();
          finish();
        }
        // addLog("#다운로드 할 파일의 갯수는 " + totalCount + "개 입니다.");
      } else if (msg.what == 1) { // 사진 파일 다운로드 시작시
        // addLog("download start ("+downloadingCount+"/"+totalCount+")");
      } else if (msg.what == 2) { // 사진 파일 다운로드 완료시
        downloadedCount++;
        // addLog("Downloaded (" + downloadedCount + "/" + totalCount
        // +")");
        updateLog("" + (int) (100 * downloadedCount / totalCount));
        if (downloadedCount == totalCount) {
          // 파일 다운로드가 완료 되었을 시
          nextActivity();
        }
      } else if (msg.what == 3) { // httpHostConnectionException 날 경우
        // 취소 하기 살려주기
        Toast.makeText(getApplicationContext(), "네트워크를 다시 확인해주세요.",
            Toast.LENGTH_LONG).show();
        isCancel = true;
        finish();
      }
    } // END handleMessage();
  }; // END netHandlerPhotoDonwAfter

  /**
   * 다운로드가 완료 되었으므로 메인 액티비티로 넘어감
   **/
  synchronized private void nextActivity() {
    try {
      // 약간 휴식
      Thread.sleep(100);
      // 가비지 콜렉터 호출
      System.gc();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    setResult(RESULT_OK);
    // 다음 액티비티를 호출
    intent = new Intent(this, nextClass);
    // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    if (studioNo > -1) {
      intent.putExtra(PARAM_STUDIO_NO, studioNo);
    }
    intent.putExtra(PARAM_IS_SAMPLE, isSample);
    startActivity(intent);
    finish();
  }

  /**
   * 오프라인일때 처리
   **/
  private void offlineMode() {
//    Log.i("bitlworks", LOG_TAG_NAVI + " offlineMode()");
//    // addLog("# 오프라인 모드입니다.");
//
//    // 다운된 데이터가 있는지 확인
//    // addLog("# 다운로드 된 앨범을 확인합니다.");
//    if (AlbumDataDownloader.isExsistAlbumData(
//        DataNetUtils.getSelectedCoupleId(this), this)) {
//      // addLog("다운로드 된 앨범이 있습니다.");
//      new BackgroundUpdateThread(this, studioNo, null);
//      nextActivity();
//    } else {
//      // addLog("다운로드 된 앨범이 없습니다.");
//      noAlbumMode();
//    }
  }

  private void offlineMode2() {
    Log.i("bitlworks", LOG_TAG_NAVI + " offlineMode()");


    StaticValues.myInfo = sqlDAO.getUser();
    StaticValues.album = sqlDAO.getalbum();

    Log.d("last hyuk", "album_name>" + StaticValues.album.album_name);
    StaticValues.photoList = sqlDAO.getphotoList(StaticValues.disk_id, StaticValues.album_id);
    StaticValues.songList = sqlDAO.getsongList(StaticValues.disk_id);
    StaticValues.videoList = sqlDAO.getvideoList(StaticValues.album_id);
    StaticValues.commentList = sqlDAO.getcommentList(StaticValues.album_id);
    StaticValues.singerList = sqlDAO.getsingerList();
    StaticValues.newinfoList = sqlDAO.getnewInfoList(StaticValues.album_id);

    nextActivity();
    // addLog("# 오프라인 모드입니다.");

    // 다운된 데이터가 있는지 확인
    // addLog("# 다운로드 된 앨범을 확인합니다.");
		/*
		if (AlbumDataDownloader.isExsistAlbumData(
				DataNetUtils.getSelectedCoupleId(this), this)) {
			// addLog("다운로드 된 앨범이 있습니다.");
			new BackgroundUpdateThread(this, studioNo, null);
			nextActivity();
		} else {
			// addLog("다운로드 된 앨범이 없습니다.");
			noAlbumMode();
		}*/
  }

  /**
   * 볼수 있는 앨범이 없는 경우
   **/
  private void noAlbumMode() {
    Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " offlineMode()");
    // addLog("# 볼수 있는 앨범이 없습니다");
    setResult(RESULT_CANCELED);
    Toast.makeText(getApplicationContext(),
        "네트워크 상태가 원할하지 않습니다. " + "네트워크가 안정적인 곳에서 다시 실행하여 주십시오.",
        Toast.LENGTH_LONG).show();
    finish();
  }

  /**
   * 로그뷰에 표시할 내용 정리 ** private void addLog(String log) { if (tvLog != null)
   * tvLog.append(log + "\n");
   *
   * // 스크롤바를 맨 아래로 갱신 mScroll.post(new Runnable() {
   *
   * @Override public void run() { mScroll.fullScroll(View.FOCUS_DOWN); } });
   *           }
   */
  /**
   * 퍼센트 업데이트
   **/
  private void updateLog(String percent) {
    if (tvLog != null)
      tvLog.setText(percent + "%");
  }

  @Override
  public void onBackPressed() {
    if (isCancel)
      super.onBackPressed();
    return;
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
  }

}