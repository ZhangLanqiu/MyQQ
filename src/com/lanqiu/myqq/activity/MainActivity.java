package com.lanqiu.myqq.activity;

import com.lanqiu.myqq.db.ChatMsgDao;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private Context mContext;
	private ChatMsgDao chatMsgDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		//����������Ϣ���ݷ��ʶ���
		chatMsgDao = new ChatMsgDao(mContext);
		
	}

}
