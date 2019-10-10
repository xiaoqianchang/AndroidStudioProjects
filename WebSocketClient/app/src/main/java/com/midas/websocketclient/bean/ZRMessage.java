package com.midas.websocketclient.bean;

/**
 * 消息Bean
 * 
 * Created by Chang.Xiao on 2016/5/17.
 * @version 1.0
 */
public class ZRMessage {

	public static final String TABLE_NAME = "message";
	public static final String _ID = "_id";
	public static final String FROM = "from";
	public static final String TO = "to";
	public static final String DATE = "date";
	public static final String CONTENT = "content";

	private String _id;
	private ZRUser from;//消息来自哪个人
	private ZRUser to;//消息发给哪个人
	private String name;
	private String date;//消息日期
	private String content;//消息内容
	private Direct direct; // 消息的方向

	public ZRMessage() {
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public ZRUser getFrom() {
		return from;
	}

	public void setFrom(ZRUser from) {
		this.from = from;
	}

	public ZRUser getTo() {
		return to;
	}

	public void setTo(ZRUser to) {
		this.to = to;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Direct getDirect() {
		return direct;
	}

	public void setDirect(Direct direct) {
		this.direct = direct;
	}

	public enum Direct {
		SEND(0),
		RECEIVE(1);

		private int code;

		private Direct(int code) {
			this.code = code;
		}

		public int getValue() {
			return code;
		}
	}
}
