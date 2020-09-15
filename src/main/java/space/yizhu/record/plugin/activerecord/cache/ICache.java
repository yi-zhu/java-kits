

package space.yizhu.record.plugin.activerecord.cache;


public interface ICache {
    <T> T get(String cacheName, Object key);

    void put(String cacheName, Object key, Object value);

    void remove(String cacheName, Object key);

    void removeAll(String cacheName);
}
