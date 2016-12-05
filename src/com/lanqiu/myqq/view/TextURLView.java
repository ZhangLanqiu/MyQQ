package com.lanqiu.myqq.view;

import com.lanqiu.myqq.activity.R;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TextURLView extends LinearLayout {
	private Context context;
	private TextView url;

	public TextURLView(Context context, AttributeSet attrs) {
		super(context, attrs);
		context = context;
		initView();
	}

	public TextURLView(Context context) {
		super(context);
		context = context;
		initView();
	}

	private void initView() {
		LayoutInflater.from(context)
				.inflate(R.layout.common_url_textview, this);
		url = (TextView) findViewById(R.id.tv_url_view);
	}

	public void setText(int txtRes) {
		// 为当前TextView添加下划线
		url.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		url.setText(txtRes);
	}

	/**
	 * TextURLView的监听事件
	 */
	public void setUrlOnClickListener(OnClickListener clickListener) {
		url.setOnClickListener(clickListener);
	}
}
