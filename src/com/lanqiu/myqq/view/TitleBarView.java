package com.lanqiu.myqq.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lanqiu.myqq.activity.R;

public class TitleBarView extends RelativeLayout {

	private Context mContext;
	private Button btnLeft;
	private Button btnRight;
	private Button btn_titleLeft;
	private Button btn_titleRight;
	private TextView tv_center;
	private LinearLayout common_constact;

	public TitleBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}


	public TitleBarView(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	private void initView() {
		//���ز���
		LayoutInflater.from(mContext).inflate(R.layout.common_title_bar, this);
		btnLeft=(Button) findViewById(R.id.title_btn_left);
		btnRight=(Button) findViewById(R.id.title_btn_right);
		btn_titleLeft=(Button) findViewById(R.id.constact_group);
		btn_titleRight=(Button) findViewById(R.id.constact_all);
		tv_center=(TextView) findViewById(R.id.title_txt);
		common_constact=(LinearLayout) findViewById(R.id.common_constact);
	}
	
	/**
	 * ������Щ������Title����ʾ����
	 */
	public void setCommonTitle(int LeftVisibility,int centerVisibility,int center1Visibilter,int rightVisibility){
		btnLeft.setVisibility(LeftVisibility);
		btnRight.setVisibility(rightVisibility);
		tv_center.setVisibility(centerVisibility);
		common_constact.setVisibility(center1Visibilter);
	}
	
	/**
	 * ������߰�ť��ͼƬ���ı�
	 */
	public void setBtnLeft(int icon,int txtRes){
		//ͨ�������Ļ�ȡһ��Drawable����
		Drawable img = mContext.getResources().getDrawable(icon);
	}
}
