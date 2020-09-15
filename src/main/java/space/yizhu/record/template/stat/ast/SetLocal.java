

package space.yizhu.record.template.stat.ast;

import space.yizhu.record.template.Env;
import space.yizhu.record.template.expr.ast.Assign;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Ctrl;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class SetLocal extends Stat {

    final Expr expr;

    public SetLocal(ExprList exprList, Location location) {
        if (exprList.length() == 0) {
            throw new ParseException("The parameter of #setLocal directive can not be blank", location);
        }

        for (Expr expr : exprList.getExprArray()) {
            if (!(expr instanceof Assign)) {
                throw new ParseException("#setLocal directive only supports assignment expressions", location);
            }
        }
        this.expr = exprList.getActualExpr();
    }

    public void exec(Env env, Scope scope, Writer writer) {
        Ctrl ctrl = scope.getCtrl();
        try {
            ctrl.setLocalAssignment();
            expr.eval(scope);
        } finally {
            ctrl.setWisdomAssignment();
        }
    }
}





