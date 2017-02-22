package com.ucom.intlib_bitlworks.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bitlworks.WeddingAlbum.DataNet.StaticValues;
import com.bitlworks.music.R;
import com.bitlworks.music.common.VOFriend;

import java.util.ArrayList;

/**
 * Created by 강혁 on 2016-08-08.
 */
public class InviteFriend extends Activity {

    ListView friends_list;

    public static ArrayList<VOFriend> friendList;
    FriendListAdapter friendAdapter;
    int menu_unit;
    String mobile="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.invite);
//////////////////////////////
        int w = getResources().getDisplayMetrics().widthPixels ;
        menu_unit = w/5;
        findViewById(R.id.image_top).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d("TOUCH EVENT", "x>>" + event.getX() + "   y>>" + event.getY());
                    Log.d("Touch unit,,,,", "menu unit>>" + menu_unit);
                    if (event.getX() > 0 && event.getX() < menu_unit) {  // menu 1
                        finish();
                    }
                }
                return true;
            }
        });
        /////////////////////////////
        friendList =  new ArrayList<VOFriend>();
        friends_list = (ListView)findViewById(R.id.lv_friends);

        friendAdapter = new FriendListAdapter(this, friendList);//, lastAnsims);
        friends_list.setAdapter(friendAdapter);


        ImageButton add_friend = (ImageButton)findViewById(R.id.add_friends);

        add_friend.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(intent, 0);

                    }
                });

        ImageButton invite_friend = (ImageButton)findViewById(R.id.invite_friends);

        invite_friend.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(friendList.size() == 0){
                            return;
                        }
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setType("vnd.android-dir/mms-sms");
                        intent.putExtra("sms_body", StaticValues.album.album_invitemsg+" "+StaticValues.album.album_inviteurl);
                        //intent.setDataAndType(uri, "image/png");
                        //intent.addCategory("android.intent.category.DEFAULT");
                        //intent.putExtra(Intent.EXTRA_STREAM, uri);
                        //intent.setType("image/*");
                        // intent.setComponent(new ComponentName("com.sec.mms",
                        // "com.sec.mms.Mms"));
                        mobile = friendList.get(0).phone_number;
                        if(friendList.size() == 1){

                        }else {
                            for (int i = 1; i < friendList.size(); i++) {
                                mobile = mobile + ";" + friendList.get(i).phone_number;
                            }
                        }
                        intent.putExtra("address", mobile);
                        startActivity(intent);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            //editName.setText(cursor.getString(0));        //이름 얻어오기
            //editPhone.setText(cursor.getString(1));     //번호 얻어오기

            friendList.add(new VOFriend(cursor.getString(0),cursor.getString(1)));
            friendAdapter.notifyDataSetChanged();
            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    ////////////////////////////////////
    public class FriendListAdapter extends ArrayAdapter<VOFriend> {

        //private final ImageLoader imageLoader;
        private LayoutInflater inflater = null;
        private ViewHolder viewHolder = null;
        //private ArrayList<VOAnsim> ansims = null;
        private Context CONTEXT;

        public FriendListAdapter(Context context, ArrayList<VOFriend> arrayList) {
            super(context, 0, arrayList);
            this.inflater = LayoutInflater.from(context);
            //this.imageLoader = new ImageLoader(context, 800);
            //this.ansims = ansims;
            this.CONTEXT = context;

        }


        /*
         * ImageView imgView = (ImageView) findViewById(R.id.pageImage);
    imgView.getLayoutParams().height = 200;
        */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
		/*
			if (position == 0){
				return lastestView();
			}else{
				convertView = null;
			}
			*/
			/* 레이아웃 설정 */
            if(convertView == null){
                convertView = inflater.inflate(R.layout.listitem_friend, null);
                viewHolder = new ViewHolder();
                viewHolder.friend_name = (TextView)convertView.findViewById(R.id.friend_name);
                viewHolder.phone_number = (TextView)convertView.findViewById(R.id.phone_number);
                viewHolder.del = (ImageView)convertView.findViewById(R.id.image_del);
                //viewHolder.ivBanner.getLayoutParams().height = 100; //(int) (viewHolder.ivBanner.getLayoutParams().width)/2;

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            final VOFriend item = getItem(position);

            //int number = position+1;
            viewHolder.friend_name.setText(item.name);
            viewHolder.phone_number.setText(item.phone_number);


            viewHolder.del.setTag(new Integer(position));
            viewHolder.del.setOnClickListener(
                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Integer pos = (Integer) v.getTag();

                            int position = pos.intValue();

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

        class ViewHolder{
            public TextView friend_name = null;
            public TextView phone_number = null;
            public ImageView del=null;
        }

    }

}
