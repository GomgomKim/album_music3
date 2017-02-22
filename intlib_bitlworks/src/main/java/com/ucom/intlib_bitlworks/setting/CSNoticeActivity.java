package com.ucom.intlib_bitlworks.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import com.ucom.intlib_bitlworks.R;

public class CSNoticeActivity extends Activity {
	public final static String PARAM_IS_ALBUM = "album";
	private boolean isAlbum;

	int menu_unit;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		isAlbum = i.getBooleanExtra(PARAM_IS_ALBUM, false);
		setContentView(R.layout.layout_cs_notice);

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
		if (!isAlbum) {
			findViewById(R.id.tv_label).setVisibility(View.GONE);
		}
		WebView wv = (WebView) findViewById(R.id.wv_notice);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		String url = "";
		wv.clearCache(true);
		wv.loadUrl(url);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();

		if (itemId == android.R.id.home) {
			finish();
			return true;
		}
		return false;
	}
/*
	@Override
	public void initActionBar() {
		if (isAlbum) {
			super.initActionBar();
		} else {
			ActionBar actionBar = getSupportActionBar();
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			actionBar.setCustomView(R.layout.custom_actionbar_base);
			final TextView tv = (TextView) actionBar.getCustomView()
					.findViewById(R.id.title);
			tv.setText("공지사항");
			RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) tv
					.getLayoutParams();
			int screenWidthSize = AlbumUtils.getDisplayMetrics(this).widthPixels;
			param.leftMargin = (screenWidthSize / 2)
					- (AlbumUtils.dpToPx(getResources(), 300 + 80) / 2);
			tv.setLayoutParams(param);

			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setIcon(new ColorDrawable(Color.TRANSPARENT));
			actionBar.setDisplayShowHomeEnabled(true);

			actionBar.setBackgroundDrawable((new ColorDrawable(Color
					.parseColor("#000000"))));
		}
	}
*/
}
