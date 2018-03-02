
package yet.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 使用HashMap和ArrayList实现
 *
 * @param <K>
 * @param <V>
 * @author yangentao
 */
public class MultiHashMapArray<K, V> {
    // HashMap<K, ArrayList<V>> / HashMap<K, HashSet<V> > 或 HasnMap<K,
    // TreeSet<V> > 哪个更高效?
    private HashMap<K, ArrayList<V>> model;
    private int listCapcity = 8;

    public MultiHashMapArray() {
        this(8, 8);
    }

    public MultiHashMapArray(int capacity, int listCapcity) {
        this.listCapcity = listCapcity < 4 ? 4 : listCapcity;
        model = new HashMap<>(capacity < 8 ? 8 : capacity);
    }

    public void clear() {
        model.clear();
    }

    public boolean containsKey(K key) {
        return model.containsKey(key);
    }

    /**
     * 需要遍历所有的value, 性能较差
     *
     * @param value
     * @return
     */
    public boolean containsValue(V value) {
        for (Entry<K, ArrayList<V>> e : model.entrySet()) {
            if (null != e.getValue()) {
                return e.getValue().contains(value);
            }
        }
        return false;
    }

    public Set<Entry<K, ArrayList<V>>> entrySet() {
        return model.entrySet();
    }

    /**
     * 根据Key查找对应的Value的集合, 应该对返回的结果集只进行读操作
     *
     * @param key
     * @return
     */
    public ArrayList<V> get(K key) {
        return model.get(key);
    }

    public boolean isEmpty() {
        return model.isEmpty();
    }

    public Set<K> keySet() {
        return model.keySet();
    }

    public void put(K key, V value) {
        ArrayList<V> ls = model.get(key);
        if (null == ls) {
            ls = new ArrayList<V>(listCapcity);
            model.put(key, ls);
        }
        ls.add(value);
    }

    public void putUnique(K key, V value) {
        ArrayList<V> ls = model.get(key);
        if (null == ls) {
            ls = new ArrayList<V>(listCapcity);
            model.put(key, ls);
        }
        if (!ls.contains(value)) {
            ls.add(value);
        }
    }

    public ArrayList<V> remove(K key) {
        return model.remove(key);
    }

    public void remove(K key, V val) {
        ArrayList<V> ls = model.get(key);
        if (null != ls) {
            ls.remove(val);
        }
    }

    /**
     * 低效率, 需要遍历
     *
     * @param val
     */
    public void removeValue(V val) {
        for (Entry<K, ArrayList<V>> e : model.entrySet()) {
            if (null != e.getValue()) {
                e.getValue().remove(val);
            }
        }
    }

    public int size() {
        return model.size();
    }

    public Collection<ArrayList<V>> values() {
        return model.values();
    }

}
