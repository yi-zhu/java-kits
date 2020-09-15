

package space.yizhu.record.template.ext.directive;

import java.io.IOException;
import java.util.Date;

import space.yizhu.record.template.Directive;
import space.yizhu.record.template.Env;
import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class DateDirective extends Directive {

    private Expr valueExpr;
    private Expr datePatternExpr;
    private int paraNum;

    public void setExprList(ExprList exprList) {
        this.paraNum = exprList.length();
        if (paraNum > 2) {
            throw new ParseException("Wrong number parameter of #date directive, two parameters allowed at most", location);
        }

        if (paraNum == 0) {
            this.valueExpr = null;
            this.datePatternExpr = null;
        } else if (paraNum == 1) {
            this.valueExpr = exprList.getExpr(0);
            this.datePatternExpr = null;
        } else if (paraNum == 2) {
            this.valueExpr = exprList.getExpr(0);
            this.datePatternExpr = exprList.getExpr(1);
        }
    }

    public void exec(Env env, Scope scope, Writer writer) {
        if (paraNum == 1) {
            outputWithoutDatePattern(env, scope, writer);
        } else if (paraNum == 2) {
            outputWithDatePattern(env, scope, writer);
        } else {
            outputToday(env, writer);
        }
    }

    private void outputToday(Env env, Writer writer) {
        write(writer, new Date(), env.getEngineConfig().getDatePattern());
    }

    private void outputWithoutDatePattern(Env env, Scope scope, Writer writer) {
        Object value = valueExpr.eval(scope);
        if (value instanceof Date) {
            write(writer, (Date) value, env.getEngineConfig().getDatePattern());
        } else if (value != null) {
            throw new TemplateException("The first parameter date of #date directive must be Date type", location);
        }
    }

    private void outputWithDatePattern(Env env, Scope scope, Writer writer) {
        Object value = valueExpr.eval(scope);
        if (value instanceof Date) {
            Object datePattern = this.datePatternExpr.eval(scope);
            if (datePattern instanceof String) {
                write(writer, (Date) value, (String) datePattern);
            } else {
                throw new TemplateException("The sencond parameter datePattern of #date directive must be String", location);
            }
        } else if (value != null) {
            throw new TemplateException("The first parameter date of #date directive must be Date type", location);
        }
    }

    private void write(Writer writer, Date date, String datePattern) {
        try {
            writer.write(date, datePattern);
        } catch (IOException e) {
            throw new TemplateException(e.getMessage(), location, e);
        }
    }
}








