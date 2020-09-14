package space.yizhu.kits;/* Created by xiuxi on 2019/1/3.*/

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

import static space.yizhu.variable.GlobalVariable.*;

public class Ftpkit {


    public static boolean init() {
        if (ftpClient == null)
            ftpClient = new FTPClient();
        else {
            try {
                ftpClient.logout();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        ftpClient.setControlEncoding("utf-8");


        try {
            ftpClient.connect(ftpHostName, ftpPort);

            ftpClient.login(ftpUser, ftpPassword);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setBufferSize(1024 * 30);
            ftpClient.setDataTimeout(10 * 1000);
            ftpClient.setConnectTimeout(20 * 1000);
            SysKit.print("ftp启动成功");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;

        }
    }


}
