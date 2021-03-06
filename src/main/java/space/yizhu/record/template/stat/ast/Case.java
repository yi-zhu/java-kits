

package space.yizhu.record.template.stat.ast;

import space.yizhu.record.template.Env;
import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class Case extends Stat implements CaseSetter {

    private Expr[] exprArray;
    private Stat stat;
    private Case nextCase;

    public Case(ExprList exprList, StatList statList, Location location) {
        if (exprList.length() == 0) {
            throw new ParseException("The parameter of #case directive can not be blank", location);
        }

        this.exprArray = exprList.getExprArray();
        this.stat = statList.getActualStat();
    }

    public void setNextCase(Case nextCase) {
        this.nextCase = nextCase;
    }

    public void exec(Env env, Scope scope, Writer writer) {
        throw new TemplateException("#case 指令的 exec 不能被调用", location);
    }

    boolean execIfMatch(Object switchValue, Env env, Scope scope, Writer writer) {
        if (exprArray.length == 1) {
            Object value = exprArray[0].eval(scope);

            
            if (value == switchValue) {
                stat.exec(env, scope, writer);
                return true;
            }

            if (value != null && value.equals(switchValue)) {
                stat.exec(env, scope, writer);
                return true;
            }
        } else {
            for (Expr expr : exprArray) {
                Object value = expr.eval(scope);

                
                if (value == switchValue) {
                    stat.exec(env, scope, writer);
                    return true;
                }

                if (value != null && value.equals(switchValue)) {
                    stat.exec(env, scope, writer);
                    return true;
                }
            }
        }

        return nextCase != null ? nextCase.execIfMatch(switchValue, env, scope, writer) : false;
    }
}


