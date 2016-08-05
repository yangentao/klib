package net.yet.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Colls {
	public interface FilterCallback<T> {
		boolean accept(T item);
	}

	public static <T> List<T> filter(List<T> ls, FilterCallback<T> callback) {
		ArrayList<T> L = new ArrayList<>();
		for (T item : ls) {
			if (callback.accept(item)) {
				L.add(item);
			}
		}
		return L;
	}

	public static <K, V> Map<K, V> copy(Map<K, V> map) {
		if (map == null) {
			return new HashMap<K, V>();
		}
		HashMap<K, V> m = new HashMap<K, V>(map.size());
		for (Entry<K, V> e : map.entrySet()) {
			m.put(e.getKey(), e.getValue());
		}
		return m;

	}

	/**
	 * 将other填充到map中, 如果replace是true, 则当存在主键有交集时替换map中的值, 如果是false,则忽略other中的值
	 *
	 * @param map
	 * @param other
	 * @param replace
	 */
	public static <K, V> void fill(Map<K, V> map, Map<K, V> other, boolean replace) {
		if (map == null || other == null) {
			return;
		}
		if (replace) {
			for (Entry<K, V> e : other.entrySet()) {
				map.put(e.getKey(), e.getValue());
			}
		} else {
			for (Entry<K, V> e : other.entrySet()) {
				if (!map.containsKey(e.getKey())) {
					map.put(e.getKey(), e.getValue());
				}
			}
		}
	}

	/**
	 * 相当于左连接
	 * 用update的值来更新base的值, 只有键同时在base和update存在的情况下才更新
	 * update({"a":1,"b":2}, {"a":3,"c":4}) => {"a":3, "b":2}
	 *
	 * @param base
	 * @param update
	 * @return
	 */
	public static <K, V> Map<K, V> leftJoin(Map<K, V> base, Map<K, V> update) {
		Map<K, V> map = new HashMap<K, V>(base == null ? 8 : base.size() * 2);
		if (base != null) {
			for (K key : base.keySet()) {
				if (update != null && update.containsKey(key)) {
					map.put(key, update.get(key));
				} else {
					map.put(key, base.get(key));
				}
			}
		}
		return map;
	}

	/**
	 * 求并集, 相当于外连接
	 *
	 * @param first          可以为null
	 * @param second         可以为null
	 * @param useSecondValue 当first和second都存在某个建时, true: 使用second的值; false:使用first的值
	 * @return
	 */
	public static <K, V> Map<K, V> outerJoin(Map<K, V> first, Map<K, V> second, boolean useSecondValue) {
		if (first == null) {
			return copy(second);
		}
		if (second == null) {
			return copy(first);
		}
		HashMap<K, V> m = new HashMap<K, V>(first.size() * 2 + second.size() * 2 + 1);
		fill(m, first, true);
		fill(m, second, useSecondValue);
		return m;
	}

	/**
	 * 内连接, 求交集, 值使用left的值
	 * 如果想使用right的值, 只需要颠倒两个实参的顺序
	 *
	 * @param left
	 * @param right
	 * @return
	 */
	public static <K, V> Map<K, V> innerJoin(Map<K, V> left, Map<K, V> right) {
		if (left == null || right == null) {
			return new HashMap<K, V>();
		}
		Map<K, V> result = new HashMap<K, V>(left.size());
		Set<K> keySet = innerJoin(left.keySet(), right.keySet());
		for (K key : keySet) {
			result.put(key, left.get(key));
		}
		return result;
	}

	/**
	 * 取交集, 相当于内连接
	 *
	 * @param map 非null
	 * @param c   非null
	 * @return
	 */
	public static <K, V> Map<K, V> innerJoin(Map<K, V> map, Collection<K> c) {
		Map<K, V> result = new HashMap<K, V>(map.size());
		for (K key : c) {
			if (map.containsKey(key)) {
				result.put(key, map.get(key));
			}
		}
		return result;
	}

	/**
	 * 取交集, 相当于内连接
	 *
	 * @param set
	 * @param c
	 * @return
	 */
	public static <K> Set<K> innerJoin(Collection<K> set, Collection<K> c) {
		Set<K> result = new HashSet<K>(set.size());
		for (K key : c) {
			if (set.contains(key)) {
				result.add(key);
			}
		}
		return result;
	}

	public static int max(Iterable<Integer> set, int emptyVal) {
		if (set == null || !set.iterator().hasNext()) {
			return emptyVal;
		}
		int max = Integer.MIN_VALUE;
		for (int n : set) {
			max = Math.max(n, max);
		}
		return max;
	}

	public static int min(Iterable<Integer> set, int emptyVal) {
		if (set == null || !set.iterator().hasNext()) {
			return emptyVal;
		}
		int min = Integer.MAX_VALUE;
		for (int n : set) {
			min = Math.min(n, min);
		}
		return min;
	}

}
