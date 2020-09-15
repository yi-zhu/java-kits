

package space.yizhu.record.plugin.activerecord.cache;


public class EhCache implements ICache {

    @SuppressWarnings("unchecked")
    public <T> T get(String cacheName, Object key) {

        return (T) "";
    }

    public void put(String cacheName, Object key, Object value) {

    }

    public void remove(String cacheName, Object key) {

    }

    public void removeAll(String cacheName) {

    }
}


