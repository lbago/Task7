package androidlab.epam.com.hometask7;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    private static final int NOTIFY_ID = 101;

    final String TAG = "LOG";

    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        taskOfSleeping();
        return super.onStartCommand(intent, flags, startId);
    }

    void taskOfSleeping() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Log.d(TAG, "Start sleep");
                    TimeUnit.SECONDS.sleep(60);
                    Log.d(TAG, "Finish sleep");
                    sendNotification();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stopSelf();
            }
        }).start();
    }

    private void sendNotification() {
        Context context = getApplicationContext();

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .setContentTitle(context.getResources().getString(R.string.notify_title))
                .setContentText(context.getResources().getString(R.string.notify_text));
        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        //id определяется для случая, если нужно несолько уведомлений, чтобы они не перетерались
        notificationManager.notify(NOTIFY_ID, notification);
    }

    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
