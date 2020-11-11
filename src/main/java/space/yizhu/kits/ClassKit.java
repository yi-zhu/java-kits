package space.yizhu.kits;/* Created by xiuxi on 2018/12/18.*/

import space.yizhu.record.plugin.activerecord.Model;
import space.yizhu.bean.BaseModel;

import javax.tools.*;
import javax.tools.JavaFileObject.Kind;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.CharBuffer;
import java.util.*;

public class ClassKit {

    private static Object creatBeanClass(String className, String classContent) throws Exception {


        String fileName = System.getProperty("user.dir") + "/src/main/java/space/yizhu/thing/bean/" + className + ".java";
        File file = new File(fileName);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(classContent);
        fileWriter.flush();
        fileWriter.close();


        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> javaFileObjects = manager.getJavaFileObjects(fileName);
//        String dest = System.getProperty("user.dir") + "/target/classes";
//
//        //options就是指定编译输入目录，与我们命令行写javac -d C://是一样的
//
//        List<String> options = new ArrayList<String>();
//        options.add("-d");
//        options.add(dest);
        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, javaFileObjects);
        task.call();
        manager.close();
        URL[] urls = new URL[]{new URL("file:/" + System.getProperty("user.dir") + "/webapps/ROOT/WEB-INF/classes/")};

        //加载class时要告诉你的classloader去哪个位置加载class文件

        ClassLoader classLoader = new URLClassLoader(urls);
        try {
            Object classObj = classLoader.loadClass("space.yizhu.bean." + className).getConstructors()[0].newInstance();

//            Object classObj =  classLoader.loadClass(className).getConstructors()[0].newInstance();
            Class<? extends Model<?>> classModel = (Class<? extends Model<?>>) classObj.getClass();
            return classModel.getMethod("getMe").invoke(classObj);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | InstantiationException e) {
             SysKit.print(e);
        }

        return null;
    }


    public static Object createClass(String className, String classContent) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();



       /* Map<String, byte[]> results;
        StandardJavaFileManager stdManager = compiler.getStandardFileManager(null, null, null);
        SysKit.print("0.1");

        try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {
            SysKit.print("1");
            JavaFileObject javaFileObject = manager.makeStringSource(className + ".java", classContent);
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, Arrays.asList(javaFileObject));
            SysKit.print("2");

            if (task.call()) {
                try {
                    results = manager.getClassBytes();
                    MemoryClassLoader memoryClassLoader = new MemoryClassLoader(results);
                    SysKit.print(className + "创建成功1memoryClassLoader");

                    Object classObj = memoryClassLoader.findClass("space.yizhu.bean." + className).getConstructors()[0].newInstance();
                    SysKit.print(className + "创建成功1mclassObjr");

                    Class<? extends Model<?>> classModel = (Class<? extends Model<?>>) classObj.getClass();
                    SysKit.print(className + "创建成功" + classModel.getName());
                    return classModel.getMethod("getMe").invoke(classObj);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException | NoSuchMethodException e) {
                     SysKit.print(e);
                }
            }
        } catch (IOException e) {
             SysKit.print(e);
        }
*/

        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(null, null, null);
        ClassJavaFileManager classJavaFileManager = new ClassJavaFileManager(standardFileManager);
        StringObject stringObject = null;
        try {
            stringObject = new StringObject(new URI(className + ".java"), Kind.SOURCE, classContent);
        } catch (URISyntaxException e) {
             SysKit.print(e);
        }
        JavaCompiler.CompilationTask task = compiler.getTask(null, classJavaFileManager, null, null, null, Arrays.asList(stringObject));
        if (task.call()) {
            ClassJavaFileObject javaFileObject = classJavaFileManager.getClassJavaFileObject();
            ClassLoader classLoader = new MyClassLoader(javaFileObject);

            try {
                Object classObj = classLoader.loadClass("space.yizhu.bean." + className).getConstructors()[0].newInstance();
                Class<? extends Model<?>> classModel = (Class<? extends Model<?>>) classObj.getClass();
                return classModel.getMethod("getMe").invoke(classObj);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | InstantiationException e) {
                 SysKit.print(e);
            }

        }

        return null;
    }

    //创建BEAN
    public static BaseModel<?> createBean(String beanName) throws Exception {


        String topName = beanName.substring(0, 1).toUpperCase();
        beanName = topName + beanName.substring(1);
        String classContent = "package space.yizhu.bean;\n" +
                "import space.yizhu.bean.BaseModel;\n" +
                "public class " + beanName + " extends BaseModel<" + beanName + "> {\n" +
                "    public static final " + beanName + " me = new " + beanName + "();\n" +
                "    public static BaseModel<?> getMe() {\n" +
                "        return me;\n" +
                "    }\n" +
                "}\n";
        BaseModel<?> objClass = (BaseModel<?>) createClass(beanName, classContent);
        BeanKit.set(beanName, objClass);

        return objClass;
    }

    // 自定义fileManager


    static class ClassJavaFileManager extends ForwardingJavaFileManager {

        private ClassJavaFileObject classJavaFileObject;

        public ClassJavaFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }

        public ClassJavaFileObject getClassJavaFileObject() {
            return classJavaFileObject;
        }

        //这个方法一定要自定义
        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            return (classJavaFileObject = new ClassJavaFileObject(className, kind));
        }
    }

    // 存储源文件
    static class StringObject extends SimpleJavaFileObject {

        private String content;

        public StringObject(URI uri, Kind kind, String content) {
            super(uri, kind);
            this.content = content;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return this.content;
        }
    }

    //class文件（不需要存到文件中）

    static class ClassJavaFileObject extends SimpleJavaFileObject {

        ByteArrayOutputStream outputStream;

        public ClassJavaFileObject(String className, Kind kind) {
            super(URI.create(className + kind.extension), kind);
            this.outputStream = new ByteArrayOutputStream();
        }

        //这个也要实现
        @Override
        public OutputStream openOutputStream() throws IOException {
            return this.outputStream;
        }

        public byte[] getBytes() {
            return this.outputStream.toByteArray();
        }
    }

    //自定义classloader
    static class MyClassLoader extends ClassLoader {
        private ClassJavaFileObject stringObject;

        public MyClassLoader(ClassJavaFileObject stringObject) {
            this.stringObject = stringObject;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] bytes = this.stringObject.getBytes();
            return defineClass(name, bytes, 0, bytes.length);
        }
    }


    static class MemoryClassLoader extends URLClassLoader {

        // class name to class bytes:
        Map<String, byte[]> classBytes = new HashMap<String, byte[]>();

        public MemoryClassLoader(Map<String, byte[]> classBytes) {
            super(new URL[0], MemoryClassLoader.class.getClassLoader());
            this.classBytes.putAll(classBytes);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] buf = classBytes.get(name);
            if (buf == null) {
                return super.findClass(name);
            }
            classBytes.remove(name);
            return defineClass(name, buf, 0, buf.length);
        }

    }

    static class MemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

        // compiled classes in bytes:
        final Map<String, byte[]> classBytes = new HashMap<String, byte[]>();

        MemoryJavaFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }

        public Map<String, byte[]> getClassBytes() {
            return new HashMap<String, byte[]>(this.classBytes);
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
            classBytes.clear();
        }

        @Override
        public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, Kind kind,
                                                   FileObject sibling) throws IOException {
            if (kind == Kind.CLASS) {
                return new MemoryOutputJavaFileObject(className);
            } else {
                return super.getJavaFileForOutput(location, className, kind, sibling);
            }
        }

        JavaFileObject makeStringSource(String name, String code) {
            return new MemoryInputJavaFileObject(name, code);
        }

        static class MemoryInputJavaFileObject extends SimpleJavaFileObject {

            final String code;

            MemoryInputJavaFileObject(String name, String code) {
                super(URI.create("string:///" + name), Kind.SOURCE);
                this.code = code;
            }

            @Override
            public CharBuffer getCharContent(boolean ignoreEncodingErrors) {
                return CharBuffer.wrap(code);
            }
        }

        class MemoryOutputJavaFileObject extends SimpleJavaFileObject {
            final String name;

            MemoryOutputJavaFileObject(String name) {
                super(URI.create("string:///" + name), Kind.CLASS);
                this.name = name;
            }

            @Override
            public OutputStream openOutputStream() {
                return new FilterOutputStream(new ByteArrayOutputStream()) {
                    @Override
                    public void close() throws IOException {
                        out.close();
                        ByteArrayOutputStream bos = (ByteArrayOutputStream) out;
                        classBytes.put(name, bos.toByteArray());
                    }
                };
            }

        }
    }

}
