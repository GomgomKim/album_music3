package com.bitlworks.intlib_music_base.common.source;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bitlworks.intlib_music_base.common.StaticValues;
import com.bitlworks.music.R;
import com.bitlworks.music._common.data.VOComment2;
import com.bitlworks.music._common.network.NetInsertComment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class CommentView extends LinearLayout implements
    CommentAdapter.CommentAdapterListener,
    PagerMainActivity.Listener {

  private View writeCommentView;
  private CommentAdapter commentAdapter;

  public CommentView(Context context) {
    super(context);
    initView();
  }

  private void initView() {
    String infService = Context.LAYOUT_INFLATER_SERVICE;
    LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
    View v = li.inflate(R.layout.layout_comment, this, false);
    addView(v);

    String in_file = "";
    for (int i = 0; i < StaticValues.photoList.size(); i++) {
      if (StaticValues.photoList.get(i).type == 3 && StaticValues.photoList.get(i).photo_order == 4) {
        in_file = StaticValues.photoList.get(i).photo_file_name;
        break;
      }
    }
    String rootPath = "/mnt/sdcard/";
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    }
    rootPath += "bitlworks/";
    rootPath += "mobilemusic/";
    final String LocalPath = rootPath + in_file;// StaticValues.photoList.get(0).photo_file_name;
    File file = new File(LocalPath);
    Drawable d = Drawable.createFromPath(file.getAbsolutePath());
    v.setBackground(d);

    findViewById(R.id.ll_root).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (writeCommentView.getVisibility() == VISIBLE) {
          writeCommentView.setVisibility(GONE);
        }
      }
    });

    writeCommentView = v.findViewById(R.id.view_write_comment);
    v.findViewById(R.id.button_write).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        writeCommentView.setVisibility(View.VISIBLE);
      }
    });
    v.findViewById(R.id.button_edit).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
      }
    });

    final ListView commentListView = (ListView) findViewById(R.id.lv_comments);
    commentAdapter = new CommentAdapter(getContext(), StaticValues.commentList);
    commentAdapter.setListener(this);
    commentListView.setAdapter(commentAdapter);

    final EditText nameEditText = (EditText) v.findViewById(R.id.edittext_name);
//    nameEditText.setTypeface(ff2);
    nameEditText.setText(AlbumUtils.getDefaultAuthorName(getContext()));
    final EditText commentEditText = (EditText) v.findViewById(R.id.edittext_comment);
//    commentEditText.setTypeface(ff2);
    v.findViewById(R.id.button_send).setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        String name = nameEditText.getText().toString();
        String comment = commentEditText.getText().toString();
        if (name.length() < 1) {
          Toast.makeText(getContext(), "작성자 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
          return;
        }
        if (comment.length() < 5) {
          Toast.makeText(getContext(), "보낼 메세지가 너무 짧아요.", Toast.LENGTH_SHORT).show();
          return;
        }
        new NetInsertComment(
            StaticValues.album_id, StaticValues.myInfo.user_id, comment, name, netHandlerInsertCommentList);

        commentEditText.getText().clear();
        writeCommentView.setVisibility(View.GONE);

        AlbumUtils.saveDefaultAuthorName(getContext(), comment);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
      }
    });
  }

  Handler netHandlerInsertCommentList = new Handler() {
    private final String LOG_TAG_NAVI = "<netHandlerGetSampleId.Handler>";

    public void handleMessage(Message msg) {

      final String resultString = (String) msg.obj;
      try {

        JSONArray result = new JSONArray(resultString);
        StaticValues.commentList.clear();
        for (int i = 0; i < result.length(); i++) {
          JSONObject obj = (JSONObject) result.get(i);
          VOComment2 voSong = new VOComment2(
              obj.getInt("comment_id"),
              obj.getInt("user_id"),
              obj.getString("user_name"),
              obj.getString("comment_time"),
              obj.getString("comment_contents"),
              obj.getInt("album_id")

          );
          StaticValues.commentList.add(voSong);
        }
        commentAdapter.notifyDataSetChanged();


      } catch (JSONException e) {
        Log.e(StaticValues.LOG_TAG, LOG_TAG_NAVI + e.toString());
      }
    }
  };

  Handler netHandlerDelCommentList = new Handler() {
    private final String LOG_TAG_NAVI = "<netHandlerGetSampleId.Handler>";

    public void handleMessage(Message msg) {

      final String resultString = (String) msg.obj;
      try {
        JSONArray result = new JSONArray(resultString);
        StaticValues.commentList.clear();
        for (int i = 0; i < result.length(); i++) {
          JSONObject obj = (JSONObject) result.get(i);
          VOComment2 voSong = new VOComment2(
              obj.getInt("comment_id"),
              obj.getInt("user_id"),
              obj.getString("user_name"),
              obj.getString("comment_time"),
              obj.getString("comment_contents"),
              obj.getInt("album_id")
          );
          StaticValues.commentList.add(voSong);
        }
        commentAdapter.notifyDataSetChanged();
      } catch (JSONException e) {
        Log.e(StaticValues.LOG_TAG, LOG_TAG_NAVI + e.toString());
        StaticValues.commentList.clear();
        commentAdapter.notifyDataSetChanged();
      }
    }
  };

  Handler netHandlerGetCommentList = new Handler() {
    private final String LOG_TAG_NAVI = "<netHandlerGetSampleId.Handler>";

    public void handleMessage(Message msg) {

      final String resultString = (String) msg.obj;
      try {
        JSONArray result = new JSONArray(resultString);
        StaticValues.commentList.clear();
        for (int i = 0; i < result.length(); i++) {
          JSONObject obj = (JSONObject) result.get(i);
          VOComment2 voSong = new VOComment2(
              obj.getInt("comment_id"),
              obj.getInt("user_id"),
              obj.getString("user_name"),
              obj.getString("comment_time"),
              obj.getString("comment_contents"),
              obj.getInt("mobile_music_id")

          );
          StaticValues.commentList.add(voSong);
        }
        commentAdapter.notifyDataSetChanged();
      } catch (JSONException e) {
        Log.e(StaticValues.LOG_TAG, LOG_TAG_NAVI + e.toString());
      }
    }
  };

  @Override
  public void onClickComment(int commentId) {
    new NETdelComment(StaticValues.album_id, commentId, netHandlerDelCommentList);
  }

  @Override
  public void updateCommentList() {
    new NetGetCommentList(StaticValues.album_id, netHandlerGetCommentList);
  }

  @Override
  public void onBackPressed() {
    if (writeCommentView.getVisibility() == VISIBLE) {
      writeCommentView.setVisibility(GONE);
    }
  }
}
