package com.he2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FocusedTextView extends TextView {

	public FocusedTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	//������ƻ�ý��㣬��������Ƶ�Ч���Ϳ�����Ч
	public boolean isFocused() {
		return true;
	}
	
	
}
