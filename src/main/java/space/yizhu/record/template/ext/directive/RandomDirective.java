

package space.yizhu.record.template.ext.directive;

import java.io.IOException;

import space.yizhu.record.template.Directive;
import space.yizhu.record.template.Env;
import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.stat.Scope;
import space.yizhu.record.template.stat.Scope;


public class RandomDirective extends Directive {

    private java.util.Random random = new java.util.Random();

    public void exec(Env env, Scope scope, Writer writer) {
        try {
            writer.write(random.nextInt());
        } catch (IOException e) {
            throw new TemplateException(e.getMessage(), location, e);
        }
    }
}




