package com.lanqiu.myqq.activity;

import com.lanqiu.myqq.util.Constant;
import com.lanqiu.myqq.util.PreferencesUtil;
import com.lanqiu.myqq.util.ToastUtil;
import com.lanqiu.myqq.view.LoadingDialog;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
		// 创建自定义的loading对话框
		loadingDialog = new LoadingDialog(this);
		loadingDialog.setTitle("正在登陆...");
		// 找控件
		findView();
		// 初始化Tvurl
		initTvUrl();
		// 初始化
		init();
		// 初始化广播
		initReceiver();

	}

	private void initReceiver() {
		// 创建一个广播
		BroadcastReceiver receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// 判断登陆广播
				if (intent.getAction().equals(Constant.ACTION_IS_LOGIN_SUCCESS)) {
					if (loadingDialog.isShowing()) {
						// 取消对话框
						loadingDialog.dismiss();
					}
					boolean isLoginSuccess = intent.getBooleanExtra(
							"isLoginSuccess", false);
					// 登陆成功
					if (isLoginSuccess) {
						// 默认开启声音和震动提醒
						PreferencesUtil.putSharePre(context,
								Constant.ACTION_MSG_IS_VOICE, true);
						PreferencesUtil.putSharePre(context,
								Constant.ACTION_MSG_IS_VIBRATE, true);
						// 登陆成功后进入Mainactivity
						startActivity(new Intent(context, Mainactivity.class));
						finish();

					} else {// 登陆失败
						ToastUtil.showToast(context,
								"登录失败，请检您的网络是否正常以及用户名和密码是否正确");
					}
				}
			}
		};
		
		//上诉逻辑处理完后注册广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_IS_LOGIN_SUCCESS);
		//注册
		registerReceiver(receiver, filter);
	}

	private void init() {

	}

	private void initTvUrl() {

	}

	private void findView() {

	}

}
