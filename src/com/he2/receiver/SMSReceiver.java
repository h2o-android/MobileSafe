package com.he2.receiver;

import com.he2.mobilesafe.R;
import com.he2.service.GPSService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.text.TextUtils;

public class SMSReceiver extends BroadcastReceiver {

	private SharedPreferences sp;

	public SMSReceiver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		/*
		 * ���ܶ��� ������ͨ��pdu��ʽ����ġ�pdu��protocol description unit��
		 * ���յ������ݾ���pdu�����飬������object����
		 */
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);

		for (Object b : objs) {
			/*
			 * ������ȡ�Ľ�����,Ҫע����Ҫǿת��byte����,����ԭʼpdu
			 * public static SmsMessage createFromPdu (byte[] pdu)
			 * ��ԭʼ��PDU��protocol description units������һ��SmsMessage��
			 */
			android.telephony.SmsMessage sms = android.telephony.SmsMessage
					.createFromPdu((byte[]) b);
			
			//��ȡ�����˵ĵ�ַ
			String sender = sms.getOriginatingAddress();
			String safenumber = sp.getString("safenumber", "");
			//��ȡ��������
			String body = sms.getMessageBody();
			
			if(sender.equals(safenumber)){
				
				if("#*location*#".equals(body)){//��ȡ�ֻ�GPS��ַ
					Intent intent2GPS = new Intent(context,GPSService.class);
					//��������
					context.startService(intent2GPS);
					 String lastlocation = sp.getString("lastlocation", null);
					 if(TextUtils.isEmpty(lastlocation)){
						 //û�л�ȡ��λ��
						 android.telephony.SmsManager.getDefault().sendTextMessage(sender, null, "��δ��ȡ����ַ������ץ����ȡ", null, null);
					 }else{
						 //��ȡ����λ��
						 SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
					 }
					//������㲥��ֹ��,��ֹ����������յ������Ϣ
					abortBroadcast();
				}else if("#*alarm*#".equals(body)){
					MediaPlayer player = MediaPlayer.create(context,R.raw.ylzs);
					//����ѭ������
					player.setLooping(true);
					//������������������1Ϊ���
					player.setVolume(1.0f, 1.0f);
					player.start();
					abortBroadcast();
				}else if("#*wipedata*#".equals(body)){
					abortBroadcast();
				}else if("#*lockscreen*#".equals(body)){
					abortBroadcast();
				}
			}
		}
	}

}
