package com.he2.ui;

import com.he2.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * �Զ������Ͽؼ�����2��TextView,1��checkBox,1��View
 * 
 * @author he2 �������Code��ʵ����һ��View����õ�һ�����캯���������xml�ж������õڶ������캯����������������ϵͳ�ǲ����õ�
 */

public class SettingItemView extends RelativeLayout {
	
	private CheckBox cb_status;
	private TextView tv_desc;
	private TextView tv_title;
	//��ȡ�Զ���ؼ����Զ�������
	private  String desc_on;
	private String desc_off;
	/**
	 * ����ʵ������ʱ����ù��캯��
	 * 
	 * @param context
	 */
	public SettingItemView(Context context) {
		super(context);
		iniView(context);
		
		// TODO Auto-generated constructor stub
	}

	/**
	 * ʹ��XML�����ʱ����õĹ��캯��
	 * 
	 * @param context
	 * @param attrs
	 */
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		iniView(context);
		//��һ�������������ռ䣬Ҫ��AndroidManifest����ע��������ռ�
		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.he2.mobilesafe", "title");
		desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.he2.mobilesafe", "desc_on");
		desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.he2.mobilesafe", "desc_off");
		tv_title.setText(title);
		setDesc(desc_off);
	}

	/**
	 * ������캯����������View�������ṩ�����Ļ�����ʽ��һ�㲻����
	 * 
	 * @param context
	 * @param attrs
	 *            �Զ���ؼ���AttributeSet���ԣ�һ���Զ���ؼ�����Щ���������������ⲿ��������̬�ı��
	 * @param defStyle
	 *            ���ڵ�ǰApplication��Activity���õ�Theme�е�Ĭ��Style
	 */
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		iniView(context);
	}



	private void iniView(Context context) {
		// ע������inflate�����һ������root��this������null,root��ָ��������һ��View
		//root A view group that will be the parent. Used to properly inflate the layout_* parameters.
		View.inflate(context, R.layout.setting_item_view, this);
		cb_status = (CheckBox) this.findViewById(R.id.cb_status);
		tv_desc = (TextView) this.findViewById(R.id.tv_desc);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
	}

	/**
	 * ����ؼ��Ƿ�ѡ��
	 * 
	 * @return
	 */
	public boolean isChecked() {
		return cb_status.isChecked();
	}

	public void setChecked(boolean checked) {
		if(checked){
			setDesc(desc_on);
		}else{
			setDesc(desc_off);
		}
		cb_status.setChecked(checked);
	}

	public void setDesc(String text) {
		tv_desc.setText(text);
	}
}
