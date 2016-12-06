package com.lanqiu.myqq.util;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

/**
 * Xmpp���������ӹ�����
 * 
 * @author jh �Ǹ�����
 */
public class XmppConnectionManager {

	private static XmppConnectionManager xmppConnectionManager;

	// ��̬�����
	static {
		try {
			Class.forName("org.jivesoftware.smack.ReconnectionManager");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private XmppConnectionManager() {

	}

	public static XmppConnectionManager getInstance() {
		if (xmppConnectionManager == null) {
			xmppConnectionManager = new XmppConnectionManager();
		}
		return xmppConnectionManager;
	}

	public XMPPConnection init() {
		Connection.DEBUG_ENABLED = false;
		// ����൱��XML������
		ProviderManager pm = ProviderManager.getInstance();
		configure(pm);
		// �������ӵ�������ַ�Ͷ˿ں�
		ConnectionConfiguration connectionConfig = new ConnectionConfiguration(
				Constant.XMPP_HOST, Constant.XMPP_PORT);
		// connectionConfig.setSASLAuthenticationEnabled(false);//
		// ��ʹ��SASL��֤������Ϊfalse
		// connectionConfig
		// .setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);

		// ������������Զ�����
		connectionConfig.setReconnectionAllowed(true);
		// �����½�ɹ����������״̬
		connectionConfig.setSendPresence(true);
		// �յ����������manual��ʾ��Ҫ����ͬ�⣬accept_all��ʾ����Ҫ����ͬ���Զ����Ϊ����
		Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all);
		// ����XMPP���Ӷ��󲢽�XMPP�������ô���
		XMPPConnection connection = new XMPPConnection(connectionConfig);
		return connection;
	}

	public void configure(ProviderManager pm) {

		pm.addIQProvider("query", "jabber:iq:private",new PrivateDataManager.PrivateDataIQProvider());
		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time",Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",new RosterExchangeProvider());
		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",new MessageEventProvider());
		// Chat State
		pm.addExtensionProvider("active","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
		pm.addExtensionProvider("composing","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
		pm.addExtensionProvider("paused","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
		pm.addExtensionProvider("inactive","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
		pm.addExtensionProvider("gone","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",new XHTMLExtensionProvider());
		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",new GroupChatInvitation.Provider());
		// Service Discovery # Items //���������б�
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",new DiscoverItemsProvider());
		// Service Discovery # Info //ĳһ���������Ϣ
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",new DiscoverInfoProvider());
		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",new MUCUserProvider());
		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",new MUCAdminProvider());
		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",new MUCOwnerProvider());
		// Delayed Delivery
		pm.addExtensionProvider("x", "jabber:x:delay",new DelayInformationProvider());
		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version",Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
		}
		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",new OfflineMessageRequest.Provider());
		// Offline Message Indicator
		pm.addExtensionProvider("offline","http://jabber.org/protocol/offline",new OfflineMessageInfo.Provider());
		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup","http://www.jivesoftware.org/protocol/sharedgroup",new SharedGroupsInfo.Provider());
		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses","http://jabber.org/protocol/address",new MultipleAddressesProvider());
		pm.addIQProvider("si", "http://jabber.org/protocol/si",new StreamInitiationProvider());
		pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",new BytestreamsProvider());
		pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
		pm.addIQProvider("command", "http://jabber.org/protocol/commands",new AdHocCommandDataProvider());
		pm.addExtensionProvider("malformed-action","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.MalformedActionError());
		pm.addExtensionProvider("bad-locale","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.BadLocaleError());
		pm.addExtensionProvider("bad-payload","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.BadPayloadError());
		pm.addExtensionProvider("bad-sessionid","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.BadSessionIDError());
		pm.addExtensionProvider("session-expired","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.SessionExpiredError());

	}
}
