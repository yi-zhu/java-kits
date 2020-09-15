

package space.yizhu.record.template.expr.ast;

import space.yizhu.record.template.stat.Scope;


public class Id extends Expr {

    private final String id;

    public Id(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Object eval(Scope scope) {
        return scope.get(id);
    }

    
    public String toString() {
        return id;
    }
}


