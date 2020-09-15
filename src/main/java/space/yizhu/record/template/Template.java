

package space.yizhu.record.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

import space.yizhu.record.template.io.ByteWriter;
import space.yizhu.record.template.io.CharWriter;
import space.yizhu.record.template.io.FastStringWriter;
import space.yizhu.record.template.stat.Scope;
import space.yizhu.record.template.stat.ast.Stat;


public class Template {

    private Env env;
    private Stat ast;

    public Template(Env env, Stat ast) {
        if (env == null || ast == null) {
            throw new IllegalArgumentException("env and ast can not be null");
        }
        this.env = env;
        this.ast = ast;
    }

    
    public void render(Map<?, ?> data, OutputStream outputStream) {
        ByteWriter byteWriter = env.engineConfig.writerBuffer.getByteWriter(outputStream);
        try {
            ast.exec(env, new Scope(data, env.engineConfig.sharedObjectMap), byteWriter);
        } finally {
            byteWriter.close();
        }
    }

    
    public void render(OutputStream outputStream) {
        render(null, outputStream);
    }

    
    public void render(Map<?, ?> data, Writer writer) {
        CharWriter charWriter = env.engineConfig.writerBuffer.getCharWriter(writer);
        try {
            ast.exec(env, new Scope(data, env.engineConfig.sharedObjectMap), charWriter);
        } finally {
            charWriter.close();
        }
    }

    
    public void render(Writer writer) {
        render(null, writer);
    }

    
    public String renderToString(Map<?, ?> data) {
        FastStringWriter fsw = env.engineConfig.writerBuffer.getFastStringWriter();
        try {
            render(data, fsw);
            return fsw.toString();
        } finally {
            fsw.close();
        }
    }

    
    public StringBuilder renderToStringBuilder(Map<?, ?> data) {
        FastStringWriter fsw = new FastStringWriter();
        render(data, fsw);
        return fsw.getBuffer();
    }

    
    public void render(Map<?, ?> data, File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            render(data, fos);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    
    public void render(Map<?, ?> data, String fileName) {
        render(data, new File(fileName));
    }

    public boolean isModified() {
        return env.isSourceListModified();
    }
}





