package com.bitlworks.intlib_music_base.data;

public class VOVideo {

    public int video_id;
    public int album_id;
    public String video_file_name;
    public String video_name;
    public String photoPath;

    public VOVideo(int video_id, int album_id, String video_file_name, String video_name, String photoPath) {
        this.video_id = video_id;
        this.album_id = album_id;
        this.video_file_name = video_file_name;
        this.video_name = video_name;
        this.photoPath = photoPath;
    }
}
