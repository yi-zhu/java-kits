

package space.yizhu.record.template.stat.ast;

import java.util.Iterator;

import space.yizhu.record.template.Env;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ForCtrl;
import space.yizhu.record.template.expr.ast.Logic;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Ctrl;
import space.yizhu.record.template.stat.Scope;


public class For extends Stat {

    private ForCtrl forCtrl;
    private Stat stat;
    private Stat _else;

    public For(ForCtrl forCtrl, StatList statList, Stat _else) {
        this.forCtrl = forCtrl;
        this.stat = statList.getActualStat();
        this._else = _else;
    }

    public void exec(Env env, Scope scope, Writer writer) {
        scope = new Scope(scope);
        if (forCtrl.isIterator()) {
            forIterator(env, scope, writer);
        } else {
            forLoop(env, scope, writer);
        }
    }

    
    private void forIterator(Env env, Scope scope, Writer writer) {
        Ctrl ctrl = scope.getCtrl();
        Object outer = scope.get("for");
        ctrl.setLocalAssignment();
        ForIteratorStatus forIteratorStatus = new ForIteratorStatus(outer, forCtrl.getExpr().eval(scope), location);
        ctrl.setWisdomAssignment();
        scope.setLocal("for", forIteratorStatus);

        Iterator<?> it = forIteratorStatus.getIterator();
        String itemName = forCtrl.getId();
        while (it.hasNext()) {
            scope.setLocal(itemName, it.next());
            stat.exec(env, scope, writer);
            forIteratorStatus.nextState();

            if (ctrl.isJump()) {
                if (ctrl.isBreak()) {
                    ctrl.setJumpNone();
                    break;
                } else if (ctrl.isContinue()) {
                    ctrl.setJumpNone();
                    continue;
                } else {
                    return;
                }
            }
        }

        if (_else != null && forIteratorStatus.getIndex() == 0) {
            _else.exec(env, scope, writer);
        }
    }

    
    private void forLoop(Env env, Scope scope, Writer writer) {
        Ctrl ctrl = scope.getCtrl();
        Object outer = scope.get("for");
        ForLoopStatus forLoopStatus = new ForLoopStatus(outer);
        scope.setLocal("for", forLoopStatus);

        Expr init = forCtrl.getInit();
        Expr cond = forCtrl.getCond();
        Expr update = forCtrl.getUpdate();

        ctrl.setLocalAssignment();
        for (init.eval(scope); cond == null || Logic.isTrue(cond.eval(scope)); update.eval(scope)) {
            ctrl.setWisdomAssignment();
            stat.exec(env, scope, writer);
            ctrl.setLocalAssignment();
            forLoopStatus.nextState();

            if (ctrl.isJump()) {
                if (ctrl.isBreak()) {
                    ctrl.setJumpNone();
                    break;
                } else if (ctrl.isContinue()) {
                    ctrl.setJumpNone();
                    continue;
                } else {
                    ctrl.setWisdomAssignment();
                    return;
                }
            }
        }

        ctrl.setWisdomAssignment();
        if (_else != null && forLoopStatus.getIndex() == 0) {
            _else.exec(env, scope, writer);
        }
    }
}






