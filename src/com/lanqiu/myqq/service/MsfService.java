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
	private FriendsPacketListener friendsPacketListener;

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
		// ��ȡ���������
		ChatManager chatManager = conn.getChatManager();
		// ͨ������������������ļ���
		chatManager.addChatListener(new ChatManagerListener() {

			@Override
			public void chatCreated(Chat chat, boolean arg1) {
				// ͨ��chat���һ����Ϣ��������������͵���Ϣ���ͺͽ���
				chat.addMessageListener(new MsgListener(MsfService.this,
						mNotificationManager));
			}
		});
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
			// �ж��û��Ƿ��Ѿ���֤
			if (conn.isAuthenticated()) {
				/*
				 * ��֤�ɹ� ���͵�½�ɹ��㲥
				 */
				sendLoginBroadcast(true);
				// ����XMPP���Ӽ��������
				checkConnctionListener = new CheckConnctionListener(this);
				conn.addConnectionListener(checkConnctionListener);
				// ע�����״̬����
				friendsPacketListener = new FriendsPacketListener(this);
				// ���ݰ�����
				PacketFilter filter = new AndFilter(new PacketTypeFilter(
						Presence.class));
				// ��Ӻ���״̬���¼���
				conn.addPacketListener(friendsPacketListener, filter);
				// �����û�״̬
				XmppUtil.setPresence(this, conn,
						PreferencesUtil.getSharePreInt(this, "online_status"));
			}
		} catch (XMPPException e) {
			// ��½ʧ�ܷ��͵�½ʧ�ܹ㲥�����Զ����ٷ���
			sendLoginBroadcast(false);
			stopSelf();
			e.printStackTrace();
		}
	}

	/**
	 * ���͵�½״̬�㲥
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
