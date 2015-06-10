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
		// �ж��Ƿ����ù���û����ת���ý���
		boolean configed = sp.getBoolean("configed", false);

		if (configed) {
			setContentView(R.layout.activity_lost_find);
			tv_safenumber = (TextView) findViewById(R.id.tv_safenumber);
			iv_protecting = (ImageView) findViewById(R.id.protecting);
			// ��ȡ����İ�ȫ���룬��������
			String safeNumber = sp.getString("safenumber", "");
			tv_safenumber.setText(safeNumber);
			// ���÷���������״̬
			boolean protecting = sp.getBoolean("protecting", false);
			if (protecting) {
				// �Ѿ�������������
				iv_protecting.setImageResource(R.drawable.lock);
			} else {
				// û�п�����������
				iv_protecting.setImageResource(R.drawable.unlock);
			}
		} else {
			// û�����ã���ת����ҳ��
			Intent intent = new Intent(this, Setup1Activity.class);
			startActivity(intent);
			// �رյ�ǰҳ��
			finish();
		}
	}

	/**
	 * ���½����ֻ�����������ҳ��
	 * 
	 * @param view
	 */
	public void reEnterSetup(View view) {
		Intent intent = new Intent(this, Setup1Activity.class);
		startActivity(intent);
		// �رյ�ǰҳ��
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
