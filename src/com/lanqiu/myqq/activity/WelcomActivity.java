package com.lanqiu.myqq.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class WelcomActivity extends Activity {
	private Context context;
	private ImageView iv_welcome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcom);
		context = this;
		//�ҿؼ�
		findView();
		init();
		
	}

	private void init() {
		//������ӳ���ת
		iv_welcome.postDelayed(new Runnable() {
			public void run() {
				startActivity(new Intent(context,LoginActivity.class));
				finish();
			}
		}, 2000);
		
	}

	/*
	 * �ҳ�����ؼ�
	 */
	private void findView() {
		iv_welcome = (ImageView) findViewById(R.id.iv_welcome);
		
	}

}
