package manager.alarm.tutorial.sadmandmcx.alarmmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    public static final String ChannelID = "ChannelID";
    public static final String Channel1ID = "Channel1ID";
    public static final String Channel2ID = "Channel2ID";
    public static final String Channel1Name = "Channel1Name";
    public static final String Channel2Name = "Channel2Name";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    public void createChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        NotificationChannel channel1 = new NotificationChannel(Channel1ID, Channel1Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel1);

        NotificationChannel channel2 = new NotificationChannel(Channel2ID, Channel2Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel2.enableLights(true);
        channel2.enableVibration(true);
        channel2.setLightColor(R.color.colorPrimary);
        channel2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel2);
    }

    public NotificationManager getManager() {
        if (mManager == null)
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotificationBuilder() {
        return new NotificationCompat.Builder(getApplicationContext(), ChannelID);
    }

    public NotificationCompat.Builder getChannel1NotificationBuilder(String title, String message) {
        return new NotificationCompat.Builder(getApplicationContext(), Channel1ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher);
    }

    public NotificationCompat.Builder getChannel2NotificationBuilder(String title, String message) {
        return new NotificationCompat.Builder(getApplicationContext(), Channel2ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher);
    }
}
