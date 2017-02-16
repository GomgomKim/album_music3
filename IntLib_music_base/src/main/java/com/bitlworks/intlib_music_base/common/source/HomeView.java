package com.bitlworks.intlib_music_base.common.source;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.common.StaticValues;

import java.io.File;

public class HomeView extends LinearLayout {

  public HomeView(Context context) {
    super(context);

    String infService = Context.LAYOUT_INFLATER_SERVICE;
    LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
    View v = li.inflate(R.layout.layout_home, this, false);
    addView(v);

    ImageView homeImage = (ImageView) v.findViewById(R.id.image_home);
    String in_file = "";
    for (int i = 0; i < StaticValues.photoList.size(); i++) {
      if (StaticValues.photoList.get(i).type == 3 && StaticValues.photoList.get(i).photo_order == 1) {
        in_file = StaticValues.photoList.get(i).photo_file_name;
        break;
      }
    }
    String rootPath = "/mnt/sdcard/";
    // 내부 저장소 경로 구하기
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    }

    // 폴더 존재 확인 및 생성
    rootPath += "bitlworks/";
    //checkFolder(rootPath);
    rootPath += "mobilemusic/";

    final String LocalPath = rootPath + in_file;// StaticValues.photoList.get(0).photo_file_name;

    File file = new File(LocalPath);
    Uri uri = Uri.fromFile(file);
    homeImage.setImageURI(uri);
    homeImage.setScaleType(ImageView.ScaleType.FIT_XY);
  }
}
