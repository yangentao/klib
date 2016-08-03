package net.yet.util;

import com.google.gson.JsonObject;

@NoProguard
public class StrVal  {
	public String value;

	public StrVal(String val) {
		this.value = val;
	}

	public boolean empty() {
		return value == null || value.length() == 0;
	}

	public String str(String defVal) {
		return empty() ? defVal : value;
	}

	public int toInt(int defVal) {
		return empty() ? defVal : Integer.parseInt(value);
	}

	public long toLong(long defVal) {
		return empty() ? defVal : Long.parseLong(value);
	}

	public JsonObject toJsonObject() {
		return empty() ? null : JsonUtil.parseObject(value);
	}

	@Override
	public String toString() {
		return value == null ? "" : value;
	}

}
