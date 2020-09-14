/**
 * Copyright (c) 2011-2019, James Zhan 詹波 (jfinal@126.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.yizhu.record.template.stat.ast;

import space.yizhu.record.template.Env;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.expr.ast.Logic;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;

/**
 * ElseIf
 */
public class ElseIf extends Stat {

    private Expr cond;
    private Stat stat;
    private Stat elseIfOrElse;

    public ElseIf(ExprList cond, StatList statList, Location location) {
        if (cond.length() == 0) {
            throw new ParseException("The condition expression of #else if statement can not be blank", location);
        }
        this.cond = cond.getActualExpr();
        this.stat = statList.getActualStat();
    }

    /**
     * take over setStat(...) method of super class
     */
    public void setStat(Stat elseIfOrElse) {
        this.elseIfOrElse = elseIfOrElse;
    }

    public void exec(Env env, Scope scope, Writer writer) {
        if (Logic.isTrue(cond.eval(scope))) {
            stat.exec(env, scope, writer);
        } else if (elseIfOrElse != null) {
            elseIfOrElse.exec(env, scope, writer);
        }
    }
}





