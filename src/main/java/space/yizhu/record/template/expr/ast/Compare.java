

package space.yizhu.record.template.expr.ast;

import java.math.BigDecimal;

import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.expr.Sym;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class Compare extends Expr {

    private Sym op;
    private Expr left;
    private Expr right;

    public Compare(Sym op, Expr left, Expr right, Location location) {
        if (left == null || right == null) {
            throw new ParseException("The target of \"" + op.value() + "\" operator can not be blank", location);
        }
        this.op = op;
        this.left = left;
        this.right = right;
        this.location = location;
    }

    public Object eval(Scope scope) {
        Object leftValue = left.eval(scope);
        Object rightValue = right.eval(scope);

        switch (op) {
            case EQUAL:
                return equal(leftValue, rightValue);
            case NOTEQUAL:
                return !equal(leftValue, rightValue);
            case GT:
                return gt(leftValue, rightValue);
            case GE:
                return ge(leftValue, rightValue);
            case LT:
                return lt(leftValue, rightValue);
            case LE:
                return le(leftValue, rightValue);
            default:
                String l = leftValue != null ? leftValue.getClass().getSimpleName() : "null";
                String r = rightValue != null ? rightValue.getClass().getSimpleName() : "null";
                throw new TemplateException("Unsupported operation: " + l + " \"" + op.value() + "\" " + r, location);
        }
    }

    Boolean equal(Object leftValue, Object rightValue) {
        if (leftValue == rightValue) {
            return Boolean.TRUE;
        }
        if (leftValue == null || rightValue == null) {
            return Boolean.FALSE;
        }
        if (leftValue.equals(rightValue)) {
            return Boolean.TRUE;
        }
        if (leftValue instanceof Number && rightValue instanceof Number) {
            Number l = (Number) leftValue;
            Number r = (Number) rightValue;
            int maxType = getMaxType(l, r);
            switch (maxType) {
                case Arith.INT:
                    return l.intValue() == r.intValue();
                case Arith.LONG:
                    return l.longValue() == r.longValue();
                case Arith.FLOAT:
                    
                    
                    return l.floatValue() == r.floatValue();
                case Arith.DOUBLE:
                    
                    
                    return l.doubleValue() == r.doubleValue();
                case Arith.BIGDECIMAL:
                    BigDecimal[] bd = toBigDecimals(l, r);
                    return (bd[0]).compareTo(bd[1]) == 0;
            }
            throw new TemplateException("Equal comparison support types of int long float double and BigDeciaml", location);
        }

        return Boolean.FALSE;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    Boolean gt(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            Number l = (Number) leftValue;
            Number r = (Number) rightValue;
            int maxType = getMaxType(l, r);
            switch (maxType) {
                case Arith.INT:
                    return l.intValue() > r.intValue();
                case Arith.LONG:
                    return l.longValue() > r.longValue();
                case Arith.FLOAT:
                    
                    return l.floatValue() > r.floatValue();
                case Arith.DOUBLE:
                    
                    return l.doubleValue() > r.doubleValue();
                case Arith.BIGDECIMAL:
                    BigDecimal[] bd = toBigDecimals(l, r);
                    return (bd[0]).compareTo(bd[1]) > 0;
            }
            throw new TemplateException("Unsupported operation: " + l.getClass().getSimpleName() + " \">\" " + r.getClass().getSimpleName(), location);
        }

        if (leftValue instanceof Comparable &&
                rightValue != null &&
                leftValue.getClass() == rightValue.getClass()) {
            return ((Comparable) leftValue).compareTo((Comparable) rightValue) > 0;
        }

        return checkComparisonValue(leftValue, rightValue);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    Boolean ge(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            Number l = (Number) leftValue;
            Number r = (Number) rightValue;
            int maxType = getMaxType(l, r);
            switch (maxType) {
                case Arith.INT:
                    return l.intValue() >= r.intValue();
                case Arith.LONG:
                    return l.longValue() >= r.longValue();
                case Arith.FLOAT:
                    
                    return l.floatValue() >= r.floatValue();
                case Arith.DOUBLE:
                    
                    return l.doubleValue() >= r.doubleValue();
                case Arith.BIGDECIMAL:
                    BigDecimal[] bd = toBigDecimals(l, r);
                    return (bd[0]).compareTo(bd[1]) >= 0;
            }
            throw new TemplateException("Unsupported operation: " + l.getClass().getSimpleName() + " \">=\" " + r.getClass().getSimpleName(), location);
        }

        if (leftValue instanceof Comparable &&
                rightValue != null &&
                leftValue.getClass() == rightValue.getClass()) {
            return ((Comparable) leftValue).compareTo((Comparable) rightValue) >= 0;
        }

        return checkComparisonValue(leftValue, rightValue);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    Boolean lt(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            Number l = (Number) leftValue;
            Number r = (Number) rightValue;
            int maxType = getMaxType(l, r);
            switch (maxType) {
                case Arith.INT:
                    return l.intValue() < r.intValue();
                case Arith.LONG:
                    return l.longValue() < r.longValue();
                case Arith.FLOAT:
                    
                    return l.floatValue() < r.floatValue();
                case Arith.DOUBLE:
                    
                    return l.doubleValue() < r.doubleValue();
                case Arith.BIGDECIMAL:
                    BigDecimal[] bd = toBigDecimals(l, r);
                    return (bd[0]).compareTo(bd[1]) < 0;
            }
            throw new TemplateException("Unsupported operation: " + l.getClass().getSimpleName() + " \"<\" " + r.getClass().getSimpleName(), location);
        }

        if (leftValue instanceof Comparable &&
                rightValue != null &&
                leftValue.getClass() == rightValue.getClass()) {
            return ((Comparable) leftValue).compareTo((Comparable) rightValue) < 0;
        }

        return checkComparisonValue(leftValue, rightValue);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    Boolean le(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            Number l = (Number) leftValue;
            Number r = (Number) rightValue;
            int maxType = getMaxType(l, r);
            switch (maxType) {
                case Arith.INT:
                    return l.intValue() <= r.intValue();
                case Arith.LONG:
                    return l.longValue() <= r.longValue();
                case Arith.FLOAT:
                    
                    return l.floatValue() <= r.floatValue();
                case Arith.DOUBLE:
                    
                    return l.doubleValue() <= r.doubleValue();
                case Arith.BIGDECIMAL:
                    BigDecimal[] bd = toBigDecimals(l, r);
                    return (bd[0]).compareTo(bd[1]) <= 0;
            }
            throw new TemplateException("Unsupported operation: " + l.getClass().getSimpleName() + " \"<=\" " + r.getClass().getSimpleName(), location);
        }

        if (leftValue instanceof Comparable &&
                rightValue != null &&
                leftValue.getClass() == rightValue.getClass()) {
            return ((Comparable) leftValue).compareTo((Comparable) rightValue) <= 0;
        }

        return checkComparisonValue(leftValue, rightValue);
    }

    private int getMaxType(Number obj1, Number obj2) {
        int t1 = getType(obj1);
        if (t1 == Arith.BIGDECIMAL) {
            return Arith.BIGDECIMAL;
        }
        int t2 = getType(obj2);
        return t1 > t2 ? t1 : t2;
    }

    private int getType(Number obj) {
        if (obj instanceof Integer) {
            return Arith.INT;
        } else if (obj instanceof Long) {
            return Arith.LONG;
        } else if (obj instanceof Float) {
            return Arith.FLOAT;
        } else if (obj instanceof Double) {
            return Arith.DOUBLE;
        } else if (obj instanceof BigDecimal) {
            return Arith.BIGDECIMAL;
        } else if (obj instanceof Short || obj instanceof Byte) {
            return Arith.INT;    
        }
        throw new TemplateException("Unsupported data type: " + obj.getClass().getName(), location);
    }

    BigDecimal[] toBigDecimals(Number left, Number right) {
        BigDecimal[] ret = new BigDecimal[2];
        ret[0] = (left instanceof BigDecimal ? (BigDecimal) left : new BigDecimal(left.toString()));
        ret[1] = (right instanceof BigDecimal ? (BigDecimal) right : new BigDecimal(right.toString()));
        return ret;
    }

    private Boolean checkComparisonValue(Object leftValue, Object rightValue) {
        if (leftValue == null) {
            throw new TemplateException("The operation target on the left side of \"" + op.value() + "\" can not be null", location);
        }
        if (rightValue == null) {
            throw new TemplateException("The operation target on the right side of \"" + op.value() + "\" can not be null", location);
        }

        throw new TemplateException(
                "Unsupported operation: " +
                        leftValue.getClass().getSimpleName() +
                        " \"" + op.value() + "\" " +
                        rightValue.getClass().getSimpleName(),
                location
        );
    }
}









