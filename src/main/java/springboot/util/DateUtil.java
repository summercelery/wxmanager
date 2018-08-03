package springboot.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    private static DateTimeFormatter strToDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");


    public static String getFormatString(Date date){

        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

        return strToDateFormatter.format(localDateTime);
    }

    public static Date getDateByString(String stringDate){

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.parse(stringDate, strToDateFormatter);
        ZonedDateTime zdt = localDateTime.atZone(zoneId);

        return  Date.from(zdt.toInstant());
    }





}
