package space.yizhu.kits;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateKit {

    public static String formatDate(Date date) {
        return DateFormatUtils.format(date, "yyyy-MM-dd");
    }

    public static String now() {
        return DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String nowTimestamp() {
        return new Date().getTime() / 1000 + "";
    }

    public static boolean timestampJudge(String timestamp) {
        try {
            if (timestamp.length() < 12)
                timestamp += "000";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String d = format.format(Long.parseLong(timestamp));
            Date date = format.parse(d);
            long jetLag;
            jetLag = new Date().getTime() - date.getTime();
            return jetLag < 3600 * 1000 && jetLag > -180000;
        } catch (ParseException e) {
            SysKit.print(e, "timestampJudge");
            return false;
        }
    }

    public static Date parseDate(String date) {
        try {
            return DateUtils.parseDate(date, "yyyy-MM","yyyyMM","yyyy/MM","yyyyMMdd"
                    ,"yyyy-MM-dd","yyyy/MM/dd","yyyyMMddHHmmss","yyyy-MM-ddHH:mm:ss","yyyy/MM/ddHH:mm:ss"
                    ,"MMM dd, yyyy hh:mm:ss a" ,"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss.S"
                    ,"EEE MMM dd HH:mm:ss zzz yyyy");
        } catch (ParseException e) {
             SysKit.print(e);
        }
        return null;
    }

    public static String formatDate(Date date, String format) {
        return DateFormatUtils.format(date, format);
    }

    public static String formatTime(Date date) {
        return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
    }


    public static List<String> listAll(String month) {
        List<String> all = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        int nm = cal.get(Calendar.MONTH);
        int nd = cal.get(Calendar.DAY_OF_MONTH);
        try {
            cal.setTime(DateUtils.parseDate(month, new String[]{"yyyy-MM"}));
            cal.set(Calendar.DATE, 1);
            int m = cal.get(Calendar.MONTH);
            while (cal.get(Calendar.MONTH) == m && m <= nm) {
                if (m < nm || m == nm && nd >= cal.get(Calendar.DAY_OF_MONTH))
                    all.add(formatDate(cal.getTime(), "MM-dd"));
                cal.add(Calendar.DATE, 1);
            }
        } catch (ParseException e) {
             SysKit.print(e);
        }
        return all;
    }


    //日期转字符
    public static String Data2String(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(d);

        return str;
    }

    //日期转字符
    public static String Data2StringD(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(d);

        return str;
    }

    //字符转日期
    public static Date String2Date(String str) {

        if (null == str || str.equals("null"))
            return null;
        if (str.contains(".")) {
            str = str.substring(0, str.indexOf("."));
        }
        SimpleDateFormat sdf = null;
        if (str.contains("+"))
            str = str.substring(0, str.indexOf("+"));
        if (str.contains("-"))
            if (str.length() < 6) {
                sdf = new SimpleDateFormat("MM-dd");
            } else if (str.length() < 8) {
                sdf = new SimpleDateFormat("yyyy-MM");
            } else if (str.length() < 11) {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            } else if (str.length() < 14) {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH");
            } else if (str.length() < 17) {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            } else {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
        else if ((!str.contains("-")) && str.contains(":")) {
            Date date = new Date();
            date.setHours(Integer.parseInt(str.split(":")[0]));
            date.setMinutes(Integer.parseInt(str.split(":")[1]));
        } else {
            //时间戳
            str = timeStamp2Date(str, null);
        }

        Date da = null;
        try {
            da = sdf.parse(str);
        } catch (ParseException e) {
            da = new Date();
        }
        return da;

    }

    public static Timestamp string2Timestamp(String string) {
        return new Timestamp(Objects.requireNonNull(parseDate(string)).getTime());
    }

    //时间戳转日期格式字符串
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        seconds += "000";
        if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    public static void main(String[] args) {
        String s = "2021-12-04 09:18:52.0";
        parseDate(s);
    }


    private final String README = "\n" +
            "字母\t描述\t示例\n" +
            "G\t纪元标记\tAD\n" +
            "y\t四位年份\t2001\n" +
            "M\t月份\tJuly or 07\n" +
            "d\t一个月的日期\t10\n" +
            "h\t A.M./P.M. (1~12)格式小时\t12\n" +
            "H\t一天中的小时 (0~23)\t22\n" +
            "m\t分钟数\t30\n" +
            "s\t秒数\t55\n" +
            "S\t毫秒数\t234\n" +
            "E\t星期几\tTuesday\n" +
            "D\t一年中的日子\t360\n" +
            "F\t一个月中第几周的周几\t2 (second Wed. in July)\n" +
            "w\t一年中第几周\t40\n" +
            "W\t一个月中第几周\t1\n" +
            "a\tA.M./P.M. 标记\tPM\n" +
            "k\t一天中的小时(1~24)\t24\n" +
            "K\t A.M./P.M. (0~11)格式小时\t10\n" +
            "z\t时区\tEastern Standard Time\n" +
            "'\t文字定界符\tDelimiter\n" +
            "\"\t单引号\t`";
}
