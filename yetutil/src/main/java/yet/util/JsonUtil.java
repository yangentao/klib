package yet.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;

import yet.file.AppFile;
import yet.file.AppFileVer;
import yet.util.log.xlog;

/**
 * Type mapType = new TypeToken<HashMap<String, String>>() {
 * }.getType();
 * HashMap<String, String> m = JsonUtil.loadGeneric(FILE_NAME, mapType);
 *
 * @author yangentao@gmail.com
 */
public class JsonUtil {
	private static Gson gson = null;

	public static Type stringHashSetType = new TypeToken<HashSet<String>>() {
	}.getType();
	public static Type stringArrayListType = new TypeToken<ArrayList<String>>() {
	}.getType();

	static {
		GsonBuilder gb = new GsonBuilder();
		gson = gb.serializeNulls()
				.serializeSpecialFloatingPointValues()
				.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
				.create();
	}

	public static int getInt(JsonObject jo, String key, int fallback) {
		JsonElement e = jo.get(key);
		if (e != null) {
			try {
				return e.getAsInt();
			} catch (Exception e1) {
			}
		}
		return fallback;
	}

	public static String getString(JsonObject jo, String key, String fallback) {
		JsonElement e = jo.get(key);
		if (e != null) {
			try {
				return e.getAsString();
			} catch (Exception e1) {
			}
		}
		return fallback;
	}

	public static double getDouble(JsonObject jo, String key, double fallback) {
		JsonElement e = jo.get(key);
		if (e != null) {
			try {
				return e.getAsDouble();
			} catch (Exception e1) {
			}
		}
		return fallback;
	}

	public static void set(JsonObject jo, String key, Object obj) {
		if (obj == null) {
			jo.add(key, null);
		} else if (obj instanceof Boolean) {
			jo.addProperty(key, (Boolean) obj);
		} else if (obj instanceof Character) {
			jo.addProperty(key, (Character) obj);
		} else if (obj instanceof Number) {
			jo.addProperty(key, (Number) obj);
		} else if (obj instanceof String) {
			jo.addProperty(key, (String) obj);
		} else if (obj instanceof JsonElement) {
			jo.add(key, (JsonElement) obj);
		} else {
			jo.addProperty(key, obj.toString());
		}

	}

	public static JsonArray array(String[] arr) {
		JsonArray jarr = new JsonArray();
		for (String s : arr) {
			jarr.add(s);
		}
		return jarr;
	}

	public static JsonArray array(boolean[] arr) {
		JsonArray jarr = new JsonArray();
		for (boolean s : arr) {
			jarr.add(s);
		}
		return jarr;
	}

	public static JsonArray array(int[] arr) {
		JsonArray jarr = new JsonArray();
		for (int s : arr) {
			jarr.add(s);
		}
		return jarr;
	}

	public static JsonArray array(long[] arr) {
		JsonArray jarr = new JsonArray();
		for (long s : arr) {
			jarr.add(s);
		}
		return jarr;
	}

	public static JsonArray array(float[] arr) {
		JsonArray jarr = new JsonArray();
		for (float s : arr) {
			jarr.add(s);
		}
		return jarr;
	}

	public static JsonArray array(double[] arr) {
		JsonArray jarr = new JsonArray();
		for (double s : arr) {
			jarr.add(s);
		}
		return jarr;
	}

	public static JsonObject parseObject(String s) {
		if (!s.isEmpty()) {
			try {
				JsonParser p = new JsonParser();
				JsonElement je = p.parse(s);
				if (je != null && je.isJsonObject()) {
					return je.getAsJsonObject();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	public static JsonArray parseArray(String s) {
		if (!s.isEmpty()) {
			try {
				JsonParser p = new JsonParser();
				JsonElement je = p.parse(s);
				if (je != null && je.isJsonArray()) {
					return je.getAsJsonArray();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	public static <T> T fromJson(String json, Class<T> cls) {
		return gson.fromJson(json, cls);
	}

	public static <T> T fromJsonGeneric(String json, Type type) {
		return gson.fromJson(json, type);
	}

	public static String toJson(Object data) {
		return gson.toJson(data);
	}

	// List<String>, Map<String, Object> ....
	public static String toJsonGeneric(Object data, Type type) {
		return gson.toJson(data, type);
	}


	// 用于泛型, 如HashMap<String,String>, List<String>, 泛型不能通过getClass得到确切的类型, 所以需要type参数
	public static void saveGeneric(OutputStream fos, Object data, Type type) {
		try {
			OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
			gson.toJson(data, type, writer);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			xlog.INSTANCE.e(e);
		} finally {
			close(fos);
		}
	}
	public static void close(Closeable c) {
		try {
			if (c != null) {
				c.close();
			}
		} catch (Exception e) {
		}
	}
	// 用于泛型, 如HashMap<String,String>, List<String>, 泛型不能通过getClass得到确切的类型, 所以需要type参数
	public static void saveGeneric(File file, Object data, Type type) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			saveGeneric(fos, data, type);
		} catch (Exception e) {
			xlog.INSTANCE.e(e);
		}
	}

	// 用于泛型, 如HashMap<String,String>, List<String>, 泛型不能通过getClass得到确切的类型, 所以需要type参数
	public static void saveGeneric(String filename, Object data, Type type) {
		saveGeneric(AppFile.INSTANCE.doc(filename), data, type);
	}

	// 用于一般的不是泛型的类, 如,自定义的Person, data是可以通过getClass得到确切类型的对象
	public static void save(OutputStream fos, Object data) {
		try {
			OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
			gson.toJson(data, writer);
			writer.close();
		} catch (Exception e) {
			xlog.INSTANCE.e(e);
		} finally {
			close(fos);
		}
	}


	// 用于一般的不是泛型的类, 如,自定义的Person, data是可以通过getClass得到确切类型的对象
	public static void save(File file, Object data) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			save(fos, data);
		} catch (Exception e) {
			xlog.INSTANCE.e(e);
		}
	}

	// 用于一般的不是泛型的类, 如,自定义的Person, data是可以通过getClass得到确切类型的对象
	public static void save(String filename, Object data) {
		save(AppFile.INSTANCE.doc(filename), data);
	}

	// 用于一般的不是泛型的类, 如,自定义的Person, data是可以通过getClass得到确切类型的对象
	public static void saveVer(String filename, Object data) {
		save(AppFileVer.INSTANCE.doc(filename), data);
	}

	/**
	 * 使用cls.getSimpleName()作为文件名
	 *
	 * @param obj
	 */
	public static <T> void saveVerByClass(Object obj) {
		saveVer(obj.getClass().getSimpleName(), obj);
	}

	// 用于一般的不是泛型的类, 如Person 等, 能通过class 得到确切类型的情况
	// 文件找不到会返回null, UTF8
	public static <T> T load(InputStream fis, Class<T> classOfT) {
		T result = null;
		try {
			InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
			result = gson.fromJson(reader, classOfT);
			reader.close();
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			xlog.INSTANCE.e(e);
		} finally {
			close(fis);
		}
		return result;
	}

	// 用于一般的不是泛型的类, 如Person 等, 能通过class 得到确切类型的情况
	// 文件找不到会返回null
	public static <T> T load(File file, Class<T> classOfT) {
		try {
			FileInputStream fis = new FileInputStream(file);
			return load(fis, classOfT);
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			xlog.INSTANCE.e(e);
		}
		return null;
	}

	// 用于一般的不是泛型的类, 如Person 等, 能通过class 得到确切类型的情况
	// 文件找不到会返回null
	public static <T> T load(String filename, Class<T> classOfT) {
		return load(AppFile.INSTANCE.doc(filename), classOfT);
	}

	/**
	 * 使用cls.getSimpleName()作为文件名
	 *
	 * @param cls
	 * @return
	 */
	public static <T> T loadVerByClass(Class<T> cls) {
		return loadVer(cls.getSimpleName(), cls);
	}

	// 用于一般的不是泛型的类, 如Person 等, 能通过class 得到确切类型的情况
	// 文件找不到会返回null
	// 文件名会追加版本号
	public static <T> T loadVer(String filename, Class<T> classOfT) {
		return load(AppFileVer.INSTANCE.doc(filename), classOfT);
	}

	// 用于泛型, 如List<String>, 可能返回null, 文件未找到, 或出现异常
	// UTF8
	public static <T> T loadGeneric(InputStream fis, Type typeOfT) {
		T result = null;
		try {
			InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
			result = gson.fromJson(reader, typeOfT);
			reader.close();
		} catch (Exception e) {
			xlog.INSTANCE.e(e);
		} finally {
			close(fis);
		}
		return result;
	}

	// 用于泛型, 如List<String>, 可能返回null, 文件未找到, 或出现异常
	public static <T> T loadGeneric(File file, Type typeOfT) {
		try {
			FileInputStream fis = new FileInputStream(file);
			return loadGeneric(fis, typeOfT);
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			xlog.INSTANCE.e(e);
		}
		return null;
	}

	// 用于泛型, 如List<String>, 可能返回null, 文件未找到, 或出现异常
	public static <T> T loadGeneric(String filename, Type typeOfT) {
		return loadGeneric(AppFile.INSTANCE.doc(filename), typeOfT);
	}

}
