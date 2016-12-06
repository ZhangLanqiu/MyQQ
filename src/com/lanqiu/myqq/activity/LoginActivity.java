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
		BroadcastReceiver receiver = new BroadcastReceiver() {

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
						startActivity(new Intent(context, Mainactivity.class));
						finish();

					} else {// ��½ʧ��
						ToastUtil.showToast(context,
								"��¼ʧ�ܣ�������������Ƿ������Լ��û����������Ƿ���ȷ");
					}
				}
			}
		};
		
		//�����߼��������ע��㲥
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_IS_LOGIN_SUCCESS);
		//ע��
		registerReceiver(receiver, filter);
	}

	private void init() {

	}

	private void initTvUrl() {

	}

	private void findView() {

	}

}
