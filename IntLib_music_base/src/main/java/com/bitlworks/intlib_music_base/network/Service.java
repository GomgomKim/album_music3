package com.bitlworks.intlib_music_base.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {
  String ROOT = "http://music.bitlworks.co.kr/mobilemusic/API/";

  @GET("getUserInfo.php")
  Call<JsonObject> getUser(@Query("u") int userId);

  @GET("getUserJoin.php")
  Call<JsonObject> insertUser(@Query("a") int albumId, @Query("p") String mobile);

  @GET("updateNick.php")
  Call<JsonObject> updateNick(@Query("c") int albumId, @Query("u") int userId, @Query("n") String name);

  @GET("updateRegid.php")
  Call<JsonObject> updateRegid(@Query("i") int userId, @Query("u") String uuid, @Query("a") String appver, @Query("r") String redId);

  @GET("GetAlbumInfo.php")
  Call<JsonObject> getAlbum(@Query("album_id") int albumId);

  @GET("getMusicAlbumList.php")
  Call<JsonArray> getDisks(@Query("album_id") int albumId);

  @GET("GetPhotoList.php")
  Call<JsonArray> getPhotos(@Query("album_id") int albumId, @Query("disk_id") int diskId);

  @GET("GetNewInfoList.php")
  Call<JsonArray> getNewInfos(@Query("album_id") int albumId);

  @GET("GetSongList.php")
  Call<JsonArray> getSongs(@Query("disk_id") int diskId);

  @GET("GetVideoList.php")
  Call<JsonArray> getVideos(@Query("album_id") int albumId);

  @GET("GetCommentList.php")
  Call<JsonArray> getComments(@Query("album_id") int albumId);

  @GET("insertComment.php")
  Call<JsonArray> insertComment(@Query("album_id") int albumId, @Query("user_id") int userId, @Query("user_name") String userName, @Query("contents") String contents);

  @GET("DelCommentList.php")
  Call<JsonArray> deleteComment(@Query("a_id") int albumId, @Query("c_id") int commentId);

  @GET("getAthuCheck.php")
  Call<JsonObject> getAthuCheck(@Query("user") int userId, @Query("studio") int albumId);
}
