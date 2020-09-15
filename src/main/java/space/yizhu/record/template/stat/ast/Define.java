

package space.yizhu.record.template.stat.ast;

import space.yizhu.record.template.Env;
import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.expr.ast.Id;
import space.yizhu.record.template.io.Writer;


public class Define extends Stat {

    private static final String[] NULL_PARAMETER_NAMES = new String[0];

    private String functionName;
    private String[] parameterNames;
    private Stat stat;

    public Define(String functionName, ExprList exprList, StatList statList, Location location) {
        setLocation(location);
        this.functionName = functionName;
        this.stat = statList.getActualStat();

        Expr[] exprArray = exprList.getExprArray();
        if (exprArray.length == 0) {
            this.parameterNames = NULL_PARAMETER_NAMES;
            return;
        }

        parameterNames = new String[exprArray.length];
        for (int i = 0; i < exprArray.length; i++) {
            if (exprArray[i] instanceof Id) {
                parameterNames[i] = ((Id) exprArray[i]).getId();
            } else {
                throw new ParseException("The parameter of template function definition must be identifier", location);
            }
        }
    }

    public String getFunctionName() {
        return functionName;
    }

    public String[] getParameterNames() {
        return parameterNames;
    }

    
    public void exec(Env env, Scope scope, Writer writer) {

    }

    
    public void call(Env env, Scope scope, ExprList exprList, Writer writer) {
        if (exprList.length() != parameterNames.length) {
            throw new TemplateException("Wrong number of argument to call the template function, right number is: " + parameterNames.length, location);
        }

        scope = new Scope(scope);
        if (exprList.length() > 0) {
            Object[] parameterValues = exprList.evalExprList(scope);
            for (int i = 0; i < parameterValues.length; i++) {
                scope.setLocal(parameterNames[i], parameterValues[i]);    
            }
        }

        stat.exec(env, scope, writer);
        scope.getCtrl().setJumpNone();    
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("#define ").append(functionName).append("(");
        for (int i = 0; i < parameterNames.length; i++) {
            if (i > 0) {
                ret.append(", ");
            }
            ret.append(parameterNames[i]);
        }
        return ret.append(")").toString();
    }

    
    
    private Env envForDevMode;

    public void setEnvForDevMode(Env envForDevMode) {
        this.envForDevMode = envForDevMode;
    }

    public boolean isSourceModifiedForDevMode() {
        if (envForDevMode == null) {
            throw new IllegalStateException("Check engine config: setDevMode(...) must be invoked before addSharedFunction(...)");
        }
        return envForDevMode.isSourceListModified();
    }
}



