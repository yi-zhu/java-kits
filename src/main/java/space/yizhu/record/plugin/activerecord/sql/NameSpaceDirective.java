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

package space.yizhu.record.plugin.activerecord.sql;

import space.yizhu.record.template.Directive;
import space.yizhu.record.template.Env;
import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.expr.ast.Const;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;
import space.yizhu.record.template.Directive;
import space.yizhu.record.template.Env;
import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.expr.ast.Const;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Scope;

/**
 * NameSpaceDirective
 */
public class NameSpaceDirective extends Directive {

    static final String NAME_SPACE_KEY = "_NAME_SPACE_";

    private String nameSpace;

    public void setExprList(ExprList exprList) {
        if (exprList.length() == 0) {
            throw new ParseException("The parameter of #namespace directive can not be blank", location);
        }
        if (exprList.length() > 1) {
            throw new ParseException("Only one parameter allowed for #namespace directive", location);
        }
        Expr expr = exprList.getExpr(0);
        if (expr instanceof Const && ((Const) expr).isStr()) {
        } else {
            throw new ParseException("The parameter of #namespace directive must be String", location);
        }

        this.nameSpace = ((Const) expr).getStr();
    }

    public void exec(Env env, Scope scope, Writer writer) {
        if (scope.get(NAME_SPACE_KEY) != null) {
            throw new TemplateException("#namespace directive can not be nested", location);
        }
        scope.set(NAME_SPACE_KEY, nameSpace);
        try {
            stat.exec(env, scope, writer);
        } finally {
            scope.remove(NAME_SPACE_KEY);
        }
    }

    public boolean hasEnd() {
        return true;
    }
}





