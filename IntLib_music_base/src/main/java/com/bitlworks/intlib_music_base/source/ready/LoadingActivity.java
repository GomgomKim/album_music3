package com.bitlworks.intlib_music_base.source.ready;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bitlworks.intlib_bitlworks.CommonUtils;
import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.MusicClient;
import com.bitlworks.intlib_music_base.StaticValues;
import com.bitlworks.intlib_music_base.data.DAOSqlite;
import com.bitlworks.intlib_music_base.data.VOAlbum;
import com.bitlworks.intlib_music_base.data.VOComment;
import com.bitlworks.intlib_music_base.data.VONewInfo;
import com.bitlworks.intlib_music_base.data.VOPhoto;
import com.bitlworks.intlib_music_base.data.VOSong;
import com.bitlworks.intlib_music_base.data.VOVideo;
import com.bitlworks.intlib_music_base.data.VODisk;
import com.bitlworks.intlib_music_base.network.DataDownloader;
import com.bitlworks.intlib_music_base.source.PagerMainActivity;
import com.bitlworks.music_resource.AlbumValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoadingActivity extends Activity {

  private TextView tvLog;
  private DAOSqlite sqlDAO;
  private ProgressBar progressBar;
  private boolean isCancel = false;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_loading);

    sqlDAO = DAOSqlite.getInstance(this);
    tvLog = (TextView) findViewById(R.id.tv_log);
    progressBar = (ProgressBar) findViewById(R.id.progressbar);

    VODisk disk = (VODisk) getIntent().getSerializableExtra("DISK");
    if (disk == null) {
      if (!CommonUtils.isNetworkConnect(LoadingActivity.this)) {
        startOfflineMode();
        return;
      }
      getAlbum();
      return;
    }

    getDiskData(disk);
  }

  public void getDiskData(VODisk disk) {
    StaticValues.selectedDisk = disk;
    if (!CommonUtils.isNetworkConnect(LoadingActivity.this)) {
      startOfflineMode();
      return;
    }
    getSongs();
  }

  private void getAlbum() {
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setCancelable(false);
    progressDialog.show();

    Call<JsonObject> call = MusicClient.getInstance().getService().getAlbum(AlbumValue.album_id);
    call.enqueue(
        new Callback<JsonObject>() {
          @Override
          public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            progressDialog.dismiss();
            JsonObject object = response.body().getAsJsonObject();
            VOAlbum album = new VOAlbum(
                object.get("album_id").getAsInt(),
                object.get("album_name").getAsString(),
                object.get("singer_id_list").getAsString(),
                object.get("album_type").getAsString(),
                object.get("album_genre").getAsString(),
                object.get("album_company").getAsString(),
                object.get("album_intro").getAsString(),
                object.get("album_time").getAsString(),
                object.get("album_invitemsg").getAsString(),
                object.get("album_inviteurl").getAsString(),
                object.get("album_cover_name").getAsString(),
                object.get("album_disk_bg_name").getAsString(),
                object.get("album_title_image").getAsString(),
                object.get("album_main_image").getAsString(),
                Color.parseColor(object.get("album_primary_color").getAsString())
            );
            StaticValues.album = album;
            sqlDAO.insertAlbum(StaticValues.album);
            getComments();
          }

          @Override
          public void onFailure(Call<JsonObject> call, Throwable t) {
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

            StaticValues.comments.addAll(comments);
            sqlDAO.insertComments(StaticValues.comments);
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

            StaticValues.videos.addAll(videos);
            sqlDAO.insertVideos(StaticValues.videos);
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
    final ProgressDialog progressDialog = new ProgressDialog(this);
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
                  object.getAsJsonObject().get("mainSubjectText").getAsString(),
                  object.getAsJsonObject().get("timeText").getAsString(),
                  object.getAsJsonObject().get("subjectImage").getAsString(),
                  object.getAsJsonObject().get("contentText").getAsString(),
                  object.getAsJsonObject().get("link_url").getAsString());
              newInfos.add(info);
            }

            StaticValues.newInfos.addAll(newInfos);
            sqlDAO.insertNewInofs(newInfos);
            getDisks();
          }

          @Override
          public void onFailure(Call<JsonArray> call, Throwable t) {
            progressDialog.dismiss();
            Log.e("onFailure", t.getMessage());
          }
        }
    );
  }


  private void getDisks() {
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setCancelable(false);
    progressDialog.show();

    Call<JsonArray> call = MusicClient.getInstance().getService().getDisks(AlbumValue.album_id);
    call.enqueue(
        new Callback<JsonArray>() {
          @Override
          public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
            progressDialog.dismiss();
            ArrayList<VODisk> disks = new ArrayList<>();
            JsonArray array = response.body().getAsJsonArray();
            for (JsonElement object : array) {
              VODisk disk = new VODisk(
                  object.getAsJsonObject().get("disk_id").getAsInt(),
                  object.getAsJsonObject().get("disk_name").getAsString(),
                  object.getAsJsonObject().get("album_id").getAsInt());
              disks.add(disk);
            }

            if (array.size() == 0) {
              Toast.makeText(
                  getApplicationContext(), "해당 앨범을 찾을 수 없습니다", Toast.LENGTH_LONG).show();
              return;
            }

            StaticValues.disks.addAll(disks);
            sqlDAO.insertDisks(StaticValues.disks);
            getDiskData(disks.get(0));
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

            StaticValues.songs.clear();
            StaticValues.songs.addAll(songs);
            sqlDAO.insertSongs(StaticValues.songs, StaticValues.selectedDisk.disk_id);
            getPhotos();
          }

          @Override
          public void onFailure(Call<JsonArray> call, Throwable t) {
            Log.e("onFailure", t.getMessage());
          }
        }
    );
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
            ArrayList<VOPhoto> photos = new ArrayList<>();
            JsonArray array = response.body().getAsJsonArray();
            for (JsonElement object : array) {
              VOPhoto photo = new VOPhoto(
                  object.getAsJsonObject().get("photo_id").getAsInt(),
                  object.getAsJsonObject().get("disk_id").getAsInt(),
                  object.getAsJsonObject().get("song_video_id").getAsInt(),
                  object.getAsJsonObject().get("type").getAsInt(),
                  object.getAsJsonObject().get("photo_file_name").getAsString(),
                  object.getAsJsonObject().get("album_id").getAsInt(),
                  object.getAsJsonObject().get("photo_order").getAsInt());
              photos.add(photo);
            }

            StaticValues.photos.clear();
            StaticValues.photos.addAll(photos);
            sqlDAO.insertPhotos(StaticValues.photos, StaticValues.selectedDisk.disk_id, StaticValues.album.album_id);
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

    public int downloadedCount = 0;
    public int totalCount = 1;

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


  private void startOfflineMode() {
    StaticValues.album = sqlDAO.getAlbum();
    StaticValues.disks = sqlDAO.getDisks();
    StaticValues.selectedDisk = StaticValues.disks.get(0);
    StaticValues.photos = sqlDAO.getPhotos(StaticValues.selectedDisk.disk_id, StaticValues.album.album_id);
    StaticValues.songs = sqlDAO.getSongs(StaticValues.selectedDisk.disk_id);
    StaticValues.videos = sqlDAO.getVideos(StaticValues.album.album_id);
    StaticValues.comments = sqlDAO.getComments(StaticValues.album.album_id);
    StaticValues.newInfos = sqlDAO.getNewInfos(StaticValues.album.album_id);

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
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
  }

}