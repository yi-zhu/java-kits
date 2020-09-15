

package space.yizhu.record.template;

import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.stat.ast.Stat;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.stat.ast.Stat;


public abstract class Directive extends Stat {

    
    protected ExprList exprList;

    
    protected Stat stat;

    
    public void setExprList(ExprList exprList) {
        this.exprList = exprList;
    }

    
    public void setStat(Stat stat) {
        this.stat = stat;
    }
}





