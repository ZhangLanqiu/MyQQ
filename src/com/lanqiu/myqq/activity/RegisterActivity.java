package com.lanqiu.myqq.activity;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.lanqiu.myqq.util.ToastUtil;
import com.lanqiu.myqq.util.XmppConnectionManager;
import com.lanqiu.myqq.view.LoadingDialog;
import com.lanqiu.myqq.view.TitleBarView;

public class RegisterActivity extends Activity {

	private Context mContext;
	private XmppConnectionManager xmppConnectionManager;
	private LoadingDialog loadDialog;
	private TitleBarView mTitleBarView;
	private Button btn_complete;
	private EditText et_name;
	private EditText et_password;
	private String account;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		mContext = this;
		// ��ȡXMPP���ӹ�����
		xmppConnectionManager = XmppConnectionManager.getInstance();
		// �����Զ���Ի���
		loadDialog = new LoadingDialog(this);
		// �ҿؼ�
		findView();
		// ��ʼ��TitleView
		initTitleView();
		// ��ʼ��
		init();
	}

	/*
	 * ע��
	 */
	private void init() {
		btn_complete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doReg();
			}
		});
	}

	/*
	 * ע���߼�
	 */
	protected void doReg() {
		//Get account and password input box
		account = et_name.getText().toString().trim();
		password = et_password.getText().toString().trim();
		if(TextUtils.isEmpty(account)){
			ToastUtil.showToast(mContext, "����д�ǳ�");
			return;
		}
		if(TextUtils.isEmpty(password)){
			ToastUtil.showToast(mContext, "����д����");
			return;
		}
		
		loadDialog.setTitle("����ע��...");
		loadDialog.show();
		//����һ��Thread��xmpp�ύע����Ϣ
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//��ȡXMPP���Ӷ���
				XMPPConnection connection = xmppConnectionManager.init();
				//���������������
				try {
					connection.connect();
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
		
	}

	/*
	 * ��ʼ��TitleView
	 */
	private void initTitleView() {
		/*
		 * ���ð�ť���ı�����ʾ����
		 * ������Title����ʾ�ı�
		 * �������
		 */
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.GONE);
		mTitleBarView.setTitleText(R.string.title_register_info);
		mTitleBarView.setBtnLeft(R.drawable.fft, R.string.back);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void findView() {
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		btn_complete = (Button) findViewById(R.id.register_complete);

		et_name = (EditText) findViewById(R.id.name);// �˺�
		et_password = (EditText) findViewById(R.id.password);// ����

	}

}
