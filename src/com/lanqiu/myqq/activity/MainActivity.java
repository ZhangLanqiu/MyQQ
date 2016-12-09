package com.lanqiu.myqq.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lanqiu.myqq.db.ChatMsgDao;
import com.lanqiu.myqq.util.Constant;

public class MainActivity extends Activity {

	private Context mContext;
	private ChatMsgDao chatMsgDao;
	private int msgCount;
	private NewMsgReceiver msgReceiver;
	private View mPopView;
	private ImageButton mConstact;
	private ImageButton mNews;
	private ImageButton mSetting;
	private TextView app_cancle;
	private TextView app_change;
	private TextView app_exit;
	private TextView tv_newmsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		// ����������Ϣ���ݷ��ʶ���
		chatMsgDao = new ChatMsgDao(mContext);
		// ��������Ϣ�㲥����
		msgReceiver = new NewMsgReceiver();
		//������ͼ���˶���
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_NEW_MSG);
		registerReceiver(msgReceiver, filter);
		tv_newmsg = (TextView) findViewById(R.id.tv_newmsg);// ��Ϣ��Ŀ����
		// �ҿؼ�
		findView();
	}

	/*
	 * �ҵ�����ؼ�
	 */
	private void findView() {
		mPopView = LayoutInflater.from(mContext).inflate(R.layout.app_exit,
				null);// pop���ֻ��˵�������

		mNews = (ImageButton) findViewById(R.id.buttom_news);// ��Ϣ
		mConstact = (ImageButton) findViewById(R.id.buttom_constact);// ��ϵ��
		mSetting = (ImageButton) findViewById(R.id.buttom_setting);// ��

		app_cancle = (TextView) mPopView.findViewById(R.id.app_cancle);// ȡ��
		app_change = (TextView) mPopView.findViewById(R.id.app_change_user);// ע���û�
		app_exit = (TextView) mPopView.findViewById(R.id.app_exit);// �˳�
	}

	public class NewMsgReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// ��ʼ����Ϣ������
			initMsgCount();
		}
	}

	/**
	 * ���������δ����Ϣ������ʾ���߼�����
	 */
	public void initMsgCount() {
		// ͨ����Ϣ���ʶ�����ʵ���Ƿ�����Ϣ�����ݲ鿴�ж�����δ����Ϣ������δ������
		msgCount = chatMsgDao.queryAllNotReadCount();
		if (msgCount > 0) {
			tv_newmsg.setText("" + msgCount);
			tv_newmsg.setVisibility(View.VISIBLE);
		} else {
			tv_newmsg.setText("");
			tv_newmsg.setVisibility(View.GONE);
		}
	}

}
