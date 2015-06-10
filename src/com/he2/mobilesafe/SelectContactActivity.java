package com.he2.mobilesafe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SelectContactActivity extends Activity {

	private ListView list_select_contact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contact);

		list_select_contact = (ListView) findViewById(R.id.list_select_contact);
		final List<Map<String, String>> data = getContactInfo();
		// 将获取到的联系人加载到界面
		list_select_contact.setAdapter(new SimpleAdapter(this, data,
				R.layout.contact_item_view, new String[] { "name", "phone" },
				new int[] { R.id.tv_name, R.id.tv_phone }));
		//为列表加监听时间
		list_select_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//list获取方法是get不是直接数组[]这种方式
				String phone = data.get(position).get("phone");
				Intent data = new Intent();
				data.putExtra("phone", phone);
				setResult(0, data);
				//当前页面关闭掉
				finish();
			}
		});
		
	}

	/**
	 * 读取联系人
	 * 
	 * @return 联系人列表
	 */

	private List<Map<String, String>> getContactInfo() {
		// TODO Auto-generated method stub
		// 获取一个存储的数组
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		// 得到一个内容解析器
		ContentResolver resolver = getContentResolver();

		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri uriData = Uri.parse("content://com.android.contacts/data");
		// 读取数据库,从数据库中取得联系人ID
		// Cursor 是每行的集合。他地数据集合相当于dataReader
		Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
				null, null, null);

		while (cursor.moveToNext()) {
			// 获取返回的第一列内容
			String contact_id = cursor.getString(0);

			if (contact_id != null) {
				// 注意这里的实例化是调用hashmap
				// 获取具体某个联系人
				Map<String, String> map = new HashMap<String, String>();

				Cursor dataCursor = resolver.query(uriData, new String[] {
						"data1", "mimetype" }, "contact_id=?",
						new String[] { contact_id }, null);

				while (dataCursor.moveToNext()) {
					String data1 = dataCursor.getString(0);
					String mimetype = dataCursor.getString(1);

					if ("vnd.android.cursor.item/name".equals(mimetype)) {
						// 联系人姓名
						map.put("name", data1);
					} else if ("vnd.android.cursor.item/phone_v2"
							.equals(mimetype)) {
						// 联系人号码
						map.put("phone", data1);
					}

				}
				list.add(map);
				// 释放资源
				dataCursor.close();
			}
		}
		// 释放资源
		cursor.close();
		return list;
	}

	public SelectContactActivity() {
		// TODO Auto-generated constructor stub
	}

}
