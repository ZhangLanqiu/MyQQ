package com.lanqiu.myqq.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.lanqiu.myqq.util.Constant;
import com.lanqiu.myqq.util.PreferencesUtil;
import com.lanqiu.myqq.util.ToastUtil;
import com.lanqiu.myqq.view.LoadingDialog;
import com.lanqiu.myqq.view.TextURLView;

public class LoginActivity extends Activity implements OnClickListener {

	private Context context;
	private LoadingDialog loadingDialog;
	private RelativeLayout rl_user;
	private Button mLogin;
	private Button register;
	private TextURLView mTextViewURL;
	private EditText account;
	private EditText password;
	private String username;
	private String pwd;
	private BroadcastReceiver receiver;

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
		receiver = new BroadcastReceiver() {

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
						// startActivity(new Intent(context,
						// Mainactivity.class));
						finish();

					} else {// 登陆失败
						ToastUtil.showToast(context,
								"登录失败，请检您的网络是否正常以及用户名和密码是否正确");
					}
				}
			}
		};

		// 上诉逻辑处理完后注册广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_IS_LOGIN_SUCCESS);
		// 注册广播
		registerReceiver(receiver, filter);
	}

	/*
	 * 找到所需控件
	 */
	private void findView() {
		rl_user = (RelativeLayout) findViewById(R.id.rl_user);
		mLogin = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);
		mTextViewURL = (TextURLView) findViewById(R.id.tv_forget_password);

		account = (EditText) findViewById(R.id.account);
		password = (EditText) findViewById(R.id.password);
	}

	/*
	 * 加载动画
	 */
	private void init() {
		Animation anim = AnimationUtils.loadAnimation(context,
				R.anim.login_anim);
		// 保留动画后的位置
		anim.setFillAfter(true);
		// 启动动画
		rl_user.startAnimation(anim);
		// 为登陆和注册按钮设置监听
		mLogin.setOnClickListener(this);
		register.setOnClickListener(this);

	}

	private void initTvUrl() {
		mTextViewURL.setText(R.string.forget_password);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:// 登陆逻辑
			dologin();
			break;

		case R.id.register:// 注册逻辑
			// 跳转到注册页面
			// startActivities(new Intent(context,RegisterActivity.class));
			break;
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		// 获取用户名和密码
		String username = PreferencesUtil.getSharePreStr(this, "username");
		String pwd = PreferencesUtil.getSharePreStr(this, "pwd");
		// 接下来进行非空判断并将获取的用户名密码设置进输入框中
		if (!TextUtils.isEmpty(username)) {
			account.setText(username);
		}
		if (!TextUtils.isEmpty(pwd)) {
			password.setText(pwd);
		}
	}

	/*
	 * 登陆方法
	 */
	private void dologin() {
		// 首先获取用户名和密码
		username = account.getText().toString().trim();
		pwd = password.getText().toString().trim();
		// 如果当前用户名或密码为空则Toast提示用户
		if (TextUtils.isEmpty(username)) {
			ToastUtil.showToast(context, "请输入您的账号");
			return;
		}
		if (TextUtils.isEmpty(pwd)) {
			ToastUtil.showToast(context, "请输入您的密码");
			return;
		}
		//将用户名和密码保存到SharedPreferences中进行持久化存储
		PreferencesUtil.putSharePre(context, "username", username);
		PreferencesUtil.putSharePre(context, "pwd", pwd);
		//显示自定义Dialog
		loadingDialog.show();
		//接下来启动核心服务
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//视图销毁时解除广播注册
		unregisterReceiver(receiver);
		
	}

}
