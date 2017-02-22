package com.bitlworks.intlib_music_base.source.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.StaticValues;
import com.bitlworks.intlib_music_base.data.VOFriend;

import java.util.ArrayList;


public class MsgActivity extends Activity {

  private FriendListAdapter friendAdapter;
  private ArrayList<VOFriend> friendList = new ArrayList<>();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_msg);

    findViewById(R.id.view_back).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });

    ListView friendListView = (ListView) findViewById(R.id.listview_friend);
    friendAdapter = new FriendListAdapter(this, friendList);
    friendListView.setAdapter(friendAdapter);

    findViewById(R.id.button_add_friend).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, 0);
      }
    });

    findViewById(R.id.button_invite_friend).setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (friendList.size() == 0) {
          return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        intent.putExtra("sms_body", StaticValues.album.album_invitemsg + " " + StaticValues.album.album_inviteurl);
        String mobile = "";
        for (int i = 0; i < friendList.size(); i++) {
          mobile = friendList.get(i).mobile + ";";
        }
        intent.putExtra("address", mobile);
        startActivity(intent);
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      Cursor cursor = getContentResolver().query(data.getData(),
          new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
              ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
      cursor.moveToFirst();

      friendList.add(new VOFriend(cursor.getString(0), cursor.getString(1)));
      friendAdapter.notifyDataSetChanged();
      cursor.close();
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  public class FriendListAdapter extends ArrayAdapter<VOFriend> {

    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;

    public FriendListAdapter(Context context, ArrayList<VOFriend> arrayList) {
      super(context, 0, arrayList);
      this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView = inflater.inflate(R.layout.item_friend, null);
        viewHolder = new ViewHolder();
        viewHolder.nameText = (TextView) convertView.findViewById(R.id.text_name);
        viewHolder.mobileText = (TextView) convertView.findViewById(R.id.text_mobile);
        viewHolder.deleteButton = convertView.findViewById(R.id.button_delete);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      final VOFriend item = getItem(position);
      viewHolder.nameText.setText(item.name);
      viewHolder.mobileText.setText(item.mobile);
      viewHolder.deleteButton.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          friendList.remove(position);
          friendAdapter.notifyDataSetChanged();
        }
      });
      return convertView;
    }

    @Override
    public int getCount() {
      return super.getCount();
    }

    @Override
    public VOFriend getItem(int position) {
      return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
      return super.getItemId(position);
    }

    private class ViewHolder {
      TextView nameText = null;
      TextView mobileText = null;
      View deleteButton = null;
    }
  }

}
