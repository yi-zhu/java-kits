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

import java.io.IOException;

import space.yizhu.record.template.Env;
import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.Scope;

/**
 * Stat
 */
public abstract class Stat {

    protected Location location;

    public Stat setLocation(Location location) {
        this.location = location;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public void setExprList(ExprList exprList) {
    }

    public void setStat(Stat stat) {
    }

    public abstract void exec(Env env, Scope scope, Writer writer);

    public boolean hasEnd() {
        return false;
    }

    protected void write(Writer writer, String str) {
        try {
            writer.write(str, 0, str.length());
        } catch (IOException e) {
            throw new TemplateException(e.getMessage(), location, e);
        }
    }
}


