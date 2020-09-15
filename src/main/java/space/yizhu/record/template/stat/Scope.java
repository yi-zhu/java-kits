

package space.yizhu.record.template.stat;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings({"rawtypes", "unchecked"})
public class Scope {

    private final Scope parent;
    private final Ctrl ctrl;
    private Map data;
    private Map<String, Object> sharedObjectMap;

    
    public Scope(Map data, Map<String, Object> sharedObjectMap) {
        this.parent = null;
        this.ctrl = new Ctrl();
        this.data = data;
        this.sharedObjectMap = sharedObjectMap;
    }

    
    public Scope(Scope parent) {
        if (parent == null) {
            throw new IllegalArgumentException("parent can not be null.");
        }
        this.parent = parent;
        this.ctrl = parent.ctrl;
        this.data = null;
        this.sharedObjectMap = parent.sharedObjectMap;
    }

    public Ctrl getCtrl() {
        return ctrl;
    }

    
    public void set(Object key, Object value) {
        for (Scope cur = this; true; cur = cur.parent) {
            
            if (cur.data != null && cur.data.containsKey(key)) {
                cur.data.put(key, value);
                return;
            }

            if (cur.parent == null) {
                if (cur.data == null) {            
                    cur.data = new HashMap();
                }
                cur.data.put(key, value);
                return;
            }
        }
    }

    
    public Object get(Object key) {
        for (Scope cur = this; cur != null; cur = cur.parent) {




            if (cur.data != null) {
                Object ret = cur.data.get(key);
                if (ret != null) {
                    return ret;
                }

                if (cur.data.containsKey(key)) {
                    return null;
                }
            }
        }
        
        return sharedObjectMap != null ? sharedObjectMap.get(key) : null;
    }

    
    public void remove(Object key) {
        for (Scope cur = this; cur != null; cur = cur.parent) {
            if (cur.data != null && cur.data.containsKey(key)) {
                cur.data.remove(key);
                return;
            }
        }
    }

    
    public void setLocal(Object key, Object value) {
        if (data == null) {
            data = new HashMap();
        }
        data.put(key, value);
    }

    
    public Object getLocal(Object key) {
        return data != null ? data.get(key) : null;
    }

    
    public void removeLocal(Object key) {
        if (data != null) {
            data.remove(key);
        }
    }

    
    public void setGlobal(Object key, Object value) {
        for (Scope cur = this; true; cur = cur.parent) {
            if (cur.parent == null) {
                cur.data.put(key, value);
                return;
            }
        }
    }

    
    public Object getGlobal(Object key) {
        for (Scope cur = this; true; cur = cur.parent) {
            if (cur.parent == null) {
                return cur.data.get(key);
            }
        }
    }

    
    public void removeGlobal(Object key) {
        for (Scope cur = this; true; cur = cur.parent) {
            if (cur.parent == null) {
                cur.data.remove(key);
                return;
            }
        }
    }

    
    public Map getMapOfValue(Object key) {
        for (Scope cur = this; cur != null; cur = cur.parent) {
            if (cur.data != null && cur.data.containsKey(key)) {
                return cur.data;
            }
        }
        return null;
    }

    
    public Map getData() {
        return data;
    }

    
    public void setData(Map data) {
        this.data = data;
    }

    
    public Map getRootData() {
        for (Scope cur = this; true; cur = cur.parent) {
            if (cur.parent == null) {
                return cur.data;
            }
        }
    }

    
    public void setRootData(Map data) {
        for (Scope cur = this; true; cur = cur.parent) {
            if (cur.parent == null) {
                cur.data = data;
                return;
            }
        }
    }

    
    public boolean exists(Object key) {
        for (Scope cur = this; cur != null; cur = cur.parent) {
            if (cur.data != null && cur.data.containsKey(key)) {
                return true;
            }
        }
        return false;
    }
}



