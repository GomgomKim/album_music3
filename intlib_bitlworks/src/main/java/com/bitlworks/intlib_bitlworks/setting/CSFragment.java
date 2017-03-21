package com.bitlworks.intlib_bitlworks.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitlworks.intlib_bitlworks.CommonUtils;
import com.bitlworks.intlib_bitlworks.R;

public class CSFragment extends Fragment {

  private Listener listener;

  public static Fragment newInstance() {
    CSFragment fragment = new CSFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_cs, container, false);

    TextView currentVersionText = (TextView) view.findViewById(R.id.text_current_version);
    currentVersionText.setText(CommonUtils.getAppVersionName(getActivity()));
    TextView newVersionText = (TextView) view.findViewById(R.id.text_new_version);
    newVersionText.setText(CommonUtils.getLatestInstallableVersion(getActivity()));

    view.findViewById(R.id.view_term).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.clickTerm();
      }
    });

    view.findViewById(R.id.view_notice).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.clickNotice();
      }
    });

    view.findViewById(R.id.view_report).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.clickReport();
      }
    });

    view.findViewById(R.id.text_call).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:" + getString(R.string.cs_call)));
        startActivity(i);
      }
    });

    view.findViewById(R.id.text_email).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new AlertDialog.Builder(getActivity()).setMessage("이메일을 전송하시겠습니까?")
            .setPositiveButton("예", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.cs_email)});
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
              }
            }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            // noting
          }
        }).show();
      }
    });
    return view;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof Listener) {
      listener = (Listener) activity;
    } else {
      throw new RuntimeException(activity.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof Listener) {
      listener = (Listener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    listener = null;
  }

  public interface Listener {
    void clickTerm();

    void clickNotice();

    void clickReport();
  }
}
