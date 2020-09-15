

package space.yizhu.record.template.expr.ast;

import space.yizhu.record.template.stat.Ctrl;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class NullSafe extends Expr {

    private Expr left;
    private Expr right;

    public NullSafe(Expr left, Expr right, Location location) {
        if (left == null) {
            throw new ParseException("The expression on the left side of null coalescing and safe access operator \"??\" can not be blank", location);
        }
        this.left = left;
        this.right = right;
        this.location = location;
    }

    public Object eval(Scope scope) {
        Ctrl ctrl = scope.getCtrl();
        boolean oldNullSafeValue = ctrl.isNullSafe();

        try {
            ctrl.setNullSafe(true);
            Object ret = left.eval(scope);
            if (ret != null) {
                return ret;
            }
        } finally {
            ctrl.setNullSafe(oldNullSafeValue);
        }

        
        return right != null ? right.eval(scope) : null;
    }
}







