

package space.yizhu.record.template.stat.ast;

import space.yizhu.record.template.Env;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Scope;


public class Default extends Stat {

    private Stat stat;

    public Default(StatList statList) {
        this.stat = statList.getActualStat();
    }

    public void exec(Env env, Scope scope, Writer writer) {
        stat.exec(env, scope, writer);
    }
}
