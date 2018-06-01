package com.ns.common.utils;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileUtil {
	public static final FileUtil instance = new FileUtil();

	// 随机一个文件名
	public String randomFileName() {
		Date dt = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyMMddHHmmssSSS");
		String fileName = sdf.format(dt);
		return fileName;
	}

	/**
	 * 修改文件名
	 * @param filePath
	 *            eg:D:/gai.jpg
	 * @return
	 */
	public String changeFileName(String filePath) {
		System.out.println(filePath+"a");
		File file = new File(filePath);// 指定文件名及路径
		String name = randomFileName() + ".xlsx";
		System.out.println(name);
		//文件夹位置
		String path = filePath.substring(0, filePath.lastIndexOf("\\"));
		String newFilePath = path+"\\"+name;
		System.out.println(newFilePath);
		try {
			file.renameTo(new File(newFilePath)); // 改名
			System.out.println(new File(newFilePath).getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("aa");
		}
		return name;
	}

	public static ArrayList<File> getListFiles(Object obj) {  
		File directory = null;  
		if (obj instanceof File) {  
			directory = (File) obj;  
		} else {  
			directory = new File(obj.toString());  
		}  
		ArrayList<File> files = new ArrayList<File>();  
		if (directory.isFile()) {  
			files.add(directory);  
			return files;  
		} else if (directory.isDirectory()) {  
			File[] fileArr = directory.listFiles();  
			for (int i = 0; i < fileArr.length; i++) {  
				File fileOne = fileArr[i];  
				files.addAll(getListFiles(fileOne));  
			}  
		}  
		return files;  
	}  
	public static String getMimeType(String fileName) {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String type = fileNameMap.getContentTypeFor(fileName);
		return type;
	}

	/**
	 * 获取文件扩展名*
	 * @param fileName 文件名
	 * @return 扩展名
	 */
	public static String getExtension(String fileName) {
		int i = fileName.lastIndexOf(".");
		if (i < 0) return null;

		return fileName.substring(i+1);
	}

	/**
	 * 判断文件是否是视频
	 * @param fileName 文件名
	 * @return 是返回true，否返回false
	 */
	public static boolean isVideo(String fileName) {
		getExtension(fileName);
		return getExtension(fileName).equals("mp4")||
				getExtension(fileName).equals("MOV")||
				getExtension(fileName).equals("flv")||
				getExtension(fileName).equals("mkv")||
				getExtension(fileName).equals("3gp")||
				getExtension(fileName).equals("wmv")||
				getExtension(fileName).equals("avi")||
				getExtension(fileName).equals("MPG")||
				getExtension(fileName).equals("rmvb");
	}
}
