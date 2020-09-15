

package space.yizhu.record.template.expr.ast;

import java.util.AbstractList;

import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class RangeArray extends Expr {

    private Expr start;
    private Expr end;

    
    public RangeArray(Expr start, Expr end, Location location) {
        if (start == null) {
            throw new ParseException("The start value of range array can not be blank", location);
        }
        if (end == null) {
            throw new ParseException("The end value of range array can not be blank", location);
        }
        this.start = start;
        this.end = end;
        this.location = location;
    }

    public Object eval(Scope scope) {
        Object startValue = start.eval(scope);
        if (!(startValue instanceof Integer)) {
            throw new TemplateException("The start value of range array must be Integer", location);
        }
        Object endValue = end.eval(scope);
        if (!(endValue instanceof Integer)) {
            throw new TemplateException("The end value of range array must be Integer", location);
        }

        return new RangeList((Integer) startValue, (Integer) endValue, location);
    }

    public static class RangeList extends AbstractList<Integer> {

        final int start;
        final int size;
        final int increment;
        final Location location;

        public RangeList(int start, int end, Location location) {
            this.start = start;
            this.increment = start <= end ? 1 : -1;
            this.size = Math.abs(end - start) + 1;
            this.location = location;
        }

        public Integer get(int index) {
            if (index < 0 || index >= size) {
                throw new TemplateException("Index out of bounds. Index: " + index + ", Size: " + size, location);
            }
            return start + index * increment;
        }

        public int size() {
            return size;
        }
    }
}




