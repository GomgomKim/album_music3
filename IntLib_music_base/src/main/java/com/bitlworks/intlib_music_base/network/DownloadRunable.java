package com.bitlworks.intlib_music_base.network;

import android.os.Handler;
import android.util.Log;

import com.bitlworks.intlib_music_base.StaticValues;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadRunable implements Runnable {
  private final String LOG_TAG_NAVI = "DownloadRunable.calss(Runnable)";
  private String LocalPath, ServerUrl;
  private Handler mHandler;

  public DownloadRunable(String ServerUrl, String LocalPath, Handler h) {
    this.ServerUrl = ServerUrl;
    this.LocalPath = LocalPath;
    this.mHandler = h;
  }

  @Override
  public void run() {
    try {
      File localFile = new File(LocalPath);

      if (localFile.exists()) {
        Log.d("local file check,,,", "hello hyuk,,,,");
        return;
      }
      // 파일 다운로드
      URL fileurl;
      int Read;
      fileurl = new URL(ServerUrl);
      HttpURLConnection conn = (HttpURLConnection) fileurl
          .openConnection();
      int len = conn.getContentLength();
      byte[] tmpByte = new byte[len];
      InputStream is = conn.getInputStream();
      File file = new File(LocalPath);
      if (file.exists()) file.delete();
      FileOutputStream fos = new FileOutputStream(file);
      for (; ; ) {
        Read = is.read(tmpByte);
        if (Read <= 0) {
          break;
        }
        fos.write(tmpByte, 0, Read);
      }
      is.close();
      fos.close();
      conn.disconnect();
    } catch (Exception e) {
      Log.e(StaticValues.LOG_TAG, LOG_TAG_NAVI + " Error=" + e.toString());
      // 실패시 다시 다운로드 시도하도록...
      NetThreadFileDownload downloadThread =
          new NetThreadFileDownload(ServerUrl, LocalPath, mHandler, 0);
      if (mHandler != null) mHandler.sendEmptyMessage(3);
      downloadThread.start();
    } finally {
      // 핸들러에 쓰레드 종료를 알림
      if (mHandler != null) mHandler.sendEmptyMessage(2);
    } // END try();

  }
}