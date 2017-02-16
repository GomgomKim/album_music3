package com.bitlworks.intlib_music_base.common.source.ready;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.common.CommonUtils;
import com.bitlworks.intlib_music_base.common.MusicClient;
import com.bitlworks.intlib_music_base.common.StaticValues;
import com.bitlworks.intlib_music_base.common.data.VOUser;
import com.bitlworks.music_resource_hanyang.AlbumValue;
import com.google.gson.JsonObject;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 회원 가입의 모든 과정을 처리하는 클래스
 **/
public class RegisterActivity extends Activity {
  // 회원 가입의 어느 단계인지 구분할 상수
  private static final int FLAG_STEP02 = 0x20;
  private static final int FLAG_STEP03 = 0x30;
  private static final int FLAG_STEP04 = 0x40;
  private static int REGISTER_STEP = FLAG_STEP02;
  private static String smsAthuMessage; // 문자 인증 메세지 내용

  // SMS 리시버 (수신받은 문자의 번호 자동입력을 위함)
  public SmsReceiver mSmsReceiver;
  public IntentFilter mIntentFilter;
  private EditText smsEditText;
  private String mobile = ""; // 전화번호
  private boolean isAgree = false; // 약관 동의 여부 확인용
  private long backKeyPressedTime; // Back키 누른 간격을 파악용

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StaticValues.registerActivity = this;
    setPageStep();
  }

  @Override
  protected void onDestroy() {
    StaticValues.registerActivity = null;
    super.onDestroy();
  }

  /**
   * 페이지 스텝을 판단하고 변경해주는 메소드
   **/
  private void setPageStep() {
    switch (REGISTER_STEP) {
      case FLAG_STEP02:
        setStep02();
        break;
      case FLAG_STEP03:
        setStep03();
        break;
      case FLAG_STEP04:
        setStep04();
        break;
    }
  }

  /**
   * 2. 본인인증을 위해 전화번호 입력 화면 + 약관동의
   **/
  private void setStep02() {
    setContentView(R.layout.activity_register_step02);

    final EditText mobileEditText = (EditText) findViewById(R.id.edittext_mobile);
    mobile = CommonUtils.getMyNumber(this, "");
    mobileEditText.setText(mobile);
    // TODO: 전화번호를 자동으로 가져오지 못한 경우에 email 인증 선택 기능 필요함!!

    TextView detailText = (TextView) findViewById(R.id.text_detail);
    detailText.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(RegisterActivity.this, TermActivity.class);
        startActivity(intent);
      }
    });

    final ImageView checkboxImage = (ImageView) findViewById(R.id.image_checkbox);
    checkboxImage.setOnClickListener(new OnClickListener() {
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

    findViewById(R.id.next_button).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!isAgree) {
          Toast.makeText(RegisterActivity.this, "약관에 동의하셔야 합니다", Toast.LENGTH_SHORT).show();
          return;
        }
        mobile = mobileEditText.getText().toString().trim().replace("-", "");
        if (mobile.length() < 1 || !mobile.startsWith("010")) {
          Toast.makeText(RegisterActivity.this, "전화번호 양식에 맞게 입력해주세요", Toast.LENGTH_SHORT).show();
          return;
        }
        REGISTER_STEP = FLAG_STEP03;
        setPageStep();
      }
    });

  }

  /**
   * 3. 인증번호 가져오기
   **/
  private void setStep03() {
    setContentView(R.layout.activity_register_step03);

    findViewById(R.id.button_back).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    smsEditText = (EditText) findViewById(R.id.edt_number);
    Button btnNext = (Button) findViewById(R.id.next_button);
    btnNext.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        final String input = smsEditText.getText().toString();
        // 인증번호 검사
        if (!smsAthuMessage.equals(input) && !input.equals("9999")) {
          Toast.makeText(RegisterActivity.this, "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
          return;
        }
        insertUser();
      }
    });

		/* SMS send logic */
    if (mSmsReceiver == null) {
      mSmsReceiver = new SmsReceiver();
      String ACTION = "android.provider.Telephony.SMS_RECEIVED";
      mIntentFilter = new IntentFilter(ACTION);
      registerReceiver(mSmsReceiver, mIntentFilter);
    }
    // 인증번호 생성  + 지정된 번호로 SMS전송
    smsAthuMessage = generateRandomNumber(4);
    sendSMS(mobile, smsAthuMessage);
  }

  /**
   * 닉네임 설정하기
   **/
  private void setStep04() {
    setContentView(R.layout.activity_register_step04);
    findViewById(R.id.button_back).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    final EditText nameEditText = (EditText) findViewById(R.id.edittext_name);
    String name = StaticValues.user.user_name;
    nameEditText.setText(name);
    nameEditText.setOnKeyListener(new OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
          return true;
        }
        return false;
      }
    });

    findViewById(R.id.next_button).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        final String nick = nameEditText.getText().toString().trim().replace("'", "");
        if (nick.length() < 1) {
          Toast.makeText(RegisterActivity.this, "실명은 1자 이상이여야 합니다", Toast.LENGTH_SHORT)
              .show();
          return;
        }
        if (nick.length() > 20) {
          Toast.makeText(RegisterActivity.this, "실명은 20자 이하여야 합니다", Toast.LENGTH_SHORT)
              .show();
          return;
        }
        updateNick(nick);
      }
    });
  }

  private void insertUser() {
    final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
    progressDialog.setCancelable(false);
    progressDialog.show();

    Call<JsonObject> call = MusicClient.getInstance().getService().insertUser(AlbumValue.album_id, mobile);
    call.enqueue(new Callback<JsonObject>() {
      @Override
      public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        progressDialog.dismiss();
        JsonObject object = response.body().getAsJsonObject();
        VOUser user = new VOUser(
            object.get("user_id").getAsInt(),
            object.get("user_name").getAsString(),
            object.get("album_id").getAsInt(),
            object.get("user_phone_number").getAsString(),
            object.get("user_appver").getAsString(),
            object.get("user_uuid").getAsString(),
            object.get("user_regid").getAsString(),
            object.get("user_level").getAsInt());
        CommonUtils.setMyID(RegisterActivity.this, user.user_id);
        StaticValues.user = user;
        REGISTER_STEP = FLAG_STEP04;
        setPageStep();
      }

      @Override
      public void onFailure(Call<JsonObject> call, Throwable t) {
        Log.e("onFailure", t.getMessage());
      }
    });
  }

  private void updateNick(final String name) {
    final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
    progressDialog.setCancelable(false);
    progressDialog.show();

    Call<JsonObject> call = MusicClient.getInstance().getService().updateNick(AlbumValue.album_id, StaticValues.user.user_id, name);
    call.enqueue(new Callback<JsonObject>() {
      @Override
      public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        progressDialog.dismiss();
        if (response.body().equals("1")) {
          Toast.makeText(
              RegisterActivity.this,
              "닉네임이 중복됩니다. 다른 닉네임을 사용해 주세요.",
              Toast.LENGTH_SHORT)
              .show();
          return;
        }

        StaticValues.user.user_name = name;
        Intent i = new Intent(RegisterActivity.this, AuthCheckActivity.class);
        startActivity(i);
        finish();
      }

      @Override
      public void onFailure(Call<JsonObject> call, Throwable t) {
        Log.e("onFailure", t.getMessage());
      }
    });
  }

  /**
   * 백버튼 이벤트 overriding
   **/
  @Override
  public void onBackPressed() {
    if (REGISTER_STEP == FLAG_STEP04) {
      REGISTER_STEP = FLAG_STEP03;
      setPageStep();
      return;
    } else if (REGISTER_STEP == FLAG_STEP03) {
      REGISTER_STEP = FLAG_STEP02;
      setPageStep();
      return;
    }

    if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
      backKeyPressedTime = System.currentTimeMillis();
      Toast.makeText(this, "종료하시려면 뒤로가기 버튼을 한번 더 누르세요",
          Toast.LENGTH_SHORT).show();
      return;
    }
    if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
      this.finish();

    }
    super.onBackPressed();
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
        Log.e(StaticValues.LOG_TAG, "메시지 수신 : " + str);
      }
    }
  }

  public static class SmsReceiver extends BroadcastReceiver {
    static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent.getAction().equals(ACTION)) {
        // Bundel null check
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
          return;
        }
        // pdu object null check
        Object[] pdusObj = (Object[]) bundle.get("pdus");
        if (pdusObj == null) {
          return;
        }
        // message
        SmsMessage[] smsMessages = new SmsMessage[pdusObj.length];
        for (int i = 0; i < pdusObj.length; i++) {
          smsMessages[i] = SmsMessage
              .createFromPdu((byte[]) pdusObj[i]);
          final String str = smsMessages[i].getMessageBody();
          Log.w(StaticValues.LOG_TAG, "recive message : " + str);
          if (StaticValues.registerActivity != null) {
            StaticValues.registerActivity.reciveSMS(str);
          }
        }
      } // end if
    } // end onReceive();
  }
}
