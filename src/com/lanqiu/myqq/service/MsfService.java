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
 * ���ķ���
 * 
 * @author jh
 *
 */
public class MsfService extends Service {

	public static MsfService mInstance = null;
	// ����MyBinderʵ����ͨ��onBinder����return
	private final IBinder binder = new MyBinder();
	private String mUsername;
	private String mPassword;
	private DatagramSocket ds;
	private NotificationManager mNotificationManager;
	private XmppConnectionManager mXmppConnectionManager;
	private XMPPConnection conn;
	private CheckConnctionListener checkConnctionListener;

	/*
	 * ����һ���ڲ���̳�Binder
	 */
	public class MyBinder extends Binder {
		// ��ȡmsf����
		public MsfService getService() {
			return MsfService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	/*
	 * ��дonCreate����
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		// ��ȡMsf�����Ķ��󲢸���msf������ȫ��ʹ��
		mInstance = this;
		// ��ȡ�û���������
		mUsername = PreferencesUtil.getSharePreStr(this, "username");
		mPassword = PreferencesUtil.getSharePreStr(this, "pwd");
		try {
			// �������ݱ��׽��ֶ���
			ds = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ��ȡ֪ͨ��������
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// ��ȡXmpp���ӹ�����
		mXmppConnectionManager = XmppConnectionManager.getInstance();
		// ��ʼ�� xmpp TASK
		initXMPPTask();

	}

	// ����һ����̬�������ص�ǰ�����������
	public static MsfService getInstance() {
		return mInstance;
	}

	/*
	 * ���ڳ�ʼ��XMPP�Ǻ�ʱ�������Կ���һ���̴߳���
	 */
	private void initXMPPTask() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// ��ʼ��Xmpp
				try {
					initXMPP();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * ��ʼ��XMPP�ľ����߼�
	 */
	protected void initXMPP() {
		// ��ʼ�����Ӷ���
		conn = mXmppConnectionManager.init();
		// ��½��XMPP
		loginXMPP();

	}

	/**
	 * ��½XMPP�ľ����߼�
	 */
	private void loginXMPP() {
		try {
			// �ٴλ�ȡ�û�����
			mPassword = PreferencesUtil.getSharePreStr(this, "pwd");
			// ��������
			conn.connect();
			// ���Ϊ�յĻ����Ƴ����Ӽ���
			if (checkConnctionListener != null) {
				conn.removeConnectionListener(checkConnctionListener);
				checkConnctionListener = null;
			}
			// ���е�¼
			conn.login(mUsername, mPassword);
			//�ж��û��Ƿ��Ѿ���֤
			if(conn.isAuthenticated()){
				/*
				 * ��֤�ɹ�
				 * ���͵�½�ɹ��㲥
				 */
				sendLoginBroadcast(true);
			}
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ���͵�½״̬�㲥
	 * @param b
	 */
	private void sendLoginBroadcast(boolean isLoginSuccess) {
		Intent intent = new Intent(Constant.ACTION_IS_LOGIN_SUCCESS);
		intent.putExtra("isLoginSuccess", isLoginSuccess);
		sendBroadcast(intent);
	}
}
