package com.lanqiu.myqq.bean;

import java.io.Serializable;

public class Session implements Serializable{
	private String id;
	private String from;		//������
	private String type;		//��Ϣ����
	private String time;		//����ʱ��
	private String content;		//��������
	private String notReadCount;//δ����¼
	private String to;		//������
	private String isdispose;//�Ƿ��Ѵ��� 0δ����1�Ѵ���
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getNotReadCount() {
		return notReadCount;
	}
	public void setNotReadCount(String notReadCount) {
		this.notReadCount = notReadCount;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getIsdispose() {
		return isdispose;
	}
	public void setIsdispose(String isdispose) {
		this.isdispose = isdispose;
	}

}
