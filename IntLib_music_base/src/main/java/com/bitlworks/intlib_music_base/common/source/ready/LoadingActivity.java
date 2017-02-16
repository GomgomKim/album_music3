package com.bitlworks.intlib_music_base.common.source.ready;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.common.MusicClient;
import com.bitlworks.intlib_music_base.common.StaticValues;
import com.bitlworks.intlib_music_base.common.data.DAOSqlite;
import com.bitlworks.intlib_music_base.common.data.DataNetUtils;
import com.bitlworks.intlib_music_base.common.data.VOComment;
import com.bitlworks.intlib_music_base.common.data.VONewInfo;
import com.bitlworks.intlib_music_base.common.data.VOPhotoM;
import com.bitlworks.intlib_music_base.common.data.VOSong;
import com.bitlworks.intlib_music_base.common.data.VOVideo;
import com.bitlworks.intlib_music_base.common.network.DataDownloader;
import com.bitlworks.intlib_music_base.common.source.PagerMainActivity;
import com.bitlworks.music_resource_hanyang.AlbumValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 시작시 로딩 클래스 1. 가입 및 인증 작업 : 서버에 인증 요청 / 서버에서 id와 권한 받기 2. 앨범 메타 데이터 확인 및 다운로드
 * 3. wifi 상태 묻고 앨범 사진 & 동영상 데이터 다운로드 4. 오프라인 모드일 경우 처리
 */
@SuppressLint("HandlerLeak")
public class LoadingActivity extends Activity {

  public static final String PARAM_DEFAULT_ALBUM_NO = "defaultAlbumNo";
  public static final String PARAM_STUDIO_NO = "STUDIO_NO";
  public static final String PARAM_NEXT_ACTIVITY = "NEXT_ACTIVITY";
  public static final String PARAM_IS_SAMPLE = "SAMPLE";

  public static final String PARAM_MUSIC_ID = "MUSIC_ID";
  TextView tvLog;
  DAOSqlite sqlDAO;
  ProgressBar progressBar;
  boolean isCancel = false;

  public int downloadedCount = 0;
  public int totalCount = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_loading);

    tvLog = (TextView) findViewById(R.id.tv_log);
    progressBar = (ProgressBar) findViewById(R.id.progressbar);

    sqlDAO = DAOSqlite.getInstance(this);

    if (DataNetUtils.isNetworkConnect(LoadingActivity.this)) {
      offlineMode2();
      return;
    }
    getPhotos();
  }

  private void getPhotos() {
    final ProgressDialog progressDialog = new ProgressDialog(LoadingActivity.this);
    progressDialog.setCancelable(false);
    progressDialog.show();

    Call<JsonArray> call = MusicClient.getInstance().getService().getPhotos(AlbumValue.album_id, StaticValues.selectedDisk.disk_id);
    call.enqueue(
        new Callback<JsonArray>() {
          @Override
          public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
            progressDialog.dismiss();
            ArrayList<VOPhotoM> photos = new ArrayList<>();
            JsonArray array = response.body().getAsJsonArray();
            for (JsonElement object : array) {
              VOPhotoM photo = new VOPhotoM(
                  object.getAsJsonObject().get("photo_id").getAsInt(),
                  object.getAsJsonObject().get("disk_id").getAsInt(),
                  object.getAsJsonObject().get("song_video_id").getAsInt(),
                  object.getAsJsonObject().get("type").getAsInt(),
                  object.getAsJsonObject().get("photo_file_name").getAsString(),
                  object.getAsJsonObject().get("album_id").getAsInt(),
                  object.getAsJsonObject().get("photo_order").getAsInt());
              photos.add(photo);
            }

            StaticValues.photoList.addAll(photos);
            sqlDAO.insertPhotoMList(StaticValues.photoList, StaticValues.selectedDisk.disk_id, StaticValues.album.album_id);
            getNewInfos();
          }

          @Override
          public void onFailure(Call<JsonArray> call, Throwable t) {
            Log.e("onFailure", t.getMessage());
          }
        }
    );
  }

  private void getNewInfos() {
    final ProgressDialog progressDialog = new ProgressDialog(LoadingActivity.this);
    progressDialog.setCancelable(false);
    progressDialog.show();

    Call<JsonArray> call = MusicClient.getInstance().getService().getNewInfos(AlbumValue.album_id);
    call.enqueue(
        new Callback<JsonArray>() {
          @Override
          public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
            progressDialog.dismiss();
            ArrayList<VONewInfo> newInfos = new ArrayList<>();
            JsonArray array = response.body().getAsJsonArray();
            for (JsonElement object : array) {
              VONewInfo info = new VONewInfo(
                  object.getAsJsonObject().get("info_id").getAsInt(),
                  object.getAsJsonObject().get("album_id").getAsInt(),
                  object.getAsJsonObject().get("main_subject").getAsString(),
                  object.getAsJsonObject().get("time").getAsString(),
                  object.getAsJsonObject().get("image_data").getAsString(),
                  object.getAsJsonObject().get("contents").getAsString(),
                  object.getAsJsonObject().get("link_url").getAsString());
              newInfos.add(info);
            }

            StaticValues.newinfoList.addAll(newInfos);
            sqlDAO.insertnewInfoList(StaticValues.newinfoList);
            getSongs();
          }

          @Override
          public void onFailure(Call<JsonArray> call, Throwable t) {
            Log.e("onFailure", t.getMessage());
          }
        }
    );
  }

  private void getSongs() {
    final ProgressDialog progressDialog = new ProgressDialog(LoadingActivity.this);
    progressDialog.setCancelable(false);
    progressDialog.show();

    Call<JsonArray> call = MusicClient.getInstance().getService().getSongs(StaticValues.selectedDisk.disk_id);
    call.enqueue(
        new Callback<JsonArray>() {
          @Override
          public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
            progressDialog.dismiss();
            ArrayList<VOSong> songs = new ArrayList<>();
            JsonArray array = response.body().getAsJsonArray();
            for (JsonElement object : array) {
              VOSong song = new VOSong(
                  object.getAsJsonObject().get("song_id").getAsInt(),
                  object.getAsJsonObject().get("song_name").getAsString(),
                  object.getAsJsonObject().get("disk_id").getAsInt(),
                  object.getAsJsonObject().get("song_file_name").getAsString(),
                  object.getAsJsonObject().get("photo_id").getAsInt(),
                  object.getAsJsonObject().get("song_lyric").getAsString(),
                  object.getAsJsonObject().get("msg1").getAsString(),
                  object.getAsJsonObject().get("msg2").getAsString(),
                  object.getAsJsonObject().get("song_order").getAsInt());
              songs.add(song);
            }

            StaticValues.songList.addAll(songs);
            sqlDAO.insertsongList(StaticValues.songList, StaticValues.selectedDisk.disk_id);
            getVideos();
          }

          @Override
          public void onFailure(Call<JsonArray> call, Throwable t) {
            Log.e("onFailure", t.getMessage());
          }
        }
    );
  }

  private void getVideos() {
    final ProgressDialog progressDialog = new ProgressDialog(LoadingActivity.this);
    progressDialog.setCancelable(false);
    progressDialog.show();

    Call<JsonArray> call = MusicClient.getInstance().getService().getVideos(StaticValues.album.album_id);
    call.enqueue(
        new Callback<JsonArray>() {
          @Override
          public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
            progressDialog.dismiss();
            ArrayList<VOVideo> videos = new ArrayList<>();
            JsonArray array = response.body().getAsJsonArray();
            for (JsonElement object : array) {
              VOVideo video = new VOVideo(
                  object.getAsJsonObject().get("video_id").getAsInt(),
                  object.getAsJsonObject().get("song_id").getAsInt(),
                  object.getAsJsonObject().get("album_id").getAsInt(),
                  object.getAsJsonObject().get("video_file_name").getAsString(),
                  object.getAsJsonObject().get("video_name").getAsString(),
                  object.getAsJsonObject().get("photo_id").getAsInt());
              videos.add(video);
            }

            StaticValues.videoList.addAll(videos);
            sqlDAO.insertvideoList(StaticValues.videoList);
            getComments();
          }

          @Override
          public void onFailure(Call<JsonArray> call, Throwable t) {
            Log.e("onFailure", t.getMessage());
          }
        }
    );
  }

  private void getComments() {
    final ProgressDialog progressDialog = new ProgressDialog(LoadingActivity.this);
    progressDialog.setCancelable(false);
    progressDialog.show();

    Call<JsonArray> call = MusicClient.getInstance().getService().getComments(StaticValues.album.album_id);
    call.enqueue(
        new Callback<JsonArray>() {
          @Override
          public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
            progressDialog.dismiss();
            ArrayList<VOComment> comments = new ArrayList<>();
            JsonArray array = response.body().getAsJsonArray();
            for (JsonElement object : array) {
              VOComment comment = new VOComment(
                  object.getAsJsonObject().get("comment_id").getAsInt(),
                  object.getAsJsonObject().get("user_id").getAsInt(),
                  object.getAsJsonObject().get("user_name").getAsString(),
                  object.getAsJsonObject().get("comment_time").getAsString(),
                  object.getAsJsonObject().get("comment_contents").getAsString(),
                  object.getAsJsonObject().get("album_id").getAsInt());
              comments.add(comment);
            }

            StaticValues.commentList.addAll(comments);
            sqlDAO.insertcommentList(StaticValues.commentList);
            new DataDownloader(getApplicationContext(), netHandlerPhotoDonwAfter);
          }

          @Override
          public void onFailure(Call<JsonArray> call, Throwable t) {
            Log.e("onFailure", t.getMessage());
          }
        }
    );
  }



  /**
   * 사진 데이터 다운로드 완료 후
   **/
  Handler netHandlerPhotoDonwAfter = new Handler() {
    private final String LOG_TAG_NAVI = "<netHandlerPhotoDonwAfter.Handler>";


    public void handleMessage(Message msg) {
      Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " handleMessage()");

      if (msg.what == 0) { // 사진 파일 카운트 초기화
        totalCount = msg.arg1;
        progressBar.setMax(totalCount);
        if (totalCount == 0) {
          Toast.makeText(getApplicationContext(),
              "앨범 로딩에 문제가 있습니다. 제작사에 문의해 주세요", Toast.LENGTH_SHORT)
              .show();
          finish();
        }
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
    Intent intent = new Intent(this, PagerMainActivity.class);
    startActivity(intent);
    finish();
  }


  private void offlineMode2() {

    StaticValues.user = sqlDAO.getUser();
    StaticValues.album = sqlDAO.getalbum();
    StaticValues.photoList = sqlDAO.getphotoList(StaticValues.selectedDisk.disk_id, StaticValues.album.album_id);
    StaticValues.songList = sqlDAO.getsongList(StaticValues.selectedDisk.disk_id);
    StaticValues.videoList = sqlDAO.getvideoList(StaticValues.album.album_id);
    StaticValues.commentList = sqlDAO.getcommentList(StaticValues.album.album_id);
    StaticValues.singerList = sqlDAO.getsingerList();

    nextActivity();
  }

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