package com.lanqiu.myqq.activity;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lanqiu.myqq.util.PreferencesUtil;
import com.lanqiu.myqq.util.ToastUtil;
import com.lanqiu.myqq.util.XmppConnectionManager;
import com.lanqiu.myqq.util.XmppUtil;
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

	// 创建Handler
	private final Handler Handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 能执行到这里说明无论成功与否服务器都有了响应信息所以取消显示对话框
			if (loadDialog.isShowing()) {
				loadDialog.dismiss();
			}
			// 通过返回的响应码来判断注册的成功与否
			switch (msg.what) {
			case 0:
				ToastUtil.showToast(mContext, "注册失败");
				break;
			case 1:
				ToastUtil.showToast(mContext, "注册成功，请牢记您的账号和密码");
				//保存
				PreferencesUtil.putSharePre(mContext, "username", account);
				PreferencesUtil.putSharePre(mContext, "pwd", password);
				//关闭当前活动
				finish();
				break;
			case 2:
				ToastUtil.showToast(mContext, "改昵称已被注册");
				break;
			case 3:
				ToastUtil.showToast(mContext, "注册失败");
				break;
			case 4:
				ToastUtil.showToast(mContext, "注册失败,请检查您的网络");
				break;

			default:
				break;
			}
		};
	};

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
		// Get account and password input box
		account = et_name.getText().toString().trim();
		password = et_password.getText().toString().trim();
		if (TextUtils.isEmpty(account)) {
			ToastUtil.showToast(mContext, "请填写昵称");
			return;
		}
		if (TextUtils.isEmpty(password)) {
			ToastUtil.showToast(mContext, "请填写密码");
			return;
		}

		loadDialog.setTitle("正在注册...");
		loadDialog.show();
		// 与服务器连接是一个耗时操作所以开启一个Thread向xmpp提交注册信息
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 获取XMPP连接对象
				XMPPConnection connection = xmppConnectionManager.init();

				try {
					// 建立连接
					connection.connect();
					// 调用XmppUtil的注册方法将用户名和密码传入并返回注册结果
					int result = XmppUtil.register(connection, account,
							password);
					// 将服务器的响应码发送至Handler
					Handler.sendEmptyMessage(result);
				} catch (XMPPException e) {
					e.printStackTrace();
					// 执行到这里说明注册失败所以直接发送结果码: 4
					Handler.sendEmptyMessage(4);
				}
			}
		}).start();

	}

	/*
	 * 初始化TitleView
	 */
	private void initTitleView() {
		/*
		 * 设置按左边按钮和中间的文本显示
		 */
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.GONE);
		mTitleBarView.setTitleText(R.string.title_register_info);
		mTitleBarView.setBtnLeft(R.drawable.fft, R.string.back);
		//相当于返回键
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
