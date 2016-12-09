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
 * 这个类的作用顾名思义对消息进行监听
 */
public class MsgListener implements MessageListener {

	private MsfService context;
	private NotificationManager notificationManager;
	private KeyguardManager mKeyguardManager;
	private SeesionDao seesionDao;
	private ChatMsgDao chatMsgDao;

	/**
	 * 创建一个构造函数接收两个参数msf的上下文对象 和通知管理器对象
	 * 
	 * @param context
	 * @param notificationManager
	 */
	public MsgListener(MsfService context,
			NotificationManager notificationManager) {
		this.context = context;
		this.notificationManager = notificationManager;
		// 获取键盘保护管理服务
		mKeyguardManager = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		seesionDao = new SeesionDao(context);
		chatMsgDao = new ChatMsgDao(context);

	}

	@Override
	public void processMessage(Chat chat, Message msg) {
		// 获取消息体
		String msgBody = msg.getBody();
		if (TextUtils.isEmpty(msgBody))
			return;
		// 接收者 发送者 消息类型 消息内容 发送时间 分隔符为 卍
		String[] msgs = msgBody.split(Constant.SPLIT);
		String to = msgs[0];// 接收者，当然是自己
		String from = msgs[1];// 发送者，谁给你发的消息
		String msgtype = msgs[2];// 消息类型
		String msgcontent = msgs[3];// 消息内容
		String msgtime = msgs[4];// 消息发送时间

		// 创建Session对象
		Session session = new Session();
		session.setFrom(from);
		session.setTo(to);
		session.setNotReadCount("");//未读消息数
		session.setTime(msgtime);
		
		//接下来判断消息的类型
		if(msgtype.equals(Constant.MSG_TYPE_ADD_FRIEND)){//添加好友的请求
			session.setType(msgtype);//消息类型
			session.setContent(msgcontent);//消息内容
			session.setIsdispose("0");
			//通过SessionDao将会话框插入
			seesionDao.insertSession(session);
		}else if(msgtype.equals(Constant.MSG_TYPE_ADD_FRIEND_SUCCESS)){//对方同意添加好友的请求
			session.setType(Constant.MSG_TYPE_TEXT);//消息类型为文本
			session.setContent("我们已经是好友了");
			seesionDao.insertSession(session);//插入
			//发送广播更新好友列表
			Intent intent = new Intent(Constant.ACTION_FRIENDS_ONLINE_STATUST_CHANG);
			context.sendBroadcast(intent);
		}else if(msgtype.equals(Constant.MSG_TYPE_TEXT)){//消息为文本类型
			//创建消息对象
			Msg m = new Msg();
			m.setToUser(to);//接收者，当然是自己
			m.setFromUser(from);//谁发送过来的
			m.setIsComing(0);//是否到达
			m.setContent(msgcontent);//消息内容
			m.setDate(msgtime);//发送时间
			m.setIsReaded("0");//是否已读
			m.setType(msgtype);//消息类型，文本
			//插入MsgDao中
			chatMsgDao.insert(m);
			//发送新消息
			sendNewMsg(m);
			
			//设置会话类型
			session.setType(Constant.MSG_TYPE_TEXT);
			session.setContent(msgcontent);//设置回话内容
			
			//判断最近联系人列表是否已存在记录
			if(seesionDao.isContent(from, to)){//from  
				//有则更新记录
				seesionDao.updateSession(session);
			}else{
				//否的话插入新的记录
				seesionDao.insertSession(session);
			}
		}else if(msgtype.equals(Constant.MSG_TYPE_IMG)){//消息类型为图片
			//创建消息实体类
			Msg m = new Msg();
			m.setToUser(to);//接收者，self
			m.setFromUser(from);//who send
			m.setIsComing(0);//是否一到达
			m.setContent(msgcontent);//要发送的内容
			m.setDate(msgtime);//发送的时间
			m.setIsReaded("0");//是否已读
			m.setType(msgtype);//消息类型
			//将消息插入数据库
			chatMsgDao.insert(m);
			//send the new message
			sendNewMsg(m);
			session.setType(Constant.MSG_TYPE_IMG);
			session.setContent("[图片]");
			if(seesionDao.isContent(from, to)){//查看联系人列表中是否已存在记录
				seesionDao.updateSession(session);
			}else{
				seesionDao.insertSession(session);
			}
		}else if(msgtype.equals(Constant.MSG_TYPE_LOCATION)){//位置信息
			//创建消息对象
			Msg m = new Msg();
			m.setToUser(to); //接收者是自己
			m.setFromUser(from);//who send
			m.setIsComing(0);//是否送达
			m.setContent(msgcontent);//设置内容
			m.setDate(msgtime);//发送时间
			m.setIsReaded("0");//是否已读
			m.setType(msgtype);//设置消息类型
			//插入数据库
			chatMsgDao.insert(m);
			//send the new message
			sendNewMsg(m);
			//设置会话类型
			session.setType(msgtype);
			//设置会话内容
			session.setContent(msgcontent);
			if(seesionDao.isContent(from, to)){//判断联系人列表中是否已存在记录
				seesionDao.updateSession(session);
			}else{
				seesionDao.insertSession(session);
			}
		}
		
		//发送广播通知消息界面更新
		Intent intent = new Intent(Constant.ACTION_ADDFRIEND);
		context.sendBroadcast(intent);
		//显示通知
		showNotice(session.getFrom()+":"+session.getContent());

	}

	/**
	 * 发送新消息逻辑
	 * @param m
	 */
	private void sendNewMsg(Msg m) {
		//发送广播到消息聊天界面
		Intent intent = new Intent(Constant.ACTION_NEW_MSG);
		//将要发送的消息存入Bundle中进行通过Intent传送
		Bundle bundle = new Bundle();
		bundle.putSerializable("msg", m);
		intent.putExtra("msg", bundle);
		context.sendBroadcast(intent);
		
		
	}
	/**
	 *在通知栏显示通知内容
	 * @param string
	 */
	private void showNotice(String content) {
		//更新通知栏
		CharSequence tickerText = content;
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
		.setSmallIcon(R.drawable.ic_notice)
		.setContentText(tickerText)
		.setWhen(System.currentTimeMillis());
		
		Notification notification = mBuilder.build();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		if(PreferencesUtil.getSharePreBoolean(context, Constant.ACTION_MSG_IS_VOICE)){
			//设置默认声音
			notification.defaults |= Notification.DEFAULT_SOUND;
		}
		if(PreferencesUtil.getSharePreBoolean(context, Constant.ACTION_MSG_IS_VIBRATE)){
			//设定震动
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		
		//LED灯
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.ledARGB = 0xff00ff00;
		notification.ledOnMS = 500;
		notification.ledOffMS = 1000;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notificationManager.notify(Constant.NOTIFY_ID, notification);
	}
}
