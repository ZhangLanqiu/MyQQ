package com.lanqiu.myqq.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;

import android.content.Context;

import com.lanqiu.myqq.bean.Session;

/**
 * �˹��������Ҫ��������Ϊ��ǰIM�ṩ��ɾ�Ĳ�
 * 
 * @author jh
 *
 */
public class XmppUtil {

	/**
	 * ע��ķ��� Ӧ�ö��ǹ̶��Ĳ���
	 * 
	 * @param XMPPConnection
	 * @param account
	 * @param password
	 * @return
	 */
	public static int register(XMPPConnection conn, String account,
			String password) {
		// asmack jar���е�ע��ǼǶ���
		Registration reg = new Registration();
		// ����IQ(info query��Ϣ��ѯ)������
		reg.setType(IQ.Type.SET);
		// ���͵���̨������
		reg.setTo(conn.getServiceName());
		// �����û���������
		reg.setUsername(account);
		reg.setPassword(password);
		// ��������ò���Ϊ�գ�����������������־��Android�ֻ������İ�(�²�����ƶ���pc�˱�ʶ�ֶ�)
		reg.addAttribute("android", "geolo_createUser_android");
		// ������������
		PacketFilter filter = new AndFilter(new PacketIDFilter(
				reg.getPacketID()), new PacketTypeFilter(IQ.class));
		// �������ռ���
		PacketCollector collector = conn.createPacketCollector(filter);
		// ���Ͱ�����ע����Ϣ����
		conn.sendPacket(reg);
		// ͨ���ռ�����ȡ��������Ϣ
		IQ result = (IQ) collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		// ��ȡ��ȡ���ռ�
		collector.cancel();
		// �������Է��صĽ�������ж�
		if (result == null) {
			return 0;
		} else if (result.getType() == IQ.Type.RESULT) {
			return 1;
		} else {
			if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
				return 2;
			} else {
				return 3;
			}
		}
	}

	/**
	 * ��ѯ�û�
	 * 
	 * @param mXMPPConnection
	 * @param userName
	 * @return
	 */
	public static List<Session> searchUsers(XMPPConnection conn, String userName) {
		// ����һ�����ڴ洢�û��ļ���
		List<Session> listUser = new ArrayList<Session>();
		// �����û�����������
		UserSearchManager search = new UserSearchManager(conn);
		// �˴���һ��Ҫ����search
		try {
			Form searchForm = search.getSearchForm("search."
					+ conn.getServiceName());
			Form answerForm = searchForm.createAnswerForm();
			answerForm.setAnswer("Username", true);
			answerForm.setAnswer("search", userName);
			// ��ȡ�������
			ReportedData data = search.getSearchResults(answerForm, "search."
					+ conn.getServiceName());
			Iterator<Row> it = data.getRows();
			Row row = null;
			while (it.hasNext()) {
				row = it.next();
				// ����Session����
				Session session = new Session();
				session.setFrom(row.getValues("Username").next().toString());
				listUser.add(session);
			}

		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listUser;
	}

	/**
	 * �����û�״̬
	 * 
	 * @param context
	 * @param con
	 * @param code
	 */
	public static void setPresence(Context context, XMPPConnection conn,
			int code) {
		if (conn == null)
			return;
		/*
		 * PresenceҲ�Ǽ̳���XMPP����Packet��Ϣ����Presence��Ҫ���������� 1)���߷��������пͻ��˵�ǰ������״̬
		 * 2)�������ɾ����������
		 */
		Presence presence = null;
		// �ж�code�����û�״̬
		switch (code) {
		case 0:
			presence = new Presence(Presence.Type.available);// ����
			break;

		case 1:
			presence = new Presence(Presence.Type.available);// ����Q�Ұ�
			presence.setMode(Presence.Mode.chat);
			break;

		case 2:
			/*
			 * Roster�����þ���������ʾ һ���û����к����嵥�Լ�����Ӻ��ѵ��û��嵥
			 */
			Roster roster = conn.getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			for (RosterEntry entry : entries) {
				presence = new Presence(Presence.Type.unavailable);
				presence.setPacketID(Packet.ID_NOT_AVAILABLE);
				presence.setFrom(conn.getUser());
				presence.setTo(entry.getUser());
			}
			// ��ͬһ�û��������ͻ��˷���������Ϣ
			presence = new Presence(Presence.Type.unavailable);
			presence.setPacketID(Packet.ID_NOT_AVAILABLE);
			presence.setFrom(conn.getUser());
			presence.setTo(StringUtils.parseBareAddress(conn.getUser()));
			break;

		case 3:// ����æµ
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.dnd);
			break;
		case 4:// �����뿪
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.away);
			break;
		case 5:
			presence = new Presence(Presence.Type.unavailable);
			break;

		default:
			break;
		}
		if (presence != null) {
			presence.setStatus(PreferencesUtil.getSharePreStr(context, "sign"));
			conn.sendPacket(presence);
		}

	}

	/**
	 * ɾ����ǰ�û�
	 * 
	 * @param connection
	 * @return
	 */
	public static boolean deleteAccount(XMPPConnection connection) {
		try {
			connection.getAccountManager().deleteAccount();
			return true;
		} catch (XMPPException e) {
			return false;
		}

	}

	/**
	 * �������е�����Ϣ�������б�����Ҫ����roster(���������)
	 * 
	 * @param roster
	 * @return
	 */
	public static List<RosterGroup> getGroups(Roster roster) {
		// ����һ��Ⱥ�鼯��
		List<RosterGroup> groupList = new ArrayList<RosterGroup>();
		// ��ȡȺ��
		Collection<RosterGroup> rosterGroups = roster.getGroups();
		// ������������
		Iterator<RosterGroup> i = rosterGroups.iterator();
		while (i.hasNext())
			groupList.add(i.next());

		return groupList;
	}

	/**
	 * ��������Ⱥ����û���Ϣ ��Ҫ���뻨�����Ⱥ����
	 * 
	 * @param roster
	 * @param groupName
	 * @return
	 */
	public static List<RosterEntry> getEntriesByGroup(Roster roster,
			String groupName) {
		// ����������Ŀ����
		List<RosterEntry> entriesList = new ArrayList<RosterEntry>();
		// ��ȡȺ����
		RosterGroup rosterGroup = roster.getGroup(groupName);
		// ��ȡ��ǰ���µ�������Ŀ
		Collection<RosterEntry> entries = rosterGroup.getEntries();
		// ���������뼯��
		Iterator<RosterEntry> i = entries.iterator();
		while (i.hasNext()) {
			entriesList.add(i.next());
		}

		return entriesList;
	}

	/**
	 * ���������û���Ϣ
	 * 
	 * @param roster
	 * @return
	 */
	public static List<RosterEntry> getAllEntries(Roster roster) {
		// ����һ���洢RosterEntry�ļ���
		List<RosterEntry> enList = new ArrayList<RosterEntry>();
		Collection<RosterEntry> entries = roster.getEntries();
		Iterator<RosterEntry> i = entries.iterator();
		while (i.hasNext())
			enList.add(i.next());
		return enList;
	}

	/**
	 * ����һ���� ��������ܼ�ͨ��asmackһ�д���Ϳ��Դ���һ��Ⱥ�� ǰ������Ҫ���뻨�����Ⱥ������
	 * 
	 * @param roster
	 * @param groupName
	 * @return
	 */
	public static boolean addGroup(Roster roster, String groupName) {
		try {
			roster.createGroup(groupName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ɾ��һ����
	 * 
	 * @param roster
	 * @param groupName
	 * @return
	 */
	public static boolean removeGroup(Roster roster, String groupName) {
		return false;
	}

	/**
	 * ���һ�������޷���
	 * 
	 * @param roster
	 * @param userName
	 * @param name
	 * @return
	 */
	public static boolean addUser(Roster roster, String userName, String name) {
		try {
			roster.createEntry(userName, name, null);
			return true;
		} catch (XMPPException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ���һ�����ѵ�һ��������
	 * 
	 * @param roster
	 * @param userName
	 * @param name
	 * @param groupName
	 * @return
	 */
	public static boolean addUsers(Roster roster, String userName, String name,
			String groupName) {
		try {
			roster.createEntry(userName, name, new String[] { groupName });
			return true;
		} catch (XMPPException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ɾ��һ������
	 * 
	 * @param roster
	 * @param userJid
	 * @return
	 */
	public static boolean removeUser(Roster roster, String userJid) {
		// ͨ���û�Jidɾ��ָ���û�
		try {
			RosterEntry entry = roster.getEntry(userJid);
			roster.removeEntry(entry);
			return true;
		} catch (XMPPException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ��һ��������ӵ�һ������
	 * 
	 * @param userJid
	 * @param groupName
	 * @param connection
	 */
	public static void addUserToGroup(final String userJid,
			final String groupName, final XMPPConnection connection) {
		// ��ȡ���������
		RosterGroup group = connection.getRoster().getGroup(groupName);
		// ������Ѿ����ھ���ӵ�����飬�����ڴ���һ�����ȡ��ӵĺ���
		RosterEntry entry = connection.getRoster().getEntry(userJid);
		try {
			if (group != null) {
				if (entry != null) {
					group.addEntry(entry);
				} else {
					// ����һ���µ���
					RosterGroup newGroup = connection.getRoster().createGroup(
							"�ҵĺ���");
					if (entry != null) {
						newGroup.addEntry(entry);
					}
				}
			}
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ��һ�����Ѵ�����ɾ��
	 * 
	 * @param userJid
	 * @param groupName
	 * @param connection
	 */
	public static void removeUserFromGroup(final String userJid,
			final String groupName, final XMPPConnection connection) {
		// ��ȡ��
		RosterGroup group = connection.getRoster().getGroup(groupName);
		try {
			if (group != null) {
				// ��ȡ����
				RosterEntry entry = connection.getRoster().getEntry(userJid);
				if (entry != null) 
					group.removeEntry(entry);
			}
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * �޸�ǩ��
	 * @param connection
	 * @param code
	 * @param content
	 */
	public static void changeSign(XMPPConnection connection,int code , String content){  
		
	}

}
