

package space.yizhu.record.template.expr.ast;

import java.util.List;
import java.util.Map;

import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class Assign extends Expr {

    private String id;
    private Expr index;    
    private Expr right;

    
    public Assign(String id, Expr index, Expr right, Location location) {
        if (index == null) {
            throw new ParseException("The index expression of array assignment can not be null", location);
        }
        if (right == null) {
            throw new ParseException("The expression on the right side of an assignment expression can not be null", location);
        }
        this.id = id;
        this.index = index;
        this.right = right;
        this.location = location;
    }

    
    public Assign(String id, Expr right, Location location) {
        if (right == null) {
            throw new ParseException("The expression on the right side of an assignment expression can not be null", location);
        }
        this.id = id;
        this.index = null;
        this.right = right;
        this.location = location;
    }

    
    public String getId() {
        return id;
    }

    public Expr getIndex() {
        return index;
    }

    public Expr getRight() {
        return right;
    }

    
    public Object eval(Scope scope) {
        if (index == null) {
            return assignVariable(scope);
        } else {
            return assignElement(scope);
        }
    }

    Object assignVariable(Scope scope) {
        Object rightValue = right.eval(scope);
        if (scope.getCtrl().isWisdomAssignment()) {
            scope.set(id, rightValue);
        } else if (scope.getCtrl().isLocalAssignment()) {
            scope.setLocal(id, rightValue);
        } else {
            scope.setGlobal(id, rightValue);
        }

        return rightValue;
    }

    
    @SuppressWarnings({"unchecked", "rawtypes"})
    Object assignElement(Scope scope) {
        Object target = scope.get(id);
        if (target == null) {
            throw new TemplateException("The assigned targets \"" + id + "\" can not be null", location);
        }
        Object idx = index.eval(scope);
        if (idx == null) {
            throw new TemplateException("The index of list/array and the key of map can not be null", location);
        }

        Object value;
        if (target instanceof Map) {
            value = right.eval(scope);
            ((Map) target).put(idx, value);
            return value;
        }

        if (!(idx instanceof Integer)) {
            throw new TemplateException("The index of list/array can only be integer", location);
        }

        if (target instanceof List) {
            value = right.eval(scope);
            ((List) target).set((Integer) idx, value);
            return value;
        }
        if (target.getClass().isArray()) {
            value = right.eval(scope);
            java.lang.reflect.Array.set(target, (Integer) idx, value);
            return value;
        }

        throw new TemplateException("Only the list array and map is supported by index assignment", location);
    }
}







