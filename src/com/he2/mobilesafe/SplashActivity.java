package com.he2.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.he2.utils.WebUtils;

public class SplashActivity extends Activity {

	protected static final String TAG = "SplashActivity";
	protected static final int SHOW_UPDATE_DIALOG = 0;
	protected static final int ENTER_HOME = 1;
	protected static final int URL_ERROR = 2;
	protected static final int NETWORK_ERROR = 3;
	protected static final int JSON_ERROR = 4;
	private TextView tv_splash_version;
	private String description;
	private TextView tv_update_info;

	/**
	 * �°汾���ص�ַ�洢����
	 */
	private String apkurl;

	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("�汾�ţ�" + getVersionName());
		tv_update_info = (TextView) findViewById(R.id.tv_update_info);
		boolean update = sp.getBoolean("update", true);
		
		//���ƹ��������ݿ⵽ϵͳ
		copyDB();
		if (update) {
			checkUpdate();
		} else {
			// �Զ������ѹر�,���ö�ʱ��2�룬ʵ�ָ��õĶ���Ч��
			//����ֱ�������߳�sleep����Ϊ�޷���ʾ����
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					enterHome();
				}
			}, 2000);
		}
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(500);
		findViewById(R.id.rl_root_splash).startAnimation(aa);
	}
	
	/**
	 * ���ƹ��������ݿ⵽��Ŀ¼�µ�file����
	 */
	private void copyDB(){
		File file = new File(getFilesDir(),"address.db");
		if(file.exists() && file.length()>0){//�ж��ļ��Ƿ���ڣ����ڼ����ø��Ƶڶ���
		
		}else{
			try {
				InputStream is = getAssets().open("address.db");
				byte[] buffer = new byte[1024];
				FileOutputStream fos = new FileOutputStream(file);
				int len = 0;
				while((len=is.read(buffer))!=-1){
					fos.write(buffer,0,len);
				}
				is.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {

			case ENTER_HOME:// ���������ݣ�ֱ�ӽ�����ҳ
				enterHome();
				break;

			case SHOW_UPDATE_DIALOG:// ��ʾ�����Ի���
				showUpdateDialog();
				break;

			case URL_ERROR:// URL����
				enterHome();
				Toast.makeText(getApplicationContext(), "URL����", 0).show();
				break;

			case NETWORK_ERROR:// �����쳣
				enterHome();
				Toast.makeText(SplashActivity.this, "�����쳣", 0).show();
				break;

			case JSON_ERROR:// ��������
				enterHome();
				Toast.makeText(SplashActivity.this, "JSON��������", 0).show();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * ������ҳ����
	 */
	private void enterHome() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * ���������Ի���
	 */
	private void showUpdateDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(SplashActivity.this);
		builder.setTitle("������ʾ");
		// builder.setCancelable(false);�Ի����޷�ȡ����ǿ���������
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				// ȡ�������ĶԻ��������������
				enterHome();
				// Ҫ�ѶԻ�������
				dialog.dismiss();
			}
		});
		builder.setMessage(description);
		builder.setPositiveButton("��������", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// ���ȷ��������APP,����װ
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {// �ж�SD���Ƿ����
					/*
					 * Afinal��һ��orm��ioc��ܣ���ѭԼ����������ԭ�������κ����ü���������й�����
					 * ��Ҳ����ͨ�����ôﵽ���˵ĸ��Ի�����Afinal�ᳫ������ټ�࣬����һ�д�����ɵ����鲻�������С�
					 * 
					 * Afinal����Ŀǰ�������Ĵ������ FinalHttp����������http���ݣ�ֱ��ajax��ʽ�����ļ��ϴ���
					 * �ϵ����������ļ��� FinalBitmap��������ʾbitmapͼƬ�������迼���̲߳�����oom�����⡣
					 * FinalActivity����ȫ����ͨ��ע�ⷽʽ�󶨿ؼ����¼��������д���롣
					 * FinalDb��android��sqlite��orm��ܣ�һ�д���㶨��ɾ�Ĳ顣
					 */

					FinalHttp finalhttp = new FinalHttp();
					finalhttp.download(
							apkurl,
							Environment.getExternalStorageDirectory()
									.getAbsolutePath()
									+ "/"
									+ apkurl.substring(apkurl.lastIndexOf("/"),
											apkurl.length()),//���ﲻ��ʹ��length-1,��Ϊ������ǳ��ȣ����Ǵ�0��ʼ
							new AjaxCallBack<File>() {

								public void onFailure(Throwable t, int errorNo,
										String strMsg) {
									t.printStackTrace();
									Toast.makeText(getApplicationContext(),
											"����ʧ��", 0).show();
									enterHome();
									super.onFailure(t, errorNo, strMsg);
								};

								public void onLoading(long count, long current) {
									super.onLoading(count, current);
									tv_update_info.setVisibility(View.VISIBLE);
									// ��ʾ���ؽ��Ȱٷֱ�
									int progress = (int) (current * 100 / count);
									tv_update_info.setText("���ؽ��ȣ�" + progress
											+ "%");
								};

								public void onStart() {
								};

								public void onSuccess(File t) {
									super.onSuccess(t);
									installApk(t);
								}

								/**
								 * ��װ���
								 * 
								 * @param t
								 */
								private void installApk(File t) {
									// TODO Auto-generated method stub
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
									intent.addCategory("android.intent.category.DEFAULT");// ִ��action�ĸ�����Ϣ
									// ע�⣬�������ñ�����������������Ȼ����data��type�ֱ������ã����ᵼ������һ��Ϊ��
									intent.setDataAndType(Uri.fromFile(t),
											"application/vnd.android.package-archive");
									//startActivity(intent);
									//���û�ȡ����װ��ֱ�ӽ�����ҳ
									startActivityForResult(intent,100);
								}

							});

				} else {
					Toast.makeText(getApplicationContext(), "û��sdcard���밲װ������",
							0).show();
					return;
				}
			}
		});
		builder.setNegativeButton("ȡ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				enterHome();
			}
		});
		builder.show();
	}
	

	/**
	 * ���û��ڰ�װ�Ľ�����ȡ����ʱ��Ӧ�ü������뵽������
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode==100 && resultCode==RESULT_CANCELED) {
			enterHome();
		}
	}
	
	/**
	 * ����Ƿ����µİ汾�����������ʾ����
	 */
	private void checkUpdate() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {

				Message msg = Message.obtain();
				long startTime = System.currentTimeMillis();

				try {
					// ��ȡ������µ�ַ
					URL url = new URL(getString(R.string.serverUrl));
					// ������������
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					// �����������ͣ�ע�⣬����ֻ��д��д��GET,POST
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(4000);
					// ��ȡ���ص�����
					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream is = conn.getInputStream();
						// ����ת����String
						String result = WebUtils.readFromStream(is, null);
						// JSON����
						JSONObject obj = new JSONObject(result);
						String version = (String) obj.get("version");
						description = (String) obj.get("description");
						apkurl = (String) obj.get("apkurl");

						if (getVersionName().equals(version)) {
							msg.what = ENTER_HOME;
						} else {
							msg.what = SHOW_UPDATE_DIALOG;
						}
					}
				} catch (MalformedURLException e) {
					// TODO: handle exception
					// ����쳣��URL�쳣
					msg.what = URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// ��������쳣���������쳣
					msg.what = NETWORK_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO: handle exception
					// jSON�쳣��������������
					msg.what = JSON_ERROR;
					e.printStackTrace();
				} finally {

					long endTime = System.currentTimeMillis();
					// ��ʱ������ӳɹ�
					long dTime = endTime - startTime;
					// ������죬Ϊ�˶���Ч����Ӧ����Ϊ�ӳ�ʱ��
					if (dTime < 2000) {
						try {
							Thread.sleep(2000 - dTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	/**
	 * �õ�����汾��
	 * 
	 * @return �����Ǹ�String���͵İ汾��
	 */
	private String getVersionName() {
		// TODO Auto-generated method stub
		PackageManager pm = getPackageManager();

		try {
			// ��ȡ��ǰAPP�İ汾��
			// getPacgeInfo��2�������ֱ���Ӧ�ð��������������������кܶ����ã�ͨ������Ϊ0��
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
			return "";
		}

		// return null;
	}

	public SplashActivity() {
		// TODO Auto-generated constructor stub
	}

}
