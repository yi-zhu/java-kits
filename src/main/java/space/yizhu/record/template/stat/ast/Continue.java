

package space.yizhu.record.template.stat.ast;

import space.yizhu.record.template.Env;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Scope;


public class Continue extends Stat {

    public static final Continue me = new Continue();

    private Continue() {
    }

    public void exec(Env env, Scope scope, Writer writer) {
        scope.getCtrl().setContinue();
    }
}




