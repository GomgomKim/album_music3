package com.bitlworks.intlib_music_base.common.network;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.bitlworks.WeddingAlbum.DataNet.StaticValues;
import com.bitlworks.WeddingAlbum.DataNet.VOPhotoLike;
import com.bitlworks.music._common.data.DAOSqlite;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NetThreadGetPhotoLikeCount extends Thread {
	private String LOG_TAG_NAVI = "<NetThreadGetPhotoLikeCount.class>";
	private final Context context;
	private final Handler handler;
	private int coupleId;
	
	
	public NetThreadGetPhotoLikeCount(Context c, Handler h, int coupleId) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " NetThreadGetPhotoLikeCount()");
		this.context = c;
		this.handler = h;
		this.coupleId = coupleId;
		
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
			//Log.d("DDDDDDDDDDDDDDDDDD>>>>>>>>>","couple id>>"+coupleId);

			StringBuffer sb = new StringBuffer();
			sb.append(StaticValues.SERVICE_URL + "getPhotoLike.php");
			sb.append("?cid=" + coupleId);
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
			JSONArray result = new JSONArray(sb.toString());
			
			// insert DB comment table
			DAOSqlite sqlDAO = DAOSqlite.getInstance(context);
			ArrayList<VOPhotoLike> photoLikeList = new ArrayList<VOPhotoLike>();
			VOPhotoLike voPhotoLike = null;
			for(int i=0; i < result.length(); i++ ){
				JSONObject obj = (JSONObject)result.get(i);
				voPhotoLike = new VOPhotoLike(
						obj.getInt("photo_id"),
						obj.getInt("user_id") );
				photoLikeList.add(voPhotoLike);
				// sqlDAO.insertPhotoLike(voPhotoLike);
			}
			voPhotoLike = null;
			sqlDAO.insertPhotoLikeList(photoLikeList);
			
			// if exsist handler send message

			if(handler != null) handler.sendEmptyMessage(1);
			//Message msg = new Message();
			//if(handler != null) handler.sendMessage(msg);
		}catch(Exception e){
			Log.e(StaticValues.LOG_TAG, LOG_TAG_NAVI + " Error=" + e.toString());
		} // END try();
		
		return;
	} // END run();
	
}
