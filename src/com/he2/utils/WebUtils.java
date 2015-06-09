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
	 * ��inputStreamת��ΪString����
	 * @param is ����Ҫת����inputStream
	 * @param charset �����inputStream�ĸ�ʽ��Ĭ��UTF-8
	 * @return ת�����String����
	 */
	public static String readFromStream(InputStream is,String charset) {
		// TODO Auto-generated method stub
		//�洢����String����
		String tmp=null;
		
		//����Ĭ�ϱ����ʽ
		if(charset==null){
			charset="utf-8";
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//���û�������С
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
