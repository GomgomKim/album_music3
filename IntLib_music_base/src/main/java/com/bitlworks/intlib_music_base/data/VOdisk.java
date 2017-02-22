package com.bitlworks.intlib_music_base.data;

/**
 * Created by 강혁 on 2016-12-15.
 */
/*
disk
[[
disk_id,
disk_name,
album_id,
]]
*/
public class VOdisk {
    public int disk_id;
    public String disk_name;
    public int album_id;

    public VOdisk(int disk_id, String disk_name, int album_id){
        this.disk_id = disk_id;
        this.disk_name = disk_name;
        this.album_id = album_id;
    }
}
