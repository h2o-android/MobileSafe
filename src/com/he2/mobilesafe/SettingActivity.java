package com.he2.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.he2.ui.SettingItemView;

public class SettingActivity extends Activity {
	//设置是否开启自动更新
	private SettingItemView siv_update;
	private SharedPreferences sp;
	
	//设置是否开启显示归属地查询
	private SettingItemView siv_show_address;
	private Intent showAddress;
	
	//设置归属地背景框
	
	public SettingActivity() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
	
		sp = getSharedPreferences("config", MODE_PRIVATE);
		siv_update  = (SettingItemView) findViewById(R.id.siv_update);
		
		boolean update = sp.getBoolean("update", true);
		if(update){
			//自动升级已经开启
			siv_update.setChecked(true);
			siv_update.setDesc("自动升级已经开启");
		}else{
			//自动升级已经关闭
			siv_update.setChecked(false);
			siv_update.setDesc("自动升级已经关闭");
		}
		siv_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Editor editor = sp.edit();
				//判断是否有选中
				//已经打开自动升级了
				if(siv_update.isChecked()){
					siv_update.setChecked(false);
					siv_update.setDesc("自动升级已经关闭");
					editor.putBoolean("update", false);
					
				}else{
					//没有打开自动升级
					siv_update.setChecked(true);
					siv_update.setDesc("自动升级已经开启");
					editor.putBoolean("update", true);
				}
				editor.commit();
			}
		});
	}

}
