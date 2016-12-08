package com.lanqiu.myqq;

import org.jivesoftware.smack.XMPPConnection;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
	// XMPP���Ӷ��󵱵�½�ɹ��󱻸�ֵ��ȫ��ʹ�ã��Լ��Ĳ²⣩
	public static XMPPConnection xmppConnction;

	@Override
	public void onCreate() {
		super.onCreate();
		// ��ʼ��ImageLoaderȫ������
		initImageLoader(getApplicationContext());
	}

	/*
	 * ��ʼ��ImageLoader
	 */
	public static void initImageLoader(Context context) {
		// ����ImageLoader������Ϣ
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)// �����߳����ȼ�
				.denyCacheImageMultipleSizesInMemory()// ��ͬһ��url��ȡ����ͬ��С��ͼƬ������ڴ�ʱ��ֻ����һ����Ĭ��ȫ������
				.discCacheFileNameGenerator(new Md5FileNameGenerator())// ���û����ļ�������
				.discCacheFileCount(60)// ���û����ļ���������
				.tasksProcessingOrder(QueueProcessingType.LIFO)// ����ͼƬ���غ���ʾ�Ĺ�����������LIFO����ȳ�,FIFO�Ƚ��ȳ�
				.build();

		// ��ȡImageLoader��ʵ������ʼ��������Ϣ
		ImageLoader.getInstance().init(configuration);

	}
}
