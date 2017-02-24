package com.bitlworks.intlib_music_base.source;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.StaticValues;

import java.io.File;

public class MusicView extends LinearLayout {

  public MusicView(Context context, int index) {
    super(context);
    LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View v = li.inflate(R.layout.layout_music, this, true);

    String in_file = "";
    int check_count = 0;
    for (int i = 0; i < StaticValues.photos.size(); i++) {
      if (StaticValues.photos.get(i).type == 1) {
        //check_count++;
        if (check_count == index) {
          in_file = StaticValues.photos.get(i).photo_file_name;
          break;
        }
        check_count++;
      }
    }

    String rootPath = "/mnt/sdcard/";
    // 내부 저장소 경로 구하기
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    }
    // 폴더 존재 확인 및 생성
    rootPath += "bitlworks/mobilemusic/";
    final String LocalPath = rootPath + in_file;
    File file = new File(LocalPath);
    Uri uri = Uri.fromFile(file);

    ImageView homeImage = (ImageView) v.findViewById(R.id.image_music);
    homeImage.setImageURI(uri);
    homeImage.setScaleType(ImageView.ScaleType.FIT_XY);
  }
}
