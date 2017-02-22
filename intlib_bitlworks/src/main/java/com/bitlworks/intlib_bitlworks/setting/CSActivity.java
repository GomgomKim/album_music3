package com.bitlworks.intlib_bitlworks.setting;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bitlworks.intlib_bitlworks.R;

public class CSActivity extends AppCompatActivity implements CSFragment.Listener {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cs);

    findViewById(R.id.view_actionbar).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
          finish();
          return;
        }
        getFragmentManager().popBackStack();
      }
    });

    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.view_frame, CSFragment.newInstance());
    fragmentTransaction.commit();
  }

  @Override
  public void clickTerm() {
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction
        .addToBackStack("TermFragment")
        .add(R.id.view_frame, TermFragment.newInstance())
        .commit();
  }

  @Override
  public void clickNotice() {
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction
        .addToBackStack("NoticeFragment")
        .add(R.id.view_frame, NoticeFragment.newInstance())
        .commit();
  }

  @Override
  public void clickReport() {
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction
        .addToBackStack("ReportFragment")
        .add(R.id.view_frame, ReportFragment.newInstance())
        .commit();
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
