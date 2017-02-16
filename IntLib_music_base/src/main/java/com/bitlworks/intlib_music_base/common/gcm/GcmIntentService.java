/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bitlworks.intlib_music_base.common.gcm;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.common.StaticValues;
import com.bitlworks.intlib_music_base.common.source.ready.AuthCheckActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.List;


/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "GCMLog";

    @Override
    protected void onHandleIntent(Intent intent) {
Log.i(TAG, "GCM push Received");
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
Log.i(TAG, "GCM messageType : " + messageType);
Log.i(TAG, "extras.getString(message) : " + extras.getString("message") );
        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
//                sendNotification("Send error: " + extras.toString());
            	Log.e(StaticValues.LOG_TAG, "Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
//                sendNotification("Deleted messages on server: " + extras.toString());
            	Log.e(StaticValues.LOG_TAG, "Deleted messages on server: " + extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.i(TAG, "Received: hyuk" + extras.toString());
            	// TODO GCM이 왔을때 수행 코드
                // Post notification of received message.
            	//String notiMsg = extras.getString("message");

               // if(StaticObject.on_off == 1) {

                StaticValues.NOTIFICATION_FLAG = 100;
                    String notiMsg = extras.getString("message");
                StaticValues.NOTIFICATION_MESSAGE = notiMsg;
                    //if(notiMsg != null)
                    if(StaticValues.pagerMainActivity == null) {
                        sendNotification(notiMsg);
                    }else{

                      Log.i(TAG, "hyuk>>>" + notiMsg);
                        ((Activity) StaticValues.pagerMainActivity).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                //notifyDataSetChanged here or update your UI on different thread
                                //IA.notifyDataSetChanged();
                                if (StaticValues.pagerMainActivity.listener != null) {
                                    StaticValues.pagerMainActivity.listener.updateCommentList();
                                }
                            }
                        });

                        //StaticObject.mainActivityRef.show_noti();
                    }

               // }

               // Intent intent2 = new Intent(this, IntroActivity.class);
               // startActivity(intent2);
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    
   	private void sendNotification(final String notiMsg) {
   		final String[] _msg_array = notiMsg.split("<@>");
    	// Message Parsing
//    	final String messageCode = _msg_array[0].trim();
    	final String messageTitle = _msg_array[1].trim();
    	final String messageContents = _msg_array[2].trim();
    	/*
    	NotificationManager mNotificationManager = (NotificationManager)
            this.getSystemService(Context.NOTIFICATION_SERVICE);

    PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
            new Intent(this, MainActivity.class), 0);

    NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("PubNub GCM Notification")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(msg))
                    .setContentText(msg);

    mBuilder.setContentIntent(contentIntent);
    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        */
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, AuthCheckActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.gallery_icon)
        .setContentTitle(messageTitle)
        .setStyle( new NotificationCompat.BigTextStyle() .bigText(messageContents) )
        .setContentText(messageContents)
        .setAutoCancel(true)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) ;

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        ///////////////////////////////////
        StaticValues.unread_count++;
        setBadge(this, StaticValues.unread_count);
    }

    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }
}
