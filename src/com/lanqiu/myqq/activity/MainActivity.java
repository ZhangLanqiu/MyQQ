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
		// 创建聊天信息数据访问对象
		chatMsgDao = new ChatMsgDao(mContext);
		// 创建新消息广播对象
		msgReceiver = new NewMsgReceiver();
		//创建意图过滤对象
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_NEW_MSG);
		registerReceiver(msgReceiver, filter);
		tv_newmsg = (TextView) findViewById(R.id.tv_newmsg);// 消息数目提醒
		// 找控件
		findView();
	}

	/*
	 * 找到所需控件
	 */
	private void findView() {
		mPopView = LayoutInflater.from(mContext).inflate(R.layout.app_exit,
				null);// pop，手机菜单键弹出

		mNews = (ImageButton) findViewById(R.id.buttom_news);// 消息
		mConstact = (ImageButton) findViewById(R.id.buttom_constact);// 联系人
		mSetting = (ImageButton) findViewById(R.id.buttom_setting);// 我

		app_cancle = (TextView) mPopView.findViewById(R.id.app_cancle);// 取消
		app_change = (TextView) mPopView.findViewById(R.id.app_change_user);// 注销用户
		app_exit = (TextView) mPopView.findViewById(R.id.app_exit);// 退出
	}

	public class NewMsgReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 初始化消息的数量
			initMsgCount();
		}
	}

	/**
	 * 在里面进行未读消息数量提示的逻辑处理
	 */
	public void initMsgCount() {
		// 通过消息访问对象其实就是访问消息表内容查看有多少条未读信息并返回未读数量
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
