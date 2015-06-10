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
	 * 新版本下载地址存储变量
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
		tv_splash_version.setText("版本号：" + getVersionName());
		tv_update_info = (TextView) findViewById(R.id.tv_update_info);
		boolean update = sp.getBoolean("update", true);
		
		//复制归属地数据库到系统
		copyDB();
		if (update) {
			checkUpdate();
		} else {
			// 自动升级已关闭,设置定时器2秒，实现更好的动画效果
			//不能直接让主线程sleep，因为无法显示动画
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
	 * 复制归属地数据库到包目录下的file下面
	 */
	private void copyDB(){
		File file = new File(getFilesDir(),"address.db");
		if(file.exists() && file.length()>0){//判断文件是否存在，存在即不用复制第二次
		
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

			case ENTER_HOME:// 无升级内容，直接进入主页
				enterHome();
				break;

			case SHOW_UPDATE_DIALOG:// 显示升级对话框
				showUpdateDialog();
				break;

			case URL_ERROR:// URL错误
				enterHome();
				Toast.makeText(getApplicationContext(), "URL错误", 0).show();
				break;

			case NETWORK_ERROR:// 网络异常
				enterHome();
				Toast.makeText(SplashActivity.this, "网络异常", 0).show();
				break;

			case JSON_ERROR:// 解析错误
				enterHome();
				Toast.makeText(SplashActivity.this, "JSON解析出错", 0).show();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 进入主页方法
	 */
	private void enterHome() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 弹出升级对话框
	 */
	private void showUpdateDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(SplashActivity.this);
		builder.setTitle("升级提示");
		// builder.setCancelable(false);对话框无法取消，强制升级软件
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				// 取消升级的对话框则进入主界面
				enterHome();
				// 要把对话框消掉
				dialog.dismiss();
			}
		});
		builder.setMessage(description);
		builder.setPositiveButton("立即升级", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// 点击确定后下载APP,并安装
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {// 判断SD卡是否挂载
					/*
					 * Afinal是一个orm、ioc框架，遵循约定大于配置原则，无需任何配置即可完成所有工作，
					 * 但也可以通过配置达到个人的个性化需求。Afinal提倡代码快速简洁，尽量一行代码完成的事情不会用两行。
					 * 
					 * Afinal里面目前包含了四大组件： FinalHttp：用于请求http数据，直接ajax方式请求，文件上传，
					 * 断点续传下载文件等 FinalBitmap：用于显示bitmap图片，而无需考虑线程并发和oom等问题。
					 * FinalActivity：完全可以通过注解方式绑定控件和事件，无需编写代码。
					 * FinalDb：android中sqlite的orm框架，一行代码搞定增删改查。
					 */

					FinalHttp finalhttp = new FinalHttp();
					finalhttp.download(
							apkurl,
							Environment.getExternalStorageDirectory()
									.getAbsolutePath()
									+ "/"
									+ apkurl.substring(apkurl.lastIndexOf("/"),
											apkurl.length()),//这里不能使用length-1,因为他算的是长度，不是从0开始
							new AjaxCallBack<File>() {

								public void onFailure(Throwable t, int errorNo,
										String strMsg) {
									t.printStackTrace();
									Toast.makeText(getApplicationContext(),
											"下载失败", 0).show();
									enterHome();
									super.onFailure(t, errorNo, strMsg);
								};

								public void onLoading(long count, long current) {
									super.onLoading(count, current);
									tv_update_info.setVisibility(View.VISIBLE);
									// 显示下载进度百分比
									int progress = (int) (current * 100 / count);
									tv_update_info.setText("下载进度：" + progress
											+ "%");
								};

								public void onStart() {
								};

								public void onSuccess(File t) {
									super.onSuccess(t);
									installApk(t);
								}

								/**
								 * 安装软件
								 * 
								 * @param t
								 */
								private void installApk(File t) {
									// TODO Auto-generated method stub
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
									intent.addCategory("android.intent.category.DEFAULT");// 执行action的附加信息
									// 注意，这里设置必须调用这个方法，不然调用data和type分别来设置，都会导致另外一个为空
									intent.setDataAndType(Uri.fromFile(t),
											"application/vnd.android.package-archive");
									//startActivity(intent);
									//当用户取消安装后，直接进入主页
									startActivityForResult(intent,100);
								}

							});

				} else {
					Toast.makeText(getApplicationContext(), "没有sdcard，请安装上在试",
							0).show();
					return;
				}
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

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
	 * 当用户在安装的界面点击取消的时候，应该继续进入到主界面
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
	 * 检查是否有新的版本，如果有则提示更新
	 */
	private void checkUpdate() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {

				Message msg = Message.obtain();
				long startTime = System.currentTimeMillis();

				try {
					// 获取网络更新地址
					URL url = new URL(getString(R.string.serverUrl));
					// 建立网络连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					// 设置请求类型，注意，这里只能写大写。GET,POST
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(4000);
					// 获取返回的类型
					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream is = conn.getInputStream();
						// 把流转换成String
						String result = WebUtils.readFromStream(is, null);
						// JSON解析
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
					// 这个异常是URL异常
					msg.what = URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// 输入输出异常，即网络异常
					msg.what = NETWORK_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO: handle exception
					// jSON异常，可能内容有误
					msg.what = JSON_ERROR;
					e.printStackTrace();
				} finally {

					long endTime = System.currentTimeMillis();
					// 耗时多久连接成功
					long dTime = endTime - startTime;
					// 如果过快，为了动画效果，应当人为延长时间
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
	 * 得到软件版本号
	 * 
	 * @return 返回是个String类型的版本号
	 */
	private String getVersionName() {
		// TODO Auto-generated method stub
		PackageManager pm = getPackageManager();

		try {
			// 获取当前APP的版本号
			// getPacgeInfo的2个参数分别是应用包名和条件，条件可以有很多设置，通常设置为0。
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
