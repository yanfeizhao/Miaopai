/** 
 * @Title: FileUtils.java 
 * @Package com.qst.fly.utils 
 * @Description: TODO 
 * @author yanfeizhao 417470640@qq.com 
 * @date 2016-3-17 上午11:38:19 
 * @version V1.0 
 */  
package com.qst.fly.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;


public class FileUtils {

	
	/**
	 * 获取所给路径下的所有的.png图片，并将图片的  “路径+名字” 存在List<String>  作为返回值。
	 */
	public static List<String> GetPictureFileName(String fileAbsolutePath) {
		List<String> vecFile = new ArrayList<String>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
 
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String filename = subFile[iFileLength].getName();
                // 判断是否为jpg结尾
                if (filename.trim().toLowerCase().endsWith(".png")) {
                	String endFileName=fileAbsolutePath+"/"+filename;
                    vecFile.add(endFileName);
                }
            }
        }
        return vecFile;
    }
	
	/**
	 * 在Environment.getExternalStorageDirectory()下边创建指定文件
	 * @param path 目录，可多层
	 * @param fileName 文件名，将创建于目录下边
	 * @return
	 */
	public static File createFile(String path,String fileName){
		File file = new File(Environment.getExternalStorageDirectory(),path);
		if(!file.exists()){
			file.mkdirs();
		}
		File targetFile = new File(file,fileName);
		if(!targetFile.exists()){
			try {
				targetFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return targetFile;
	}
}
