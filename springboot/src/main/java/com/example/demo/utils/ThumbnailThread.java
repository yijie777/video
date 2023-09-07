package com.example.demo.utils;

import java.io.InputStream;

public class ThumbnailThread extends Thread {

	private String saveFileName;

	private String thumbnailUrl;


	public ThumbnailThread(String saveFileName, String thumbnailUrl) {
		this.saveFileName = saveFileName;
		this.thumbnailUrl = thumbnailUrl;
	}

	@Override
	public void run() {
		try {
			sleep(1000);
//			String videothumbnailcommand = "cmd /c start /b D:\\software\\ffmpeg-4.4-essentials_build\\bin\\ffmpeg -i " + "\"" + saveFileName + "\""
//					+ " -y -f image2 -ss 3 -t 0.001 -s 1920*1080 " + "\"" + thumbnailUrl + "\"";
//			String videothumbnailcommand = "cmd /c start /b C:\\Users\\Administrator\\Desktop\\shipin\\web\\ffmpeg-4.4-essentials_build\\bin\\ffmpeg -i " + "\"" + saveFileName + "\""
//					+ " -y -f image2 -ss 3 -t 0.001 -s 1920*1080 " + "\"" + thumbnailUrl + "\"";
			String videothumbnailcommand = "ffmpeg -i " + saveFileName
					+ " -y -f image2 -ss 3 -t 0.001 -s 1920*1080 " + thumbnailUrl ;
			Process process = Runtime.getRuntime().exec(videothumbnailcommand);

			InputStream inputStream = process.getInputStream();
			new FileUtil().copyFile(inputStream, thumbnailUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ThumbnailThread thumbnailThread=new ThumbnailThread("/Users/zhuyijie/Study/4.mp4","/Users/zhuyijie/Study/2.jpg");
		Thread thread = new Thread(thumbnailThread);
		thread.start();
	}

}
