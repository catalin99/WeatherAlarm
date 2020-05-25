package catalin.facultate.weatheralarm;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationManagerCompat;

public class MyNewIntentService extends JobIntentService {
    private static final int NOTIFICATION_ID = 3;

    public MyNewIntentService() {
        super();
    }


    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MyNewIntentService.class, 1003, work);
    }

    @Override
    protected void onHandleWork (Intent intent) {
        Notification.Builder builder = new Notification.Builder(this, "M_CH_ID");
        builder.setContentTitle("My Title");
        builder.setContentText("This is the Body");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }



    @Override
    public boolean onStopCurrentWork() {
        return super.onStopCurrentWork();
    }

}
