package com.bitlworks.intlib_music_base.common.network;

/**
 * Created by 강혁 on 2016-07-27.
 */
/*
public class NETgetUserInfo {
}
*/

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bitlworks.music._common.StaticValues;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by 강혁 on 2016-07-21.
 */

public class NETgetUserInfo extends Thread {
    private String LOG_TAG_NAVI = "<NetGetMobileMusic.class>";
    private int mobile_music_id;
    int user_id;
    String user_name;
    String comment_contents;
    String phone_number;

    private final Handler mHandler;

    public NETgetUserInfo( int id, Handler h) {
        Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " NetGetMobileMusic()");
        this.mHandler = h;
        this.user_id = id;
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
            sb.append(StaticValues.SERVICE_URL + "getUserInfo.php");
            sb.append("?u=" + user_id);
            //sb.append("&p=" +  URLEncoder.encode(phone_number, "UTF-8"));

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

            // Handler return
            Message msg = new Message();
            msg.obj = sb.toString();
            if (mHandler != null) mHandler.sendMessage(msg);
        }catch(Exception e){
            Log.e(StaticValues.LOG_TAG, LOG_TAG_NAVI + " Error=" + e.toString());
        } // END try();

        return;
    } // END run();

}

