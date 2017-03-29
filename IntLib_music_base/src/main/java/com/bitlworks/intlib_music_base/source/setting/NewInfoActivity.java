package com.bitlworks.intlib_music_base.source.setting;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.data.VONewInfo;


public class NewInfoActivity extends Activity implements NewInfoFragment.Listener {


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_info);

    findViewById(R.id.view_back).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
          finish();
          return;
        }
        getFragmentManager().popBackStack();
      }
    });

    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(com.bitlworks.intlib_bitlworks.R.id.view_frame, NewInfoFragment.newInstance());
    fragmentTransaction.commit();
  }


  @Override
  public void clickNewInfo(VONewInfo item) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.link_url));
    startActivity(intent);
//    FragmentManager fragmentManager = getFragmentManager();
//    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//    fragmentTransaction
//        .addToBackStack("NewInfoFragment")
//        .add(com.bitlworks.intlib_bitlworks.R.id.view_frame, NewInfoDetailFragment.newInstance(item))
//        .commit();
  }

  @Override
  public void onBackPressed() {
    if (getFragmentManager().getBackStackEntryCount() == 0) {
      super.onBackPressed();
      return;
    }
    getFragmentManager().popBackStack();
  }
}
