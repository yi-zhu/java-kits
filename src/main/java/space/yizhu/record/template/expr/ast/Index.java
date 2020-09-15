

package space.yizhu.record.template.expr.ast;

import java.util.List;

import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class Index extends Expr {

    private Expr expr;
    private Expr index;

    public Index(Expr expr, Expr index, Location location) {
        if (expr == null || index == null) {
            throw new ParseException("array/list/map and their index can not be null", location);
        }
        this.expr = expr;
        this.index = index;
        this.location = location;
    }

    @SuppressWarnings("rawtypes")
    public Object eval(Scope scope) {
        Object target = expr.eval(scope);
        if (target == null) {
            if (scope.getCtrl().isNullSafe()) {
                return null;
            }
            throw new TemplateException("The index access operation target can not be null", location);
        }

        Object idx = index.eval(scope);
        if (idx == null) {
            if (scope.getCtrl().isNullSafe()) {
                return null;
            }

            if (target instanceof java.util.Map) {
                
            } else {
                throw new TemplateException("The index of list and array can not be null", location);
            }
        }

        if (target instanceof List) {
            if (idx instanceof Integer) {
                return ((List<?>) target).get((Integer) idx);
            }
            throw new TemplateException("The index of list must be integer", location);
        }

        if (target instanceof java.util.Map) {
            return ((java.util.Map) target).get(idx);
        }

        if (target.getClass().isArray()) {
            if (idx instanceof Integer) {
                return java.lang.reflect.Array.get(target, (Integer) idx);
            }
            throw new TemplateException("The index of array must be integer", location);
        }

        throw new TemplateException("Only the list array and map is supported by index access", location);
    }
}




