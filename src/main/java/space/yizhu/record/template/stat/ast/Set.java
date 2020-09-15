

package space.yizhu.record.template.stat.ast;

import space.yizhu.record.template.Env;
import space.yizhu.record.template.expr.ast.Assign;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class Set extends Stat {

    private Expr expr;

    public Set(ExprList exprList, Location location) {
        if (exprList.length() == 0) {
            throw new ParseException("The parameter of #set directive can not be blank", location);
        }

        for (Expr expr : exprList.getExprArray()) {
            if (!(expr instanceof Assign)) {
                throw new ParseException("#set directive only supports assignment expressions", location);
            }
        }
        this.expr = exprList.getActualExpr();
    }

    public void exec(Env env, Scope scope, Writer writer) {
        scope.getCtrl().setWisdomAssignment();
        expr.eval(scope);
    }
}

