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
 * 此工具类的主要的作用是为当前IM提供增删改查
 * 
 * @author jh
 *
 */
public class XmppUtil {

	/**
	 * 注册的方法 应该都是固定的步骤
	 * 
	 * @param XMPPConnection
	 * @param account
	 * @param password
	 * @return
	 */
	public static int register(XMPPConnection conn, String account,
			String password) {
		// asmack jar包中的注册登记对象
		Registration reg = new Registration();
		// 设置IQ(info query信息查询)的类型
		reg.setType(IQ.Type.SET);
		// 发送到哪台服务器
		reg.setTo(conn.getServiceName());
		// 设置用户名和密码
		reg.setUsername(account);
		reg.setPassword(password);
		// 下面的设置不能为空，否则出错，所以这个标志是Android手机创建的吧(猜测可是移动端pc端标识字段)
		reg.addAttribute("android", "geolo_createUser_android");
		// 创建包过滤器
		PacketFilter filter = new AndFilter(new PacketIDFilter(
				reg.getPacketID()), new PacketTypeFilter(IQ.class));
		// 创建包收集器
		PacketCollector collector = conn.createPacketCollector(filter);
		// 发送包并将注册信息传入
		conn.sendPacket(reg);
		// 通过收集器获取包返回信息
		IQ result = (IQ) collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		// 获取后取消收集
		collector.cancel();
		// 解析来对返回的结果进行判断
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
	 * 查询用户
	 * 
	 * @param mXMPPConnection
	 * @param userName
	 * @return
	 */
	public static List<Session> searchUsers(XMPPConnection conn, String userName) {
		// 创建一个用于存储用户的集合
		List<Session> listUser = new ArrayList<Session>();
		// 创建用户搜索管理器
		UserSearchManager search = new UserSearchManager(conn);
		// 此处已一定要加上search
		try {
			Form searchForm = search.getSearchForm("search."
					+ conn.getServiceName());
			Form answerForm = searchForm.createAnswerForm();
			answerForm.setAnswer("Username", true);
			answerForm.setAnswer("search", userName);
			// 获取搜索结果
			ReportedData data = search.getSearchResults(answerForm, "search."
					+ conn.getServiceName());
			Iterator<Row> it = data.getRows();
			Row row = null;
			while (it.hasNext()) {
				row = it.next();
				// 创建Session对象
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
	 * 更改用户状态
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
		 * Presence也是继承自XMPP基类Packet信息包，Presence主要有两个作用 1)告诉服务器所有客户端当前所处的状态
		 * 2)发出添加删除好友请求
		 */
		Presence presence = null;
		// 判断code更改用户状态
		switch (code) {
		case 0:
			presence = new Presence(Presence.Type.available);// 在线
			break;

		case 1:
			presence = new Presence(Presence.Type.available);// 设置Q我吧
			presence.setMode(Presence.Mode.chat);
			break;

		case 2:
			/*
			 * Roster的作用就是用来表示 一个用户所有好友清单以及申请加好友的用户清单
			 */
			Roster roster = conn.getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			for (RosterEntry entry : entries) {
				presence = new Presence(Presence.Type.unavailable);
				presence.setPacketID(Packet.ID_NOT_AVAILABLE);
				presence.setFrom(conn.getUser());
				presence.setTo(entry.getUser());
			}
			// 向同一用户的其他客户端发送隐身消息
			presence = new Presence(Presence.Type.unavailable);
			presence.setPacketID(Packet.ID_NOT_AVAILABLE);
			presence.setFrom(conn.getUser());
			presence.setTo(StringUtils.parseBareAddress(conn.getUser()));
			break;

		case 3:// 设置忙碌
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.dnd);
			break;
		case 4:// 设置离开
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
	 * 删除当前用户
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
	 * 返回所有的组信息，参数列表中需要传入roster(花名册对象)
	 * 
	 * @param roster
	 * @return
	 */
	public static List<RosterGroup> getGroups(Roster roster) {
		// 创建一个群组集合
		List<RosterGroup> groupList = new ArrayList<RosterGroup>();
		// 获取群组
		Collection<RosterGroup> rosterGroups = roster.getGroups();
		// 遍历并传入结合
		Iterator<RosterGroup> i = rosterGroups.iterator();
		while (i.hasNext())
			groupList.add(i.next());

		return groupList;
	}

	/**
	 * 返回所有群组的用户信息 需要传入花名册和群组名
	 * 
	 * @param roster
	 * @param groupName
	 * @return
	 */
	public static List<RosterEntry> getEntriesByGroup(Roster roster,
			String groupName) {
		// 创建名册条目集合
		List<RosterEntry> entriesList = new ArrayList<RosterEntry>();
		// 获取群组名
		RosterGroup rosterGroup = roster.getGroup(groupName);
		// 获取当前组下的所有条目
		Collection<RosterEntry> entries = rosterGroup.getEntries();
		// 遍历并存入集合
		Iterator<RosterEntry> i = entries.iterator();
		while (i.hasNext()) {
			entriesList.add(i.next());
		}

		return entriesList;
	}

	/**
	 * 返回所有用户信息
	 * 
	 * @param roster
	 * @return
	 */
	public static List<RosterEntry> getAllEntries(Roster roster) {
		// 创建一个存储RosterEntry的集合
		List<RosterEntry> enList = new ArrayList<RosterEntry>();
		Collection<RosterEntry> entries = roster.getEntries();
		Iterator<RosterEntry> i = entries.iterator();
		while (i.hasNext())
			enList.add(i.next());
		return enList;
	}

	/**
	 * 创建一个组 这个方法很简单通过asmack一行代码就可以创建一个群组 前提是需要传入花名册和群组名称
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
	 * 删除一个组
	 * 
	 * @param roster
	 * @param groupName
	 * @return
	 */
	public static boolean removeGroup(Roster roster, String groupName) {
		return false;
	}

	/**
	 * 添加一个好友无分组
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
	 * 添加一个好友到一个分组下
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
	 * 删除一个好友
	 * 
	 * @param roster
	 * @param userJid
	 * @return
	 */
	public static boolean removeUser(Roster roster, String userJid) {
		// 通过用户Jid删除指定用户
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
	 * 把一个好友添加到一个组中
	 * 
	 * @param userJid
	 * @param groupName
	 * @param connection
	 */
	public static void addUserToGroup(final String userJid,
			final String groupName, final XMPPConnection connection) {
		// 获取传入的组名
		RosterGroup group = connection.getRoster().getGroup(groupName);
		// 这个组已经存在就添加到这个组，不存在创建一个组获取添加的好友
		RosterEntry entry = connection.getRoster().getEntry(userJid);
		try {
			if (group != null) {
				if (entry != null) {
					group.addEntry(entry);
				} else {
					// 创建一个新的组
					RosterGroup newGroup = connection.getRoster().createGroup(
							"我的好友");
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
	 * 把一个好友从组中删除
	 * 
	 * @param userJid
	 * @param groupName
	 * @param connection
	 */
	public static void removeUserFromGroup(final String userJid,
			final String groupName, final XMPPConnection connection) {
		// 获取组
		RosterGroup group = connection.getRoster().getGroup(groupName);
		try {
			if (group != null) {
				// 获取好友
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
	 * 修改签名
	 * @param connection
	 * @param code
	 * @param content
	 */
	public static void changeSign(XMPPConnection connection,int code , String content){  
		
	}

}
