

package space.yizhu.record.template.expr.ast;

import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.expr.ast.SharedMethodKit.SharedMethodInfo;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class SharedMethod extends Expr {

    private SharedMethodKit sharedMethodKit;
    private String methodName;
    private ExprList exprList;

    public SharedMethod(SharedMethodKit sharedMethodKit, String methodName, ExprList exprList, Location location) {
        if (MethodKit.isForbiddenMethod(methodName)) {
            throw new ParseException("Forbidden method: " + methodName, location);
        }
        this.sharedMethodKit = sharedMethodKit;
        this.methodName = methodName;
        this.exprList = exprList;
        this.location = location;
    }

    public Object eval(Scope scope) {
        Object[] argValues = exprList.evalExprList(scope);

        try {
            SharedMethodKit.SharedMethodInfo sharedMethodInfo = sharedMethodKit.getSharedMethodInfo(methodName, argValues);
            if (sharedMethodInfo != null) {
                return sharedMethodInfo.invoke(argValues);
            } else {
                
                throw new TemplateException(Method.buildMethodNotFoundSignature("Shared method not found: ", methodName, argValues), location);
            }

        } catch (TemplateException | ParseException e) {
            throw e;
        } catch (Exception e) {
            throw new TemplateException(e.getMessage(), location, e);
        }
    }
}




