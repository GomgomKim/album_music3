package com.bitlworks.intlib_music_base.source;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

import com.bitlworks.intlib_bitlworks.CommonUtils;
import com.bitlworks.intlib_music_base.MusicUtils;
import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.StaticValues;
import com.bitlworks.intlib_music_base.data.VOSong;
import com.bitlworks.intlib_music_base.data.VODisk;
import com.bitlworks.intlib_music_base.source.ready.LoadingActivity;
import com.bitlworks.music_resource.AlbumValue;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.File;

public class PagerMainActivity extends AppCompatActivity implements
    SeekBar.OnSeekBarChangeListener,
    ViewPager.OnPageChangeListener,
    DiskAdapter.AlbumListListener,
    SongAdapter.SongListListener {

  public Listener listener;
  private MusicService musicService;
  private MusicNotification musicNotification;
  private ImageView playSongButton;
  private SeekBar songProgressBar;

  private ViewPager viewPager;
  private TextView songNameText, singerText, songMakerText;
  private TextView lyrics, lyricSongNameText;
  private View songInfoView, lyricsView, songListView;
  private TextView songCurrentDurationText;
  private TextView songTotalDurationText;
  private Handler handler = new Handler();
  private Runnable updateProgressRunnable = new Runnable() {
    public void run() {

      if (StaticValues.pagerMainActivity == null || musicService == null) return;

      long totalDuration = musicService.getDuration();
      long currentDuration = musicService.getCurrentPosition();

      if (songTotalDurationText != null) {
        songTotalDurationText.setText(String.valueOf(CommonUtils.milliSecondsToTimer(totalDuration)));
        songCurrentDurationText.setText(String.valueOf(CommonUtils.milliSecondsToTimer(currentDuration)));
      }
      int progress = CommonUtils.getProgressPercentage(currentDuration, totalDuration);
      songProgressBar.setProgress(progress);

      if (progress == 100) {
        viewPager.setCurrentItem(songIndexToPagerIndex(StaticValues.playIndex + 1));
        return;
      }
      handler.postDelayed(this, 100);
    }
  };
  private BroadcastReceiver musicReceiver = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent.getAction().equals("play")) {
        if (musicService.isPlaying()) {
          musicService.pauseMusic();
          musicNotification.updatePause();
          togglePlayerButton(true);
        } else {
          musicService.restartMusic();
          musicNotification.updatePlay();
          togglePlayerButton(false);
        }
        return;
      }

      if (intent.getAction().equals("prev")) {
        if (StaticValues.playIndex == 0) {
          return;
        }
        viewPager.setCurrentItem(songIndexToPagerIndex(StaticValues.playIndex - 1));
        return;
      }

      if (intent.getAction().equals("next")) {
        if (StaticValues.playIndex == StaticValues.songs.size() - 1) {
          return;
        }
        viewPager.setCurrentItem(songIndexToPagerIndex(StaticValues.playIndex + 1));
      }

      if (intent.getAction().equals("end")) {
        finish();
      }
    }
  };

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    setContentView(R.layout.activity_mainpager);

    if (StaticValues.songs.size() == 0) {
      finish();
    }

    musicService = new MusicService(this);
    musicService.startMusic(0);
    musicNotification = new MusicNotification(this);
    musicNotification.updateName(StaticValues.songs.get(0).song_name);

    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction("play");
    intentFilter.addAction("prev");
    intentFilter.addAction("next");
    intentFilter.addAction("end");
    registerReceiver(musicReceiver, intentFilter);

    StaticValues.pagerMainActivity = this;
    viewPager = (ViewPager) findViewById(R.id.pager_main);
    viewPager.setAdapter(new CustomPagerAdapter());
    viewPager.addOnPageChangeListener(this);

    songInfoView = findViewById(R.id.view_song_info);
    ImageView miniIconImage = (ImageView) findViewById(R.id.image_mini_icon);
    File file = new File(MusicUtils.getAlbumPath(this) + "metadata/" + StaticValues.metadata.mini_icon);
    miniIconImage.setImageURI(Uri.fromFile(file));
    miniIconImage.setScaleType(ImageView.ScaleType.FIT_XY);

    TextView song_title = (TextView) findViewById(R.id.song_title);
    songNameText = (TextView) findViewById(R.id.text_song_name);
    singerText = (TextView) findViewById(R.id.text_singer);
    songMakerText = (TextView) findViewById(R.id.text_song_maker);

    File musicPlayerFile = new File(MusicUtils.getAlbumPath(this) + "metadata/" + StaticValues.metadata.music_player_bg);
    Drawable d = Drawable.createFromPath(musicPlayerFile.getAbsolutePath());
    findViewById(R.id.view_music_player).setBackground(d);
    songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
    songProgressBar.setOnSeekBarChangeListener(this);
    songProgressBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
    songCurrentDurationText = (TextView) findViewById(R.id.songCurrentDurationLabel);
    songTotalDurationText = (TextView) findViewById(R.id.songTotalDurationLabel);
    findViewById(R.id.song_button_prev).setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (StaticValues.playIndex == 0) {
          return;
        }
        viewPager.setCurrentItem(songIndexToPagerIndex(StaticValues.playIndex - 1));
      }
    });
    findViewById(R.id.song_button_next).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (StaticValues.playIndex == StaticValues.songs.size() - 1) {
          return;
        }
        viewPager.setCurrentItem(songIndexToPagerIndex(StaticValues.playIndex + 1));
      }
    });
    playSongButton = (ImageView) findViewById(R.id.button_play_song);
    playSongButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (musicService.isPlaying()) {
          musicService.pauseMusic();
          musicNotification.updatePause();
          togglePlayerButton(true);
        } else {
          musicService.restartMusic();
          musicNotification.updatePlay();
          togglePlayerButton(false);
        }
      }
    });

    lyricsView = findViewById(R.id.view_lyric);
    lyrics = (TextView) findViewById(R.id.song_lyric_text);
    lyricSongNameText = (TextView) findViewById(R.id.text_song_name_lyric);
    ImageView songLyricView = (ImageView) findViewById(R.id.view_song_lyric);
    File lyricsFile = new File(MusicUtils.getAlbumPath(this) + "metadata/" + StaticValues.metadata.lyrics_icon);
    songLyricView.setImageURI(Uri.fromFile(lyricsFile));
    songLyricView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (lyricsView.getVisibility() == View.VISIBLE) {
          lyricsView.setVisibility((View.GONE));
        } else {
          lyricsView.setVisibility(View.VISIBLE);
          songListView.setVisibility(View.GONE);
        }
      }
    });

    songListView = findViewById(R.id.view_song_list);
    songListView.setBackgroundColor(StaticValues.metadata.color);
    songListView.setAlpha(1);

    ImageView titleImage = (ImageView) findViewById(R.id.image_title);
    File titleFile = new File(MusicUtils.getAlbumPath(this) + "metadata/" + StaticValues.metadata.title_image);
    titleImage.setImageURI(Uri.fromFile(titleFile));
    titleImage.setScaleType(ImageView.ScaleType.FIT_XY);

    ListView songList = (ListView) findViewById(R.id.list_song);
    songListView.setVisibility(View.INVISIBLE);
    SongAdapter songAdapter = new SongAdapter(PagerMainActivity.this, StaticValues.songs);
    songAdapter.setListener(this);
    songList.setAdapter(songAdapter);
    ImageView songListImage = (ImageView) findViewById(R.id.image_song_list);
    File songListFile = new File(MusicUtils.getAlbumPath(this) + "metadata/" + StaticValues.metadata.song_list_icon);
    songListImage.setImageURI(Uri.fromFile(songListFile));
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

    song_title.setText(StaticValues.selectedDisk.disk_name);

    initActionBar();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    viewPager.removeOnPageChangeListener(this);
    handler.removeCallbacks(updateProgressRunnable);
    musicNotification.notificationCancel();
    musicService.releaseMusic();
    unregisterReceiver(musicReceiver);
    StaticValues.playIndex = -1;
  }

  @Override
  public void onBackPressed() {
    if (listener != null) {
      listener.onBackPressed();
      return;
    }

    if (songListView.getVisibility() == View.VISIBLE) {
      songListView.setVisibility(View.GONE);
      return;
    }
    if (lyricsView.getVisibility() == View.VISIBLE) {
      lyricsView.setVisibility((View.GONE));
      return;
    }

    new AlertDialog.Builder(this)
        .setMessage("종료하시겠습니까?")
        .setPositiveButton("예",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                finish();
              }
            })
        .setNegativeButton("아니오",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
              }
            }).show();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.action_bar, menu);
    MenuItem item = menu.findItem(R.id.menu_sound);
    item.setIcon(R.drawable.actionbar_button_sound_on);
    return true;
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
      if (musicService.isSoundOn) {
        item.setIcon(R.drawable.actionbar_button_sound_off);
        musicService.soundOff();
      } else {
        item.setIcon(R.drawable.actionbar_button_sound_on);
        musicService.soundOn();
      }
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
  public void onClickDisk(VODisk disk) {
    int id = disk.disk_id;
    if (id == StaticValues.selectedDisk.disk_id) {
      Toast.makeText(this, "이미 선택된 디스크입니다.", Toast.LENGTH_SHORT).show();
      return;
    }

    handler.removeCallbacks(updateProgressRunnable);

    StaticValues.first_check = 0;
    StaticValues.selectedDisk = disk;

    finish();
    Intent i = new Intent(this, LoadingActivity.class);
    i.putExtra("DISK", disk);
    startActivity(i);
  }

  @Override
  public void onClickSong(int position) {
    musicService.startMusic(position);
    viewPager.setCurrentItem(songIndexToPagerIndex(StaticValues.playIndex));
  }


  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
    // noting
  }

  /**
   * When user starts moving the progress handler
   */
  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {
    // remove message Handler from updating progress bar
    handler.removeCallbacks(updateProgressRunnable);
  }

  /**
   * When user stops moving the progress hanlder
   */
  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {
    int totalDuration = musicService.getDuration();
    int currentPosition = CommonUtils.progressToTimer(seekBar.getProgress(), totalDuration);
    musicService.seekTo(currentPosition);
    startUpdatingProgress(false);
  }

  private void initActionBar() {
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
    Uri uri;
    if (flag) {
      File file = new File(MusicUtils.getAlbumPath(this) + "metadata/" + StaticValues.metadata.song_play_icon);
      uri = Uri.fromFile(file);
    } else {
      File file = new File(MusicUtils.getAlbumPath(this) + "metadata/" + StaticValues.metadata.song_pause_icon);
      uri = Uri.fromFile(file);
    }
    playSongButton.setImageURI(uri);
  }

  private void updateMusicPlayerView(int songIndex) {
    lyricsView.setVisibility(View.GONE);
    songListView.setVisibility(View.GONE);

    VOSong song = StaticValues.songs.get(songIndex);
    songNameText.setText(song.song_name);
    lyrics.setText(song.song_lyric);
    lyricSongNameText.setText(song.song_name);
    singerText.setText(song.msg1);
    songMakerText.setText(song.msg2);
    togglePlayerButton(false);
    startUpdatingProgress(true);
    musicNotification.updateName(song.song_name);
  }

  private int songIndexToPagerIndex(int index) {
    return AlbumValue.isSingle ? index + 2 : index + 3;
  }

  private int pagerIndexToSongIndex(int index) {
    return AlbumValue.isSingle ? index - 2 : index - 3;
  }

  private int getPageCount() {
    return AlbumValue.isSingle
        ? StaticValues.songs.size() + 4 : StaticValues.songs.size() + 5;
  }

  private void startUpdatingProgress(boolean isReset) {
    if (isReset) {
      songProgressBar.setProgress(0);
      songProgressBar.setMax(100);
    }
    handler.removeCallbacks(updateProgressRunnable);
    handler.postDelayed(updateProgressRunnable, 100);
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

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    // null
  }

  @Override
  public void onPageSelected(int position) {
    if (position == 0
        || position == 1
        || (!AlbumValue.isSingle && position == 2)
        || position == getPageCount() - 2
        || position == getPageCount() - 1) {
      songInfoView.setVisibility(View.GONE);
      return;
    }
    songInfoView.setVisibility(View.VISIBLE);
    updateMusicPlayerView(pagerIndexToSongIndex(position));
    musicService.startMusic(pagerIndexToSongIndex(position));
  }

  @Override
  public void onPageScrollStateChanged(int state) {
    // null
  }

  public interface Listener {
    void updateCommentList();

    void onBackPressed();
  }

  public class CustomPagerAdapter extends PagerAdapter {

    public CustomPagerAdapter() {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      final View v;
      if (position == 0) {
        v = new HomeView(PagerMainActivity.this);
      } else if (!AlbumValue.isSingle && position == 1) {
        v = new DiskListView(PagerMainActivity.this, PagerMainActivity.this);
      } else if ((!AlbumValue.isSingle && position == 2) || (AlbumValue.isSingle && position == 1)) {
        v = new SongListView(PagerMainActivity.this, PagerMainActivity.this, PagerMainActivity.this);
      } else if (position == getPageCount() - 2) {
        v = new CommentView(PagerMainActivity.this);
        listener = (Listener) v;
      } else if (position == getPageCount() - 1) {
        // 설정
        v = new SettingView(PagerMainActivity.this);
      } else {
        v = new MusicView(PagerMainActivity.this, pagerIndexToSongIndex(position));
      }
      setActionBar(v);
      container.addView(v);
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
}