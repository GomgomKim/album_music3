package com.ucom.intlib_bitlworks.setting;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;

import com.bitlworks.music.R;

import java.io.IOException;
import java.io.InputStream;

public class TermsActivity extends Activity {
	WebView etAgreement;
	WebView etPersonalInfomation;

	int menu_unit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.agreement);
		//////////////////////////////////////
		int w = getResources().getDisplayMetrics().widthPixels ;
		menu_unit = w/5;
		findViewById(R.id.imageView1).setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Log.d("TOUCH EVENT", "x>>" + event.getX() + "   y>>" + event.getY());
					Log.d("Touch unit,,,,", "menu unit>>" + menu_unit);
					if (event.getX() > 0 && event.getX() < menu_unit) {  // menu 1
						finish();
					}
				}
				return true;
			}
		});

		/////////////////////////////////////
		etAgreement = (WebView) findViewById(R.id.et_agreement);
		etPersonalInfomation = (WebView) findViewById(R.id.et_personal_infomation);
		insertContent();
	}

	private void insertContent() {
		/*String txtAgreement;
		String txtPersonalInformation;
		try {
			txtAgreement = readText("agreement.txt");
			txtPersonalInformation = readText("personal_infomation.txt");
			etAgreement.setText(txtAgreement);
			etPersonalInfomation.setText(txtPersonalInformation);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		etAgreement.loadUrl("http://wedding.bitlworks.co.kr/wedding/agreement1.html");
		etPersonalInfomation.loadUrl("http://wedding.bitlworks.co.kr/wedding/agreement2.html");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
	private String readText(String file) throws IOException {
		InputStream is = getAssets().open(file);

		int size = is.available();
		byte[] buffer = new byte[size];
		is.read(buffer);
		is.close();

		String text = new String(buffer);

		return text;
	}

}
