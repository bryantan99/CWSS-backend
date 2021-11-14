package com.chis.communityhealthis.utility;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DatetimeUtil {
    public static String to12HourString(Calendar startCalendar) {
        String hour;
        if (startCalendar.get(Calendar.HOUR) < 10) {
            if (startCalendar.get(Calendar.HOUR) == 0) {
                hour = "12";
            } else {
                hour = "0" + startCalendar.get(Calendar.HOUR);
            }
        } else {
            hour = String.valueOf(startCalendar.get(Calendar.HOUR));
        }
        String minute = String.valueOf(startCalendar.get(Calendar.MINUTE));
        if (startCalendar.get(Calendar.MINUTE) < 10) {
            minute = "0" + startCalendar.get(Calendar.MINUTE);
        }
        String unit = startCalendar.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
        return hour + ":" + minute + " " + unit;
    }

    public static boolean isLunchHour(Calendar startCalendar) {
        return startCalendar.get(Calendar.HOUR_OF_DAY) >= 12 && startCalendar.get(Calendar.HOUR_OF_DAY) <= 13;
    }

    public static boolean isWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.SATURDAY || day == Calendar.SUNDAY;
    }

    public static boolean isEqualDatetime(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            return true;
        } else if (date1 == null || date2 == null) {
            return false;
        } else {
            return Objects.equals(date1.getTime(), date2.getTime());
        }
    }
}
