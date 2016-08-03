package net.yet.orm;

import android.database.Cursor;
import android.text.TextUtils;

import net.yet.orm.annotation.Column;
import net.yet.orm.annotation.PrimaryKey;
import net.yet.orm.annotation.Table;
import net.yet.util.Util;
import net.yet.util.database.LiteBase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

class TableDefUtil {

	public static void createOrModifyTable(LiteBase lb, TableInfo tableInfo) {
		boolean hasTable = lb.existTable(tableInfo.tableName);
		if (!hasTable) {
			createTable(lb, tableInfo);
		} else {
			final Table tableAnnotation = tableInfo.tableType.getAnnotation(Table.class);
			if (tableAnnotation == null || tableAnnotation.autoAddField()) {
				mayAddField(lb, tableInfo);
			}
		}
	}

	private static void mayAddField(LiteBase lb, TableInfo tableInfo) {
		Cursor c = lb.select().from(tableInfo.tableName).queryOne();
		if (c.moveToFirst()) {
			Map<String, Integer> colMap = new HashMap<>();
			for (int i = 0; i < c.getColumnCount(); ++i) {
				String name = c.getColumnName(i);
				int type = c.getType(i);
				colMap.put(name, type);
			}
			c.close();

			for (Map.Entry<String, Field> e : tableInfo.nameFieldMap.entrySet()) {
				Integer type = colMap.get(e.getKey());
				if (type == null) {//add field
					String s = defColumn(tableInfo, e.getValue(), e.getKey());
					if (s != null && s.length() > 0) {
						lb.addColumn(tableInfo.tableName, s);
					}
				}
			}
		} else {
			c.close();
			lb.dropTable(tableInfo.tableName);
			createTable(lb, tableInfo);
		}

	}

	private static void createTable(LiteBase lb, final TableInfo tableInfo) {
		lb.trans(new Function1<LiteBase, Unit>() {
			@Override
			public Unit invoke(LiteBase db) {
				defTable(db, tableInfo);
				defIndex(db, tableInfo);
				return Unit.INSTANCE;
			}
		});

	}

	private static void defIndex(LiteBase lb, TableInfo tableInfo) {
		for (Field field : tableInfo.fieldSet) {
			String name = tableInfo.fieldMap.get(field);
			if (field.isAnnotationPresent(PrimaryKey.class)) {
				continue;
			}
			Column column = field.getAnnotation(Column.class);
			if (column == null || column.unique() || !column.index()) {
				continue;
			}
			lb.createIndex(tableInfo.tableName, name);
		}
	}

	private static void defTable(LiteBase lb, TableInfo tableInfo) {
		final ArrayList<String> definitions = new ArrayList<String>();
		for (Field field : tableInfo.fieldSet) {
			String name = tableInfo.fieldMap.get(field);
			String definition = defColumn(tableInfo, field, name);
			if (!TextUtils.isEmpty(definition)) {
				definitions.add(definition);
			}
		}

		final Table tableAnnotation = tableInfo.tableType.getAnnotation(Table.class);
		if (tableAnnotation != null) {
			String[] arr = tableAnnotation.unique();
			if (arr != null && arr.length > 0) {
				//CONSTRAINT name_age UNIQUE (name, age)
				String s = "CONSTRAINT " + Util.join("_", arr) + " UNIQUE (" + Util.join(",", arr) + ")";
				definitions.add(s);
			}
		}

		lb.createTable(tableInfo.tableName, definitions);
	}

	@SuppressWarnings("unchecked")
	private static String defColumn(TableInfo tableInfo, Field field, String name) {
		StringBuilder definition = new StringBuilder();
		Class<?> type = field.getType();
		SqliteType sqlType = tableInfo.sqlTypeMap.get(field);
		if (sqlType == null) {
			return null;
		}
		definition.append(name);
		definition.append(" ");
		definition.append(sqlType.toString());

		PrimaryKey pk = field.getAnnotation(PrimaryKey.class);
		if (pk != null) {
			if (pk.length() > 0) {
				definition.append("(");
				definition.append(pk.length());
				definition.append(")");
			}
			if (pk.autoIncrease() && sqlType == SqliteType.INTEGER) {
				definition.append(" PRIMARY KEY AUTOINCREMENT");
			} else {
				definition.append(" PRIMARY KEY ");
			}
		} else {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				if (column.length() > -1) {
					definition.append("(");
					definition.append(column.length());
					definition.append(")");
				}
				if (column.notNull()) {
					definition.append(" NOT NULL ");
				}
				if (column.unique()) {
					definition.append(" UNIQUE  ");
				}
			}
		}
		return definition.toString();
	}

}
