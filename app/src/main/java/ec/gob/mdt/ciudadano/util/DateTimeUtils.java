package ec.gob.mdt.ciudadano.util;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by francisco on 15/09/16.
 */
public class DateTimeUtils {
    public static void cron(Runnable task){

        Calendar with = Calendar.getInstance();
        int hour = with.get(Calendar.HOUR_OF_DAY);
        int Minutes = with.get(Calendar.MINUTE);

        int MinutesPassed12AM = hour * 60 + Minutes;
        int MinutesAt7AM = 7 * 60;
        //int OneDayMinutes = 24 * 8 * 60;
        int OneDayMinutes = 60;
        long DelayInMinutes = MinutesPassed12AM <= MinutesAt7AM ? MinutesAt7AM - MinutesPassed12AM : OneDayMinutes - (MinutesPassed12AM - MinutesAt7AM);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(task, DelayInMinutes, OneDayMinutes, TimeUnit.MINUTES);
        //scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
    }
}
