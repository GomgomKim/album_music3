package com.bitlworks.music_app_hanyang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bitlworks.intlib_music_base.source.ready.AuthCheckActivity;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    finish();
    Intent intent = new Intent(this, AuthCheckActivity.class);
    startActivity(intent);
  }
}
