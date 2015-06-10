package com.he2.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Setup4Activity extends BaseSetupActivity {
	
	private SharedPreferences sp;
	private CheckBox cb_protecting;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		cb_protecting = (CheckBox) findViewById(R.id.cb_proteting);
		
		//��ȡ�����Ƿ���
		boolean protecting = sp.getBoolean("protecting", false);
		
		if(protecting){
			cb_protecting.setText("�ֻ������Ѿ�����");
			cb_protecting.setChecked(true);
		}else{
			cb_protecting.setText("�ֻ�����δ����");
			cb_protecting.setChecked(false);
		}
		
		cb_protecting.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					cb_protecting.setText("�ֻ������Ѿ�����");
				}else{
					cb_protecting.setText("�ֻ�����û�п���");
				}
				//����Ϣ������SharedPreferences
				Editor editor = sp.edit();
				editor.putBoolean("protecting", isChecked);
				editor.commit();
			}
		});
	}
	public Setup4Activity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		//�������ã��洢��ʾ�Ѿ����óɹ�
		Editor editor = sp.edit();
		editor.putBoolean("configed", true);
		editor.commit();
		
		Intent intent = new Intent(this,LostFindActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		
		Intent intent = new Intent(this,Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}

}
