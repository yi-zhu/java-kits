

package space.yizhu.record.template.io;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public class DateFormats {

    private Map<String, SimpleDateFormat> map = new HashMap<String, SimpleDateFormat>();

    public SimpleDateFormat getDateFormat(String datePattern) {
        SimpleDateFormat ret = map.get(datePattern);
        if (ret == null) {
            ret = new SimpleDateFormat(datePattern);
            map.put(datePattern, ret);
        }
        return ret;
    }
}






