

package space.yizhu.record.template.source;


public class ClassPathSourceFactory implements ISourceFactory {

    public ISource getSource(String baseTemplatePath, String fileName, String encoding) {
        return new ClassPathSource(baseTemplatePath, fileName, encoding);
    }
}



