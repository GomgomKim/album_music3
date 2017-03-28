package com.bitlworks.intlib_music_base;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class MusicUtils {
  public static String getAlbumPath(Context context) {
    String path = context.getFilesDir().getPath();
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      path = context.getExternalFilesDir(null).getPath();
    }
    return path + "/";
  }

  public static void initDirectory(Context context) {
    String path = getAlbumPath(context);

    // 메타데이터(앨범자켓 등의 배경파일)
    checkFolder(path + "metadata");
    // 비디오배경
    checkFolder(path + "video");
    // 새소식
    checkFolder(path + "news");

    // 현재 선택된 디스크에 대한 폴더
    path += StaticValues.selectedDisk.disk_id +"/";
    checkFolder(path);
    // 현재 선택된 디스크 폴더의 photo
    checkFolder(path + "photo");
    // 현재 선택된 디스크 폴더의 mp3
    checkFolder(path + "mp3");
  }

  public static void checkFolder(String path) {
    File dir = new File(path);
    if (!dir.exists()) {
      dir.mkdir();
    }
  }
}
