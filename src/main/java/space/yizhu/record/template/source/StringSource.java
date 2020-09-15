

package space.yizhu.record.template.source;

import space.yizhu.kits.HashKit;
import space.yizhu.kits.StrKit;
import space.yizhu.record.template.EngineConfig;


public class StringSource implements ISource {

    private String cacheKey;
    private StringBuilder content;

    
    public StringSource(String content, boolean cache) {
        if (StrKit.isBlank(content)) {
            throw new IllegalArgumentException("content can not be blank");
        }
        this.content = new StringBuilder(content);
        this.cacheKey = cache ? HashKit.md5(content) : null;    
    }

    public StringSource(StringBuilder content, boolean cache) {
        if (content == null || content.length() == 0) {
            throw new IllegalArgumentException("content can not be blank");
        }
        this.content = content;
        this.cacheKey = cache ? HashKit.md5(content.toString()) : null;    
    }

    public boolean isModified() {
        return false;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public StringBuilder getContent() {
        return content;
    }

    public String getEncoding() {
        return EngineConfig.DEFAULT_ENCODING;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("cacheKey : ").append(cacheKey).append("\n");
        sb.append("content : ").append(content).append("\n");
        return sb.toString();
    }
}







