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
			 * MessageDigest信息摘要器，生成信息摘要。digest整理
			 * MessageDigest类用于为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。简单点说就是用于生成散列码。
			 * 信息摘要是安全的单向哈希函数，它接收任意大小的数据，输出固定长度的哈希值。 对于给定数量的更新数据，digest
			 * 方法只能被调用一次。digest 方法被调用后，MessageDigest 对象被重新设置成其初始状态。
			 * 
			 * digest(byte[] input)  使用指定的字节数组对摘要进行最后更新，然后完成摘要计算。
			 * getInstance(String algorithm) 生成实现指定摘要算法的 MessageDigest 对象。
			 */
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(password.getBytes());
			StringBuffer buffer = new StringBuffer();
			// 每一个byte都做一个与运算0xff
			for (byte b : result) {
				// 与运算
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
