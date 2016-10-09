package net.yet.ui.dialogs;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

/**
 * Created by yet on 2015/11/1.
 */
public class MenuDialog {
	public interface MenuCallback {
		void onMenuCallback(MenuDialog menuDialog, String group, String item);
	}

	private String title;
	private String group;
	public String argS;
	public int argN = 0;
	private MenuCallback callback;

	private final List<String> items = new ArrayList<>();

	public static MenuDialog call(MenuCallback callback) {
		MenuDialog dlg = new MenuDialog();
		return dlg.callback(callback);
	}

	public MenuDialog argS(String s) {
		this.argS = s;
		return this;
	}

	public MenuDialog argN(int n) {
		this.argN = n;
		return this;
	}

	public MenuDialog title(String title) {
		this.title = title;
		return this;
	}

	public MenuDialog group(String group) {
		this.group = group;
		return this;
	}

	public MenuDialog items(String... items) {
		for (String s : items) {
			this.items.add(s);
		}
		return this;
	}

	public MenuDialog callback(MenuCallback callback) {
		this.callback = callback;
		return this;
	}

	public void show(Context context) {
		StringSelectDialog dlg = new StringSelectDialog();
		dlg.setOnSelect(new Function2<Integer, String, Unit>() {
			@Override
			public Unit invoke(Integer integer, String s) {
				if (callback != null) {
					callback.onMenuCallback(MenuDialog.this, group, s);
				}
				return Unit.INSTANCE;
			}
		});
		dlg.addItems(this.items);
		dlg.show(context, title);
	}
}
