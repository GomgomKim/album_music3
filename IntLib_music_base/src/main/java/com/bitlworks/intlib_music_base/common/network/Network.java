package com.bitlworks.intlib_music_base.common.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.bitlworks.WeddingAlbum.DataNet.StaticValues;
import com.bitlworks.music._common.data.DAOSqlite;
import com.bitlworks.music._common.data.DataNetUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Network {
	private String LOG_TAG_NAVI = "<Network.class>";

	public final static int URL_PORTRAIT = 0;
	public final static int URL_LANDSCAPE = 1;
	static Network instance = null;
	static CookieStore sCookieStore;

	public static Network getInstance() {
		if (instance == null)
			instance = new Network();
		return instance;
	}

	public void insertAuthorInfo(Context context, String url, File file,
															 int sid, String name, String password, String uuid)
			throws Exception {

		ContentType contentType = ContentType.create("String", "UTF-8");
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);
		DefaultHttpClient httpClient = new DefaultHttpClient(params);

		HttpPost httppost = new HttpPost(url);

		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
				.create();
		multipartEntityBuilder.addPart("sid", new StringBody(sid + "",
				contentType));
		multipartEntityBuilder.addPart("name",
				new StringBody(name, contentType));
		multipartEntityBuilder.addPart("pw", new StringBody(password,
				contentType));
		if (file.isFile()) {
			multipartEntityBuilder.addPart("imgfile", new FileBody(file));
		}
		multipartEntityBuilder.addPart("uuid",
				new StringBody(uuid, contentType));
		httppost.setEntity(multipartEntityBuilder.build());
		httpClient
				.execute(httppost, new AuthorInfoInsertHandler(context, file));

	}

	public void updateAuthorInfo(Context context, String url, int ano,
															 File file, String name, String uuid) throws Exception {

		ContentType contentType = ContentType.create("String", "UTF-8");
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);
		DefaultHttpClient httpClient = new DefaultHttpClient(params);

		HttpPost httppost = new HttpPost(url);

		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
				.create();
		multipartEntityBuilder.addPart("ano", new StringBody(ano + "",
				contentType));
		if (file != null) {
			multipartEntityBuilder.addPart("imgfile", new FileBody(file));
		}
		multipartEntityBuilder.addPart("name",
				new StringBody(name, contentType));
		multipartEntityBuilder.addPart("uuid",
				new StringBody(uuid, contentType));
		httppost.setEntity(multipartEntityBuilder.build());
		httpClient
				.execute(httppost, new AuthorInfoUpdateHandler(context, file));

	}

	public void uploadAuthorComment(Context context, String url, int studioId,
																	int authorId, String authorName, String comment,
																	ArrayList<File> images) throws Exception {

		ContentType contentType = ContentType.create("String", "UTF-8");
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);
		DefaultHttpClient httpClient = new DefaultHttpClient(params);

		HttpPost httppost = new HttpPost(url);

		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
				.create();
		multipartEntityBuilder.addTextBody("sno", studioId + "");
		multipartEntityBuilder.addPart("ano", new StringBody(authorId + "",
				contentType));
		multipartEntityBuilder.addPart("name", new StringBody(authorName,
				contentType));
		multipartEntityBuilder.addPart("cmt", new StringBody(comment,
				contentType));
		for (int i = 0; i < images.size(); i++) {
			multipartEntityBuilder.addPart("imgfile[]",
					new FileBody(images.get(i)));
		}
		httppost.setEntity(multipartEntityBuilder.build());
		httpClient.execute(httppost, new PhotoUploadResponseHandler(context,
				images));

	}

	public void uploadPurchaseForm(Context context, String url, int studioId,
																 String uuid, String filePath, String name1, String contact,
																 boolean isNew) throws Exception {

		ContentType contentType = ContentType.create("String", "UTF-8");
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);
		DefaultHttpClient httpClient = new DefaultHttpClient(params);

		HttpPost httppost = new HttpPost(url);

		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
				.create();
		multipartEntityBuilder.addPart("uuid",
				new StringBody(uuid, contentType));
		multipartEntityBuilder.addPart("imgfile", new FileBody(new File(
				filePath)));
		multipartEntityBuilder.addPart("name1", new StringBody(name1,
				contentType));
		multipartEntityBuilder.addPart("tel", new StringBody(contact,
				contentType));
		multipartEntityBuilder.addPart("sid", new StringBody(studioId + "",
				contentType));
		if (isNew) {
			multipartEntityBuilder.addPart("new", new StringBody("1",
					contentType));
		}
		httppost.setEntity(multipartEntityBuilder.build());
		ArrayList<File> array = new ArrayList<File>();
		array.add(new File(filePath));
		httpClient.execute(httppost, new PhotoUploadResponseHandler(context,
				array));

	}

	private class AuthorInfoInsertHandler implements ResponseHandler<Object> {
		private Context mContext;
		private File mFile;

		public AuthorInfoInsertHandler(Context context, File f) {
			mContext = context;
			mFile = f;
		}

		@Override
		public Object handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {

			HttpEntity r_entity = response.getEntity();
			final String responseString = EntityUtils.toString(r_entity);
			Log.e("ab1233", "UPLOAD RES=" + responseString);
			if (response.getStatusLine().getStatusCode() != 200) {
				if (mContext instanceof Activity) {
					((Activity) mContext).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(mContext, "서버 접속에 실패했습니다",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			} else {
				if (mContext instanceof Activity) {
					((Activity) mContext).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(mContext, "작가 가입이 완료되었습니다",
									Toast.LENGTH_SHORT).show();
							Intent i = new Intent();
							try {
								JSONObject jAuthor = new JSONObject(
										responseString);
								i.putExtra("name",
										jAuthor.getString("artist_name"));
								i.putExtra("aid", jAuthor.getInt("artist_no"));
							} catch (JSONException e) {
								e.printStackTrace();
							}

							((Activity) mContext).setResult(Activity.RESULT_OK,
									i);
							((Activity) mContext).finish();
							if (mFile != null && mFile.exists()) {
								mFile.delete();
							}
						}
					});
				}
			}
			return null;
		}

	}

	private class AuthorInfoUpdateHandler implements ResponseHandler<Object> {
		private Context mContext;
		private File mFile;

		public AuthorInfoUpdateHandler(Context context, File f) {
			mContext = context;
			mFile = f;
		}

		@Override
		public Object handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {

			HttpEntity r_entity = response.getEntity();
			final String responseString = EntityUtils.toString(r_entity);
			Log.e("ab1233", "UPLOAD RES=" + responseString);
			if (response.getStatusLine().getStatusCode() != 200) {
				if (mContext instanceof Activity) {
					((Activity) mContext).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(mContext, "서버 접속에 실패했습니다",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			} else {
				if (mContext instanceof Activity) {
					((Activity) mContext).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(mContext, "작가 정보 수정이 완료되었습니다",
									Toast.LENGTH_SHORT).show();
							Intent i = new Intent();
							try {
								JSONObject jAuthor = new JSONObject(
										responseString);
								i.putExtra("name",
										jAuthor.getString("artist_name"));
								i.putExtra("aid", jAuthor.getInt("artist_no"));
							} catch (JSONException e) {
								e.printStackTrace();
							}

							((Activity) mContext).setResult(Activity.RESULT_OK,
									i);
							((Activity) mContext).finish();
							if (mFile != null && mFile.exists()) {
								mFile.delete();
							}
							// ((Activity)
							// mContext).setResult(Activity.RESULT_OK);
							// ((Activity) mContext).finish();
							// if (mFile != null && mFile.exists()) {
							// mFile.delete();
							// }
						}
					});
				}
			}
			return null;
		}

	}

	private class PhotoUploadResponseHandler implements ResponseHandler<Object> {
		private Context mContext;
		private ArrayList<File> fileList;

		public PhotoUploadResponseHandler(Context context, ArrayList<File> f) {
			mContext = context;
			this.fileList = f;
		}

		@Override
		public Object handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			HttpEntity r_entity = response.getEntity();
			String responseString = EntityUtils.toString(r_entity);
			Log.e("ab1233", "UPLOAD RES=" + responseString);
			if (response.getStatusLine().getStatusCode() != 200) {
				if (mContext instanceof Activity) {
					((Activity) mContext).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(mContext, "업로드에 실패했습니다",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			} else {
				if (mContext instanceof Activity) {
					((Activity) mContext).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// Toast.makeText(mContext, "업로드가 완료되었습니다",
							// Toast.LENGTH_SHORT).show();
							((Activity) mContext).setResult(Activity.RESULT_OK);
							((Activity) mContext).finish();
						}
					});
				}
			}
			for (File file : fileList) {
				if (file.exists()) {
					file.delete();
				}
			}
			return null;
		}
	}

	public String insertAuthorCommentReply(Context context, int commentId,
																				 String uuid, String nick, String reply) throws Exception {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI
				+ " insertAuthorCommentReply()");

		// Set HttpParams
		DefaultHttpClient httpClient = new DefaultHttpClient();
		BasicHttpContext httpContext = new BasicHttpContext();
		HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);

		// Set RequsetParams
		StringBuffer sb = new StringBuffer();
		sb.append(StaticValues.SERVICE_URL + "insertStudioCommentReply.php");
		sb.append("?cno=" + commentId);
		sb.append("&uuid=" + URLEncoder.encode(uuid, "UTF-8"));
		if (nick != null) {
			sb.append("&nick=" + URLEncoder.encode(nick, "UTF-8"));
		}
		sb.append("&cmt=" + URLEncoder.encode(reply, "UTF-8"));
		final String surl = sb.toString();
		Log.d(StaticValues.LOG_TAG, LOG_TAG_NAVI + " requestUrl=" + surl);
		HttpGet httpReq = new HttpGet(surl);

		// Execute
		HttpResponse res = httpClient.execute(httpReq, httpContext);
		int statusCode = res.getStatusLine().getStatusCode();
		Log.d(StaticValues.LOG_TAG, LOG_TAG_NAVI + " statusCode=" + statusCode);
		if (statusCode != 200)
			throw new RuntimeException("HTTP response code is not 200 OK");
		BufferedReader rd = new BufferedReader(new InputStreamReader(res
				.getEntity().getContent()));

		// response body
		String line = "";
		sb = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		String result = sb.toString();
		return result;
	}

	public String deleteAuthorCommentReply(Context context, int commentId,
																				 int replyId, String uuid) throws Exception {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI
				+ " deleteAuthorCommentReply()");
		// Set HttpParams
		DefaultHttpClient httpClient = new DefaultHttpClient();
		BasicHttpContext httpContext = new BasicHttpContext();
		HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);

		// Set RequsetParams
		StringBuffer sb = new StringBuffer();
		sb.append(StaticValues.SERVICE_URL + "deleteStudioCommentReply.php");
		sb.append("?cno=" + commentId);
		sb.append("&rno" + "=" + replyId);
		sb.append("&uuid" + "=" + uuid);
		final String surl = sb.toString();
		Log.d(StaticValues.LOG_TAG, LOG_TAG_NAVI + " requestUrl=" + surl);
		HttpGet httpReq = new HttpGet(surl);

		// Execute
		HttpResponse res = httpClient.execute(httpReq, httpContext);
		int statusCode = res.getStatusLine().getStatusCode();
		Log.d(StaticValues.LOG_TAG, LOG_TAG_NAVI + " statusCode=" + statusCode);
		if (statusCode != 200)
			throw new RuntimeException("HTTP response code is not 200 OK");
		BufferedReader rd = new BufferedReader(new InputStreamReader(res
				.getEntity().getContent()));

		// response body
		String line = "";
		sb = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		String result = sb.toString();
		return result;
	}

	public String deleteAuthorComment(Context context, int studioId,
																		int authorId, int commentId) throws Exception {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " deleteAuthorComment()");
		// Set HttpParams
		DefaultHttpClient httpClient = new DefaultHttpClient();
		BasicHttpContext httpContext = new BasicHttpContext();
		HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);

		// Set RequsetParams
		StringBuffer sb = new StringBuffer();
		sb.append(StaticValues.SERVICE_URL + "deleteStudioComment.php");
		sb.append("?cno=" + commentId);
		sb.append("&sno" + "=" + studioId);
		sb.append("&ano" + "=" + authorId);
		final String surl = sb.toString();
		Log.d(StaticValues.LOG_TAG, LOG_TAG_NAVI + " requestUrl=" + surl);
		HttpGet httpReq = new HttpGet(surl);

		// Execute
		HttpResponse res = httpClient.execute(httpReq, httpContext);
		int statusCode = res.getStatusLine().getStatusCode();
		Log.d(StaticValues.LOG_TAG, LOG_TAG_NAVI + " statusCode=" + statusCode);
		if (statusCode != 200)
			throw new RuntimeException("HTTP response code is not 200 OK");
		BufferedReader rd = new BufferedReader(new InputStreamReader(res
				.getEntity().getContent()));

		// response body
		String line = "";
		sb = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		String result = sb.toString();
		return result;
	}

	/** JSON 배열 변환 메소드 **/
	public static JSONArray getJsonArray(HttpResponse response)
			throws IllegalStateException, IOException, JSONException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuffer sb = new StringBuffer();
		String resBody;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		resBody = sb.toString();
		// Log.d(TAG, "resBody=" + resBody);
		return new JSONArray(resBody);
	}

	/** JSON 객체 변환 메소드 **/
	public static JSONObject getJsonObject(HttpResponse response)
			throws IllegalStateException, IOException, JSONException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuffer sb = new StringBuffer();
		String resBody;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		resBody = sb.toString();
		return new JSONObject(resBody);
	}

	public DefaultHttpClient initHttpClient() {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		return httpClient;
	}

	public JSONObject getJsonObjectFromUrl(Context context, String url)
			throws IOException, ClientProtocolException, IllegalStateException,
			JSONException {
		Log.d(StaticValues.LOG_TAG, LOG_TAG_NAVI + " requestUrl=" + url);
		DefaultHttpClient httpClient = initHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		HttpGet httpReq = new HttpGet(url);
		// Execute
		HttpResponse res = httpClient.execute(httpReq, httpContext);
		int statusCode = res.getStatusLine().getStatusCode();
		if (statusCode != 200) {
			throw new RuntimeException("HTTP response code is not 200 OK");
		}
		return getJsonObject(res);
	}

	public JSONArray getJsonArrayFromUrl(Context context, String url)
			throws IOException, ClientProtocolException, IllegalStateException,
			JSONException {
		DefaultHttpClient httpClient = initHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		HttpGet httpReq = new HttpGet(url);
		// Execute
		HttpResponse res = httpClient.execute(httpReq, httpContext);
		int statusCode = res.getStatusLine().getStatusCode();
		if (statusCode != 200) {
			throw new RuntimeException("HTTP response code is not 200 OK");
		}
		return getJsonArray(res);
	}

	public String getStringFromUrl(Context context, String url)
			throws IOException, ClientProtocolException, IllegalStateException,
			JSONException {
		DefaultHttpClient httpClient = initHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		HttpGet httpReq = new HttpGet(url);
		// Execute
		HttpResponse res = httpClient.execute(httpReq, httpContext);
		int statusCode = res.getStatusLine().getStatusCode();
		if (statusCode != 200) {
			throw new RuntimeException("HTTP response code is not 200 OK");
		}
		BufferedReader rd = new BufferedReader(new InputStreamReader(res
				.getEntity().getContent()));
		String line = "";
		StringBuffer sb = new StringBuffer();
		String resBody;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		resBody = sb.toString();
		return resBody;
	}

	public static class HttpPostParameter {
		String strParamName[];
		String strparamValue[];
		File[] imgFiles;

		public HttpPostParameter(String[] paramNames, String[] paramValues) {
			this.strParamName = paramNames;
			this.strparamValue = paramValues;
		}

		public HttpPostParameter(String[] paramNames, String[] paramValues,
														 File[] imgFiles) {
			this.strParamName = paramNames;
			this.strparamValue = paramValues;
			this.imgFiles = imgFiles;
		}
	}

	public String getStringFromUrlPost(Context context, String url,
																		 HttpPostParameter params) throws IOException,
			ClientProtocolException, IllegalStateException, JSONException {
		DefaultHttpClient httpClient = initHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		HttpPost httppost = new HttpPost(url);

		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
				.create();
		ContentType contentType = ContentType.create("String", "UTF-8");
		for (int i = 0; i < params.strParamName.length; i++) {
			multipartEntityBuilder.addPart(params.strParamName[i],
					new StringBody(params.strparamValue[i], contentType));
		}
		if (params.imgFiles != null) {
			for (int i = 0; i < params.imgFiles.length; i++) {
				multipartEntityBuilder.addPart("imgfile[]", new FileBody(
						params.imgFiles[i]));
			}
		}
		httppost.setEntity(multipartEntityBuilder.build());
		// Execute
		HttpResponse res = httpClient.execute(httppost, httpContext);
		int statusCode = res.getStatusLine().getStatusCode();
		if (statusCode != 200) {
			throw new RuntimeException("HTTP response code is not 200 OK");
		}
		BufferedReader rd = new BufferedReader(new InputStreamReader(res
				.getEntity().getContent()));
		String line = "";
		StringBuffer sb = new StringBuffer();
		String resBody;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		resBody = sb.toString();
		// new PhotoUpdater(context, , h)
		return resBody;
	}


	/** 사진댓글(or방명록) 글삭제 **/
	public String deleteComment(Context context, int commentId, int userId)
			throws Exception {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " deleteComment()");
		final int coupleId = DataNetUtils.getSelectedCoupleId(context);

		// Set HttpParams
		DefaultHttpClient httpClient = new DefaultHttpClient();
		BasicHttpContext httpContext = new BasicHttpContext();
		HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);

		// Set RequsetParams
		StringBuffer sb = new StringBuffer();
		sb.append(StaticValues.SERVICE_URL + "deleteComment.php");
		sb.append("?cmtid=" + commentId);
		sb.append("&uid=" + userId);
		sb.append("&cid=" + coupleId);
		final String surl = sb.toString();
		Log.d(StaticValues.LOG_TAG, LOG_TAG_NAVI + " requestUrl=" + surl);
		HttpGet httpReq = new HttpGet(surl);

		// Execute
		HttpResponse res = httpClient.execute(httpReq, httpContext);
		int statusCode = res.getStatusLine().getStatusCode();
		Log.d(StaticValues.LOG_TAG, LOG_TAG_NAVI + " statusCode=" + statusCode);
		if (statusCode != 200)
			throw new RuntimeException("HTTP response code is not 200 OK");
		BufferedReader rd = new BufferedReader(new InputStreamReader(res
				.getEntity().getContent()));

		// response body
		String line = "";
		sb = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		final String result = sb.toString();

		// 직전에 로컬 DB에 싱크
		if (result.contains("1")) {
			DAOSqlite.getInstance(context).deleteComment(commentId);
			DAOSqlite.getInstance(context).updateCoupleUpdateFlag(coupleId);
		}
		return result;
	}


	/** 앨범 관리자 인증하기 **/
	public String getAdminCheck(Context context) throws Exception {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " getAdminCheck()");
		final int cid = DataNetUtils.getSelectedCoupleId(context);
		final int uid = DataNetUtils.getMyId(context);

		// Set HttpParams
		DefaultHttpClient httpClient = new DefaultHttpClient();
		BasicHttpContext httpContext = new BasicHttpContext();
		HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);

		// Set RequsetParams
		StringBuffer sb = new StringBuffer();
		sb.append(StaticValues.SERVICE_URL + "getAdminCheck.php");
		sb.append("?uid=" + uid);
		sb.append("&cid=" + cid);
		final String surl = sb.toString();
		Log.d(StaticValues.LOG_TAG, LOG_TAG_NAVI + " requestUrl=" + surl);
		HttpGet httpReq = new HttpGet(surl);

		// Execute
		HttpResponse res = httpClient.execute(httpReq, httpContext);
		int statusCode = res.getStatusLine().getStatusCode();
		Log.d(StaticValues.LOG_TAG, LOG_TAG_NAVI + " statusCode=" + statusCode);
		if (statusCode != 200)
			throw new RuntimeException("HTTP response code is not 200 OK");
		BufferedReader rd = new BufferedReader(new InputStreamReader(res
				.getEntity().getContent()));

		// response body
		String line = "";
		sb = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}

		return sb.toString();
	} // END getAdminCheck();

	/**
	 * 사용자 초대
	 * 
	 * @return 성공시 1, 실패시 0문자열 반환
	 * @param ulist
	 *            사용자의 모바일 번호 리스트
	 **/
	public String insertInvite(Context context, String ulist, int cid, String m)
			throws Exception {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " insertInvite()");

		// Set HttpParams
		DefaultHttpClient httpClient = new DefaultHttpClient();
		BasicHttpContext httpContext = new BasicHttpContext();
		HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);

		// Set RequsetParams
		StringBuffer sb = new StringBuffer();
		sb.append(StaticValues.SERVICE_URL + "insertInvite.php");
		sb.append("?cid=" + cid);
		sb.append("&ulist=" + ulist);
		sb.append("&m=" + m);
		final String surl = sb.toString();
		Log.d(StaticValues.LOG_TAG, LOG_TAG_NAVI + " requestUrl=" + surl);
		HttpGet httpReq = new HttpGet(surl);

		// Execute
		HttpResponse res = httpClient.execute(httpReq, httpContext);
		int statusCode = res.getStatusLine().getStatusCode();
		Log.d(StaticValues.LOG_TAG, LOG_TAG_NAVI + " statusCode=" + statusCode);
		if (statusCode != 200)
			throw new RuntimeException("HTTP response code is not 200 OK");
		BufferedReader rd = new BufferedReader(new InputStreamReader(res
				.getEntity().getContent()));

		// response body
		String line = "";
		sb = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}

		return sb.toString();
	}

	/** 앨범 관리에서 사진 보이기/숨기기 설정 **/
	public String updatePhotoState(Context context, JSONArray jPhotoStateArray)
			throws Exception {
		JSONObject jPhotoState = new JSONObject();
		jPhotoState.put("data", jPhotoStateArray);

		DefaultHttpClient httpClient = new DefaultHttpClient();
		BasicHttpContext httpContext = new BasicHttpContext();

		// Set params
		HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);

		// Set request
		StringBuffer sb = new StringBuffer();
		sb.append(StaticValues.SERVICE_URL + "updatePhotoState.php");
		String surl = sb.toString();
		Log.d(StaticValues.LOG_TAG, LOG_TAG_NAVI + " requestUrl=" + surl);
		HttpPost httpReq = new HttpPost(surl);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("JSON", jPhotoState
				.toString()));
		httpReq.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		Log.d(StaticValues.LOG_TAG,
				LOG_TAG_NAVI + " postJSON=" + jPhotoState.toString());

		// Execute
		HttpResponse res = httpClient.execute(httpReq, httpContext);
		int statusCode = res.getStatusLine().getStatusCode();
		Log.d(StaticValues.LOG_TAG, LOG_TAG_NAVI + " statusCode=" + statusCode);
		if (statusCode != 200) {
			throw new RuntimeException("HTTP response code is not 200 OK");
		}
		BufferedReader rd = new BufferedReader(new InputStreamReader(res
				.getEntity().getContent()));

		// response body
		String line = "";
		sb = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		final String result = sb.toString();

		if (result.trim().equals("1")) {
			DAOSqlite sqlDAO = DAOSqlite.getInstance(context);
			sqlDAO.updatePhotoState(jPhotoStateArray);
			return "1";
		}

		return "0";
	}
}
