

package space.yizhu.record.template.ext.directive;

import java.util.ArrayList;

import space.yizhu.record.template.Directive;
import space.yizhu.record.template.Env;
import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.expr.ast.Const;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;
import space.yizhu.record.template.stat.ast.Define;
import space.yizhu.record.template.expr.ast.Const;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;
import space.yizhu.record.template.stat.ast.Define;


public class CallDirective extends Directive {

    protected Expr funcNameExpr;
    protected ExprList paraExpr;

    protected boolean nullSafe = false;        

    public void setExprList(ExprList exprList) {
        int len = exprList.length();
        if (len == 0) {
            throw new ParseException("模板函数名不能缺失", location);
        }

        int index = 0;
        Expr expr = exprList.getExpr(index);
        if (expr instanceof Const && ((Const) expr).isBoolean()) {
            if (len == 1) {
                throw new ParseException("模板函数名不能缺失", location);
            }

            nullSafe = ((Const) expr).getBoolean();
            index++;
        }

        funcNameExpr = exprList.getExpr(index++);

        ArrayList<Expr> list = new ArrayList<Expr>();
        for (int i = index; i < len; i++) {
            list.add(exprList.getExpr(i));
        }
        paraExpr = new ExprList(list);
    }

    public void exec(Env env, Scope scope, Writer writer) {
        Object funcNameValue = funcNameExpr.eval(scope);
        if (funcNameValue == null) {
            if (nullSafe) {
                return;
            }
            throw new TemplateException("模板函数名为 null", location);
        }

        if (!(funcNameValue instanceof String)) {
            throw new TemplateException("模板函数名必须是字符串", location);
        }

        Define func = env.getFunction(funcNameValue.toString());

        if (func == null) {
            if (nullSafe) {
                return;
            }
            throw new TemplateException("模板函数未找到 : " + funcNameValue, location);
        }

        func.call(env, scope, paraExpr, writer);
    }
}




