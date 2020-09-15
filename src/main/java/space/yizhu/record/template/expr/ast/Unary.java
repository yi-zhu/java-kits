

package space.yizhu.record.template.expr.ast;

import java.math.BigDecimal;

import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.expr.Sym;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class Unary extends Expr {

    private Sym op;
    private Expr expr;

    public Unary(Sym op, Expr expr, Location location) {
        if (expr == null) {
            throw new ParseException("The parameter of \"" + op.value() + "\" operator can not be blank", location);
        }
        this.op = op;
        this.expr = expr;
        this.location = location;
    }

    
    public Object eval(Scope scope) {
        Object value = expr.eval(scope);
        if (value == null) {
            if (scope.getCtrl().isNullSafe()) {
                return null;
            }
            throw new TemplateException("The parameter of \"" + op.value() + "\" operator can not be blank", location);
        }
        if (!(value instanceof Number)) {
            throw new TemplateException(op.value() + " operator only support int long float double BigDecimal type", location);
        }

        switch (op) {
            case ADD:
                return value;
            case SUB:
                Number n = (Number) value;
                if (n instanceof Integer) {
                    return Integer.valueOf(-n.intValue());
                }
                if (n instanceof Long) {
                    return Long.valueOf(-n.longValue());
                }
                if (n instanceof Float) {
                    return Float.valueOf(-n.floatValue());
                }
                if (n instanceof Double) {
                    return Double.valueOf(-n.doubleValue());
                }
                if (n instanceof BigDecimal) {
                    return ((BigDecimal) n).negate();
                }
                throw new TemplateException("Unsupported data type: " + n.getClass().getName(), location);
            default:
                throw new TemplateException("Unsupported operator: " + op.value(), location);
        }
    }

    
    public Expr toConstIfPossible() {
        if (expr instanceof Const && (op == Sym.SUB || op == Sym.ADD || op == Sym.NOT)) {
        } else {
            return this;
        }

        Expr ret = this;
        Const c = (Const) expr;
        if (op == Sym.SUB) {
            if (c.isInt()) {
                ret = new Const(Sym.INT, -c.getInt());
            } else if (c.isLong()) {
                ret = new Const(Sym.LONG, -c.getLong());
            } else if (c.isFloat()) {
                ret = new Const(Sym.FLOAT, -c.getFloat());
            } else if (c.isDouble()) {
                ret = new Const(Sym.DOUBLE, -c.getDouble());
            }
        } else if (op == Sym.ADD) {
            if (c.isNumber()) {
                ret = c;
            }
        } else if (op == Sym.NOT) {
            if (c.isBoolean()) {
                ret = c.isTrue() ? Const.FALSE : Const.TRUE;
            }
        }

        return ret;
    }

    public String toString() {
        return op.toString() + expr.toString();
    }
}





