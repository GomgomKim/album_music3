package com.bitlworks.intlib_music_base.data;

/**
 * Created by 강혁 on 2016-07-21.
 */


public class VOSong {
    public int song_id;
    public String song_name;
    public int disk_id;
    public String song_file_name;
    public int photo_id;
    public String song_lyric;

    public String msg1;
    public String msg2;
    public int song_order;

    /*
    sql = "CREATE TABLE song("
				+"song_id INTEGER primary key,"
				+"song_name TEXT ,"
				+"disk_id INTEGER ,"
				+"song_file_name TEXT,"
				+"photo_id INTEGER,"
				+"song_order INTEGER,"
				+"msg1 TEXT,"
				+"msg2 TEXT,"
				+"song_lyric TEXT);";

    */

    public VOSong(int song_id, String song_name, int disk_id, String song_file_name, int photo_id,
                  String song_lyric, String msg1, String msg2, int song_order) {
        //super();
        this.song_id = song_id;
        this.song_name = song_name;
        this.disk_id = disk_id;
        this.song_file_name = song_file_name;
        this.photo_id = photo_id;
        this.song_lyric = song_lyric;
        this.msg1 = msg1;
        this.msg2 = msg2;

        this.song_order = song_order;

    }

}
