

package space.yizhu.record.plugin.activerecord.sql;

import space.yizhu.record.plugin.activerecord.SqlPara;
import space.yizhu.record.template.Directive;
import space.yizhu.record.template.Env;
import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.expr.ast.Const;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.expr.ast.Id;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;
import space.yizhu.record.template.Directive;
import space.yizhu.record.template.Env;
import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.expr.ast.Const;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.expr.ast.Id;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class ParaDirective extends Directive {

    private int index = -1;
    private String paraName = null;
    private static boolean checkParaAssigned = true;

    public static void setCheckParaAssigned(boolean checkParaAssigned) {
        ParaDirective.checkParaAssigned = checkParaAssigned;
    }

    public void setExprList(ExprList exprList) {
        if (exprList.length() == 0) {
            throw new ParseException("The parameter of #para directive can not be blank", location);
        }

        if (exprList.length() == 1) {
            Expr expr = exprList.getExpr(0);
            if (expr instanceof Const && ((Const) expr).isInt()) {
                index = ((Const) expr).getInt();
                if (index < 0) {
                    throw new ParseException("The index of para array must greater than -1", location);
                }
            }
        }

        if (checkParaAssigned && exprList.getLastExpr() instanceof Id) {
            Id id = (Id) exprList.getLastExpr();
            paraName = id.getId();
        }

        this.exprList = exprList;
    }

    public void exec(Env env, Scope scope, Writer writer) {
        SqlPara sqlPara = (SqlPara) scope.get(SqlKit.SQL_PARA_KEY);
        if (sqlPara == null) {
            throw new TemplateException("#para directive invoked by getSqlPara(...) method only", location);
        }

        write(writer, "?");
        if (index == -1) {
            
            
            if (checkParaAssigned && paraName != null && !scope.exists(paraName)) {
                throw new TemplateException("The parameter \"" + paraName + "\" must be assigned", location);
            }

            sqlPara.addPara(exprList.eval(scope));
        } else {
            Object[] paras = (Object[]) scope.get(SqlKit.PARA_ARRAY_KEY);
            if (paras == null) {
                throw new TemplateException("The #para(" + index + ") directive must invoked by getSqlPara(String, Object...) method", location);
            }
            if (index >= paras.length) {
                throw new TemplateException("The index of #para directive is out of bounds: " + index, location);
            }
            sqlPara.addPara(paras[index]);
        }
    }
}



