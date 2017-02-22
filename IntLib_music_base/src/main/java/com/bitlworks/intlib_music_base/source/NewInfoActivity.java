package com.bitlworks.intlib_music_base.source;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitlworks.WeddingAlbum.DataNet.DataNetUtils;
import com.bitlworks.WeddingAlbum.DataNet.Network;
import com.bitlworks.WeddingAlbum.DataNet.StaticValues;
import com.bitlworks.WeddingAlbum.DataNet.VONewInfo;
import com.bitlworks.music.R;
import com.bitlworks.music._common.AddNewInfo;
import com.bitlworks.music.common.Static;
import com.bitlworks.music.common.WebViewNewInfo;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NewInfoActivity extends Activity {

  ListView info_list;
  NewInfoListAdapter infoAdapter;

  int menu_unit;


  private ArrayList<>

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.new_info_layout);






    findViewById(R.id.imageView1).setOnTouchListener(new View.OnTouchListener() {

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
    ///////////////////////////////////
    ImageView w_button = (ImageView) findViewById(R.id.write_button);

    w_button.setOnClickListener(
        new View.OnClickListener() {

          @Override
          public void onClick(View v) {
//            Intent i = new Intent(NewInfoActivity.this, AddNewInfo.class);
//            startActivity(i);
          }
        });

    if (StaticValues.myInfo.user_level == 9) {
      w_button.setVisibility(View.VISIBLE);
    } else {
      w_button.setVisibility(View.INVISIBLE);
    }

    info_list = (ListView) findViewById(R.id.new_info_list);

    infoAdapter = new NewInfoListAdapter(this, StaticValues.newinfoList);//, lastAnsims);
    info_list.setAdapter(infoAdapter);

  }

  public void update() {
    infoAdapter.notifyDataSetChanged();
    Log.d("hyuk23333333333", "update called");
  }

  ////////////////////////////////////
  public class NewInfoListAdapter extends ArrayAdapter<VONewInfo> {

    //private final ImageLoader imageLoader;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;
    //private ArrayList<VOAnsim> ansims = null;
    private Context CONTEXT;

    public NewInfoListAdapter(Context context, ArrayList<VONewInfo> arrayList) {
      super(context, 0, arrayList);
      this.inflater = LayoutInflater.from(context);
      //this.imageLoader = new ImageLoader(context, 800);
      //this.ansims = ansims;
      this.CONTEXT = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

			/* 레이아웃 설정 */
      if (convertView == null) {
        convertView = inflater.inflate(R.layout.listitem_newinfo, null);
        viewHolder = new ViewHolder();
        viewHolder.contents = (TextView) convertView.findViewById(R.id.contents);
        viewHolder.main_subject = (TextView) convertView.findViewById(R.id.main_subject);
        viewHolder.time = (TextView) convertView.findViewById(R.id.time);
        viewHolder.image_data = (ImageView) convertView.findViewById(R.id.imageView1);
        viewHolder.image_new = (ImageView) convertView.findViewById(R.id.imageNEW);
        viewHolder.del = (ImageView) convertView.findViewById(R.id.del);

        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }
      final VONewInfo item = getItem(position);

      //int number = position+1;
      // viewHolder.song_name.setText(""+number+". "+item.song_name);

      if (position == 0) {
        viewHolder.image_new.setVisibility(View.VISIBLE);
      } else {
        viewHolder.image_new.setVisibility(View.GONE);
      }

      viewHolder.contents.setText(item.contents);
      viewHolder.main_subject.setText(item.main_subject);
      viewHolder.time.setText(item.time);

      //////////////////////////////

      if (StaticValues.myInfo.user_level == 9) {
        viewHolder.del.setVisibility(View.VISIBLE);
      } else {
        viewHolder.del.setVisibility(View.INVISIBLE);
      }
      viewHolder.del.setOnClickListener(
          new View.OnClickListener() {

            @Override
            public void onClick(View v) {

              new AlertDialog.Builder(NewInfoActivity.this)
                  .setIcon(android.R.drawable.ic_dialog_alert)
                  .setTitle("새소식 삭제")
                  .setMessage("선택한 소식 정보를 정말 삭제하시겠습니까?")
                  .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      //finish();
                      new UploadFriendsShotTask(NewInfoActivity.this, item.info_id, item.album_id).execute();
                    }

                  })
                  .setNegativeButton("No", null)
                  .show();


            }
          });
      ///////////////////////////
      /////////////////////////////


      String in_file = "";

      in_file = item.image_data;
      String rootPath = "/mnt/sdcard/";
      // 내부 저장소 경로 구하기
      if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
      }

      // 폴더 존재 확인 및 생성
      rootPath += "bitlworks/";
      //checkFolder(rootPath);
      rootPath += "mobilemusic/";

      final String LocalPath = rootPath + in_file;// StaticValues.photoList.get(0).photo_file_name;

      File file = new File(LocalPath);
      Uri uri = Uri.fromFile(file);
      viewHolder.image_data.setImageURI(uri);

      viewHolder.image_data.setOnClickListener(
          new View.OnClickListener() {

            @Override
            public void onClick(View v) {


              if (item.link_url.length() > 3) {
                Static.link_url = item.link_url;

                Intent i = new Intent(NewInfoActivity.this, WebViewNewInfo.class);

                startActivity(i);
              }


            }
          });

      return convertView;
    }

    @Override
    public int getCount() {
      return super.getCount();
    }

    @Override
    public VONewInfo getItem(int position) {
      return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
      return super.getItemId(position);
    }

    class ViewHolder {
      public TextView main_subject = null;
      public TextView time = null;
      public ImageView image_data = null;
      public TextView contents = null;
      public ImageView image_new = null;

      public ImageView del = null;
    }

  }

  class UploadFriendsShotTask extends AsyncTask<Void, Void, Integer> {
    private Context mContext;
    //private ProgressDialog mProgressDlg;
    private int coupleId, dThemeId, userId;
    private String subject, content2, time2;
    private String url;
    private String[] paramName, paramValue;

    JSONArray result;
    int music_id, info_id;

    public UploadFriendsShotTask(Context context, int info_id, int music_id) {
      mContext = context;
      //mProgressDlg = new ProgressDialog(context);
      //mProgressDlg.setCancelable(false);
      //this.coupleId = DataNetUtils.getSelectedCoupleId(context);
      //this.dThemeId = dThemeId;
      this.userId = DataNetUtils.getMyId(context);
      this.info_id = info_id;
      this.music_id = music_id;//StudioValues.MOBILE_MUSIC_ID;
      //this.userNick = nick;
      //this.content = content;
      this.url = StaticValues.SERVICE_URL + "deleteNewInfo.php";
      this.paramName = new String[]{"album_id", "info_id"};
      this.paramValue = new String[]{this.music_id + "", this.info_id + ""};
    }

    @Override
    protected void onPreExecute() {
      //showProgressDlg();
    }

    @Override
    protected Integer doInBackground(Void... params) {
      Network.HttpPostParameter param = new Network.HttpPostParameter(paramName,
          paramValue);

      try {
        String str = Network.getInstance().getStringFromUrlPost(
            mContext, url, param);
        Log.e("ab1233", "result=" + str);
        //result2 = new JSONObject(str);
        result = new JSONArray(str);
        return 1;
      } catch (ClientProtocolException e) {
        e.printStackTrace();
      } catch (IllegalStateException e) {
        e.printStackTrace();
      } catch (JSONException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return 0;
    }

    @Override
    protected void onPostExecute(final Integer result) {
      if (result == 0) {
        //dismissProgressDlg();
                /*
                Toast.makeText(mContext, "업로드 중 에러가 발생했습니다", Toast.LENGTH_SHORT)
                        .show();
*/
        StaticValues.newinfoList.clear();
        //StaticValues.newinfoList.addAll(photoList);
        Static.new_info_act.update();
      } else {

        //JSONArray result = new JSONArray(result2);
        // insert DB comment table
        try {
          ArrayList<VONewInfo> photoList = new ArrayList<VONewInfo>();
          VONewInfo voPhoto = null;
          for (int i = 0; i < this.result.length(); i++) {
            JSONObject obj = (JSONObject) this.result.get(i);
            voPhoto = new VONewInfo(
                obj.getInt("info_id"),
                obj.getInt("album_id"),
                obj.getString("main_subject"),
                obj.getString("time"),
                obj.getString("image_data"),
                obj.getString("contents"),
                obj.getString("link_url")

            );
            Log.d("hyuk2", "id>>" + voPhoto.info_id);
            photoList.add(voPhoto);
          }

          StaticValues.newinfoList.clear();
          StaticValues.newinfoList.addAll(photoList);
          Static.new_info_act.update();
          //StaticValues.newinfoList = photoList;
          //new DataDownloader(getApplicationContext(), netHandlerPhotoDonwAfter);


        } catch (JSONException e) {
          Log.e(StaticValues.LOG_TAG, "error" + e.toString());

          StaticValues.newinfoList.clear();
          //StaticValues.newinfoList.addAll(photoList);
          Static.new_info_act.update();
        }
                /*
                new PhotoUpdater(mContext, StudioValues.STUDIO_ID, new Handler(
                        Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == PhotoUpdater.FLAG_UPDATE_END) {
                            AlbumUtils
                                    .saveDefaultAuthorName(mContext, userNick);
                            //dismissProgressDlg();
                            Toast.makeText(mContext, "글이 등록되었습니다",
                                    Toast.LENGTH_SHORT).show();
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    }
                });*/
      }

    }
/*
        public void showProgressDlg() {
            if (mProgressDlg != null && !mProgressDlg.isShowing()) {
                mProgressDlg.show();
            }
        }

        public void dismissProgressDlg() {
            if (mProgressDlg != null && mProgressDlg.isShowing()) {
                mProgressDlg.dismiss();
            }
        }*/
  }

  /**
   * 사진 데이터 다운로드 완료 후
   **/
  Handler netHandlerPhotoDonwAfter = new Handler() {
    private final String LOG_TAG_NAVI = "<netHandlerPhotoDonwAfter.Handler>";

    int downloadedCount = 0;
    int totalCount = 1;

    public void handleMessage(Message msg) {
      Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " handleMessage()");

      if (msg.what == 0) { // 사진 파일 카운트 초기화
        // totalCount = ( msg.arg1 < totalCount)?msg.arg1 : totalCount;
        totalCount = msg.arg1;
        //progressBar.setMax(totalCount);
        if (totalCount == 0) {
          Toast.makeText(getApplicationContext(),
              "앨범 로딩에 문제가 있습니다. 제작사에 문의해 주세요", Toast.LENGTH_SHORT)
              .show();
          finish();
        }
        // addLog("#다운로드 할 파일의 갯수는 " + totalCount + "개 입니다.");
      } else if (msg.what == 1) { // 사진 파일 다운로드 시작시
        // addLog("download start ("+downloadingCount+"/"+totalCount+")");
      } else if (msg.what == 2) { // 사진 파일 다운로드 완료시
        downloadedCount++;
        // addLog("Downloaded (" + downloadedCount + "/" + totalCount
        // +")");
        //updateLog("" + (int) (100 * downloadedCount / totalCount));
        if (downloadedCount == totalCount) {
          // 파일 다운로드가 완료 되었을 시
          //nextActivity();
          Static.new_info_act.update();
          finish();
        }
      } else if (msg.what == 3) { // httpHostConnectionException 날 경우
        // 취소 하기 살려주기
        Toast.makeText(getApplicationContext(), "네트워크를 다시 확인해주세요.",
            Toast.LENGTH_LONG).show();
        //isCancel = true;
        finish();
      }
    } // END handleMessage();
  }; // END netHandlerPhotoDonwAfter

  ///////////////////////

}
