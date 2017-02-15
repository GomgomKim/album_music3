package com.bitlworks.intlib_music_base.common.source;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bitlworks.intlib_music_base.common.StaticValues;
import com.bitlworks.intlib_music_base.common.source.ready.ActivityStartLoading;
import com.bitlworks.intlib_music_base.common.source.ready.AuthCheckActivity;
import com.bitlworks.music._common.data.VOSong;
import com.bitlworks.music._common.data.VOdisk;
import com.bitlworks.music._common.setting.HomeView;
import com.bitlworks.music._common.setting.SettingView;
import com.bitlworks.wedding.resources.StudioValues;

public class PagerMainActivity extends ActionBarActivity implements
    SeekBar.OnSeekBarChangeListener,
    DiskAdapter.AlbumListListener,
    SongAdapter.SongListListener {


  public interface Listener {
    void updateCommentList();

    void onBackPressed();
  }

  private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
    }
  };

  public Listener listener;
  public static MusicService musicService;
  public Intent playIntent;
  public boolean musicBound = false;

  private ViewPager viewPager;
  private CustomPagerAdapter pagerAdapter;
  private TextView songNameText;
  private TextView lyrics, lyricSongNameText;
  public TextView singerText, songMakerText;
  private View songInfoView, lyricView, songListView;
  public ImageView playSongButton;
  private TextView songCurrentDurationLabel;
  private TextView songTotalDurationLabel;
  public SeekBar songProgressBar;

  public Typeface tf,  ff, ff2;
  public NotificationPanel nPanel;
  private Utilities utils;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
    PagerMainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    setContentView(R.layout.main_pager);

    if (StaticValues.songList == null) {
      finish();
      System.exit(0);
    }
    tf = Typeface.createFromAsset(getAssets(), "bbb.otf");
    ff = Typeface.createFromAsset(getAssets(), "aaf.ttf");
    ff2 = Typeface.createFromAsset(getAssets(), "aaf.ttf");
    StaticValues.pagerMainActivity = this;
    utils = new Utilities();
    nPanel = new NotificationPanel(this);
    bindService(playIntent,musicConnection, Context.BIND_AUTO_CREATE);
    startService(playIntent);


    viewPager = (CustomViewPager) findViewById(R.id.pager_main);
    pagerAdapter = new CustomPagerAdapter(PagerMainActivity.this);
    viewPager.setAdapter(pagerAdapter);

    songInfoView = findViewById(R.id.view_song_info);
    TextView song_title = (TextView) findViewById(R.id.song_title);
    song_title.setTypeface(ff);
    songNameText = (TextView) findViewById(R.id.text_song_name);
    songNameText.setTypeface(ff2);
    singerText = (TextView) findViewById(R.id.text_singer);
    singerText.setTypeface(ff);
    songMakerText = (TextView) findViewById(R.id.text_song_maker);
    songMakerText.setTypeface(ff);
    songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
    songProgressBar.setOnSeekBarChangeListener(this);
    songProgressBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
    songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
    songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
    findViewById(R.id.song_button_prev).setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        musicService.startMusic(StaticValues.playIndex - 1);
        updateMusicPlayerView(StaticValues.playIndex);
      }
    });
    findViewById(R.id.song_button_next).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        musicService.startMusic(StaticValues.playIndex + 1);
        updateMusicPlayerView(StaticValues.playIndex);
      }
    });
    playSongButton = (ImageView) findViewById(R.id.button_play_song);
    playSongButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (musicService.getMedia() == null) {
          return;
        }

        if (musicService.getMedia().isPlaying()) {
          musicService.stopMusic();
          nPanel.updatePause();
          togglePlayerButton(true);
        } else {
          musicService.startMusic(StaticValues.playIndex);
          nPanel.updatePlay();
          togglePlayerButton(false);
        }
      }
    });

    lyricView = findViewById(R.id.view_lyric);
    lyrics = (TextView) findViewById(R.id.song_lyric_text);
    lyrics.setTypeface(ff);
    lyricSongNameText = (TextView) findViewById(R.id.text_song_name_lyric);
    lyricSongNameText.setTypeface(ff, Typeface.BOLD);
    findViewById(R.id.view_song_lyric).setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (lyricView.getVisibility() == View.VISIBLE) {
          lyricView.setVisibility((View.GONE));
        } else {
          lyricView.setVisibility(View.VISIBLE);
          songListView.setVisibility(View.GONE);
        }
      }
    });

    songListView = findViewById(R.id.view_song_list);
    ListView songList = (ListView) findViewById(R.id.list_song);
    songListView.setVisibility(View.INVISIBLE);
    SongAdapter songAdapter = new SongAdapter(PagerMainActivity.this, StaticValues.songList);
    songAdapter.setListener(this);
    songList.setAdapter(songAdapter);
    ImageView songListImage = (ImageView) findViewById(R.id.image_song_list);
    songListImage.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (songListView.getVisibility() == View.VISIBLE) {
          songListView.setVisibility(View.GONE);
        } else {
          songListView.setVisibility(View.VISIBLE);
        }
      }
    });

    if (StaticValues.disk_name.equals("A")) {
      song_title.setText("DISK 1");
      songListImage.setImageResource(R.drawable.song_button_playlist_disk1);
    } else if (StaticValues.disk_name.equals("B")) {
      song_title.setText("DISK 2");
      songListImage.setImageResource(R.drawable.song_button_playlist_disk2);
    } else if (StaticValues.disk_name.equals("C")) {
      song_title.setText("DISK 3");
      songListImage.setImageResource(R.drawable.song_button_playlist_disk3);
    }

    initActionBar();
    int playIndex = (int) (Math.random() % StaticValues.songList.size() - 1);
    updateMusicPlayerView(playIndex);
    musicService.startMusic(playIndex);
  }

  @Override
  public void onClickDisk(VOdisk disk) {
    int id = disk.disk_id;
    if (id == StaticValues.disk_id) {
      Toast.makeText(this, "이미 선택된 디스크입니다.", Toast.LENGTH_SHORT).show();
      return;
    }

    if (musicService.getMedia() != null) {
      musicService.stopMusic();
    }
    mHandler.removeCallbacks(mUpdateTimeTask);

    stopService(playIntent);
    musicService = null;
    nPanel.notificationCancel();

    StaticValues.first_check = 0;
    StudioValues.MOBILE_MUSIC_ID = id;
    StaticValues.disk_id = id;
    StaticValues.album_id = StudioValues.album_id;
    StaticValues.disk_name = disk.disk_name;

    Intent i = new Intent(this, ActivityStartLoading.class);
    i.putExtra(ActivityStartLoading.PARAM_NEXT_ACTIVITY, PagerMainActivity.class);
    i.putExtra(ActivityStartLoading.PARAM_STUDIO_NO, StudioValues.STUDIO_ID);
    i.putExtra(ActivityStartLoading.PARAM_DEFAULT_ALBUM_NO, StudioValues.DEFAULT_ALBUM_ID);
    i.putExtra(ActivityStartLoading.PARAM_MUSIC_ID, StudioValues.MOBILE_MUSIC_ID);
    startActivity(i);
    finish();
  }

  @Override
  public void onClickSong(int position) {
    musicService.startMusic(position);
    updateMusicPlayerView(position);
  }

  @Override
  public void onBackPressed() {
    if (listener != null) {
      listener.onBackPressed();
    }

    if (songListView.getVisibility() == View.VISIBLE) {
      songListView.setVisibility(View.GONE);
      return;
    }
    if (lyrics.getVisibility() == View.VISIBLE) {
      lyrics.setVisibility((View.GONE));
      return;
    }

    new AlertDialog.Builder(this)
        .setMessage("종료하시겠습니까?")
        .setPositiveButton("예",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                stopService(playIntent);
                musicService = null;
                nPanel.notificationCancel();
                Intent intent = new Intent(PagerMainActivity.this, AuthCheckActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("exit", true);
                startActivity(intent);
              }
            })
        .setNegativeButton("아니오",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
              }
            }).show();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      Log.e("ab1233", "onConfigurationChanged");
    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

    }
    super.onConfigurationChanged(newConfig);
  }


  @Override
  public boolean onOptionsItemSelected(android.view.MenuItem item) {
    int i = item.getItemId();
    if (i == R.id.menu_home) {
      songInfoView.setVisibility(View.GONE);
      viewPager.setCurrentItem(0);
    } else if (i == R.id.menu_song_list) {
      songInfoView.setVisibility(View.GONE);
      viewPager.setCurrentItem(1);
    } else if (i == R.id.menu_guest_book) {
      songInfoView.setVisibility(View.GONE);
      viewPager.setCurrentItem(getPageCount() - 2);
    } else if (i == R.id.menu_setting) {
      songInfoView.setVisibility(View.GONE);
      viewPager.setCurrentItem(getPageCount() - 1);
    } else if (i == R.id.menu_sound) {
      item.setIcon(R.drawable.actionbar_button_sound_on);
      togglePlayerButton(true);
      musicService.startMusic(StaticValues.playIndex);
      viewPager.setCurrentItem(StaticValues.playIndex + 3);
      try {
        Thread.sleep(500);
      } catch (Exception e) {
        e.getLocalizedMessage();
      }
    } else {
      throw new RuntimeException("Unknown menu type");
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.action_bar, menu);
    MenuItem item = menu.findItem(R.id.menu_sound);
    if (!musicService.getMedia().isPlaying()) {
      item.setIcon(R.drawable.actionbar_button_sound_off);
    }
    return true;
  }

  @Override
  protected void onPause() {
    super.onPause();
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
  }

  /**
   *
   * */
  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

  }

  /**
   * When user starts moving the progress handler
   */
  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {
    // remove message Handler from updating progress bar
    mHandler.removeCallbacks(mUpdateTimeTask);
  }

  /**
   * When user stops moving the progress hanlder
   */
  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {
    mHandler.removeCallbacks(mUpdateTimeTask);
    int totalDuration = musicService.getMedia().getDuration();
    int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
    musicService.getMedia().seekTo(currentPosition);
    updateProgressBar();
  }

  public void initActionBar() {
    ActionBar actionBar = getSupportActionBar();
    actionBar.setTitle("");
    actionBar.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.home_bar_background));
    actionBar.setLogo(null);
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeButtonEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);
    actionBar.hide();
  }

  private void togglePlayerButton(boolean flag) {
    playSongButton.setImageResource(flag ? R.drawable.song_play_button : R.drawable.song_pause_button);
  }

  private void updateMusicPlayerView(int songIndex) {
    lyricView.setVisibility(View.GONE);
    songListView.setVisibility(View.GONE);

    viewPager.setCurrentItem(songIndexToPagerIndex(songIndex));
    VOSong song = StaticValues.songList.get(songIndex);
    lyrics.setText(song.song_lyric);
    lyricSongNameText.setText(song.song_name);
    singerText.setText(song.msg1);
    songMakerText.setText(song.msg2);
    togglePlayerButton(false);

    songProgressBar.setProgress(0);
    songProgressBar.setMax(100);
    updateProgressBar();
    nPanel.updateName(song.song_name);
  }

  private int songIndexToPagerIndex(int index) {
    return StudioValues.isSingle ? index + 2 : index + 3;
  }

  private int pagerIndexToSongIndex(int index) {
    return StudioValues.isSingle ? index - 2 : index - 3;
  }

  private int getPageCount() {
    return StudioValues.isSingle
        ? StaticValues.songList.size() + 4 : StaticValues.songList.size() + 5;
  }

  public void updateProgressBar() {
    mHandler.postDelayed(mUpdateTimeTask, 100);
  }

  private void setActionBar(View view) {
    view.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar.isShowing()) {
          actionBar.hide();
        } else {
          actionBar.show();
          new Handler().postDelayed(new Runnable() {
            public void run() {
              ActionBar actionBar = getSupportActionBar();
              if (actionBar.isShowing()) {
                actionBar.hide();
              }
            }
          }, 4000);
        }
      }
    });
  }

  public class Utilities {

    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     */
    public String milliSecondsToTimer(long milliseconds) {
      String finalTimerString = "";
      String secondsString = "";

      // Convert total duration into time
      int hours = (int) (milliseconds / (1000 * 60 * 60));
      int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
      int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
      // Add hours if there
      if (hours > 0) {
        finalTimerString = hours + ":";
      }

      // Prepending 0 to seconds if it is one digit
      if (seconds < 10) {
        secondsString = "0" + seconds;
      } else {
        secondsString = "" + seconds;
      }

      finalTimerString = finalTimerString + minutes + ":" + secondsString;

      // return timer string
      return finalTimerString;
    }

    /**
     * Function to get Progress percentage
     *
     * @param currentDuration
     * @param totalDuration
     */
    public int getProgressPercentage(long currentDuration, long totalDuration) {
      Double percentage = (double) 0;

      long currentSeconds = (int) (currentDuration / 1000);
      long totalSeconds = (int) (totalDuration / 1000);

      // calculating percentage
      percentage = (((double) currentSeconds) / totalSeconds) * 100;

      // return percentage
      return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     *
     * @param progress      -
     * @param totalDuration returns current duration in milliseconds
     */
    public int progressToTimer(int progress, int totalDuration) {
      int currentDuration = 0;
      totalDuration = (int) (totalDuration / 1000);
      currentDuration = (int) ((((double) progress) / 100) * totalDuration);

      // return current duration in milliseconds
      return currentDuration * 1000;
    }
  }


  private Runnable mUpdateTimeTask = new Runnable() {
    public void run() {

      if (StaticValues.pagerMainActivity == null) return;
      if (musicService == null) return;

      long totalDuration =musicService.getMedia().getDuration();
      long currentDuration = musicService.getMedia().getCurrentPosition();

      if (songTotalDurationLabel != null) {
        // Displaying Total Duration time
        songTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
        // Displaying time completed playing
        songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));
      }
      // Updating progress bar
      int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
      //Log.d("Progress", ""+progress);
      songProgressBar.setProgress(progress);

      // Running this thread after 100 milliseconds
      mHandler.postDelayed(this, 100);
    }
  };


  public ServiceConnection musicConnection = new ServiceConnection() {

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
      musicService = binder.getService();
      musicBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      musicBound = false;
    }
  };



  public class CustomPagerAdapter extends PagerAdapter {
    private LayoutInflater mInflater;

    public CustomPagerAdapter(Context context) {
      mInflater = LayoutInflater.from(context);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      final View v;
      songInfoView.setVisibility(View.GONE);
      if (position == 0) {
        // 표지
        v = new HomeView(PagerMainActivity.this);
      } else if (position == 1) {
        // 디스크 목록
        v = new DiskListView(PagerMainActivity.this, PagerMainActivity.this);
      } else if (position == 2) {
        // 노래 목록
        v = new SongListView(PagerMainActivity.this, PagerMainActivity.this, PagerMainActivity.this);
      } else if (position == getPageCount() - 2) {
        // 코멘트
        v = new CommentView(PagerMainActivity.this);
        listener = (CommentView) v;
      } else if (position == getPageCount() - 1) {
        // 설정
        v = new SettingView(PagerMainActivity.this);
      } else {
        songInfoView.setVisibility(View.VISIBLE);
        v = mInflater.inflate(R.layout.layout_song_background, null);
        updateMusicPlayerView(pagerIndexToSongIndex(position));
        musicService.startMusic(pagerIndexToSongIndex(position));
      }
      return v;
    }

    @Override
    public void destroyItem(ViewGroup container, final int position, Object object) {
      container.removeView((View) object);
    }

    @Override
    public int getCount() {
      return getPageCount();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
      return arg0 == arg1;
    }
  }

  public void end() {
    mHandler.removeCallbacks(mUpdateTimeTask);
    stopService(playIntent);
    musicService = null;
    nPanel.notificationCancel();

    Intent intent = new Intent(PagerMainActivity.this, AuthCheckActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.putExtra("exit", true);
    startActivity(intent);
    System.exit(0);
  }
}