

package space.yizhu.record.template.stat.ast;

import java.util.Map.Entry;


public class ForEntry implements Entry<Object, Object> {

    private Entry<Object, Object> entry;

    public void init(Entry<Object, Object> entry) {
        this.entry = entry;
    }

    public Object getKey() {
        return entry.getKey();
    }

    public Object getValue() {
        return entry.getValue();
    }

    public Object setValue(Object value) {
        return entry.setValue(value);
    }
}



