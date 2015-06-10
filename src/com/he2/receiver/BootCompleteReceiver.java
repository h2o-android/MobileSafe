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
		if(protecting){//����Ƿ�������
			tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);			
			//��ȡ�洢��sim��Ϣ
			String saveSim = sp.getString("sim", "");
			//��ȡ��ǰ��sim����Ϣ
			String realSim = tm.getSimSerialNumber();
			if(saveSim.equals(realSim)){
				//sim���ʹ洢��һ�£��������
			}else{
				//sim���ѱ�������Ͱ�ȫ����
				Toast.makeText(context, "sim���ѱ��", 1).show();
				//SmsManager.getDefault().sendTextMessage(sp.getString("safename", ""), null, "sim���ѱ���������ֻ��Ƿ񱻵�������������Ͱ�ȫ���Ž�����ز���", null, null);
				android.telephony.SmsManager.getDefault().sendTextMessage(sp.getString("safename", ""), null, "sim���ѱ���������ֻ��Ƿ񱻵�������������Ͱ�ȫ���Ž�����ز���", null, null);
			}
		}
	}

}
