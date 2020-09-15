

package space.yizhu.record.template.stat.ast;

import space.yizhu.record.template.Env;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Scope;


public class Break extends Stat {

    public static final Break me = new Break();

    private Break() {
    }

    public void exec(Env env, Scope scope, Writer writer) {
        scope.getCtrl().setBreak();
    }
}



