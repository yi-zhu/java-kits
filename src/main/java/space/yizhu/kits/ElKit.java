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

package space.yizhu.kits;

import java.util.Map;

import space.yizhu.record.template.Directive;
import space.yizhu.record.template.Engine;
import space.yizhu.record.template.Env;
import space.yizhu.record.template.Template;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Scope;

/**
 * EL 表达式语言求值工具类
 *
 * <pre>
 * 1：不带参示例
 * 	  Integer value = ElKit.eval("1 + 2 * 3");
 *
 * 2：带参示例
 * 	  Kv data = Kv.by("a", 2).set("b", 3);
 * 	  Integer value = ElKit.eval("1 + a * b", data);
 * </pre>
 */

public class ElKit {

    private static Engine engine = new Engine();
    private static final String RETURN_VALUE_KEY = "_RETURN_VALUE_";

    static {
        engine.addDirective("eval", InnerEvalDirective.class);
    }

    public static Engine getEngine() {
        return engine;
    }

    public static <T> T eval(String expr) {
        return eval(expr, Kv.create());
    }

    @SuppressWarnings("unchecked")
    public static <T> T eval(String expr, Map<?, ?> data) {
        String stringTemplate = "#eval(" + expr + ")";
        Template template = engine.getTemplateByString(stringTemplate);
        template.render(data, (java.io.Writer) null);
        return (T) data.get(RETURN_VALUE_KEY);
    }

    public static class InnerEvalDirective extends Directive {
        public void exec(Env env, Scope scope, Writer writer) {
            Object value = exprList.eval(scope);
            scope.set(RETURN_VALUE_KEY, value);
        }
    }
}




