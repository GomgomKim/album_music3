package com.bitlworks.intlib_bitlworks.auth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.bitlworks.intlib_bitlworks.StaticValues;

public class SmsReceiver  extends BroadcastReceiver {
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
        StaticValues.registerFragment.reciveSMS(str);
      }
    } // end if
  } // end onReceive();
}
