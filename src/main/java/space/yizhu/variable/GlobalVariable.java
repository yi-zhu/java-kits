package space.yizhu.variable;/* Created by xiuxi on 2018/12/7.*/

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ConfigurableApplicationContext;

public class GlobalVariable {
    public static Integer serverPort = 0;

    //---sys
    public static ConfigurableApplicationContext applicationContext;


    //--ssl
    public static boolean isSsl = false;

    //-----ftp
    public static FTPClient ftpClient;
    public static String ftpHostName = ""; //
    public static int ftpPort = 21; //
    public static String ftpUser = ""; //
    public static String ftpPassword = ""; //
    public static String ftpEncoding = "utf-8"; //


    public static WebServer webServer;




}
