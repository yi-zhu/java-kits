

package space.yizhu.record.template.stat.ast;

import space.yizhu.record.template.Env;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Scope;


public class Return extends Stat {

    public static final Return me = new Return();

    private Return() {
    }

    public void exec(Env env, Scope scope, Writer writer) {
        scope.getCtrl().setReturn();
    }
}







