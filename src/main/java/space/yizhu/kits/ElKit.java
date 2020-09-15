

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




