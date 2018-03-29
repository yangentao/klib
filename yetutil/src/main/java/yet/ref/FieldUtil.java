package yet.ref;

import android.support.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import yet.util.log.xlog;

/**
 * Created by entaoyang@163.com on 2017-03-13.
 */

public class FieldUtil {

	public static boolean isFieldType(Field field, Class<?> fieldClass) {
		return field.getType() == fieldClass;
	}

	//HashSet<String> children; ==>String::class
	@Nullable
	public static Class<?> getFirstFieldGenericParamType(Field field) {
		try {
			Type t = field.getGenericType();
			if (t instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) t;
				Type[] arr = pt.getActualTypeArguments();
				if (arr.length > 0) {
					return (Class) arr[0];
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	//field HashSet<String> children =...
	//fieldType(childrenField, HashSet.class, String.class) => true
	public static boolean isFieldTypeGeneric(Field field, Class<?> fieldClass, Class<?> genericParamClass) {
		try {
			Type t = field.getGenericType();
			if (t instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) t;
				if (pt.getRawType() == fieldClass) {
					Type[] arr = pt.getActualTypeArguments();
					if (arr.length > 0) {
						xlog.INSTANCE.d("arg[0]: ", arr[0]);
						xlog.INSTANCE.d("genericParamClass: ", genericParamClass);
						return arr[0].equals(genericParamClass);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static boolean isFieldTypeGeneric(Field field, Class<?> fieldClass, Class<?> genericParamClass1, Class<?> genericParamClass2) {
		try {
			Type t = field.getGenericType();
			if (t instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) t;
				if (pt.getRawType() == fieldClass) {
					Type[] arr = pt.getActualTypeArguments();
					if (arr.length > 1) {
						return arr[0] == genericParamClass1 && arr[1] == genericParamClass2;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}


}
