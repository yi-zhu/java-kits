

package space.yizhu.record.template.stat;

import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.stat.ast.Output;


public class OutputDirectiveFactory {

    public static final OutputDirectiveFactory me = new OutputDirectiveFactory();

    public Output getOutputDirective(ExprList exprList, Location location) {
        return new Output(exprList, location);
    }
}


