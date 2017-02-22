package com.bitlworks.intlib_music_base.network;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;


import com.bitlworks.intlib_music_base.StaticValues;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 앨범 데이터를 다운로드 하는 클래스
 * 내부 저장소는 sdcard/bitlworks/mobilemusic를 기본
 **/
public class DataDownloader { // 일종의 Logic class
  public DataDownloader(Context c, final Handler mHandler) {

    // 경로 정하기
    String rootPath = "/mnt/sdcard/";
    // 내부 저장소 경로 구하기
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    } else return;

    // 폴더 존재 확인 및 생성
    rootPath += "bitlworks/";
    checkFolder(rootPath);
    rootPath += "mobilemusic/";
    checkFolder(rootPath);

    ExecutorService executorService = Executors.newFixedThreadPool(15);

    Message msg = new Message();
    msg.what = 0;
    msg.arg1 = StaticValues.photoList.size() + StaticValues.songList.size() + StaticValues.newinfoList.size();
    if (mHandler != null) mHandler.sendMessage(msg);


    int firstPhotoCount = 0;
    for (int i = 0; i < StaticValues.photoList.size(); i++) {
      String url = "";
      if (StaticValues.photoList.get(i).type == 1) {
        url = "http://music.bitlworks.co.kr/mobilemusic/image_home/" + StaticValues.album.album_id + "/" + StaticValues.selectedDisk.disk_id + "/photo/"
            + StaticValues.photoList.get(i).photo_file_name;
      } else {
        url = "http://music.bitlworks.co.kr/mobilemusic/image_home/" + StaticValues.album.album_id + "/disk0/"
            + StaticValues.photoList.get(i).photo_file_name;
      }
      String f_name = StaticValues.photoList.get(i).photo_file_name;

      String CurrentString = f_name;
      String[] separated = CurrentString.split("/");
      if (separated.length > 1) {
        String local_path = rootPath;
        for (int j = 0; j < separated.length - 1; j++) {

          local_path += "" + separated[j] + "/";
          checkFolder(local_path);
        }
      }
      final String LocalPath = rootPath + StaticValues.photoList.get(i).photo_file_name;
      executorService.execute(new DownloadRunable(url, LocalPath, mHandler));

      firstPhotoCount++;

    }
    for (int i = 0; i < StaticValues.songList.size(); i++) {
      String url = "http://music.bitlworks.co.kr/mobilemusic/image_home/" + StaticValues.album.album_id + "/" + StaticValues.selectedDisk.disk_id + "/mp3/" + StaticValues.songList.get(i).song_file_name;
      String f_name = StaticValues.songList.get(i).song_file_name;
      String CurrentString = f_name;
      String[] separated = CurrentString.split("/");
      if (separated.length > 1) {
        String local_path = rootPath;
        for (int j = 0; j < separated.length - 1; j++) {
          local_path += "" + separated[j] + "/";
          checkFolder(local_path);
        }
      }

      final String LocalPath = rootPath + StaticValues.songList.get(i).song_file_name;
      executorService.execute(new DownloadRunable(url, LocalPath, mHandler));
      firstPhotoCount++;
    }

    for (int i = 0; i < StaticValues.newinfoList.size(); i++) {
      String url = "http://music.bitlworks.co.kr/mobilemusic/image_home/" + StaticValues.album.album_id + "/news/"
          + StaticValues.newinfoList.get(i).image_data;
      final String LocalPath = rootPath + StaticValues.newinfoList.get(i).image_data;
      executorService.execute(new DownloadRunable(url, LocalPath, mHandler));
      firstPhotoCount++;
    }
    executorService.shutdown();
  }

  /**
   * 폴더가 존재하지 않을 경우 폴더를 만들어준다
   **/
  private void checkFolder(String path) {
    File dir = new File(path);
    if (!dir.exists()) {
      dir.mkdir();
    }
  }
}


