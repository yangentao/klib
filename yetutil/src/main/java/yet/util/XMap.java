package yet.util;

import com.google.gson.Gson;

import yet.anno.NoProguard;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@NoProguard
public class XMap {
	private static final String FILE_DEF = "def_map_json";

	public Map<String, Object> map = null;

	private transient String filename;

	public XMap() {
		map = new HashMap<>(128);
	}

	/**
	 * load from file
	 *
	 * @param filename
	 */
	public XMap(String filename) {
		XMap m = load(filename);
		this.map = m.map;
		this.filename = m.filename;
	}

	public Set<String> keys() {
		return map.keySet();
	}

	public void remove(String key) {
		map.remove(key);
	}

	public XMap put(String key, Object value) {
		map.put(key, value);
		return this;
	}

	public XMap putSave(String key, Object value) {
		put(key, value);
		save();
		return this;
	}

	public int size() {
		return map.size();
	}

	public XMap clear() {
		map.clear();
		return this;
	}

	public boolean empty() {
		return map.isEmpty();
	}

	public boolean has(String key) {
		return map.containsKey(key);
	}

	public String getString(String key) {
		return getString(key, null);
	}

	public String getString(String key, String defValue) {
		Object val = map.get(key);
		if (val == null) {
			return defValue;
		}
		return (String) val;
	}

	public int getInt(String key, int defValue) {
		return getNumber(key, defValue).intValue();
	}

	public long getLong(String key, long defValue) {
		return getNumber(key, defValue).longValue();
	}

	public double getDouble(String key, double defValue) {
		return getNumber(key, defValue).doubleValue();
	}

	public Number getNumber(String key, Number defValue) {
		Object val = map.get(key);
		if (val == null) {
			return defValue;
		}
		return (Number) val;
	}

	public boolean getBool(String key, boolean defValue) {
		Object val = map.get(key);
		if (val == null) {
			return defValue;
		}
		return (Boolean) val;
	}

	public boolean toggleBool(String key, boolean defValue) {
		boolean b = getBool(key, defValue);
		put(key, !b);
		return !b;
	}

	public boolean toggleBoolSave(String key, boolean defValue) {
		boolean b = getBool(key, defValue);
		put(key, !b);
		save();
		return !b;
	}

	@SuppressWarnings("unchecked")
	public Map<String, ?> getAsMap(String key) {
		return (Map<String, ?>) map.get(key);
	}

	public Object getAs(String key, Class<?> cls) {
		Object obj = map.get(key);
		if (obj != null) {
			Gson gs = new Gson();
			String s = gs.toJson(obj);
			return JsonUtil.fromJson(s, cls);
		}
		return null;
	}

	public void saveAs(String filename) {
		JsonUtil.save(filename, this);
	}

	public void save() {
		if (Util.notEmpty(filename)) {
			saveAs(filename);
		} else {
			throw new IllegalArgumentException("没有指定文件名");
		}
	}

	/**
	 * @param filename
	 * @return 不会返回null, 如果文件不存在, 则返回size是0的map
	 */
	public static XMap load(String filename) {
		XMap xm = JsonUtil.load(filename, XMap.class);
		if (xm == null) {
			xm = new XMap();
		}
		xm.filename = filename;
		return xm;
	}

	public static XMap loadDef() {
		return load(FILE_DEF);
	}

}
