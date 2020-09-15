

package space.yizhu.record.template.stat.ast;

import java.util.List;

import space.yizhu.record.template.Env;
import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Ctrl;
import space.yizhu.record.template.stat.Scope;


public class StatList extends Stat {

    public static final Stat NULL_STAT = NullStat.me;
    public static final Stat[] NULL_STAT_ARRAY = new Stat[0];

    private Stat[] statArray;

    public StatList(List<Stat> statList) {
        if (statList.size() > 0) {
            this.statArray = statList.toArray(new Stat[statList.size()]);
        } else {
            this.statArray = NULL_STAT_ARRAY;
        }
    }

    
    public Stat getActualStat() {
        if (statArray.length > 1) {
            return this;
        } else if (statArray.length == 1) {
            return statArray[0];
        } else {
            return NULL_STAT;
        }
    }

    public void exec(Env env, Scope scope, Writer writer) {
        Ctrl ctrl = scope.getCtrl();
        for (int i = 0; i < statArray.length; i++) {
            if (ctrl.isJump()) {
                break;
            }
            statArray[i].exec(env, scope, writer);
        }
    }

    public int length() {
        return statArray.length;
    }

    public Stat getStat(int index) {
        if (index < 0 || index >= statArray.length) {
            throw new TemplateException("Index out of bounds: index = " + index + ", length = " + statArray.length, location);
        }
        return statArray[index];
    }
}


