

package space.yizhu.record.template.stat.ast;

import space.yizhu.record.template.Env;
import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Scope;


public class Call extends Stat {

    private String funcName;
    private ExprList exprList;
    private boolean callIfDefined;

    public Call(String funcName, ExprList exprList, boolean callIfDefined) {
        this.funcName = funcName;
        this.exprList = exprList;
        this.callIfDefined = callIfDefined;
    }

    public void exec(Env env, Scope scope, Writer writer) {
        Define function = env.getFunction(funcName);
        if (function != null) {
            function.call(env, scope, exprList, writer);
        } else if (callIfDefined) {
            return;
        } else {
            throw new TemplateException("Template function not defined: " + funcName, location);
        }
    }
}

