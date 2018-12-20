package manager.alarm.tutorial.sadmandmcx.alarmmanager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, IAlarm {

    // Class
    private class TimerTaskClass extends TimerTask {

        @Override
        public void run() {
            if (Objects.equals(alarmTime.getText().toString(), met_AlarmTime().toString())) {
                ringtone.play();
            } else {
                ringtone.stop();
            }
        }
    }

    private class AlarmState {
        public static final String STATE_STOPPED = "Stop";
        public static final String STATE_STARTED = "Start";
        public static final String STATE_CANCELED = "Cancel";
    }

    // Variables
    @BindView(R.id.tcAlarmTime)
    TextClock alarmTime;

    @BindView(R.id.tpTimePicker)
    TimePicker timePicker;

    @BindView(R.id.tvAlarmDateTimeText)
    TextView alarmDateTimeText;

    @BindView(R.id.btnStartCancelAlarm)
    Button startCancelAlarm;

    private Ringtone ringtone;
    private Calendar alarmDateTimeCalender;
    private boolean isTimeSet = false;

    // Method
    private void met_Init() {
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        alarmDateTimeCalender = Calendar.getInstance();
    }

    private void met_InitView() {
        startCancelAlarm.setTag(AlarmState.STATE_STOPPED);
    }

    private void met_Alarm() {
        Timer timer = new Timer();
        timer.schedule(new TimerTaskClass(), 0, 1000);
    }

    public StringBuilder met_AlarmTime() {
        Integer alarmHours;
        Integer alarmMinutes;
        String am_pm = "a.m.";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmHours = timePicker.getHour();
            alarmMinutes = timePicker.getMinute();
        } else {
            alarmHours = timePicker.getCurrentHour();
            alarmMinutes = timePicker.getCurrentMinute();
        }

        if (alarmHours > 12) {
            alarmHours = alarmHours - 12;
            am_pm = "p.m.";
        }

        return new StringBuilder().append(alarmHours).append(":").append(alarmMinutes).append(" ").append(am_pm);
    }

    private void met_UpdateDateTimeText(Calendar calendar, boolean isTime) {
        String finalDT = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(calendar.get(Calendar.MONTH)) + " - " +
            String.valueOf(calendar.get(Calendar.HOUR)) + ":" + String.valueOf(calendar.get(Calendar.MINUTE));

        alarmDateTimeText.setText(finalDT);
    }

    private void met_StartAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alertReceiver = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, alertReceiver, 0);

        if (alarmManager != null)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmDateTimeCalender.getTimeInMillis(), pendingIntent);
    }

    private void met_CancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alertReceiver = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, alertReceiver, 0);

        if (alarmManager != null)
            alarmManager.cancel(pendingIntent);
    }

    private void met_Reset() {
        isTimeSet = false;
    }

    /*
    * @method OVERRIDE
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        met_Init();
        met_InitView();
//        met_Alarm();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        alarmDateTimeCalender.set(Calendar.HOUR, hour);
        alarmDateTimeCalender.set(Calendar.MINUTE, minute);
        alarmDateTimeCalender.set(Calendar.SECOND, 0);

        met_UpdateDateTimeText(alarmDateTimeCalender, true);
        isTimeSet = true;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        alarmDateTimeCalender.set(Calendar.YEAR, year);
        alarmDateTimeCalender.set(Calendar.MONTH, month);
        alarmDateTimeCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        met_UpdateDateTimeText(alarmDateTimeCalender, false);
    }

    @Override
    public void onAlarm() {

    }

    @Override
    public Ringtone getRingtone() {
        return ringtone;
    }

    @Override
    public IAlarm getiAlarm() {
        return this;
    }

    /*
    * @event CLICK
    * */
    @OnClick(R.id.btnChangeTime)
    public void onTimeChange() {
        DialogFragment timePickerDialog = new TimePickerFragment();
        timePickerDialog.show(getSupportFragmentManager(), "Alarm Time Picker");
    }

    @OnClick(R.id.btnChangeDate)
    public void onDateChange() {
        DialogFragment dateFragmentDialog = new DatePickerFragment();
        dateFragmentDialog.show(getSupportFragmentManager(), "Alarm Date Picker");
    }

    @OnClick(R.id.btnStartCancelAlarm)
    public void onChangeAlarmState(Button startCancelAlarmButton) {
        if (Objects.equals(startCancelAlarmButton.getTag(), AlarmState.STATE_STOPPED)) {
            startCancelAlarmButton.setText("Cancel Alarm");
            startCancelAlarmButton.setTag(AlarmState.STATE_STARTED);
        } else if (Objects.equals(startCancelAlarmButton.getTag(), AlarmState.STATE_STARTED)) {
            startCancelAlarmButton.setText("Start Alarm");
            startCancelAlarmButton.setTag(AlarmState.STATE_STOPPED);
        }

        if (isTimeSet) {
            met_StartAlarm();
        } else {
            met_Reset();
            met_CancelAlarm();
        }
    }
}
