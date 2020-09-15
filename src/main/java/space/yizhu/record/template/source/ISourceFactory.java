

package space.yizhu.record.template.source;


public interface ISourceFactory {
    ISource getSource(String baseTemplatePath, String fileName, String encoding);
}




