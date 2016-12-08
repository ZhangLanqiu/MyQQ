package com.lanqiu.myqq.service;

import java.net.DatagramSocket;
import java.net.SocketException;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import com.lanqiu.myqq.listener.CheckConnctionListener;
import com.lanqiu.myqq.util.Constant;
import com.lanqiu.myqq.util.PreferencesUtil;
import com.lanqiu.myqq.util.XmppConnectionManager;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * 核心服务
 * 
 * @author jh
 *
 */
public class MsfService extends Service {

	public static MsfService mInstance = null;
	// 创建MyBinder实例并通过onBinder方法return
	private final IBinder binder = new MyBinder();
	private String mUsername;
	private String mPassword;
	private DatagramSocket ds;
	private NotificationManager mNotificationManager;
	private XmppConnectionManager mXmppConnectionManager;
	private XMPPConnection conn;
	private CheckConnctionListener checkConnctionListener;

	/*
	 * 创建一个内部类继承Binder
	 */
	public class MyBinder extends Binder {
		// 获取msf服务
		public MsfService getService() {
			return MsfService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	/*
	 * 重写onCreate方法
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		// 获取Msf上下文对象并赋给msf变量供全局使用
		mInstance = this;
		// 获取用户名和密码
		mUsername = PreferencesUtil.getSharePreStr(this, "username");
		mPassword = PreferencesUtil.getSharePreStr(this, "pwd");
		try {
			// 创建数据报套接字对象
			ds = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 获取通知栏管理器
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// 获取Xmpp连接管理器
		mXmppConnectionManager = XmppConnectionManager.getInstance();
		// 初始化 xmpp TASK
		initXMPPTask();

	}

	// 创建一个静态方法返回当前服务的上下文
	public static MsfService getInstance() {
		return mInstance;
	}

	/*
	 * 由于初始化XMPP是耗时操作所以开启一个线程处理
	 */
	private void initXMPPTask() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 初始化Xmpp
				try {
					initXMPP();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 初始化XMPP的具体逻辑
	 */
	protected void initXMPP() {
		// 初始化连接对象
		conn = mXmppConnectionManager.init();
		// 登陆到XMPP
		loginXMPP();

	}

	/**
	 * 登陆XMPP的具体逻辑
	 */
	private void loginXMPP() {
		try {
			// 再次获取用户密码
			mPassword = PreferencesUtil.getSharePreStr(this, "pwd");
			// 建立连接
			conn.connect();
			// 如果为空的话，移除连接监听
			if (checkConnctionListener != null) {
				conn.removeConnectionListener(checkConnctionListener);
				checkConnctionListener = null;
			}
			// 进行登录
			conn.login(mUsername, mPassword);
			//判断用户是否已经验证
			if(conn.isAuthenticated()){
				/*
				 * 验证成功
				 * 发送登陆成功广播
				 */
				sendLoginBroadcast(true);
			}
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 发送登陆状态广播
	 * @param b
	 */
	private void sendLoginBroadcast(boolean isLoginSuccess) {
		Intent intent = new Intent(Constant.ACTION_IS_LOGIN_SUCCESS);
		intent.putExtra("isLoginSuccess", isLoginSuccess);
		sendBroadcast(intent);
	}
}
