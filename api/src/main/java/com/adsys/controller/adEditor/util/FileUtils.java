package com.adsys.controller.adEditor.util;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class FileUtils {
	
	public static void main(String[] args) {
		 String filePath = "d:\\test\\ad\\zipDir\\clientxxxxxxxxx\\296dbf1a1b4a4dcbab72adcdf225d084\\con\\296dbf1a1b4a4dcbab72adcdf225d084\\";
//		String filePath = "d:\\test\\ad\\zipDir\\clientxxxxxxxxx\\296dbf1a1b4a4dcbab72adcdf225d084";
		 File file  = new File(filePath);
		 if(!file.exists()) {
			 file.mkdirs(); 
		 }
	}
	
	public static  boolean delAllFile(String path) {  
        boolean flag = false;  
        File file = new File(path);  
        if (!file.exists()) {  
            return flag;  
        }  
        if (!file.isDirectory()) {  
            return flag;  
        }  
        String[] tempList = file.list();  
        File temp = null;  
        for (int i = 0; i < tempList.length; i++) {  
            if (path.endsWith(File.separator)) {  
                temp = new File(path + tempList[i]);  
            } else {  
                temp = new File(path + File.separator + tempList[i]);  
            }  
            if (temp.isFile()) {  
                temp.delete();  
            }  
            if (temp.isDirectory()) {  
                delAllFile(path + File.separator + tempList[i]);// 先删除文件夹里面的文件  
                delFolder(path + File.separator + tempList[i]);// 再删除空文件夹  
                flag = true;  
            }  
        }  
        return flag;  
    }  
      
    /*** 
     * 删除文件夹 
     *  
     * @param folderPath文件夹完整绝对路径 
     */  
    public  static void delFolder(String folderPath) {  
        try {  
            delAllFile(folderPath); // 删除完里面所有内容  
            String filePath = folderPath;  
            filePath = filePath.toString();  
            java.io.File myFilePath = new java.io.File(filePath);  
            myFilePath.delete(); // 删除空文件夹  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    
    public static String swrapFilePath(String[] fs) {
    	return StringUtils.join(fs, File.separator);
    }
    
    public static void writeXml(File file,Document doc) {
    	try {
    		OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            //设置不自动进行转义
            writer.setEscapeText(false);
            // 生成XML文件
            writer.write(doc);
            //关闭XMLWriter对象
            writer.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
}
