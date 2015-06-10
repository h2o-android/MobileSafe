package com.he2.service;

import java.io.IOException;
import java.io.InputStream;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class GPSService extends Service {
	// λ�÷���
	private LocationManager lm;
	private MyLocationListener listener;

	public GPSService() {
		// TODO Auto-generated constructor stub

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// ��ȡϵͳ����
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		listener = new MyLocationListener();
		/*
		 * ���ö�λ��ȷ�� Criteria.ACCURACY_COARSE �Ƚϴ���, Criteria.ACCURACY_FINE��ȽϾ�ϸ
		 * 
		 * ���ò���ϸ���� criteria.setAccuracy(Criteria.ACCURACY_FINE);//����Ϊ��󾫶�
		 * criteria.setAltitudeRequired(false);//��Ҫ�󺣰���Ϣ
		 * criteria.setBearingRequired(false);//��Ҫ��λ��Ϣ
		 * criteria.setCostAllowed(true);//�Ƿ�������
		 * criteria.setPowerRequirement(Criteria.POWER_LOW);//�Ե�����Ҫ��
		 */
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);

		// ���ֶ�λ��ѡȡ״̬��õ�
		String proveder = lm.getBestProvider(criteria, true);
		lm.requestLocationUpdates(proveder, 0, 0, listener);
	}

	/**
	 * ȡ����������ֹ���Ⱥĵ�
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		lm.removeUpdates(listener);
		listener = null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			String longitude = "j:" + location.getLongitude() + "\n";
			String latitude = "w:" + location.getLatitude() + "\n";
			String accuracy = "a" + location.getAccuracy() + "\n";
			// �����Ÿ���ȫ����
			// �ѱ�׼��GPS����ת���ɻ�������
			//��ת���������ʹ�ã���Ҫ��������ͨ
			InputStream is;
			try {
				is = getAssets().open("axisoffset.dat");
				ModifyOffset offset = ModifyOffset.getInstance(is);
				PointDouble double1 = offset.s2c(new PointDouble(location
						.getLongitude(), location.getLatitude()));
				longitude = "j:" + offset.X + "\n";
				latitude = "w:" + offset.Y + "\n";

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("lastlocation", longitude + latitude + accuracy);
			editor.commit();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	}

}
