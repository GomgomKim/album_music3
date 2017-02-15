package com.bitlworks.intlib_music_base.common.source;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitlworks.intlib_music_base.common.StaticValues;
import com.bitlworks.music._common.data.DataNetUtils;
import com.bitlworks.music._common.data.VONewInfo;
import com.bitlworks.music._common.network.DataDownloader;
import com.bitlworks.music._common.network.Network;
import com.bitlworks.wedding.resources.StudioValues;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import eu.janmuller.android.simplecropimage.CropImage;

/**
 * Created by 강혁 on 2016-09-20.
 */
public class AddNewInfo extends Activity {

    private static final int PICK_FROM_CAMERA = 121;
    private static final int PICK_FROM_ALBUM = 122;

    private static final String UPLOAD_FILE_NAME_BASE = "upload";
    private static final String DEFAULT_IMAGEFILE_EXTENTION = ".png";
    private File mFileTemp;

    int photo_check=0;
    private final String[] dialogItems = { "사진 촬영", "사진 앨범" };
    EditText name, time, pic, contents, link;

    int menu_unit;
    ImageView pic_button, ok, upload_image;
    public String makeFileName(int uploadImageIdx) {

        return AddNewInfo.this.getExternalFilesDir(null) + "/"
                + UPLOAD_FILE_NAME_BASE + uploadImageIdx
                + DEFAULT_IMAGEFILE_EXTENTION;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFileTemp = new File(makeFileName(1));

        setContentView(R.layout.add_new_info_layout);
///////////////////////////////////////////////
        int w = getResources().getDisplayMetrics().widthPixels ;
        menu_unit = w/5;
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
        ///////////////////////////////
        name =(EditText) findViewById(R.id.edit1);
        time =(EditText) findViewById(R.id.edit2);
        //pic =(EditText) findViewById(R.id.edit3);
        contents =(EditText) findViewById(R.id.edit4);
        link = (EditText) findViewById(R.id.edit5);
/////////////////////////////////////////////////////
/*
        contents.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //do stuff
                String mStr = contents.getText().toString();
                if(mStr.charAt(0) != ' '){
                    contents.setText(' ' + mStr);
                }
            }
        });*/
        ///////////////////////////////////////
        pic_button = (ImageView) findViewById(R.id.pic);
        upload_image = (ImageView) findViewById(R.id.image);
        ok = (ImageView)findViewById(R.id.add_new_button_ok);

        ok.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String name2 = name.getText().toString();
                        String content = contents.getText().toString();
                        String t = time.getText().toString();
                        //String p = pic.getText().toString();
                        String url = link.getText().toString();

                        if(photo_check == 0){
                            Toast.makeText(AddNewInfo.this, "사진을 추가해주세요",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        /*
                        if (scrollView.getVisibility() != View.VISIBLE) {
                            Toast.makeText(WriteFriendsShotActivity.this, "사진을 추가해주세요",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }*/
                        if (name2.length() < 1 || content.length() < 1 || t.length() < 1) {
                            Toast.makeText(AddNewInfo.this, "입력란을 모두 채워주세요",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        new UploadFriendsShotTask(AddNewInfo.this,name2,t, content,url).execute();
                    }
                });


        pic_button.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(AddNewInfo.this)
                                .setTitle("사진 추가")
                                .setItems(dialogItems,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                switch (which) {
                                                    case 0:
                                                        takePicture();
                                                        break;
                                                    case 1:
                                                        openGallery();
                                                        break;

                                                    default:
                                                        break;
                                                }
                                            }
                                        }).show();
                    }
                });

    }

    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            mImageCaptureUri = Uri.fromFile(mFileTemp);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                    mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {

        }
    }

    private void openGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_FROM_ALBUM);
    }

    public static final int REQUEST_CODE_CROP_IMAGE = 123;

    private void runCropImage(String filePath) {
        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, filePath);
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);
        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("ab1233", "onActivityResult " + requestCode + " " + resultCode);
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case PICK_FROM_ALBUM:
                if (mFileTemp.exists()) {
                    mFileTemp.delete();
                }
                try {
                    mFileTemp.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    InputStream inputStream = AddNewInfo.this
                            .getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(
                            mFileTemp);
                    AlbumUtils.copyStream(inputStream, fileOutputStream);

                    fileOutputStream.close();
                    inputStream.close();
                    // startCropImage();

                    runCropImage(mFileTemp.getPath());
                    //pic.setText("사진이 첨부되었습니다.");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                          break;
            case REQUEST_CODE_CROP_IMAGE:
                //Log.d("hyuk", "hyuk test helllo>> " + check);

                //refreshIcon();
                //if(resultCode == Activity.RESULT_OK) {
                //if(check == 1) {
                String mTempPath = data.getStringExtra(CropImage.IMAGE_PATH);
                Log.d("hyuk", "hyuk test helllo");

                //if(mTempPath == null) return;

                mFileTemp = new File(mTempPath);

                Bitmap bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                //bitmap = Util.rotateImage(bitmap, exifDegree);
                upload_image.setImageBitmap(bitmap);
                //if (path == null) return;
                //pic.setText("사진이 첨부되었습니다.");
                photo_check = 100;//사진이 첨부가 됬다는 신호,,
                pic_button.setVisibility(View.INVISIBLE);
                //b.setImageBitmap(decodeFile(mFileTemp.getPath()));
                //new NETuploadImage(StaticObject.clinic_id, StaticObject.myInfo.user_id, mFileTemp, ShowMsg.this, afteruploadImage);
                //check=100;
                //}else{
                //	check++;
                //}
                //}
                break;
            case PICK_FROM_CAMERA:
                runCropImage(mFileTemp.getPath());
                break;

                // startCropImage();
                // break;
                // case CROP_FROM_CAMERA:
                // String path = data.getStringExtra(CropImage.IMAGE_PATH);
                //
                // if (path == null) {
                //
                // return;
                // }
                // path = mFileTemp.getPath();
                /*
                ExifInterface exif = null;

                try {
                    exif = new ExifInterface(mFileTemp.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int exifOrientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = Util.exifOrientationToDegrees(exifOrientation);
                Log.e("before size=", mFileTemp.length() / 1000l + "KB");
                Bitmap bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                bitmap = Util.rotateImage(bitmap, exifDegree);

                pic.setText("사진이 첨부되었습니다.");

                if (mFileTemp.length() > 1000000) {
                    try {
                        AlbumUtils.saveBitmapJPEG(AddNewInfo.this,
                                bitmap, mFileTemp.getParent(), mFileTemp.getName(),
                                50);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Log.e("after size=", mFileTemp.length() / 1000l + "KB");
                // VOPhoto photo = new VOPhoto();
                // photo.setPhoto_id(UPLOAD_PHOTO_ID);
                // photo.setPhoto_inpath(mFileTemp.getPath());
                // mAdapter.addPhoto(photo);
                Log.e("ab1233", mFileTemp.getParent() + " " + mFileTemp.getName());
                // mFileTemp = new File(makeFileName(++mUploadImageCount));
                */
        }
    }

    class UploadFriendsShotTask extends AsyncTask<Void, Void, Integer> {
        private Context mContext;
        //private ProgressDialog mProgressDlg;
        private int coupleId, dThemeId, userId;
        private String subject, content2, time2, link_url;
        private String url;
        private String[] paramName, paramValue;

        JSONArray result;
        int music_id;

        public UploadFriendsShotTask(Context context, String nick2, String t, String c2, String l2) {
            mContext = context;
            //mProgressDlg = new ProgressDialog(context);
            //mProgressDlg.setCancelable(false);
            //this.coupleId = DataNetUtils.getSelectedCoupleId(context);
            //this.dThemeId = dThemeId;
            this.userId = DataNetUtils.getMyId(context);
            this.subject = nick2;
            this.time2 = t;
            this.content2 =c2;
            this.link_url = l2;
            this.music_id = StudioValues.MOBILE_MUSIC_ID;
            //this.userNick = nick;
            //this.content = content;
            this.url = StaticValues.SERVICE_URL  + "insertNewInfo.php";
            this.paramName = new String[] { "music_id", "main_subject", "time", "newinfo_contents","link_url"};
            this.paramValue = new String[] { music_id + "", subject, time2, content2,this.link_url };
        }

        @Override
        protected void onPreExecute() {
            //showProgressDlg();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            Network.HttpPostParameter param = new Network.HttpPostParameter(paramName,
                    paramValue, new File[] { mFileTemp });

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
                Toast.makeText(mContext, "업로드 중 에러가 발생했습니다", Toast.LENGTH_SHORT)
                        .show();
            } else {

                //JSONArray result = new JSONArray(result2);
                // insert DB comment table
                try{
                ArrayList<VONewInfo> photoList = new ArrayList<VONewInfo>();
                VONewInfo voPhoto = null;
                for(int i=0; i < this.result.length(); i++ ){
                    JSONObject obj = (JSONObject)this.result.get(i);
                    voPhoto = new VONewInfo(
                            obj.getInt("info_id"),
                            obj.getInt("mobile_music_id"),
                            obj.getString("main_subject"),
                            obj.getString("time"),
                            obj.getString("image_data"),
                            obj.getString("contents"),
                            obj.getString("link_url")

                    );
                    Log.d("hyuk2","id>>"+voPhoto.info_id);
                    photoList.add(voPhoto);
                }

                    StaticValues.newinfoList.clear();
                    StaticValues.newinfoList.addAll(photoList);
                //StaticValues.newinfoList = photoList;
                    new DataDownloader(getApplicationContext(), netHandlerPhotoDonwAfter);




                }catch (JSONException e) {
                    Log.e(StaticValues.LOG_TAG, "error" + e.toString());
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

    /** 사진 데이터 다운로드 완료 후 **/
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
//                    Static.new_info_act.update();
//                    finish();
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

}
