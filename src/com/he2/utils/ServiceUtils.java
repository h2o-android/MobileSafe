package com.he2.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {

	public ServiceUtils() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * У��ĳ�������Ƿ񻹻��� 
	 * serviceName :�������ķ��������
	 */
	public static boolean isServiceRunning(Context context,String servicename){
		//ϵͳ����ʵ����
		ActivityManager am  = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		//��ȡϵͳ�������в�����ʾ��ȡ���ٷ�����������������������ͷ���ʵ������
		List<RunningServiceInfo> infos = am.getRunningServices(100);
		for(RunningServiceInfo info:infos){
			String name = info.service.getClassName();
			if(servicename.equals(name)){
				return true;
			}
		}
		return false;
	}
}
