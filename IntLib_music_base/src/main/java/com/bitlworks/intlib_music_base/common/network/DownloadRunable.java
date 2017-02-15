package com.bitlworks.intlib_music_base.common.network;

import android.os.Handler;
import android.util.Log;

import com.bitlworks.music._common.StaticValues;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadRunable implements Runnable {
	final String LOG_TAG_NAVI = "DownloadRunable.calss(Runnable)";
	String LocalPath, ServerUrl;
	Handler mHandler;
	
	public DownloadRunable(String ServerUrl, String LocalPath, Handler h ) {
		this.ServerUrl = ServerUrl;
		this.LocalPath =LocalPath; 
		this.mHandler = h;
	}
	
	@Override
	public void run() {
    	try{
			File localFile = new File(LocalPath);
	    	long fileSize = localFile.length();
    		
        	if ( localFile.exists() ) {
				Log.d("local file check,,,","hello hyuk,,,,");
				return;
				/*
		    	// Set HttpParams
				DefaultHttpClient httpClient = new DefaultHttpClient();
				BasicHttpContext httpContext = new BasicHttpContext();
				HttpParams httpParams = httpClient.getParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
				HttpConnectionParams.setSoTimeout(httpParams, 10000);
				
				// Set RequsetParams 
				StringBuffer sb = new StringBuffer();
				sb.append(StaticValues.SERVICE_URL + "checkFile.php"); 
				sb.append("?size=" + fileSize );
				sb.append("&path=" + ServerUrl.replace(StaticValues.SERVICE_URL, "") );
				final String surl = sb.toString();
				Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " requestUrl=" + surl);
				HttpGet httpReq = new HttpGet(surl);
				
				// Execute
				HttpResponse res = httpClient.execute(httpReq, httpContext);
				int statusCode = res.getStatusLine().getStatusCode();
				Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " statusCode=" + statusCode);
				if (statusCode != 200) throw new RuntimeException("HTTP response code is not 200 OK");
		
				// Set ResaltStream
				BufferedReader rd = new BufferedReader(
						new InputStreamReader(res.getEntity().getContent()));			
				String line = "";
				sb = new StringBuffer();
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				
				// Result Process
				final String result = sb.toString();
				if( !result.trim().equals("1") ) return;
				*/
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
		}catch(Exception e){
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