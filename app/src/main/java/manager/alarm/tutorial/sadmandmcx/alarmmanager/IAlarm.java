package manager.alarm.tutorial.sadmandmcx.alarmmanager;

import android.media.Ringtone;

public interface IAlarm {

    Ringtone getRingtone();
    IAlarm getiAlarm();
    void onAlarm();

}
