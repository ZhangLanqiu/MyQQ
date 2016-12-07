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
		// 获取XMPP链接管理器
		xmppConnectionManager = XmppConnectionManager.getInstance();
		// 创建自定义对话框
		loadDialog = new LoadingDialog(this);
		// 找控件
		findView();
		// 初始化TitleView
		initTitleView();
		// 初始化
		init();
	}

	/*
	 * 注册
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
	 * 注册逻辑
	 */
	protected void doReg() {
		//Get account and password input box
		account = et_name.getText().toString().trim();
		password = et_password.getText().toString().trim();
		if(TextUtils.isEmpty(account)){
			ToastUtil.showToast(mContext, "请填写昵称");
			return;
		}
		if(TextUtils.isEmpty(password)){
			ToastUtil.showToast(mContext, "请填写密码");
			return;
		}
		
		loadDialog.setTitle("正在注册...");
		loadDialog.show();
		//开启一个Thread向xmpp提交注册信息
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//获取XMPP连接对象
				XMPPConnection connection = xmppConnectionManager.init();
				//与服务器进行连接
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
	 * 初始化TitleView
	 */
	private void initTitleView() {
		/*
		 * 设置按钮和文本的显示隐藏
		 * 设置左Title的显示文本
		 * 设置左边
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

		et_name = (EditText) findViewById(R.id.name);// 账号
		et_password = (EditText) findViewById(R.id.password);// 密码

	}

}
