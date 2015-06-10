package com.he2.mobilesafe;

import com.he2.db.dao.NumberAddressQueryUtils;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NumberAddressQueryActivity extends Activity {

	private EditText ed_phone;
	private TextView result;
	// ϵͳ�ṩ���𶯷���
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_address_query);
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		result = (TextView) findViewById(R.id.result);
		// ʵ����ϵͳ�𶯷���
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		// ע����������¼���textwatcher��������֮ǰ����˼ά��onʲô��
		ed_phone.addTextChangedListener(new TextWatcher() {

			/**
			 * �ı������仯ʱ�ص���s��ʾ���ݣ�start��ʾ�����￪ʼ��
			 * before��ʾ��start��ʼ��count���ַ��滻����Ϊbefore�ľ��ı� count��ʾ�滻�س��� �ʼ�����ϸ����
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s != null && s.length() >= 3) {
					// ��ѯ���ݿ�
					String address = NumberAddressQueryUtils.queryNumber(s
							.toString());
					result.setText(address);
				}
			}

			/**
			 * ���ı������仯ǰ�ص�
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			/**
			 * ���ı������仯֮��ص�
			 */
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void numberAddressQuery(View view) {
		String phone = ed_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(this, "����Ϊ��", 0).show();
			// ����Ϊ��ʱ���õĶ���������С���Ȱڶ�7�Σ�ʵ�ֻζ�Ч����
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			ed_phone.startAnimation(shake);

			// ����Ϊ�գ�����ʾ
			vibrator.vibrate(2000);

			/*
			 * long[] pattern = {200,200,300,300,1000,2000};
			 * pattern������ÿ2��Ϊһ�ԣ���һ����ʱ��
			 * ���ڶ�����ֹͣʱ�䣬exp:��0.2���룬ֹͣ0.2�룬����0.3�룬ֹͣ0.3�룬����1�룬ֹͣ2�룬
			 * vibrator.vibrate(pattern, -1); -1���ظ� 0ѭ���� 1�������ֱ�ʾ�𶯴�����
			 */
			return;
		}else{
			String address = NumberAddressQueryUtils.queryNumber(phone);
			result.setText(address);
		}
	}

	public NumberAddressQueryActivity() {
		// TODO Auto-generated constructor stub

	}

}
