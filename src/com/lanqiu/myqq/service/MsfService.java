package com.lanqiu.myqq.service;

import java.net.DatagramSocket;
import java.net.SocketException;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.workgroup.ext.history.ChatMetadata;

import com.lanqiu.myqq.listener.CheckConnctionListener;
import com.lanqiu.myqq.listener.FriendsPacketListener;
import com.lanqiu.myqq.listener.MsgListener;
import com.lanqiu.myqq.util.Constant;
import com.lanqiu.myqq.util.PreferencesUtil;
import com.lanqiu.myqq.util.XmppConnectionManager;
import com.lanqiu.myqq.util.XmppUtil;

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
	private FriendsPacketListener friendsPacketListener;

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
		// 获取聊天管理器
		ChatManager chatManager = conn.getChatManager();
		// 通过聊天管理器添加聊天的监听
		chatManager.addChatListener(new ChatManagerListener() {

			@Override
			public void chatCreated(Chat chat, boolean arg1) {
				// 通过chat添加一个消息监听处理各种类型的消息发送和接收
				chat.addMessageListener(new MsgListener(MsfService.this,
						mNotificationManager));
			}
		});
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
			// 判断用户是否已经验证
			if (conn.isAuthenticated()) {
				/*
				 * 验证成功 发送登陆成功广播
				 */
				sendLoginBroadcast(true);
				// 创建XMPP连接监听并添加
				checkConnctionListener = new CheckConnctionListener(this);
				conn.addConnectionListener(checkConnctionListener);
				// 注册好友状态监听
				friendsPacketListener = new FriendsPacketListener(this);
				// 数据包过滤
				PacketFilter filter = new AndFilter(new PacketTypeFilter(
						Presence.class));
				// 添加好友状态更新监听
				conn.addPacketListener(friendsPacketListener, filter);
				// 更改用户状态
				XmppUtil.setPresence(this, conn,
						PreferencesUtil.getSharePreInt(this, "online_status"));
			}
		} catch (XMPPException e) {
			// 登陆失败发送登陆失败广播，并自动销毁服务
			sendLoginBroadcast(false);
			stopSelf();
			e.printStackTrace();
		}
	}

	/**
	 * 发送登陆状态广播
	 * 
	 * @param b
	 */
	private void sendLoginBroadcast(boolean isLoginSuccess) {
		Intent intent = new Intent(Constant.ACTION_IS_LOGIN_SUCCESS);
		intent.putExtra("isLoginSuccess", isLoginSuccess);
		sendBroadcast(intent);
	}

	@Override
	public void onDestroy() {
		try {
			if (mNotificationManager != null) {
				if (conn != null) {
					conn.disconnect();
					conn = null;
				}

				if (mXmppConnectionManager != null) {
					mXmppConnectionManager = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
}
