

package space.yizhu.record.template.expr.ast;

import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;


public class Ternary extends Expr {

    private Expr cond;
    private Expr exprOne;
    private Expr exprTwo;

    
    public Ternary(Expr cond, Expr exprOne, Expr exprTwo, Location location) {
        if (cond == null || exprOne == null || exprTwo == null) {
            throw new ParseException("The parameter of ternary expression can not be blank", location);
        }
        this.cond = cond;
        this.exprOne = exprOne;
        this.exprTwo = exprTwo;
        this.location = location;
    }

    public Object eval(Scope scope) {
        return Logic.isTrue(cond.eval(scope)) ? exprOne.eval(scope) : exprTwo.eval(scope);
    }
}








