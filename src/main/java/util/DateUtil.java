package util;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;

import java.util.Calendar;
import java.util.Date;

/**
 * 类DateUtil.java的实现描述
 * @author zhh 2015年4月21日 下午9:00:57
 */
public class DateUtil {
    public static Date parseOf(String dateStr) throws Exception {
        String str        = dateStr.trim();
        String dateFormat = null;
        if (str.matches("\\d{4}-\\d{1,2}-\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}")) {
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        } else if (str.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
            dateFormat = "yyyy-MM-dd";
        } else {
            throw new IllegalArgumentException("默认格式无法解析");
        }
        return DateUtils.parseDate(str, dateFormat);

    }

    /**
     * 获取距目标时间的剩余时间
     * @param date
     * @return xx天xx时xx分
     */
    public static String getRemainTime(Date date) {
        return getRemainTime(date, "{day}天{hour}时{minute}分");
    }

    /**
     * 获取距目标时间的剩余时间
     * @param date
     * @param format {day}天{hour}时{minute}分
     * @return
     */
    public static String getRemainTime(Date date, String format) {
        StringBuilder sb = new StringBuilder();

        Calendar start = Calendar.getInstance();
        Calendar end   = Calendar.getInstance();
        end.setTime(date);
        long m1     = start.getTimeInMillis();
        long m2     = end.getTimeInMillis();
        long remain = m2 - m1;
        // 天
        long day = remain / 24 / 60 / 60 / 1000;
        if (day > 0) {
            format.replace("{day}", String.valueOf(day));
        }
        // 小时
        remain -= (day * 24 * 60 * 60 * 1000);
        long hour = remain / 60 / 60 / 1000;
        if (hour > 0 || sb.length() > 0) {
            format.replace("{hour}", String.valueOf(hour));
        }
        // 分
        remain -= (hour * 60 * 60 * 1000);
        long minute = remain / 60 / 1000;
        if (minute > 0 || sb.length() > 0) {
            sb.append(minute + "分");
            format.replace("{minute}", String.valueOf(minute));
        }
        return sb.toString();
    }

    /**
     * 获取当前x天x时x分后的时间
     */
    public static Date getTargetTime(int day, int hour, int minutes) {
        return DateTime.now().withFieldAdded(DurationFieldType.days(), day).withFieldAdded(DurationFieldType.hours(),
                hour).withFieldAdded(DurationFieldType.minutes(), minutes).toDate();
    }

    /**
     * 两个时间比较
     * @param d1
     * @param d2
     * @return true:d1>d2 false:d1<d2
     */
    public static boolean dateCompare(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        long p1 = c1.getTimeInMillis();
        long p2 = c2.getTimeInMillis();
        if (p1 > p2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 传入的日期是否在指定时间内
     * @param day
     * @param hour
     * @param minutes
     * @return
     */
    public static boolean isDateInSpecial(int day, int hour, int minutes) {
        Calendar current = Calendar.getInstance();
        Calendar special = Calendar.getInstance();
        special.setTime(getTargetTime(day, hour, minutes));
        return special.getTimeInMillis() >= current.getTimeInMillis();
    }
}
