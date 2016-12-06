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
		// 加载布局
		LayoutInflater.from(mContext).inflate(R.layout.common_title_bar, this);
		btnLeft = (Button) findViewById(R.id.title_btn_left);
		btnRight = (Button) findViewById(R.id.title_btn_right);
		btn_titleLeft = (Button) findViewById(R.id.constact_group);
		btn_titleRight = (Button) findViewById(R.id.constact_all);
		tv_center = (TextView) findViewById(R.id.title_txt);
		common_constact = (LinearLayout) findViewById(R.id.common_constact);
	}

	/**
	 * 设置这些公共的Title的显示隐藏
	 */
	public void setCommonTitle(int LeftVisibility, int centerVisibility,
			int center1Visibilter, int rightVisibility) {
		btnLeft.setVisibility(LeftVisibility);
		btnRight.setVisibility(rightVisibility);
		tv_center.setVisibility(centerVisibility);
		common_constact.setVisibility(center1Visibilter);
	}

	/**
	 * 设置左边按钮的图片和文本
	 */
	public void setBtnLeft(int icon, int txtRes) {
		// 通过上下文获取一个Drawable对象
		Drawable img = mContext.getResources().getDrawable(icon);
		int height = SystemMethod.dip2px(mContext, 20);
		int width = img.getIntrinsicWidth() * height / img.getIntrinsicHeight();
		img.setBounds(0, 0, width, height);
		btnLeft.setText(txtRes);
		btnLeft.setCompoundDrawables(img, null, null, null);

	}

	/**
	 * 设置文本的方法
	 */
	public void setBtnLeft(int txtRes) {
		btnLeft.setText(txtRes);
	}

	/**
	 * 设置右边的文本
	 */
	public void setBtnRight(int icon, int txtRes) {
		// 获取Drawable对象
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
	 * 一下方法的作用可以通过resid或直接传入字符串的方式进行设置文本
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
		// 设置背景
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#E9E9E9")));
		//设置显示位置
		mPopupWindow.showAsDropDown(titleBarView, 0, -15);
		//设置进出动画
		mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
		////获取popwindow焦点
		mPopupWindow.setFocusable(true);
		//设置popwindow如果点击外面区域，便关闭。
		mPopupWindow.setOutsideTouchable(true);
		//更新
		mPopupWindow.update();
		
		//设置右按钮的图片
		setBtnRight(R.drawable.skin_conversation_title_right_btn_selected, 0);
		
	}
	
	/*
	 * 设置中间的textView文本
	 */
	public void setTitleText(int txtRes){
		tv_center.setText(txtRes);
	}
	
	/*
	 * 设置左边按钮和右边按钮的监听回调
	 */
	public void setBtnLeftOnclickListener(OnClickListener listener){
		btnLeft.setOnClickListener(listener);
	}
	
	public void setBtnRightOnclickListener(OnClickListener listener){
		btnRight.setOnClickListener(listener);
	}
	
	/*
	 * 获取左右按钮的引用
	 */
	public Button getTitleLeft(){
		return btn_titleLeft;
	}
	
	public Button getTitleRight(){
		return btn_titleRight;
	}
	
	/*
	 * 销毁时按钮和中间的文本置空
	 */
	public void destoryView(){
		btnLeft.setText(null);
		btnRight.setText(null);
		tv_center.setText(null);
	}

}
