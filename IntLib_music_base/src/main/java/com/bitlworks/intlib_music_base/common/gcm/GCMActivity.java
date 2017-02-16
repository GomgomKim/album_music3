package com.bitlworks.intlib_music_base.common.gcm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bitlworks.intlib_music_base.common.StaticValues;
import com.bitlworks.intlib_music_base.common.source.ready.AuthCheckActivity;

public class GCMActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final String[] _msg_array = StaticValues.NOTIFICATION_MESSAGE.split("<@>");
    	final String messageCode = _msg_array[0].trim();
//    	final String messageTitle = _msg_array[1].trim();
//    	final String messageContents = _msg_array[2].trim();

		Intent intent = new Intent(this, AuthCheckActivity.class);
		startActivity(intent);
		/*
        if (StaticObject.mainActivityRef == null) {
           	StaticObject.NOTIFICATION_FLAG=1;
        	Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
		}*/
        // b. 앱이 실행되고 있는 경우
        //else{
        	/*
        	if (StaticObject.lawyerActivityRef != null)
				StaticObject.lawyerActivityRef.finish();
			if (StaticObject.tipsViewerActivityRef != null)
				StaticObject.tipsViewerActivityRef.finish();
			StaticObject.mainActivityRef.loadMyFragment();
			*/
        //}
		super.onCreate(savedInstanceState);
		
		finish();
	}

}
