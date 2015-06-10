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
		 * 接受短信 短信是通过pdu形式传输的。pdu（protocol description unit）
		 * 接收到的数据就是pdu的数组，所以用object接收
		 */
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);

		for (Object b : objs) {
			/*
			 * 创建读取的接收器,要注意需要强转成byte数组,返回原始pdu
			 * public static SmsMessage createFromPdu (byte[] pdu)
			 * 从原始的PDU（protocol description units）创建一个SmsMessage。
			 */
			android.telephony.SmsMessage sms = android.telephony.SmsMessage
					.createFromPdu((byte[]) b);
			
			//获取来信人的地址
			String sender = sms.getOriginatingAddress();
			String safenumber = sp.getString("safenumber", "");
			//获取短信内容
			String body = sms.getMessageBody();
			
			if(sender.equals(safenumber)){
				
				if("#*location*#".equals(body)){//获取手机GPS地址
					Intent intent2GPS = new Intent(context,GPSService.class);
					//开启服务
					context.startService(intent2GPS);
					 String lastlocation = sp.getString("lastlocation", null);
					 if(TextUtils.isEmpty(lastlocation)){
						 //没有获取到位置
						 android.telephony.SmsManager.getDefault().sendTextMessage(sender, null, "暂未获取到地址，正在抓紧获取", null, null);
					 }else{
						 //获取到了位置
						 SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
					 }
					//把这个广播终止掉,防止其他软件接收到这个信息
					abortBroadcast();
				}else if("#*alarm*#".equals(body)){
					MediaPlayer player = MediaPlayer.create(context,R.raw.ylzs);
					//设置循环播放
					player.setLooping(true);
					//设置左右声道音量，1为最大
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
