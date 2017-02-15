package com.bitlworks.intlib_music_base.common.network;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.bitlworks.WeddingAlbum.DataNet.StaticValues;
import com.bitlworks.WeddingAlbum.DataNet.VOComment;
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

public class NetThreadGetCommentList extends Thread {
	private String LOG_TAG_NAVI = "<NetThreadGetCommentList.class>";
	private final Context context;
	private final Handler handler;
	private int coupleId;
	
	
	public NetThreadGetCommentList(Context c, Handler h, int coupleId) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " NetThreadGetCommentList()");
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
			StringBuffer sb = new StringBuffer();
			sb.append(StaticValues.SERVICE_URL + "getCommentList.php"); 
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
			final String resultString = sb.toString();
			// 리턴이 null일때 댓글 테이블 초기화
			if(resultString.trim().equals("null")){
				DAOSqlite.getInstance(context).deleteCommentTable();
				if(handler != null) handler.sendEmptyMessage(1);
				return;
			}
			
			JSONArray result = new JSONArray(resultString);
			// insert DB comment table
			ArrayList<VOComment> commentList = new ArrayList<VOComment>();
			VOComment voComment = null;
			for(int i=0; i < result.length(); i++ ){
				JSONObject obj = (JSONObject)result.get(i);
				voComment = new VOComment(
						obj.getInt("comment_id"),
						obj.getInt("photo_id"),
						obj.getInt("couple_id"),
						obj.getInt("user_id"),
						obj.getString("user_name"),
						obj.getString("comment_time"),
						obj.getString("comment_contents") );
				commentList.add(voComment);
			}			
			voComment = null;
			DAOSqlite.getInstance(context).insertCommentList(commentList);
			
			// if exsist handler send message
			if(handler != null) handler.sendEmptyMessage(1);
			
		}catch(Exception e){
			Log.e(StaticValues.LOG_TAG, LOG_TAG_NAVI + " Error=" + e.toString());
		} // END try();
		
		return;
	} // END run();
	
}
