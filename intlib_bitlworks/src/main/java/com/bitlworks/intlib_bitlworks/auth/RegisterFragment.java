package com.bitlworks.intlib_bitlworks.auth;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitlworks.intlib_bitlworks.CommonUtils;
import com.bitlworks.intlib_bitlworks.R;

import java.util.Random;

public class RegisterFragment extends Fragment {

  // 회원 가입의 어느 단계인지 구분할 상수
  private static final int FLAG_STEP02 = 0x20;
  private static final int FLAG_STEP03 = 0x30;
  private static final int FLAG_STEP04 = 0x40;
  private static int REGISTER_STEP = FLAG_STEP02;
  private static String smsAthuMessage; // 문자 인증 메세지 내용
  public SmsReceiver mSmsReceiver;
  private Listener listener;
  private EditText smsEditText;
  private String mobile = ""; // 전화번호
  private boolean isAgree = false; // 약관 동의 여부 확인용
  private View registerView1, registerView2, registerView3;

  public static Fragment newInstance() {
    RegisterFragment fragment = new RegisterFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_register, container, false);

    registerView1 = view.findViewById(R.id.view_register1);
    registerView2 = view.findViewById(R.id.view_register2);
    registerView3 = view.findViewById(R.id.view_register3);

    view.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (REGISTER_STEP == FLAG_STEP04) {
          REGISTER_STEP = FLAG_STEP03;
        } else if (REGISTER_STEP == FLAG_STEP03) {
          REGISTER_STEP = FLAG_STEP02;
        }
        setPageStep();
      }
    });


    final EditText mobileEditText = (EditText) view.findViewById(R.id.edittext_mobile);
    mobile = CommonUtils.getMyNumber(getActivity(), "");
    mobileEditText.setText(mobile);
    // TODO: 전화번호를 자동으로 가져오지 못한 경우에 email 인증 선택 기능 필요함!!

    TextView detailText = (TextView) view.findViewById(R.id.text_detail);
    detailText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), TermActivity.class);
        startActivity(intent);
      }
    });

    final ImageView checkboxImage = (ImageView) view.findViewById(R.id.image_checkbox);
    checkboxImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isAgree) {
          isAgree = false;
          checkboxImage.setImageResource(R.drawable.register_button_agree_off);
        } else {
          isAgree = true;
          checkboxImage.setImageResource(R.drawable.register_button_agree_on);
        }
      }
    });
    if (isAgree)
      checkboxImage.setImageResource(R.drawable.register_button_agree_on);
    else
      checkboxImage.setImageResource(R.drawable.register_button_agree_off);


    smsEditText = (EditText) view.findViewById(R.id.edt_number);


    final EditText nameEditText = (EditText) view.findViewById(R.id.edittext_name);
    nameEditText.setText("User");
    nameEditText.setOnKeyListener(new View.OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
          return true;
        }
        return false;
      }
    });


    view.findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        switch (REGISTER_STEP) {
          case FLAG_STEP02:
            if (!isAgree) {
              Toast.makeText(getActivity(), "약관에 동의하셔야 합니다", Toast.LENGTH_SHORT).show();
              return;
            }
            mobile = mobileEditText.getText().toString().trim().replace("-", "");
            if (mobile.length() < 1 || !mobile.startsWith("010")) {
              Toast.makeText(getActivity(), "전화번호 양식에 맞게 입력해주세요", Toast.LENGTH_SHORT).show();
              return;
            }
                /* SMS send logic */
            if (mSmsReceiver == null) {
              mSmsReceiver = new SmsReceiver();
              String ACTION = "android.provider.Telephony.SMS_RECEIVED";
              IntentFilter mIntentFilter = new IntentFilter(ACTION);
              getActivity().registerReceiver(mSmsReceiver, mIntentFilter);
            }
            // 인증번호 생성  + 지정된 번호로 SMS전송
            smsAthuMessage = generateRandomNumber(4);
            sendSMS(mobile, smsAthuMessage);

            REGISTER_STEP = FLAG_STEP03;
            setPageStep();
            break;
          case FLAG_STEP03:
            final String input = smsEditText.getText().toString();
            // 인증번호 검사
            if (!smsAthuMessage.equals(input) && !input.equals("9999")) {
              Toast.makeText(getActivity(), "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
              return;
            }
            listener.insertUser(mobile);
            REGISTER_STEP = FLAG_STEP04;
            setPageStep();
            break;
          case FLAG_STEP04:
            final String nick = nameEditText.getText().toString().trim().replace("'", "");
            if (nick.length() < 1) {
              Toast.makeText(getActivity(), "실명은 1자 이상이여야 합니다", Toast.LENGTH_SHORT)
                  .show();
              return;
            }
            if (nick.length() > 20) {
              Toast.makeText(getActivity(), "실명은 20자 이하여야 합니다", Toast.LENGTH_SHORT)
                  .show();
              return;
            }
            listener.updateName(nick);
            break;
        }
      }
    });
    return view;
  }

  private void setPageStep() {
    switch (REGISTER_STEP) {
      case FLAG_STEP02:
        registerView1.setVisibility(View.VISIBLE);
        registerView2.setVisibility(View.GONE);
        registerView3.setVisibility(View.GONE);
        break;
      case FLAG_STEP03:
        registerView1.setVisibility(View.GONE);
        registerView2.setVisibility(View.VISIBLE);
        registerView3.setVisibility(View.GONE);
        break;
      case FLAG_STEP04:
        registerView1.setVisibility(View.GONE);
        registerView2.setVisibility(View.GONE);
        registerView3.setVisibility(View.VISIBLE);
        break;
    }
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

  /**
   * 인증 번호 생성 메소드
   **/
  private String generateRandomNumber(int len) {
    String baseNumberString = "0123456789";
    Random random = new Random();
    int baseLen = baseNumberString.length();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < len; i++) {
      int idx = random.nextInt(baseLen);
      sb.append(baseNumberString.charAt(idx));
    }
    return sb.toString();
  }

  /**
   * SMS 발신 메소드
   **/
  private void sendSMS(String mobile, String message) {
    android.telephony.SmsManager smsManager = android.telephony.SmsManager
        .getDefault();
    String sendTo = mobile;
    smsManager.sendTextMessage(sendTo, null, message, null, null);
  }

  public void reciveSMS(String str) {
    if (REGISTER_STEP == FLAG_STEP03 && smsEditText != null) {
      if (str != null && str.equals(smsAthuMessage)) {
        smsEditText.setText(str);
      }
    }
  }

  public interface Listener {
    void insertUser(String mobile);

    void updateName(String name);
  }
}
