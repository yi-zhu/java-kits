

package space.yizhu.record.template.expr.ast;

import space.yizhu.kits.HashKit;
import space.yizhu.kits.StrKit;
import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class Field extends Expr {

    private Expr expr;
    private String fieldName;
    private String getterName;
    private long getterNameHash;

    public Field(Expr expr, String fieldName, Location location) {
        if (expr == null) {
            throw new ParseException("The object for field access can not be null", location);
        }
        this.expr = expr;
        this.fieldName = fieldName;
        this.getterName = "get" + StrKit.firstCharToUpperCase(fieldName);
        
        this.getterNameHash = HashKit.fnv1a64(getterName);
        this.location = location;
    }

    public Object eval(Scope scope) {
        Object target = expr.eval(scope);
        if (target == null) {
            if (scope.getCtrl().isNullSafe()) {
                return null;
            }
            if (expr instanceof Id) {
                String id = ((Id) expr).getId();
                throw new TemplateException("\"" + id + "\" can not be null for accessed by \"" + id + "." + fieldName + "\"", location);
            }
            throw new TemplateException("Can not accessed by \"" + fieldName + "\" field from null target", location);
        }


        try {
            Class<?> targetClass = target.getClass();
            Object key = FieldKeyBuilder.instance.getFieldKey(targetClass, getterNameHash);
            FieldGetter fieldGetter = FieldKit.getFieldGetter(key, targetClass, fieldName);
            if (fieldGetter.notNull()) {
                return fieldGetter.get(target, fieldName);
            }
        } catch (TemplateException | ParseException e) {
            throw e;
        } catch (Exception e) {
            throw new TemplateException(e.getMessage(), location, e);
        }


        if (scope.getCtrl().isNullSafe()) {
            return null;
        }
        if (expr instanceof Id) {
            String id = ((Id) expr).getId();
            throw new TemplateException("public field not found: \"" + id + "." + fieldName + "\" and public getter method not found: \"" + id + "." + getterName + "()\"", location);
        }
        throw new TemplateException("public field not found: \"" + fieldName + "\" and public getter method not found: \"" + getterName + "()\"", location);
    }

    
    
    
}






