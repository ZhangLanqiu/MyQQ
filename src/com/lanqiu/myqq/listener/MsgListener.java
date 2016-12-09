package com.lanqiu.myqq.listener;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.workgroup.ext.history.ChatMetadata;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.lanqiu.myqq.activity.R;
import com.lanqiu.myqq.bean.Msg;
import com.lanqiu.myqq.bean.Session;
import com.lanqiu.myqq.db.ChatMsgDao;
import com.lanqiu.myqq.db.SeesionDao;
import com.lanqiu.myqq.service.MsfService;
import com.lanqiu.myqq.util.Constant;
import com.lanqiu.myqq.util.PreferencesUtil;

/**
 * ���������ù���˼�����Ϣ���м���
 */
public class MsgListener implements MessageListener {

	private MsfService context;
	private NotificationManager notificationManager;
	private KeyguardManager mKeyguardManager;
	private SeesionDao seesionDao;
	private ChatMsgDao chatMsgDao;

	/**
	 * ����һ�����캯��������������msf�������Ķ��� ��֪ͨ����������
	 * 
	 * @param context
	 * @param notificationManager
	 */
	public MsgListener(MsfService context,
			NotificationManager notificationManager) {
		this.context = context;
		this.notificationManager = notificationManager;
		// ��ȡ���̱����������
		mKeyguardManager = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		seesionDao = new SeesionDao(context);
		chatMsgDao = new ChatMsgDao(context);

	}

	@Override
	public void processMessage(Chat chat, Message msg) {
		// ��ȡ��Ϣ��
		String msgBody = msg.getBody();
		if (TextUtils.isEmpty(msgBody))
			return;
		// ������ ������ ��Ϣ���� ��Ϣ���� ����ʱ�� �ָ���Ϊ �d
		String[] msgs = msgBody.split(Constant.SPLIT);
		String to = msgs[0];// �����ߣ���Ȼ���Լ�
		String from = msgs[1];// �����ߣ�˭���㷢����Ϣ
		String msgtype = msgs[2];// ��Ϣ����
		String msgcontent = msgs[3];// ��Ϣ����
		String msgtime = msgs[4];// ��Ϣ����ʱ��

		// ����Session����
		Session session = new Session();
		session.setFrom(from);
		session.setTo(to);
		session.setNotReadCount("");//δ����Ϣ��
		session.setTime(msgtime);
		
		//�������ж���Ϣ������
		if(msgtype.equals(Constant.MSG_TYPE_ADD_FRIEND)){//��Ӻ��ѵ�����
			session.setType(msgtype);//��Ϣ����
			session.setContent(msgcontent);//��Ϣ����
			session.setIsdispose("0");
			//ͨ��SessionDao���Ự�����
			seesionDao.insertSession(session);
		}else if(msgtype.equals(Constant.MSG_TYPE_ADD_FRIEND_SUCCESS)){//�Է�ͬ����Ӻ��ѵ�����
			session.setType(Constant.MSG_TYPE_TEXT);//��Ϣ����Ϊ�ı�
			session.setContent("�����Ѿ��Ǻ�����");
			seesionDao.insertSession(session);//����
			//���͹㲥���º����б�
			Intent intent = new Intent(Constant.ACTION_FRIENDS_ONLINE_STATUST_CHANG);
			context.sendBroadcast(intent);
		}else if(msgtype.equals(Constant.MSG_TYPE_TEXT)){//��ϢΪ�ı�����
			//������Ϣ����
			Msg m = new Msg();
			m.setToUser(to);//�����ߣ���Ȼ���Լ�
			m.setFromUser(from);//˭���͹�����
			m.setIsComing(0);//�Ƿ񵽴�
			m.setContent(msgcontent);//��Ϣ����
			m.setDate(msgtime);//����ʱ��
			m.setIsReaded("0");//�Ƿ��Ѷ�
			m.setType(msgtype);//��Ϣ���ͣ��ı�
			//����MsgDao��
			chatMsgDao.insert(m);
			//��������Ϣ
			sendNewMsg(m);
			
			//���ûỰ����
			session.setType(Constant.MSG_TYPE_TEXT);
			session.setContent(msgcontent);//���ûػ�����
			
			//�ж������ϵ���б��Ƿ��Ѵ��ڼ�¼
			if(seesionDao.isContent(from, to)){//from  
				//������¼�¼
				seesionDao.updateSession(session);
			}else{
				//��Ļ������µļ�¼
				seesionDao.insertSession(session);
			}
		}else if(msgtype.equals(Constant.MSG_TYPE_IMG)){//��Ϣ����ΪͼƬ
			//������Ϣʵ����
			Msg m = new Msg();
			m.setToUser(to);//�����ߣ�self
			m.setFromUser(from);//who send
			m.setIsComing(0);//�Ƿ�һ����
			m.setContent(msgcontent);//Ҫ���͵�����
			m.setDate(msgtime);//���͵�ʱ��
			m.setIsReaded("0");//�Ƿ��Ѷ�
			m.setType(msgtype);//��Ϣ����
			//����Ϣ�������ݿ�
			chatMsgDao.insert(m);
			//send the new message
			sendNewMsg(m);
			session.setType(Constant.MSG_TYPE_IMG);
			session.setContent("[ͼƬ]");
			if(seesionDao.isContent(from, to)){//�鿴��ϵ���б����Ƿ��Ѵ��ڼ�¼
				seesionDao.updateSession(session);
			}else{
				seesionDao.insertSession(session);
			}
		}else if(msgtype.equals(Constant.MSG_TYPE_LOCATION)){//λ����Ϣ
			//������Ϣ����
			Msg m = new Msg();
			m.setToUser(to); //���������Լ�
			m.setFromUser(from);//who send
			m.setIsComing(0);//�Ƿ��ʹ�
			m.setContent(msgcontent);//��������
			m.setDate(msgtime);//����ʱ��
			m.setIsReaded("0");//�Ƿ��Ѷ�
			m.setType(msgtype);//������Ϣ����
			//�������ݿ�
			chatMsgDao.insert(m);
			//send the new message
			sendNewMsg(m);
			//���ûỰ����
			session.setType(msgtype);
			//���ûỰ����
			session.setContent(msgcontent);
			if(seesionDao.isContent(from, to)){//�ж���ϵ���б����Ƿ��Ѵ��ڼ�¼
				seesionDao.updateSession(session);
			}else{
				seesionDao.insertSession(session);
			}
		}
		
		//���͹㲥֪ͨ��Ϣ�������
		Intent intent = new Intent(Constant.ACTION_ADDFRIEND);
		context.sendBroadcast(intent);
		//��ʾ֪ͨ
		showNotice(session.getFrom()+":"+session.getContent());

	}

	/**
	 * ��������Ϣ�߼�
	 * @param m
	 */
	private void sendNewMsg(Msg m) {
		//���͹㲥����Ϣ�������
		Intent intent = new Intent(Constant.ACTION_NEW_MSG);
		//��Ҫ���͵���Ϣ����Bundle�н���ͨ��Intent����
		Bundle bundle = new Bundle();
		bundle.putSerializable("msg", m);
		intent.putExtra("msg", bundle);
		context.sendBroadcast(intent);
		
		
	}
	/**
	 *��֪ͨ����ʾ֪ͨ����
	 * @param string
	 */
	private void showNotice(String content) {
		//����֪ͨ��
		CharSequence tickerText = content;
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
		.setSmallIcon(R.drawable.ic_notice)
		.setContentText(tickerText)
		.setWhen(System.currentTimeMillis());
		
		Notification notification = mBuilder.build();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		if(PreferencesUtil.getSharePreBoolean(context, Constant.ACTION_MSG_IS_VOICE)){
			//����Ĭ������
			notification.defaults |= Notification.DEFAULT_SOUND;
		}
		if(PreferencesUtil.getSharePreBoolean(context, Constant.ACTION_MSG_IS_VIBRATE)){
			//�趨��
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		
		//LED��
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.ledARGB = 0xff00ff00;
		notification.ledOnMS = 500;
		notification.ledOffMS = 1000;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notificationManager.notify(Constant.NOTIFY_ID, notification);
	}
}
