package com.lanqiu.myqq.activity;

import com.lanqiu.myqq.view.LoadingDialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class LoginActivity extends Activity {

	private Context context;
	private LoadingDialog loadingDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = this;
		//创建自定义的loading对话框
		loadingDialog = new LoadingDialog(this);
		loadingDialog.setTitle("正在登陆...");
		//找控件
		findView();
		//初始化Tvurl
		initTvUrl();
		//初始化
		init();
		//初始化广播
		initReceiver();
		
	}
	private void initReceiver() {
		
	}
	private void init() {
		
	}
	private void initTvUrl() {
		
	}
	private void findView() {
		
	}

	
}
