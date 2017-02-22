package com.bitlworks.intlib_music_base;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;


import com.bitlworks.intlib_music_base.network.Service;
import com.tsengvn.typekit.Typekit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MusicClient extends Application {

  private static MusicClient instance;
  private Service service;

  public static MusicClient getInstance() {
    return instance;
  }

  public Service getService() {
    return service;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
    initService();

    Typekit.getInstance()
        .addNormal(Typekit.createFromAsset(this, "font.ttf"))
        .addBold(Typekit.createFromAsset(this, "font.ttf"));
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
  }

  private void initService() {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Service.ROOT)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    service = retrofit.create(Service.class);
  }
}
