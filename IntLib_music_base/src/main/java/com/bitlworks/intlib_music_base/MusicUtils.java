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

  public static void checkFolder(String path) {
    File dir = new File(path);
    if (!dir.exists()) {
      dir.mkdir();
    }
  }
}
