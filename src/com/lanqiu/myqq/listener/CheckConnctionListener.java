package com.lanqiu.myqq.listener;

import org.jivesoftware.smack.ConnectionListener;

import com.lanqiu.myqq.service.MsfService;
import com.lanqiu.myqq.util.ToastUtil;

/**
 * ����һ���������״̬���� ��ʵ�������Ӽ����Ľӿ���д���ڲ�����
 * 
 * @author jh
 *
 */
public class CheckConnctionListener implements ConnectionListener {

	private MsfService context;

	// �������췽��������MsfService�����Ļ���
	public CheckConnctionListener(MsfService context) {
		this.context = context;
	}

	// ���ӹر�
	@Override
	public void connectionClosed() {

	}

	// �����쳣�ر�
	@Override
	public void connectionClosedOnError(Exception e) {
		//���µ�equalsӦ��Ϊ�̶���ʽ
		if (e.getMessage().equals("stream:error (conflict)")) {
			ToastUtil.showToast(context, "�����˺�����ص�¼");
		}
	}

	// ������
	@Override
	public void reconnectingIn(int arg0) {

	}

	// ����ʧ��
	@Override
	public void reconnectionFailed(Exception arg0) {

	}

	// �����ɹ�
	@Override
	public void reconnectionSuccessful() {

	}

}
