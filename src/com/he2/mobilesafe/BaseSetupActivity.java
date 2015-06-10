package com.he2.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {
	//����һ������ʶ����
	private GestureDetector detector;
	protected SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//ʵ��������ʶ��
		detector = new GestureDetector(this,new SimpleOnGestureListener(){
			
			/**
			 * ����ʱ���ã�velocityX��velocityY�ֱ��Ƕ�Ӧ�Ļ����ٶ�
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				// TODO Auto-generated method stub
				//�������������
				if(Math.abs(velocityX)<200){
					//Toast.makeText(getApplicationContext(), "������Ч���ٶȹ���", 0).show();
					return true;
				}
				//����б���������
				if(Math.abs(e1.getRawY()-e2.getRawY())>100){
					return true;
				}
				//��ʾ��һ������
				if((e2.getRawX()-e1.getRawX())>200){
					showPre();
					return true;
				}
				//��ʾ��һ������
				if((e1.getRawX()-e2.getRawX())>200){
					showNext();
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
				
				
			}
		});
	}
	public abstract void showNext();
	public abstract void showPre();
	/**
	 * ��һ������İ�ť
	 * @param view
	 */
	public void next(View view){
		showNext();
	}
	/**
	 * ��һ������İ�ť
	 * @param view
	 */
	public void pre(View view){
		showPre();
	}
	public BaseSetupActivity() {
		// TODO Auto-generated constructor stub
	}
	//ʹ������ʶ������������Ļʱ��������
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

}
