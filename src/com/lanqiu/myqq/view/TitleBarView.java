package com.lanqiu.myqq.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lanqiu.myqq.activity.R;
import com.lanqiu.myqq.util.SystemMethod;

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
		// ���ز���
		LayoutInflater.from(mContext).inflate(R.layout.common_title_bar, this);
		btnLeft = (Button) findViewById(R.id.title_btn_left);
		btnRight = (Button) findViewById(R.id.title_btn_right);
		btn_titleLeft = (Button) findViewById(R.id.constact_group);
		btn_titleRight = (Button) findViewById(R.id.constact_all);
		tv_center = (TextView) findViewById(R.id.title_txt);
		common_constact = (LinearLayout) findViewById(R.id.common_constact);
	}

	/**
	 * ������Щ������Title����ʾ����
	 */
	public void setCommonTitle(int LeftVisibility, int centerVisibility,
			int center1Visibilter, int rightVisibility) {
		btnLeft.setVisibility(LeftVisibility);
		btnRight.setVisibility(rightVisibility);
		tv_center.setVisibility(centerVisibility);
		common_constact.setVisibility(center1Visibilter);
	}

	/**
	 * ������߰�ť��ͼƬ���ı�
	 */
	public void setBtnLeft(int icon, int txtRes) {
		// ͨ�������Ļ�ȡһ��Drawable����
		Drawable img = mContext.getResources().getDrawable(icon);
		int height = SystemMethod.dip2px(mContext, 20);
		int width = img.getIntrinsicWidth() * height / img.getIntrinsicHeight();
		img.setBounds(0, 0, width, height);
		btnLeft.setText(txtRes);
		btnLeft.setCompoundDrawables(img, null, null, null);

	}

	/**
	 * �����ı��ķ���
	 */
	public void setBtnLeft(int txtRes) {
		btnLeft.setText(txtRes);
	}

	/**
	 * �����ұߵ��ı�
	 */
	public void setBtnRight(int icon, int txtRes) {
		// ��ȡDrawable����
		Drawable img = mContext.getResources().getDrawable(icon);
		int height = SystemMethod.dip2px(mContext, 30);
		int width = img.getIntrinsicWidth() * height / img.getIntrinsicHeight();
		img.setBounds(0, 0, width, height);
		if(txtRes!=0){
			btnRight.setText(txtRes);
		}
		btnRight.setCompoundDrawables(img, null, null, null);
	}

	/*
	 * һ�·��������ÿ���ͨ��resid��ֱ�Ӵ����ַ����ķ�ʽ���������ı�
	 */
	public void setTitleLeft(int resId) {
		btn_titleLeft.setText(resId);
	}

	public void setTitleLeft(String res) {
		btnLeft.setText(res);
	}

	public void setTitleRight(int resId) {
		btn_titleRight.setText(resId);
	}

	public void setTitleRight(String res) {
		btnRight.setText(res);
	}

	public void setPopWindow(PopupWindow mPopupWindow, TitleBarView titleBarView) {
		// ���ñ���
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#E9E9E9")));
		//������ʾλ��
		mPopupWindow.showAsDropDown(titleBarView, 0, -15);
		//���ý�������
		mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
		////��ȡpopwindow����
		mPopupWindow.setFocusable(true);
		//����popwindow�������������򣬱�رա�
		mPopupWindow.setOutsideTouchable(true);
		//����
		mPopupWindow.update();
		
		//�����Ұ�ť��ͼƬ
		setBtnRight(R.drawable.skin_conversation_title_right_btn_selected, 0);
		
	}
	
	/*
	 * �����м��textView�ı�
	 */
	public void setTitleText(int txtRes){
		tv_center.setText(txtRes);
	}
	
	/*
	 * ������߰�ť���ұ߰�ť�ļ����ص�
	 */
	public void setBtnLeftOnclickListener(OnClickListener listener){
		btnLeft.setOnClickListener(listener);
	}
	
	public void setBtnRightOnclickListener(OnClickListener listener){
		btnRight.setOnClickListener(listener);
	}
	
	/*
	 * ��ȡ���Ұ�ť������
	 */
	public Button getTitleLeft(){
		return btn_titleLeft;
	}
	
	public Button getTitleRight(){
		return btn_titleRight;
	}
	
	/*
	 * ����ʱ��ť���м���ı��ÿ�
	 */
	public void destoryView(){
		btnLeft.setText(null);
		btnRight.setText(null);
		tv_center.setText(null);
	}

}
