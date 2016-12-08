package com.lanqiu.myqq.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lanqiu.myqq.activity.R;

public class LoadingDialog extends Dialog {

	// ȡ���Ի���ı��λ
	private boolean canceable = true;
	private TextView tv;

	public LoadingDialog(Context context) {
		super(context,R.style.Dialog_bocop);
		init();
	}

	private void init() {
		// ��ȡ�����ļ�
		View contentView = View.inflate(getContext(),
				R.layout.activity_custom_loding_dialog_layout, null);
		// ������ǰ�Ի���
		setContentView(contentView);
		// �Ե�ǰ�Ի�����Ӽ���
		contentView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (canceable) {
					dismiss();
				}
			}
		});

		tv = (TextView) findViewById(R.id.tv);
		// ���ö���
		getWindow().setWindowAnimations(R.anim.activity_up);
	}

	// ��дshow����
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
