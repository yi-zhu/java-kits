

package space.yizhu.record.template;

import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.Location;


@SuppressWarnings("serial")
public class TemplateException extends RuntimeException {

    public TemplateException(String msg, Location loc) {
        super(loc != null ? msg + loc : msg);
    }

    public TemplateException(String msg, Location loc, Throwable t) {
        super(loc != null ? msg + loc : msg, t);
    }
}


