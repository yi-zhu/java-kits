

package space.yizhu.record.template.source;


public interface ISource {

    
    boolean isModified();

    
    String getCacheKey();

    
    StringBuilder getContent();

    
    String getEncoding();
}


