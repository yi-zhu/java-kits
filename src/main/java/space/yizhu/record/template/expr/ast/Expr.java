

package space.yizhu.record.template.expr.ast;

import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.Scope;


public abstract class Expr {

    protected Location location;

    public abstract Object eval(Scope scope);
}




