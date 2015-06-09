package com.he2.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.R.integer;

public class WebUtils {

	public WebUtils() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 把inputStream转换为String类型
	 * @param is 传入要转换的inputStream
	 * @param charset 传入的inputStream的格式，默认UTF-8
	 * @return 转换后的String类型
	 */
	public static String readFromStream(InputStream is,String charset) {
		// TODO Auto-generated method stub
		//存储返回String数据
		String tmp=null;
		
		//设置默认编码格式
		if(charset==null){
			charset="utf-8";
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//设置缓存区大小
		byte[] buffer = new byte[1024];
		int len = 0;
		try {
			while ((len=is.read(buffer))!=-1) {
				baos.write(buffer,0,len);
			}
			is.close();
			 tmp  = baos.toString();
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tmp;
	}
	
}
