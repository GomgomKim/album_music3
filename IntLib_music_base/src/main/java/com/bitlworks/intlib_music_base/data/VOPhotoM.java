package com.bitlworks.intlib_music_base.data;

/**
 * Created by 강혁 on 2016-07-21.
 */
public class VOPhotoM {
    public int photo_id;
    public int disk_id;
    public int song_video_id;
    public int type;
    public String photo_file_name;
    public int album_id;
    public int photo_order;

    public VOPhotoM(int photo_id, int disk_id, int song_video_id, int type,
                    String photo_file_name, int album_id, int photo_order) {
        //super();
        this.photo_id = photo_id;
        this.disk_id = disk_id;
        this.song_video_id = song_video_id;
        this.type = type;
        this.photo_file_name = photo_file_name;
        this.album_id =album_id;
        this.photo_order = photo_order;
    }
}
