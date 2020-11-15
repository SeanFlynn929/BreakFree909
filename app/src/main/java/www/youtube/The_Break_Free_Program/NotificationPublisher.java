package www.youtube.The_Break_Free_Program;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationPublisher extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getExtras().getString("message");
        if (message.length() == 0) {
            message = "This is your reminder";
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "BreakFree")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Reminder")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());
    }
}
