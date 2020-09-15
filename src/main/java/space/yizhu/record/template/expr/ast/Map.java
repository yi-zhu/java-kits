

package space.yizhu.record.template.expr.ast;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import space.yizhu.record.template.stat.Scope;
import space.yizhu.record.template.stat.Scope;


public class Map extends Expr {

    private LinkedHashMap<Object, Expr> map;

    public Map(LinkedHashMap<Object, Expr> map) {
        this.map = map;
    }

    public Object eval(Scope scope) {
        LinkedHashMap<Object, Object> valueMap = new LinkedHashMap<Object, Object>(map.size());
        for (Entry<Object, Expr> e : map.entrySet()) {
            valueMap.put(e.getKey(), e.getValue().eval(scope));
        }
        return valueMap;
    }
}






