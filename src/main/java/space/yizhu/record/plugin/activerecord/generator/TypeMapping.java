

package space.yizhu.record.plugin.activerecord.generator;

import java.util.HashMap;
import java.util.Map;


public class TypeMapping {

    @SuppressWarnings("serial")
    protected Map<String, String> map = new HashMap<String, String>(32) {{
        
        
        

        
        put("java.sql.Date", "java.util.Date");

        
        put("java.sql.Time", "java.util.Date");

        
        put("java.sql.Timestamp", "java.util.Date");

        
        
        put("[B", "byte[]");

        

        
        put("java.lang.String", "java.lang.String");

        
        put("java.lang.Integer", "java.lang.Integer");

        
        put("java.lang.Long", "java.lang.Long");

        
        put("java.lang.Double", "java.lang.Double");

        
        put("java.lang.Float", "java.lang.Float");

        
        put("java.lang.Boolean", "java.lang.Boolean");

        
        put("java.math.BigDecimal", "java.math.BigDecimal");

        
        put("java.math.BigInteger", "java.math.BigInteger");

        
        put("java.lang.Short", "java.lang.Short");

        
        put("java.lang.Byte", "java.lang.Byte");
    }};

    public String getType(String typeString) {
        return map.get(typeString);
    }
}
