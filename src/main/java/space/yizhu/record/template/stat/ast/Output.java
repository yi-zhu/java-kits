

package space.yizhu.record.template.stat.ast;

import space.yizhu.record.template.Env;
import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class Output extends Stat {

    private Expr expr;

    public Output(ExprList exprList, Location location) {
        if (exprList.length() == 0) {
            throw new ParseException("The expression of output directive like #(expression) can not be blank", location);
        }
        this.expr = exprList.getActualExpr();
    }

    public void exec(Env env, Scope scope, Writer writer) {
        try {
            Object value = expr.eval(scope);

            if (value instanceof String) {
                String str = (String) value;
                writer.write(str, 0, str.length());
            } else if (value instanceof Number) {
                Class<?> c = value.getClass();
                if (c == Integer.class) {
                    writer.write((Integer) value);
                } else if (c == Long.class) {
                    writer.write((Long) value);
                } else if (c == Double.class) {
                    writer.write((Double) value);
                } else if (c == Float.class) {
                    writer.write((Float) value);
                } else if (c == Short.class) {
                    writer.write((Short) value);
                } else {
                    writer.write(value.toString());
                }
            } else if (value instanceof Boolean) {
                writer.write((Boolean) value);
            } else if (value != null) {
                writer.write(value.toString());
            }
        } catch (TemplateException | ParseException e) {
            throw e;
        } catch (Exception e) {
            throw new TemplateException(e.getMessage(), location, e);
        }
    }
}




