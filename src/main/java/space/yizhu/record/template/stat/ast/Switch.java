

package space.yizhu.record.template.stat.ast;

import space.yizhu.record.template.Env;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class Switch extends Stat implements CaseSetter {

    private Expr expr;
    private Case nextCase;
    private Default _default;

    public Switch(ExprList exprList, Location location) {
        if (exprList.length() == 0) {
            throw new ParseException("The parameter of #switch directive can not be blank", location);
        }
        this.expr = exprList.getActualExpr();
    }

    public void setNextCase(Case nextCase) {
        this.nextCase = nextCase;
    }

    public void setDefault(Default _default, Location location) {
        if (this._default != null) {
            throw new ParseException("The #default case of #switch is already defined", location);
        }
        this._default = _default;
    }

    public void exec(Env env, Scope scope, Writer writer) {
        Object switchValue = expr.eval(scope);

        if (nextCase != null && nextCase.execIfMatch(switchValue, env, scope, writer)) {
            return;
        }

        if (_default != null) {
            _default.exec(env, scope, writer);
        }
    }

    public boolean hasEnd() {
        return true;
    }
}







