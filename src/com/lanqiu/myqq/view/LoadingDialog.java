package com.lanqiu.myqq.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lanqiu.myqq.activity.R;

public class LoadingDialog extends Dialog {

	// 取消对话框的标记位
	private boolean canceable = true;
	private TextView tv;

	public LoadingDialog(Context context) {
		super(context,R.style.Dialog_bocop);
		init();
	}

	private void init() {
		// 获取布局文件
		View contentView = View.inflate(getContext(),
				R.layout.activity_custom_loding_dialog_layout, null);
		// 填充进当前对话框
		setContentView(contentView);
		// 对当前对话框添加监听
		contentView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (canceable) {
					dismiss();
				}
			}
		});

		tv = (TextView) findViewById(R.id.tv);
		// 设置动画
		getWindow().setWindowAnimations(R.anim.activity_up);
	}

	// 重写show方法
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}

	@Override
	public void setCancelable(boolean flag) {
		// TODO Auto-generated method stub
		canceable = flag;
		super.setCancelable(flag);
	}

	@Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
		tv.setText(title);
	}

	@Override
	public void setTitle(int titleId) {
		// TODO Auto-generated method stub
		tv.setText(getContext().getString(titleId));
	}
}
