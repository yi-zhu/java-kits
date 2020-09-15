

package space.yizhu.record.plugin.druid;

import javax.servlet.http.HttpServletRequest;


public interface IDruidStatViewAuth {
    boolean isPermitted(HttpServletRequest request);
}
