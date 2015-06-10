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
	//定义一个手势识别器
	private GestureDetector detector;
	protected SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//实例化手势识别
		detector = new GestureDetector(this,new SimpleOnGestureListener(){
			
			/**
			 * 滑动时调用，velocityX和velocityY分别是对应的滑动速度
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				// TODO Auto-generated method stub
				//滑动过慢的情况
				if(Math.abs(velocityX)<200){
					//Toast.makeText(getApplicationContext(), "滑动无效，速度过慢", 0).show();
					return true;
				}
				//屏蔽斜滑这种情况
				if(Math.abs(e1.getRawY()-e2.getRawY())>100){
					return true;
				}
				//显示上一个界面
				if((e2.getRawX()-e1.getRawX())>200){
					showPre();
					return true;
				}
				//显示下一个界面
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
	 * 下一个界面的按钮
	 * @param view
	 */
	public void next(View view){
		showNext();
	}
	/**
	 * 上一个界面的按钮
	 * @param view
	 */
	public void pre(View view){
		showPre();
	}
	public BaseSetupActivity() {
		// TODO Auto-generated constructor stub
	}
	//使用手势识别器，触摸屏幕时启动手势
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

}
