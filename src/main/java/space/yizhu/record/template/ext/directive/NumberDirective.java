

package space.yizhu.record.template.ext.directive;

import java.text.DecimalFormat;

import space.yizhu.record.template.Directive;
import space.yizhu.record.template.Env;
import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class NumberDirective extends Directive {

    private Expr valueExpr;
    private Expr patternExpr;
    private int paraNum;

    public void setExprList(ExprList exprList) {
        this.paraNum = exprList.length();
        if (paraNum == 0) {
            throw new ParseException("The parameter of #number directive can not be blank", location);
        }
        if (paraNum > 2) {
            throw new ParseException("Wrong number parameter of #number directive, two parameters allowed at most", location);
        }

        if (paraNum == 1) {
            this.valueExpr = exprList.getExpr(0);
            this.patternExpr = null;
        } else if (paraNum == 2) {
            this.valueExpr = exprList.getExpr(0);
            this.patternExpr = exprList.getExpr(1);
        }
    }

    public void exec(Env env, Scope scope, Writer writer) {
        Object value = valueExpr.eval(scope);
        if (value == null) {
            return;
        }

        if (paraNum == 1) {
            outputWithoutPattern(writer, value);
        } else if (paraNum == 2) {
            outputWithPattern(scope, writer, value);
        }
    }

    private void outputWithoutPattern(Writer writer, Object value) {
        String ret = new DecimalFormat().format(value);
        write(writer, ret);
    }

    private void outputWithPattern(Scope scope, Writer writer, Object value) {
        Object pattern = patternExpr.eval(scope);
        if (!(pattern instanceof String)) {
            throw new TemplateException("The sencond parameter pattern of #number directive must be String", location);
        }

        String ret = new DecimalFormat((String) pattern).format(value);
        write(writer, ret);
    }
}





