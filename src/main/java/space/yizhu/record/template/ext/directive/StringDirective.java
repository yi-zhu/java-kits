

package space.yizhu.record.template.ext.directive;

import space.yizhu.record.template.Directive;
import space.yizhu.record.template.Env;
import space.yizhu.record.template.io.CharWriter;
import space.yizhu.record.template.io.FastStringWriter;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.expr.ast.Const;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.expr.ast.Id;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class StringDirective extends Directive {

    private String name;
    private boolean isLocalAssignment = false;

    public void setExprList(ExprList exprList) {
        Expr[] exprArray = exprList.getExprArray();
        if (exprArray.length == 0) {
            throw new ParseException("#string directive parameter cant not be null", location);
        }
        if (exprArray.length > 2) {
            throw new ParseException("wrong number of #string directive parameter, two parameters allowed at most", location);
        }

        if (!(exprArray[0] instanceof Id)) {
            throw new ParseException("#string first parameter must be identifier", location);
        }
        this.name = ((Id) exprArray[0]).getId();
        if (exprArray.length == 2) {
            if (exprArray[1] instanceof Const) {
                if (((Const) exprArray[1]).isBoolean()) {
                    this.isLocalAssignment = ((Const) exprArray[1]).getBoolean();
                } else {
                    throw new ParseException("#string sencond parameter must be boolean", location);
                }
            }
        }
    }

    public void exec(Env env, Scope scope, Writer writer) {
        CharWriter charWriter = new CharWriter(64);
        FastStringWriter fsw = new FastStringWriter();
        charWriter.init(fsw);
        try {
            stat.exec(env, scope, charWriter);
        } finally {
            charWriter.close();
        }

        if (this.isLocalAssignment) {
            scope.setLocal(name, fsw.toString());
        } else {
            scope.set(name, fsw.toString());
        }
    }

    
    public boolean hasEnd() {
        return true;
    }
}








