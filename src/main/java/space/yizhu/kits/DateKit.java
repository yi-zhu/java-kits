package space.yizhu.kits;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
            return DateUtils.parseDate(date, new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});
        } catch (ParseException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
        return new Timestamp(String2Date(string).getTime());
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
}
