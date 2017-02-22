package com.bitlworks.intlib_music_base.data;

/**
 * Created by 강혁 on 2016-07-21.
 */
public class VOVideo {

    public int video_id;
    public int song_id;
    public int album_id;
    public String video_file_name;
    public String video_name;
    public int photo_id;


    /*
    sql = "CREATE TABLE video("
				+"video_id INTEGER primary key,"
				+"song_id INTEGER ,"
				+"album_id INTEGER ,"
				+"video_file_name TEXT,"
				+"video_name TEXT,"
				+"photo_id INTEGER);";
    */
    public VOVideo(int video_id, int song_id, int album_id, String video_file_name, String video_name, int photo_id) {
        //super();
        this.video_id = video_id;
        this.song_id = song_id;
        this.album_id = album_id;
        this.video_file_name = video_file_name;
        this.video_name = video_name;
        this.photo_id = photo_id;



    }
}
