package com.lanqiu.myqq.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {

	/**
	 * ��ͨ�ֶδ�ŵ�ַ
	 */
	public static String QQXMPP = "qqxmpp";

	/**
	 * ��ȡps�е�string�ֶ�
	 */
	public static String getSharePreStr(Context context, String field) {
		SharedPreferences sp = context.getSharedPreferences(QQXMPP, 0);
		String s = sp.getString(field, "");
		return s;
	}

	/**
	 * ȡ��whichSp��field�ֶζ�Ӧ��int���͵�ֵ
	 * 
	 * @param mContext
	 * @param field
	 * @return
	 */
	public static int getSharePreInt(Context mContext, String field) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(QQXMPP, 0);
		int i = sp.getInt(field, 0);// ������ֶ�û��Ӧֵ����ȡ��0
		return i;
	}

	/**
	 * ȡ��whichSp��field�ֶζ�Ӧ��boolean���͵�ֵ
	 * 
	 * @param mContext
	 * @param field
	 * @return
	 */
	public static boolean getSharePreBoolean(Context mContext, String field) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(QQXMPP, 0);
		boolean i = sp.getBoolean(field, false);// ������ֶ�û��Ӧֵ����ȡ��0
		return i;
	}

	/**
	 * ����string���͵�value��whichSp�е�field�ֶ�
	 * 
	 * @param mContext
	 * @param field
	 * @param value
	 */
	public static void putSharePre(Context mContext, String field, String value) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(QQXMPP, 0);
		sp.edit().putString(field, value).commit();
	}

	/**
	 * ����int���͵�value��whichSp�е�field�ֶ�
	 * 
	 * @param mContext
	 * @param field
	 * @param value
	 */
	public static void putSharePre(Context mContext, String field, int value) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(QQXMPP, 0);
		sp.edit().putInt(field, value).commit();
	}

	/**
	 * ����boolean���͵�value��whichSp�е�field�ֶ�
	 * 
	 * @param mContext
	 * @param field
	 * @param value
	 */
	public static void putSharePre(Context mContext, String field, Boolean value) {
		SharedPreferences sp = (SharedPreferences) mContext
				.getSharedPreferences(QQXMPP, 0);
		sp.edit().putBoolean(field, value).commit();
	}

	/**
	 * ��ձ��������
	 * 
	 * @param mContext
	 */
	public static void clearSharePre(Context mContext) {
		try {
			SharedPreferences sp = (SharedPreferences) mContext
					.getSharedPreferences(QQXMPP, 0);
			sp.edit().clear().commit();
		} catch (Exception e) {
		}
	}
}
