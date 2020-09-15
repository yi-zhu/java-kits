

package space.yizhu.record.template.expr.ast;

import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class StaticMethod extends Expr {

    private Class<?> clazz;
    private String methodName;
    private ExprList exprList;

    public StaticMethod(String className, String methodName, Location location) {
        init(className, methodName, ExprList.NULL_EXPR_LIST, location);
    }

    public StaticMethod(String className, String methodName, ExprList exprList, Location location) {
        if (exprList == null || exprList.length() == 0) {
            throw new ParseException("exprList can not be blank", location);
        }
        init(className, methodName, exprList, location);
    }

    private void init(String className, String methodName, ExprList exprList, Location location) {
        try {
            this.clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ParseException("Class not found: " + className, location, e);
        } catch (Exception e) {
            throw new ParseException(e.getMessage(), location, e);
        }
        this.methodName = methodName;
        this.exprList = exprList;
        this.location = location;
    }

    public Object eval(Scope scope) {
        Object[] argValues = exprList.evalExprList(scope);

        try {
            MethodInfo methodInfo = MethodKit.getMethod(clazz, methodName, argValues);

            if (methodInfo != null) {
                if (methodInfo.isStatic()) {
                    return methodInfo.invoke(null, argValues);
                } else {
                    throw new TemplateException(Method.buildMethodNotFoundSignature("Not public static method: " + clazz.getName() + "::", methodName, argValues), location);
                }
            } else {
                
                throw new TemplateException(Method.buildMethodNotFoundSignature("public static method not found: " + clazz.getName() + "::", methodName, argValues), location);
            }

        } catch (TemplateException | ParseException e) {
            throw e;
        } catch (Exception e) {
            throw new TemplateException(e.getMessage(), location, e);
        }
    }
}




