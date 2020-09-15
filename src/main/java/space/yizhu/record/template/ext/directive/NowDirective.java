

package space.yizhu.record.template.ext.directive;

import java.io.IOException;
import java.util.Date;

import space.yizhu.record.template.Directive;
import space.yizhu.record.template.Env;
import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class NowDirective extends Directive {

    public void setExprList(ExprList exprList) {
        if (exprList.length() > 1) {
            throw new ParseException("#now directive support one parameter only", location);
        }
        super.setExprList(exprList);
    }

    public void exec(Env env, Scope scope, Writer writer) {
        String datePattern;
        if (exprList.length() == 0) {
            datePattern = env.getEngineConfig().getDatePattern();
        } else {
            Object dp = exprList.eval(scope);
            if (dp instanceof String) {
                datePattern = (String) dp;
            } else {
                throw new TemplateException("The parameter of #now directive must be String", location);
            }
        }

        try {
            writer.write(new Date(), datePattern);
        } catch (IOException e) {
            throw new TemplateException(e.getMessage(), location, e);
        }
    }
}



