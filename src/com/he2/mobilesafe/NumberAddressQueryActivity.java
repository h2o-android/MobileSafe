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
	// 系统提供的震动服务
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_address_query);
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		result = (TextView) findViewById(R.id.result);
		// 实例化系统震动服务
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		// 注意这个监听事件是textwatcher，而不是之前惯性思维的on什么的
		ed_phone.addTextChangedListener(new TextWatcher() {

			/**
			 * 文本框发生变化时回调，s表示内容，start表示从哪里开始，
			 * before表示从start开始的count个字符替换长度为before的旧文本 count表示替换地长度 笔记有详细介绍
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s != null && s.length() >= 3) {
					// 查询数据库
					String address = NumberAddressQueryUtils.queryNumber(s
							.toString());
					result.setText(address);
				}
			}

			/**
			 * 当文本发生变化前回调
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			/**
			 * 当文本发生变化之后回调
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
			Toast.makeText(this, "号码为空", 0).show();
			// 号码为空时调用的动画（左右小幅度摆动7次，实现晃动效果）
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			ed_phone.startAnimation(shake);

			// 号码为空，震动提示
			vibrator.vibrate(2000);

			/*
			 * long[] pattern = {200,200,300,300,1000,2000};
			 * pattern里面是每2个为一对，第一个震动时间
			 * ，第二个是停止时间，exp:震动0.2毫秒，停止0.2秒，又震动0.3秒，停止0.3秒，又震动1秒，停止2秒，
			 * vibrator.vibrate(pattern, -1); -1不重复 0循环振动 1以上数字表示震动次数；
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
