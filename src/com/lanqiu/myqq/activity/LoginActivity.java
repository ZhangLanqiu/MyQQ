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
		//�����Զ����loading�Ի���
		loadingDialog = new LoadingDialog(this);
		loadingDialog.setTitle("���ڵ�½...");
		//�ҿؼ�
		findView();
		//��ʼ��Tvurl
		initTvUrl();
		//��ʼ��
		init();
		//��ʼ���㲥
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
