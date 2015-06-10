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
	 * 校验某个服务是否还活着 
	 * serviceName :传进来的服务的名称
	 */
	public static boolean isServiceRunning(Context context,String servicename){
		//系统服务实例化
		ActivityManager am  = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		//获取系统服务，其中参数表示获取多少服务，如果不到参数的数量，就返回实际数量
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
