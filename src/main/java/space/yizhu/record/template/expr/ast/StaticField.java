

package space.yizhu.record.template.expr.ast;

import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;

import java.lang.reflect.Field;


public class StaticField extends Expr {

    private Class<?> clazz;
    private String fieldName;
    private Field field;

    public StaticField(String className, String fieldName, Location location) {
        try {
            this.clazz = Class.forName(className);
            this.fieldName = fieldName;
            this.field = clazz.getField(fieldName);
            this.location = location;
        } catch (Exception e) {
            throw new ParseException(e.getMessage(), location, e);
        }
    }

    public Object eval(Scope scope) {
        try {
            return field.get(null);
        } catch (Exception e) {
            throw new TemplateException(e.getMessage(), location, e);
        }
    }

    public String toString() {
        return clazz.getName() + "::" + fieldName;
    }
}







