package com.bitlworks.intlib_music_base.common.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bitlworks.intlib_music_base.common.StaticValues;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/** 인터넷에서 파일을 다운로드 하는 클래스 **/
public class NetThreadFileDownload extends Thread {
	private String LOG_TAG_NAVI = "<NetThreadFileDownload.Thread>";
	
    String ServerUrl;
    String LocalPath;
    final Handler mHandler;
    private final int retryCount;

    public NetThreadFileDownload(String serverPath, String localPath, Handler h, int retryCount) {
    	Log.i( StaticValues.LOG_TAG, LOG_TAG_NAVI + " NetThreadFileDownload("+serverPath+")" );
    	
        ServerUrl = serverPath;  
        LocalPath = localPath;
        mHandler = h;
        this.retryCount = retryCount; 
    }  

    @Override
    public void run(){
    	if(retryCount > 3){
    		return;
    	}
    	// 핸들러에 다운로드 스레드 시작을 알림
    	if (mHandler != null) mHandler.sendEmptyMessage(1);

        URL fileurl;
        int Read;
        try {  
        	
        	fileurl = new URL(ServerUrl);
            HttpURLConnection conn = (HttpURLConnection) fileurl
                    .openConnection();  
            int len = conn.getContentLength();  
            byte[] tmpByte = new byte[len];  
            InputStream is = conn.getInputStream();
            File file = new File(LocalPath);
            if( file.exists() == true ) file.delete(); 
            FileOutputStream fos = new FileOutputStream(file);
            for (;;) {  
                Read = is.read(tmpByte);  
                if (Read <= 0) {  
                    break;  
                }  
                fos.write(tmpByte, 0, Read);  
            }  
            is.close();  
            fos.close();  
            conn.disconnect();
            // 핸들러에 다운로드 완료를 알림
        	if (mHandler != null) mHandler.sendEmptyMessage(2);
        }catch (Exception e) {
			Log.e( StaticValues.LOG_TAG, LOG_TAG_NAVI + ServerUrl );
            Log.e( StaticValues.LOG_TAG, e.toString() );
        	new NetThreadFileDownload(ServerUrl, LocalPath, mHandler, retryCount+1).start();
        	Log.i( StaticValues.LOG_TAG, LOG_TAG_NAVI + " [retry] FileDownloaded("+LocalPath+")" );
        } // END try
    } // END run();
}
