

package space.yizhu.record.plugin.activerecord.generator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class TableMeta implements Serializable {

    public String name;                    
    public String remarks;                
    public String primaryKey;            
    public List<ColumnMeta> columnMetas = new ArrayList<ColumnMeta>();    

    

    public String baseModelName;        
    public String baseModelContent;        

    public String modelName;            
    public String modelContent;            

    

    public int colNameMaxLen = "Field".length();            
    public int colTypeMaxLen = "Type".length();                
    public int colDefaultValueMaxLen = "Default".length();    
}




