

package space.yizhu.record.template.expr.ast;

import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class ForCtrl extends Expr {

    private String id;
    private Expr expr;

    private Expr init;
    private Expr cond;
    private Expr update;

    
    public ForCtrl(Id id, Expr expr, Location location) {
        if (expr == null) {
            throw new ParseException("The iterator target of #for statement can not be null", location);
        }
        this.id = id.getId();
        this.expr = expr;
        this.init = null;
        this.cond = null;
        this.update = null;
        this.location = location;
    }

    
    public ForCtrl(ExprList init, Expr cond, ExprList update, Location location) {
        this.init = init.getActualExpr();
        this.cond = cond;
        this.update = update.getActualExpr();
        this.id = null;
        this.expr = null;
        this.location = location;
    }

    public boolean isIterator() {
        return id != null;
    }

    public String getId() {
        return id;
    }

    public Expr getExpr() {
        return expr;
    }

    public Expr getInit() {
        return init;
    }

    public Expr getCond() {
        return cond;
    }

    public Expr getUpdate() {
        return update;
    }

    public Object eval(Scope scope) {
        throw new TemplateException("The eval(Scope scope) method can not be invoked", location);
    }
}


