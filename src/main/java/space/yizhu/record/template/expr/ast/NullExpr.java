

package space.yizhu.record.template.expr.ast;

import space.yizhu.record.template.stat.Scope;


public class NullExpr extends Expr {

    public static final NullExpr me = new NullExpr();

    private NullExpr() {
    }

    public Object eval(Scope scope) {
        return null;
    }
}



