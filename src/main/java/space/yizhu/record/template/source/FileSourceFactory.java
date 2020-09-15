

package space.yizhu.record.template.source;


public class FileSourceFactory implements ISourceFactory {

    public ISource getSource(String baseTemplatePath, String fileName, String encoding) {
        return new FileSource(baseTemplatePath, fileName, encoding);
    }
}




