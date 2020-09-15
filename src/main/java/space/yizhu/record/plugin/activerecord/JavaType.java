

package space.yizhu.record.plugin.activerecord;

import java.util.HashMap;
import java.util.Map;


public class JavaType {

    @SuppressWarnings("serial")
    private Map<String, Class<?>> strToType = new HashMap<String, Class<?>>(32) {{

        
        put("java.lang.String", java.lang.String.class);

        
        put("java.lang.Integer", java.lang.Integer.class);

        
        put("java.lang.Long", java.lang.Long.class);

        
        
        

        
        put("java.sql.Date", java.sql.Date.class);

        
        put("java.lang.Double", java.lang.Double.class);

        
        put("java.lang.Float", java.lang.Float.class);

        
        put("java.lang.Boolean", java.lang.Boolean.class);

        
        put("java.sql.Time", java.sql.Time.class);

        
        put("java.sql.Timestamp", java.sql.Timestamp.class);

        
        put("java.math.BigDecimal", java.math.BigDecimal.class);

        
        put("java.math.BigInteger", java.math.BigInteger.class);

        
        
        put("[B", byte[].class);

        
        
        put("java.lang.Short", java.lang.Short.class);
        put("java.lang.Byte", java.lang.Byte.class);
    }};

    public Class<?> getType(String typeString) {
        return strToType.get(typeString);
    }
}


