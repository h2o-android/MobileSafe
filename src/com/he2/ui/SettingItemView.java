package com.he2.ui;

import com.he2.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 自定义的组合控件，有2个TextView,1个checkBox,1个View
 * 
 * @author he2 　如果在Code中实例化一个View会调用第一个构造函数，如果在xml中定义会调用第二个构造函数，而第三个函数系统是不调用的
 */

public class SettingItemView extends RelativeLayout {
	
	private CheckBox cb_status;
	private TextView tv_desc;
	private TextView tv_title;
	//获取自定义控件的自定义属性
	private  String desc_on;
	private String desc_off;
	/**
	 * 代码实例化的时候调用构造函数
	 * 
	 * @param context
	 */
	public SettingItemView(Context context) {
		super(context);
		iniView(context);
		
		// TODO Auto-generated constructor stub
	}

	/**
	 * 使用XML定义的时候调用的构造函数
	 * 
	 * @param context
	 * @param attrs
	 */
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		iniView(context);
		//第一个参数是命名空间，要在AndroidManifest里面注册的命名空间
		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.he2.mobilesafe", "title");
		desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.he2.mobilesafe", "desc_on");
		desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.he2.mobilesafe", "desc_off");
		tv_title.setText(title);
		setDesc(desc_off);
	}

	/**
	 * 这个构造函数的作用是View的子类提供这个类的基础样式，一般不调用
	 * 
	 * @param context
	 * @param attrs
	 *            自定义控件的AttributeSet属性：一个自定义控件的有些属性内容是随着外部条件而动态改变的
	 * @param defStyle
	 *            它在当前Application或Activity所用的Theme中的默认Style
	 */
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		iniView(context);
	}



	private void iniView(Context context) {
		// 注意这里inflate的最后一个参数root是this，不是null,root是指挂在他上一个View
		//root A view group that will be the parent. Used to properly inflate the layout_* parameters.
		View.inflate(context, R.layout.setting_item_view, this);
		cb_status = (CheckBox) this.findViewById(R.id.cb_status);
		tv_desc = (TextView) this.findViewById(R.id.tv_desc);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
	}

	/**
	 * 检验控件是否被选中
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
