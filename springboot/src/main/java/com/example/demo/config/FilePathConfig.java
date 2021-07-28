package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;



@Component
@ConfigurationProperties(prefix="filepathconfig")
public class FilePathConfig {
	private String fileUrl;
	private String imagePath;
	private String videoPath;
	private String enclosurePath;
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getVideoPath() {
		return videoPath;
	}
	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}
	public String getEnclosurePath() {
		return enclosurePath;
	}
	public void setEnclosurePath(String enclosurePath) {
		this.enclosurePath = enclosurePath;
	}
	@Override
	public String toString() {
		return "FilePathConfig [fileUrl=" + fileUrl + ", imagePath=" + imagePath + ", videoPath=" + videoPath + ", enclosurePath=" + enclosurePath+ "]";
	}
	
}
