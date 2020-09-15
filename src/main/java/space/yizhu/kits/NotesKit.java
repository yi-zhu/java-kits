package space.yizhu.kits;/* Created by yi on 9/15/2020.*/

import java.io.*;

public class NotesKit {

    private static int count = 0;

    /**
     * 删除文件中的各种注释，包含//、/* * /等
     * @param charset 文件编码
     * @param file 文件
     */
    public static void clearNotes(File file, String charset) {
        try {
//递归处理文件夹
            if (!file.exists()) {
                return;
            }

            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    clearNotes(f, charset); //递归调用
                }
                return;
            } else if (!file.getName().endsWith(".java")) {
//非java文件直接返回
                return;
            }
            System.out.println("-----开始处理文件：" + file.getAbsolutePath());

//根据对应的编码格式读取
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
            StringBuffer content = new StringBuffer();
            String tmp = null;
            while ((tmp = reader.readLine()) != null) {
                content.append(tmp);
                content.append("\n");
            }
            String target = content.toString();

            String s = target.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", "");

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
            out.write(s);
            out.flush();
            out.close();
            count++;
            System.out.println("-----文件处理完成---" + count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearNotes(String filePath, String charset) {
        clearNotes(new File(filePath), charset);
    }

    public static void clearNotes(String filePath) {
        clearNotes(new File(filePath), "UTF-8");
    }

    public static void clearNotes(File file) {
        clearNotes(file, "UTF-8");
    }

    public static void main(String[] args) {
        clearNotes("D:\\work\\platform\\template2020kits\\src\\main\\java\\space\\yizhu\\kits\\StrKit.java");
    }
}
