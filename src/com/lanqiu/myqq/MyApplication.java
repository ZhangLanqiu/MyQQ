package com.lanqiu.myqq;

import org.jivesoftware.smack.XMPPConnection;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
	// XMPP连接对象当登陆成功后被赋值后供全局使用（自己的猜测）
	public static XMPPConnection xmppConnction;

	@Override
	public void onCreate() {
		super.onCreate();
		// 初始化ImageLoader全局配置
		initImageLoader(getApplicationContext());
	}

	/*
	 * 初始化ImageLoader
	 */
	public static void initImageLoader(Context context) {
		// 构建ImageLoader环境信息
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)// 设置线程优先级
				.denyCacheImageMultipleSizesInMemory()// 当同一个url获取到不同大小的图片缓存进内存时，只缓存一个！默认全部缓存
				.discCacheFileNameGenerator(new Md5FileNameGenerator())// 设置缓存文件的名字
				.discCacheFileCount(60)// 设置缓存文件的最大个数
				.tasksProcessingOrder(QueueProcessingType.LIFO)// 设置图片下载和显示的工作队列排序，LIFO后进先出,FIFO先进先出
				.build();

		// 获取ImageLoader的实例并初始化配置信息
		ImageLoader.getInstance().init(configuration);

	}
}
