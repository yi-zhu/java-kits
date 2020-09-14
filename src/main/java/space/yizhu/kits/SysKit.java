package space.yizhu.kits;/* Created by xiuxi on 2018/10/19.*/

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SysKit {
    private static String prefix = "系统";
    private static Calendar lastDate = Calendar.getInstance();
    private static ConcurrentHashMap<String, Calendar> timeMap = new ConcurrentHashMap<>();


    //--system信息
    public static String getSysInfo(String key) {
        //---print
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        MBeanServer mBeanServer = null;
        ArrayList<MBeanServer> mBeanServers = MBeanServerFactory.findMBeanServer(null);
        if (mBeanServers.size() > 0) {
            for (MBeanServer _mBeanServer : mBeanServers) {
                mBeanServer = _mBeanServer;
                break;
            }
        }
        if (mBeanServer == null) {
            throw new IllegalStateException("没有发现JVM中关联的MBeanServer.");
        }
        Set<ObjectName> objectNames = null;
        try {

            objectNames = mBeanServer.queryNames(new ObjectName("Catalina:type=Connector,*"), null);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (objectNames == null || objectNames.size() <= 0) {
            throw new IllegalStateException("没有发现JVM中关联的MBeanServer : "
                    + mBeanServer.getDefaultDomain() + " 中的对象名称.");
        }
        try {
            for (ObjectName objectName : objectNames) {
                String protocol = (String) mBeanServer.getAttribute(objectName, "protocol");
                if (protocol.equals("HTTP/1.1")) {
                    int port = (Integer) mBeanServer.getAttribute(objectName, "port");
                }
                // String scheme = (String) mBeanServer.getAttribute(objectName,
                // "scheme");
                // int port = (Integer) mBeanServer.getAttribute(objectName,
                // "port");

            }
        } catch (AttributeNotFoundException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (MBeanException e) {
            e.printStackTrace();
        } catch (ReflectionException e) {
            e.printStackTrace();
        }

        return null;

    }


    //---print
    public static void print() {
        System.out.println(DateKit.now() + " - [" + prefix + "]: ----------------------------  " + take());
    }

    public static void print(String msg) {
        if (msg == null)
            return;
        if (msg.length() > 1)
            if (msg.contains("br"))
                System.out.println(DateKit.now() + " - [" + prefix + "]:" + msg.substring(0, msg.indexOf("br")) + take());
            else
                System.out.println(DateKit.now() + " - [" + prefix + "]:" + msg + take());

    }

    public static void print(String msg, String lab) {
        lab = lab.toLowerCase();
        if (lab.contains("test"))
            print(msg);
        else if (null == timeMap.get(lab)) {
            if (Calendar.getInstance().get(Calendar.DATE) != lastDate.get(Calendar.DATE))
                timeMap.clear();
            if (!lab.startsWith("self"))
                System.out.println(DateKit.now() + " - [" + lab + "]:" + msg + " - " + "开始计时 ------");
            timeMap.put(lab, Calendar.getInstance());

        } else {
            System.out.println(DateKit.now() + " - [" + lab + "]:" + msg + " - " + "结束计时," + take(timeMap.get(lab)));
            timeMap.remove(lab);
            if (Calendar.getInstance().get(Calendar.DATE) != lastDate.get(Calendar.DATE))
                timeMap.clear();
        }
    }

    public static void print(String msg, String lab, boolean isStart) {
        if (isStart) {
            timeMap.remove(lab);
            print(msg, lab);

        }
    }

    private static String take(Calendar lastDateT) {
        if (lastDateT == null)
            lastDateT = Calendar.getInstance();
        String prefix = " - 用时:";
        long n = Calendar.getInstance().getTimeInMillis();
        long range = n - lastDateT.getTimeInMillis();
//        long len = 12 * 24 * 60 * 60 * 1000;
        lastDate = Calendar.getInstance();
        if (range > 12 * 24 * 60 * 60 * 1000)
            return prefix + CharKit.doubleKeepDecimal(range / (12 * 24 * 60 * 60 * 1000 * 1.0)) + "年";
        else if (range > 24 * 60 * 60 * 1000)
            return prefix + CharKit.doubleKeepDecimal(range / (24 * 60 * 60 * 1000 * 1.0)) + "天";
        else if (range > 60 * 60 * 1000)
            return prefix + CharKit.doubleKeepDecimal(range / (60 * 60 * 1000 * 1.0)) + "小时";
        else if (range > 60 * 1000)
            return prefix + CharKit.doubleKeepDecimal(range / (60 * 1000 * 1.0)) + "分钟";
        else if (range > 1000)
            return prefix + CharKit.doubleKeepDecimal(range / (1000 * 1.0)) + "秒";
        else if (range < 1000)
            return prefix + CharKit.doubleKeepDecimal(range / (1.0)) + "毫秒";
        else
            return prefix + CharKit.doubleKeepDecimal(range / (1.0)) + "毫秒";
    }

    public static String take(String lab) {
        lab = lab.toLowerCase();
        return take(timeMap.get(lab));
    }

    public static void print(Exception msg) {
        try {
            System.err.println(DateKit.now() + " - [" + msg.getStackTrace()[0].getMethodName() + msg.getStackTrace()[0].getLineNumber() + "]:" + msg.toString() + take());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(DateKit.now() + msg.getLocalizedMessage() + take());
        }
    }

    public static void print(Throwable msg) {
        try {
            System.err.println(DateKit.now() + " - [" + msg.getStackTrace()[0].getMethodName() + msg.getStackTrace()[0].getLineNumber() + "]:" + msg.getLocalizedMessage() + take());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void print(Throwable msg, String prefix) {
        try {
            System.err.println(DateKit.now() + " - [" + prefix + "]:" + msg.getLocalizedMessage() + take());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void print(Exception msg, String prefix) {
        try {
            System.err.println(DateKit.now() + " - [" + prefix + "]:" + (msg.getLocalizedMessage() == null ? msg.getMessage() : msg.getLocalizedMessage()) + take());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void print(int num) {
        System.err.println(DateKit.now() + " - [" + prefix + "]:" + num + take());
    }

    private static String take() {
        return take(lastDate);
    }

    //---print


    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        SysKit.prefix = prefix;
    }


}
