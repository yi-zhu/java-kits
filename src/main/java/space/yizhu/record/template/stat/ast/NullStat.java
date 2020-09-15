

package space.yizhu.record.template.stat.ast;

import space.yizhu.record.template.Env;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Scope;


public class NullStat extends Stat {

    public static final NullStat me = new NullStat();

    private NullStat() {
    }

    public void exec(Env env, Scope scope, Writer writer) {

    }
}





