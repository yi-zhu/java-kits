

package space.yizhu.record.template.ext.directive;

import space.yizhu.record.template.Directive;
import space.yizhu.record.template.Env;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Scope;


public class EscapeDirective extends Directive {

    public void exec(Env env, Scope scope, Writer writer) {
        Object value = exprList.eval(scope);
        if (value != null) {
            write(writer, escape(value.toString()));
        }
    }

    
    private String escape(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }

        int len = str.length();
        StringBuilder ret = new StringBuilder(len * 2);
        for (int i = 0; i < len; i++) {
            char cur = str.charAt(i);
            switch (cur) {
                case '<':
                    ret.append("&lt;");
                    break;
                case '>':
                    ret.append("&gt;");
                    break;
                case '"':
                    ret.append("&quot;");
                    break;
                case '\'':
                    
                    ret.append("&#39;");
                    break;
                case '&':
                    ret.append("&amp;");
                    break;
                default:
                    ret.append(cur);
                    break;
            }
        }

        return ret.toString();
    }
}
