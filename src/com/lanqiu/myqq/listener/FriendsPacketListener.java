package com.lanqiu.myqq.listener;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

import com.lanqiu.myqq.service.MsfService;

public class FriendsPacketListener implements PacketListener {

	private MsfService context;

	public FriendsPacketListener(MsfService context) {
		this.context = context;
	}
	
	@Override
	public void processPacket(Packet arg0) {
		///
	}

}
