package com.lanqiu.myqq.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ���ݿ������ ������һ������ģʽ
 * 
 * @author jh
 *
 */
public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "qq_byl";// ���ݿ�����
	private static final int DB_VERSION = 1;// ���ݿ�汾
	private static DBHelper mInstance;// ��ǰ���ݿ����

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public DBHelper(Context context, int version) {
		super(context, DB_NAME, null, version);
	}

	public synchronized static DBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DBHelper(context);
		}
		return mInstance;
	};

	@Override
	public void onCreate(SQLiteDatabase db) {
		/**
		 * �����¼
		 */
		String sql_msg = "Create table IF NOT EXISTS " + DBcolumns.TABLE_MSG
				+ "(" + DBcolumns.MSG_ID + " integer primary key autoincrement,"
				+ DBcolumns.MSG_FROM + " text," 
				+ DBcolumns.MSG_TO + " text," 
				+ DBcolumns.MSG_TYPE + " text,"
				+ DBcolumns.MSG_CONTENT + " text,"
				+ DBcolumns.MSG_ISCOMING + " integer,"
				+ DBcolumns.MSG_DATE + " text,"
				+ DBcolumns.MSG_ISREADED + " text,"
				+ DBcolumns.MSG_BAK1 + " text,"
				+ DBcolumns.MSG_BAK2 + " text,"
				+ DBcolumns.MSG_BAK3 + " text,"
				+ DBcolumns.MSG_BAK4 + " text,"
				+ DBcolumns.MSG_BAK5 + " text,"
				+ DBcolumns.MSG_BAK6 + " text);";
		
		/**
		 * �����ϵ��
		 */
		String sql_session = "Create table IF NOT EXISTS "
				+ DBcolumns.TABLE_SESSION + "(" + DBcolumns.SESSION_id + " integer primary key AUTOINCREMENT,"
				+ DBcolumns.SESSION_FROM + " text,"
				+ DBcolumns.SESSION_TYPE + " text,"
				+ DBcolumns.SESSION_TIME + " text,"
				+ DBcolumns.SESSION_TO + " text,"
				+ DBcolumns.SESSION_CONTENT + " text,"
				+ DBcolumns.SESSION_ISDISPOSE + " text);";
		
		String sql_notice = "Create table IF NOT EXISTS "
				+ DBcolumns.TABLE_SYS_NOTICE + "(" + DBcolumns.SYS_NOTICE_ID + " integer primary key AUTOINCREMENT,"
				+ DBcolumns.SYS_NOTICE_TYPE + " text,"
				+ DBcolumns.SYS_NOTICE_FROM + " text,"
				+ DBcolumns.SYS_NOTICE_FROM_HEAD + " text,"
				+ DBcolumns.SYS_NOTICE_CONTENT + " text,"
				+ DBcolumns.SYS_NOTICE_ISDISPOSE + " text);";
		
		db.execSQL(sql_msg);
		db.execSQL(sql_session);
		db.execSQL(sql_notice);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
