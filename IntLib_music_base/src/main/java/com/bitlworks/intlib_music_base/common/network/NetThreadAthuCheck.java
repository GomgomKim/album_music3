package com.bitlworks.intlib_music_base.common.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bitlworks.WeddingAlbum.DataNet.StaticValues;
import com.bitlworks.music._common.data.DataNetUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class NetThreadAthuCheck extends Thread {
	private String LOG_TAG_NAVI = "<NetThreadAthuCheck.Thread>";
	private final Context context;
	private final Handler handler;
	private final int studioNo;
	
	public NetThreadAthuCheck(Context c, int studioNo, Handler h) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " NetThreadAthuCheck()");
		this.context = c;
		this.handler = h;
		this.studioNo = studioNo;  
		
		this.start();
	}
	
	@Override
	public void run() {
		try{
			// Set HttpParams
			DefaultHttpClient httpClient = new DefaultHttpClient();
			BasicHttpContext httpContext = new BasicHttpContext();
			HttpParams httpParams = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
			HttpConnectionParams.setSoTimeout(httpParams, 5000);
			
			// Set RequsetParams 
			final String user = DataNetUtils.getStringPref(context,
					DataNetUtils.PREFERENCE_NAME_USER_MOBILE, "null");
			StringBuffer sb = new StringBuffer();
			sb.append(StaticValues.SERVICE_URL + "getAthuCheck.php"); 
			sb.append("?user=" + user);
			sb.append("&studio=" + studioNo);
			final String surl = sb.toString();
			Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " requestUrl=" + surl);
			HttpGet httpReq = new HttpGet(surl);
			
			// Execute
			HttpResponse res = httpClient.execute(httpReq, httpContext);
			int statusCode = res.getStatusLine().getStatusCode();
			Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " statusCode=" + statusCode);
			if (statusCode != 200) throw new RuntimeException("HTTP response code is not 200 OK");
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(res.getEntity().getContent()));
			
			// Set ResaltStream
			String line = "";
			sb = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			
			// Send Message Handler
			Message msg = new Message();
			msg.obj = sb.toString();
			handler.sendMessage(msg);
			
		}catch(Exception e){
			Log.e(StaticValues.LOG_TAG, LOG_TAG_NAVI + " Error=" + e.toString());
			if(handler != null) handler.sendEmptyMessage(3);
		} // END try();
		return;
	} // END run();

}
