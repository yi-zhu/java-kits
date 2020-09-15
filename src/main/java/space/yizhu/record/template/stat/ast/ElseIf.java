

package space.yizhu.record.template.stat.ast;

import space.yizhu.record.template.Env;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.expr.ast.Logic;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class ElseIf extends Stat {

    private Expr cond;
    private Stat stat;
    private Stat elseIfOrElse;

    public ElseIf(ExprList cond, StatList statList, Location location) {
        if (cond.length() == 0) {
            throw new ParseException("The condition expression of #else if statement can not be blank", location);
        }
        this.cond = cond.getActualExpr();
        this.stat = statList.getActualStat();
    }

    
    public void setStat(Stat elseIfOrElse) {
        this.elseIfOrElse = elseIfOrElse;
    }

    public void exec(Env env, Scope scope, Writer writer) {
        if (Logic.isTrue(cond.eval(scope))) {
            stat.exec(env, scope, writer);
        } else if (elseIfOrElse != null) {
            elseIfOrElse.exec(env, scope, writer);
        }
    }
}





