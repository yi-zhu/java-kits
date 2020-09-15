

package space.yizhu.record.plugin.activerecord.generator;

import java.io.Serializable;


@SuppressWarnings("serial")
public class ColumnMeta implements Serializable {

    public String name;                
    public String javaType;            
    public String attrName;            

    

    
    public String type;                
    public String isNullable;        
    public String isPrimaryKey;        
    public String defaultValue;        
    public String remarks;            
}

