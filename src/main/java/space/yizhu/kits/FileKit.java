package space.yizhu.kits;

import java.io.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * FileKit.
 */

public class FileKit {
    public static void delete(File file) {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        delete(files[i]);
                    }
                }
                file.delete();
            }
        }
    }

    /**
     * 读取文件
     *
     * @param file
     * @return strs
     */
    public static String read(File file) {
        BufferedReader bufferedReader = null;
        String strs = "";
        if (!file.exists()) {
            return strs;
        }
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));
            LineNumberReader reader = new LineNumberReader(bufferedReader);
            strs = reader.lines().collect(Collectors.joining());
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strs;
    }

    /**
     * 读取文件
     * @param file
     * @param startRows
     * @return strs
     */
    public static String read(File file, long startRows) {
        BufferedReader bufferedReader = null;
        String strs = "";

        if (!file.exists()) {
            return strs;
        }
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));
            LineNumberReader reader = new LineNumberReader(bufferedReader);
            while (reader.getLineNumber() < startRows) {
                reader.readLine();
            }
            strs = reader.lines().collect(Collectors.joining());
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strs;
    }

    /**
     * 读取文件
     * @param lens
     * @param file
     * @param startRows
     * @return strs
     */
    public static String read(File file, long startRows, long lens) {
        BufferedReader bufferedReader = null;
        String strs = "";
        if (!file.exists()) {
            return strs;
        }
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));
            LineNumberReader reader = new LineNumberReader(bufferedReader);
            while (reader.getLineNumber() < startRows) {
                reader.readLine();
            }
            while (lens-- > 0) {
                strs += reader.readLine() + "\r\n";
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strs;
    }

    /**
     * 文件总行数
     *
     * @param fileName
     * @return lines
     * @throws IOException
     */
    static long getTotalLines(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(fileName)));
        LineNumberReader reader = new LineNumberReader(in);
        long lines = reader.lines().count();
        reader.close();
        in.close();
        return lines;
    }

    public static String getFileExtension(String fileFullName) {
        if (StrKit.isBlank(fileFullName)) {
            throw new RuntimeException("fileFullName is empty");
        }
        return getFileExtension(new File(fileFullName));
    }

    public static String getFileExtension(File file) {
        if (null == file) {
            throw new NullPointerException();
        }
        String fileName = file.getName();
        int dotIdx = fileName.lastIndexOf('.');
        return (dotIdx == -1) ? "" : fileName.substring(dotIdx + 1);
    }

    public static String getUsrDir() {
        return System.getProperty("user.dir");
    }

    public static void main(String[] args) {
        SysKit.print(read(new File(getUsrDir() + "/README.md"),3));
    }
}
