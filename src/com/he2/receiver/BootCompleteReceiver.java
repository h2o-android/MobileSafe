package com.he2.receiver;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {
	
	private SharedPreferences sp;
	private TelephonyManager tm;
	
	
	public BootCompleteReceiver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		
		boolean protecting  = sp.getBoolean("protecting", false);
		if(protecting){//检测是否开启防盗
			tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);			
			//读取存储的sim信息
			String saveSim = sp.getString("sim", "");
			//读取当前的sim卡信息
			String realSim = tm.getSimSerialNumber();
			if(saveSim.equals(realSim)){
				//sim卡和存储的一致，无需操作
			}else{
				//sim卡已变更，发送安全短信
				Toast.makeText(context, "sim卡已变更", 1).show();
				//SmsManager.getDefault().sendTextMessage(sp.getString("safename", ""), null, "sim卡已变更，请检查手机是否被盗，如果被盗发送安全短信进行相关操作", null, null);
				android.telephony.SmsManager.getDefault().sendTextMessage(sp.getString("safename", ""), null, "sim卡已变更，请检查手机是否被盗，如果被盗发送安全短信进行相关操作", null, null);
			}
		}
	}

}
