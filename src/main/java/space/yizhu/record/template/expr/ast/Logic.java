

package space.yizhu.record.template.expr.ast;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.expr.Sym;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class Logic extends Expr {

    private Sym op;
    private Expr left;        
    private Expr right;

    
    private static boolean newWorkMode = true;

    
    @Deprecated
    public static void setToOldWorkMode() {
        newWorkMode = false;
    }

    
    public Logic(Sym op, Expr left, Expr right, Location location) {
        if (left == null) {
            throw new ParseException("The target of \"" + op.value() + "\" operator on the left side can not be blank", location);
        }
        if (right == null) {
            throw new ParseException("The target of \"" + op.value() + "\" operator on the right side can not be blank", location);
        }
        this.op = op;
        this.left = left;
        this.right = right;
        this.location = location;
    }

    
    public Logic(Sym op, Expr right, Location location) {
        if (right == null) {
            throw new ParseException("The target of \"" + op.value() + "\" operator on the right side can not be blank", location);
        }
        this.op = op;
        this.left = null;
        this.right = right;
        this.location = location;
    }

    public Object eval(Scope scope) {
        switch (op) {
            case NOT:
                return evalNot(scope);
            case AND:
                return evalAnd(scope);
            case OR:
                return evalOr(scope);
            default:
                throw new TemplateException("Unsupported operator: " + op.value(), location);
        }
    }

    Object evalNot(Scope scope) {
        return !isTrue(right.eval(scope));
    }

    Object evalAnd(Scope scope) {
        return isTrue(left.eval(scope)) && isTrue(right.eval(scope));
    }

    Object evalOr(Scope scope) {
        return isTrue(left.eval(scope)) || isTrue(right.eval(scope));
    }

    
    public static boolean isTrue(Object v) {
        if (v == null) {
            return false;
        }

        if (v instanceof Boolean) {
            return (Boolean) v;
        }
        if (v instanceof CharSequence) {
            return ((CharSequence) v).length() > 0;
        }

        
        if (!newWorkMode) {
            if (v instanceof Number) {
                if (v instanceof Double) {
                    return ((Number) v).doubleValue() != 0;
                }
                if (v instanceof Float) {
                    return ((Number) v).floatValue() != 0;
                }
                return ((Number) v).intValue() != 0;
            }

            
            
            if (v instanceof Collection) {
                return ((Collection<?>) v).size() > 0;
            }
            if (v instanceof Map) {
                return ((Map<?, ?>) v).size() > 0;
            }
            if (v.getClass().isArray()) {
                return Array.getLength(v) > 0;
            }
            if (v instanceof Iterator) {
                return ((Iterator<?>) v).hasNext();
            }
        }

        return true;
    }

    public static boolean isFalse(Object v) {
        return !isTrue(v);
    }
}



