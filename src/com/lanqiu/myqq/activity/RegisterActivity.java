package com.lanqiu.myqq.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class RegisterActivity extends Activity {

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		mContext = this;
		//获取XMPP链接管理
	}

}
