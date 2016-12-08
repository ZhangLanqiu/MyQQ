package com.lanqiu.myqq.listener;

import org.jivesoftware.smack.ConnectionListener;

import com.lanqiu.myqq.service.MsfService;
import com.lanqiu.myqq.util.ToastUtil;

/**
 * 这是一个检查连接状态的类 并实现了连接监听的接口重写其内部方法
 * 
 * @author jh
 *
 */
public class CheckConnctionListener implements ConnectionListener {

	private MsfService context;

	// 创建构造方法并传入MsfService上下文环境
	public CheckConnctionListener(MsfService context) {
		this.context = context;
	}

	// 连接关闭
	@Override
	public void connectionClosed() {

	}

	// 连接异常关闭
	@Override
	public void connectionClosedOnError(Exception e) {
		//以下的equals应该为固定格式
		if (e.getMessage().equals("stream:error (conflict)")) {
			ToastUtil.showToast(context, "您的账号在异地登录");
		}
	}

	// 重连中
	@Override
	public void reconnectingIn(int arg0) {

	}

	// 重连失败
	@Override
	public void reconnectionFailed(Exception arg0) {

	}

	// 重连成功
	@Override
	public void reconnectionSuccessful() {

	}

}
