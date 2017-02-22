package com.bitlworks.intlib_bitlworks.setting;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bitlworks.intlib_bitlworks.R;


public class ReportFragment extends Fragment {

  public static Fragment newInstance() {
    ReportFragment fragment = new ReportFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_report, container, false);
    final EditText contentEditText = (EditText) view.findViewById(R.id.edittext_content);

    view.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String contentStr = contentEditText.getText().toString();
        // TODO: 메일 직접 전송으로 바꾸어야함
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("plain/text");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.cs_email)});
        email.putExtra(Intent.EXTRA_SUBJECT, "모바일뮤직 버그리포트 및 제휴신청");
        email.putExtra(Intent.EXTRA_TEXT, contentStr);
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
      }
    });

    view.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        getActivity().getFragmentManager().popBackStack();
      }
    });
    return view;
  }
}
