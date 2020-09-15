

package space.yizhu.record.template.expr.ast;

import java.util.ArrayList;
import java.util.List;

import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class Array extends Expr {

    private Expr[] exprList;

    public Array(Expr[] exprList, Location location) {
        if (exprList == null) {
            throw new ParseException("exprList can not be null", location);
        }
        this.exprList = exprList;
    }

    public Object eval(Scope scope) {
        List<Object> array = new ArrayListExt(exprList.length);
        for (Expr expr : exprList) {
            array.add(expr.eval(scope));
        }
        return array;
    }

    
    @SuppressWarnings("serial")
    public static class ArrayListExt extends ArrayList<Object> {

        public ArrayListExt(int initialCapacity) {
            super(initialCapacity);
        }

        public Integer getLength() {
            return size();
        }
    }
}



