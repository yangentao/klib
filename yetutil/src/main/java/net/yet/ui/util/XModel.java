package net.yet.ui.util;

import java.util.HashMap;
import java.util.Map;

public class XModel {
	public String title;
	public String msg;
	public String status;
	public long time = 0;

	public Map<String, Object> attrMap = new HashMap<String, Object>();

	public XModel() {
		this(null);
	}

	public XModel(String title) {
		this(title, null);
	}

	public XModel(String title, String msg) {
		this(title, msg, null, System.currentTimeMillis());
	}

	public XModel(String title, String msg, long time) {
		this(title, msg, null, time);
	}

	public XModel(String title, String msg, String status) {
		this(title, msg, status, System.currentTimeMillis());
	}

	public XModel(String title, String msg, String status, long time) {
		this.title = title;
		this.msg = msg;
		this.status = status;
		this.time = time;
	}

	public Object attr(String name) {
		return attrMap.get(name);
	}

	public Object attr(String name, Object value) {
		return attrMap.put(name, value);
	}

	public void clear() {
		title = null;
		msg = null;
		status = null;
		time = 0;
		attrMap.clear();
	}

}
