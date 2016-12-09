package com.lanqiu.myqq.listener;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import android.content.Intent;
import android.util.Log;

import com.lanqiu.myqq.service.MsfService;
import com.lanqiu.myqq.util.Constant;

public class FriendsPacketListener implements PacketListener {

	private MsfService context;

	public FriendsPacketListener(MsfService context) {
		this.context = context;
	}

	@Override
	public void processPacket(Packet packet) {
		if (packet.getFrom().equals(packet.getTo())) {
			return;
		}
		if (packet instanceof Presence) {
			Presence presence = (Presence) packet;
			final String from = presence.getFrom().split("@")[0];// ���ͷ�
			String to = presence.getTo().split("@")[0];// ���շ�
			if (from.equals(to)) {
				return;
			}
			if (presence.getType().equals(Presence.Type.subscribe)) {// ��������
				Log.e("jj", "��������");
			} else if (presence.getType().equals(Presence.Type.subscribed)) {// ͬ����Ӻ���
				Log.e("jj", "ͬ����Ӻ���");
			} else if (presence.getType().equals(Presence.Type.unsubscribe)) {// �ܾ���Ӻ���
																				// ��
																				// ɾ������
				Log.e("jj", "�ܾ���Ӻ���");
			} else if (presence.getType().equals(Presence.Type.unsubscribed)) {

			} else if (presence.getType().equals(Presence.Type.unavailable)) {// ��������
																				// Ҫ���º����б����������յ����󣬷��㲥��ָ��ҳ��
																				// �����б�
				Log.e("jj", "��������");
				Intent intent = new Intent(
						Constant.ACTION_FRIENDS_ONLINE_STATUST_CHANG);
				intent.putExtra("from", from);
				intent.putExtra("status", 0);
				context.sendBroadcast(intent);
			} else if (presence.getType().equals(Presence.Type.available)) {// ��������
				Log.e("jj", "��������");
				Intent intent = new Intent(
						Constant.ACTION_FRIENDS_ONLINE_STATUST_CHANG);
				intent.putExtra("from", from);
				intent.putExtra("status", 1);
				context.sendBroadcast(intent);
			} else {
				Log.e("jj", "error");
			}
		}
	};

}
