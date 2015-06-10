package com.he2.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {

	private SharedPreferences sp;

	private TextView tv_safenumber;
	private ImageView iv_protecting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 判断是否设置过，没有跳转设置界面
		boolean configed = sp.getBoolean("configed", false);

		if (configed) {
			setContentView(R.layout.activity_lost_find);
			tv_safenumber = (TextView) findViewById(R.id.tv_safenumber);
			iv_protecting = (ImageView) findViewById(R.id.protecting);
			// 获取保存的安全密码，并且设置
			String safeNumber = sp.getString("safenumber", "");
			tv_safenumber.setText(safeNumber);
			// 设置防盗保护的状态
			boolean protecting = sp.getBoolean("protecting", false);
			if (protecting) {
				// 已经开启防盗保护
				iv_protecting.setImageResource(R.drawable.lock);
			} else {
				// 没有开启防盗保护
				iv_protecting.setImageResource(R.drawable.unlock);
			}
		} else {
			// 没有设置，跳转设置页面
			Intent intent = new Intent(this, Setup1Activity.class);
			startActivity(intent);
			// 关闭当前页面
			finish();
		}
	}

	/**
	 * 重新进入手机防盗设置向导页面
	 * 
	 * @param view
	 */
	public void reEnterSetup(View view) {
		Intent intent = new Intent(this, Setup1Activity.class);
		startActivity(intent);
		// 关闭当前页面
		finish();
	}
	
	public void setProtecting(View view){
		boolean protecting = sp.getBoolean("protecting", false);
		Editor editor = sp.edit();
		editor.putBoolean("protecting", !protecting);
		if (!protecting) {
			iv_protecting.setImageResource(R.drawable.lock);
		} else {	
			iv_protecting.setImageResource(R.drawable.unlock);
		}
		editor.commit();
	}
	
	public LostFindActivity() {
		// TODO Auto-generated constructor stub
	}

}
