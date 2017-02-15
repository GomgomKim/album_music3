package com.bitlworks.intlib_music_base.common.network;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bitlworks.music._common.StaticValues;
import com.bitlworks.music._common.data.DAOSqlite;
import com.bitlworks.music._common.data.DataNetUtils;
import com.bitlworks.music._common.data.VOCouple;
import com.bitlworks.music._common.data.VOPhoto;
import com.bitlworks.music._common.data.VOTheme;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** 앨범 데이터를 다운로드 하는 클래스
 *  내부 저장소는 sdcard/bitlworks/wedding를 기본
 **/
public class AlbumDataDownloader { // 일종의 Logic class
	private final static String LOG_TAG_NAVI = "<AlbumDataDownloader.class>";
	
	public AlbumDataDownloader(Context c, int coupleId, final Handler mHandler) {
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " AlbumDataDownloader()");
		DAOSqlite sqlDAO = DAOSqlite.getInstance(c);
		
		// 경로 정하기
		String rootPath = "/mnt/sdcard/";
		// 내부 저장소 경로 구하기
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
		}else return;
				
		// 폴더 존재 확인 및 생성
		rootPath += "bitlworks/";
		checkFolder(rootPath);
		rootPath += "wedding/";
		checkFolder(rootPath);
		
		// 커플 정보를 읽어 옵니다.
		VOCouple voCouple = sqlDAO.getCouple(coupleId);
		
		// 테마 목록을 읽어 옵니다.
		ArrayList<VOTheme> themeList = sqlDAO.getThemeList(coupleId, false);
		HashMap<Integer, String> themeIDtoPath = new HashMap<Integer, String>();
		for(VOTheme vo : themeList){
			themeIDtoPath.put(vo.getTheme_id(), vo.getTheme_path());
		}
		
		// 쓰레드 풀링
		ExecutorService executorService = Executors.newFixedThreadPool(15);
		
		// (0) 볼수 있는 앨범의 표지들 다운로드
		int firstPhotoCount = 0;
		final String[] albumNos = sqlDAO.getCoupleIdList().split(",");
		for (int i = 1; i < albumNos.length; i++) {
			final VOCouple vo = sqlDAO.getCouple(Integer.parseInt(albumNos[i]));
			final String url_fimg = StaticValues.SERVICE_URL + "first_photo/"
					+ vo.getCouple_photo();
	/*		final String url_fimg = "http://14.63.160.237/Mobile_Album/wdisk/" + "first_photo/"
					+ vo.getCouple_photo();*/


			final String[] filenames = vo.getCouple_photo().split("\\.");
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < filenames.length-1; j++) {
				sb.append(filenames[j]);
			}
			sb.append(".ti");
			final String LocalPath = rootPath + sb.toString();
			executorService.execute( new DownloadRunable(url_fimg, LocalPath, mHandler) );
			
			firstPhotoCount++;
		}
		
		// (1) 동영상 확인 후 다운로드 쓰레드 돌리기
		int videoCount = 0;
		try {
			if( voCouple.getCouple_video() != null){ // 널체크
					if(voCouple.getCouple_video().length() > 3){
						videoCount++;
						VideoDownloader.newInstance(c, executorService, mHandler);
					}
			}
		} catch (NullPointerException e) { videoCount=0; }

		// 사진 파일의 정보를 읽어옴
		ArrayList<VOPhoto> photoList = sqlDAO.getDownPhotoList(coupleId);
		/*/ couple_state가 5이하일 때는 보일 사진만 가져오기
		if(voCouple.getCouple_state() < 5)
			photoList = sqlDAO.getPhotoList(coupleId, true, false); 
		else
			photoList = sqlDAO.getPhotoList(coupleId, false, false);
		*/
		
		// (?) 배경음악 다운로드 쓰레드 돌리기
		int musicCount = 0;
		if(voCouple.getCouple_music().contains("http://")){
			String inpath = voCouple.getMusic_inpath();
			if( inpath == null ){
				inpath = rootPath + voCouple.getCouple_id() + ".bgm";
				sqlDAO.updateBGMPath(voCouple.getCouple_id(), inpath);
			}
			File musicFile = new File( inpath );
			if( !musicFile.exists() ){
				musicCount++;
				// 다운로드 하기
				String LocalPath = rootPath + voCouple.getCouple_id() + ".bgm";
				executorService.execute( new DownloadRunable(voCouple.getCouple_music(),
						LocalPath, mHandler) );
				
			}
		}
		
		
		// 핸들러에 총 다운로드할 사진 갯수 전달
		Message msg = new Message();
		msg.what = 0;
		msg.arg1 = firstPhotoCount + videoCount + musicCount + photoList.size();
		if(mHandler != null) mHandler.sendMessage(msg);
		
		
		
		
		// (2) 사진 파일 다운로드 쓰레드 돌리기
		ArrayList<VOPhotoPath> photoPathList = new ArrayList<VOPhotoPath>();
		for(VOPhoto vo : photoList){
			String ServerUrl = "";
			String LocalPath = "";
			if(vo.getTheme_id() > 0){
				ServerUrl = StaticValues.SERVICE_URL + voCouple.getCouple_path() + "/"
				+ themeIDtoPath.get( vo.getTheme_id() ) + "/" + vo.getPhoto_path() ;
				LocalPath = rootPath + vo.getTheme_id()+ "_"+ vo.getPhoto_id() ;

				/*
				ServerUrl = "http://14.63.160.237/Mobile_Album/wdisk/" + voCouple.getCouple_path() + "/"
						+ themeIDtoPath.get( vo.getTheme_id() ) + "/" + vo.getPhoto_path() ;
				LocalPath = rootPath + vo.getTheme_id()+ "_"+ vo.getPhoto_id() ;
				*/
			}else/* if(vo.getDtheme_id() > 0)*/{
				ServerUrl = StaticValues.SERVICE_URL + vo.getPhoto_path() ;
				LocalPath = rootPath + "T" + vo.getDtheme_id()+ "P"+ vo.getPhoto_id() ;

				//ServerUrl = "http://14.63.160.237/Mobile_Album/wdisk/" + vo.getPhoto_path() ;
				//LocalPath = rootPath + "T" + vo.getDtheme_id()+ "P"+ vo.getPhoto_id() ;
			}
		
			photoPathList.add(new VOPhotoPath(vo.getPhoto_id(), ServerUrl, LocalPath));
			
			// 이미 파일이 존재하는지 확인후 다운로드
			executorService.execute( new DownloadRunable(ServerUrl, LocalPath, mHandler) );
		}
		sqlDAO.updatePhotoPaths(photoPathList);
		
		executorService.shutdown();
	}

	/** 폴더가 존재하지 않을 경우 폴더를 만들어준다 **/
	private void checkFolder(String path) {
		File dir = new File(path);
		if (!dir.exists()) {  
		    dir.mkdir();
		}
	}
	
	/** 앨범 데이터 존재 여부 체크 **/
	public static boolean isExsistAlbumData(int coupleId, Context c){
		Log.i(StaticValues.LOG_TAG, LOG_TAG_NAVI + " isExsistAlbumData()");
		
		DAOSqlite sqlDAO = DAOSqlite.getInstance(c);
		// 1. 커플의 메타 데이터 존재 여부
		VOCouple voCouple = sqlDAO.getCouple(coupleId);
		if (voCouple == null) return false;
		
		// 2. 커플의 테마 및 사진 메타 존재 여부 확인
		ArrayList<VOTheme> themes = sqlDAO.getThemeList(coupleId, false);
		if (themes == null) return false;
		if (themes.size() < 1) return false;
		
		// 2.5 커플 앨범 이미지의 존재 여부
		ArrayList<VOCouple> couples = sqlDAO.getCoupleList(false);
		for(VOCouple couple : couples){
			String path = DataNetUtils.getInternalStorePath() + couple.getCouple_photoPath();
			if(new File(path).exists()==false) return false;
		}

		// 3. 동영상 존재 여부 확인
		final String vpath = DataNetUtils.getInternalStorePath() + voCouple.getCouple_video();
		if(new File(vpath).exists() == false) return false;
		
		// 3.5 BGM 존재 여부 확인
		final String mpath = voCouple.getMusic_inpath();
		if(mpath != null)
			if (mpath.length() > 4 ) 
				if(new File(mpath).exists() == false) return false;

		// 4. 실제 사진 존재 여부
		ArrayList<VOPhoto> photos = null;
		if (voCouple.getCouple_state() < 5) {
			photos = sqlDAO.getPhotoList(coupleId, true, false);	
		}else{
			photos = sqlDAO.getPhotoList(coupleId, false, false);
		}
		
		if (photos == null) return false;
		if (photos.size() < 1) return false;
		for(VOPhoto vo : photos){
			final String inpath = vo.getPhoto_inpath();
			if(inpath == null ) return false;
			if(inpath.length() < 1) return false;
			if(new File(inpath).exists()==false) return false;
		}
		return true;
	}
}


