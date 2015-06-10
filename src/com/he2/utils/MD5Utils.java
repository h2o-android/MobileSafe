package com.he2.utils;

import java.nio.Buffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.R.integer;

public class MD5Utils {

	public MD5Utils() {
		// TODO Auto-generated constructor stub
	}

	public static String md5Password(String password) {
		try {
			/*
			 * MessageDigest��ϢժҪ����������ϢժҪ��digest����
			 * MessageDigest������ΪӦ�ó����ṩ��ϢժҪ�㷨�Ĺ��ܣ��� MD5 �� SHA �㷨���򵥵�˵������������ɢ���롣
			 * ��ϢժҪ�ǰ�ȫ�ĵ����ϣ�����������������С�����ݣ�����̶����ȵĹ�ϣֵ�� ���ڸ��������ĸ������ݣ�digest
			 * ����ֻ�ܱ�����һ�Ρ�digest ���������ú�MessageDigest �����������ó����ʼ״̬��
			 * 
			 * digest(byte[] input)  ʹ��ָ�����ֽ������ժҪ���������£�Ȼ�����ժҪ���㡣
			 * getInstance(String algorithm) ����ʵ��ָ��ժҪ�㷨�� MessageDigest ����
			 */
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(password.getBytes());
			StringBuffer buffer = new StringBuffer();
			// ÿһ��byte����һ��������0xff
			for (byte b : result) {
				// ������
				int number = b & 0xff;
				String str = Integer.toHexString(number);
				if (str.length() == 1) {
					buffer.append(0);
				}
				buffer.append(str);
			}
			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}
}
