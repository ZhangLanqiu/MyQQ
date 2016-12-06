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
		// �����Զ����loading�Ի���
		loadingDialog = new LoadingDialog(this);
		loadingDialog.setTitle("���ڵ�½...");
		// �ҿؼ�
		findView();
		// ��ʼ��Tvurl
		initTvUrl();
		// ��ʼ��
		init();
		// ��ʼ���㲥
		initReceiver();

	}

	private void initReceiver() {
		// ����һ���㲥
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// �жϵ�½�㲥
				if (intent.getAction().equals(Constant.ACTION_IS_LOGIN_SUCCESS)) {
					if (loadingDialog.isShowing()) {
						// ȡ���Ի���
						loadingDialog.dismiss();
					}
					boolean isLoginSuccess = intent.getBooleanExtra(
							"isLoginSuccess", false);
					// ��½�ɹ�
					if (isLoginSuccess) {
						// Ĭ�Ͽ���������������
						PreferencesUtil.putSharePre(context,
								Constant.ACTION_MSG_IS_VOICE, true);
						PreferencesUtil.putSharePre(context,
								Constant.ACTION_MSG_IS_VIBRATE, true);
						// ��½�ɹ������Mainactivity
						// startActivity(new Intent(context,
						// Mainactivity.class));
						finish();

					} else {// ��½ʧ��
						ToastUtil.showToast(context,
								"��¼ʧ�ܣ�������������Ƿ������Լ��û����������Ƿ���ȷ");
					}
				}
			}
		};

		// �����߼��������ע��㲥
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_IS_LOGIN_SUCCESS);
		// ע��㲥
		registerReceiver(receiver, filter);
	}

	/*
	 * �ҵ�����ؼ�
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
	 * ���ض���
	 */
	private void init() {
		Animation anim = AnimationUtils.loadAnimation(context,
				R.anim.login_anim);
		// �����������λ��
		anim.setFillAfter(true);
		// ��������
		rl_user.startAnimation(anim);
		// Ϊ��½��ע�ᰴť���ü���
		mLogin.setOnClickListener(this);
		register.setOnClickListener(this);

	}

	private void initTvUrl() {
		mTextViewURL.setText(R.string.forget_password);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:// ��½�߼�
			dologin();
			break;

		case R.id.register:// ע���߼�
			// ��ת��ע��ҳ��
			// startActivities(new Intent(context,RegisterActivity.class));
			break;
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		// ��ȡ�û���������
		String username = PreferencesUtil.getSharePreStr(this, "username");
		String pwd = PreferencesUtil.getSharePreStr(this, "pwd");
		// ���������зǿ��жϲ�����ȡ���û����������ý��������
		if (!TextUtils.isEmpty(username)) {
			account.setText(username);
		}
		if (!TextUtils.isEmpty(pwd)) {
			password.setText(pwd);
		}
	}

	/*
	 * ��½����
	 */
	private void dologin() {
		// ���Ȼ�ȡ�û���������
		username = account.getText().toString().trim();
		pwd = password.getText().toString().trim();
		// �����ǰ�û���������Ϊ����Toast��ʾ�û�
		if (TextUtils.isEmpty(username)) {
			ToastUtil.showToast(context, "�����������˺�");
			return;
		}
		if (TextUtils.isEmpty(pwd)) {
			ToastUtil.showToast(context, "��������������");
			return;
		}
		//���û��������뱣�浽SharedPreferences�н��г־û��洢
		PreferencesUtil.putSharePre(context, "username", username);
		PreferencesUtil.putSharePre(context, "pwd", pwd);
		//��ʾ�Զ���Dialog
		loadingDialog.show();
		//�������������ķ���
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//��ͼ����ʱ����㲥ע��
		unregisterReceiver(receiver);
		
	}

}
