package net.yet.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by yangentao on 2015/11/22.
 * entaoyang@163.com
 */
public class FixedQueue<T> implements Iterable<T> {
	private int max = 0;
	private LinkedList<T> list = new LinkedList<>();

	public FixedQueue(int max) {
		this.max = max;
	}

	synchronized public void clear() {
		list.clear();
	}

	synchronized public void add(T value) {
		list.add(value);
		if (list.size() > max) {
			list.pop();
		}
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int size() {
		return list.size();
	}

	public T get(int index) {
		return list.get(index);
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	synchronized public FixedQueue<T> copy() {
		FixedQueue<T> q = new FixedQueue<>(max);
		for (T val : list) {
			q.add(val);
		}
		return q;
	}

	synchronized public ArrayList<T> toList() {
		ArrayList<T> ls = new ArrayList<>(max);
		for (T val : list) {
			ls.add(val);
		}
		return ls;
	}
}
