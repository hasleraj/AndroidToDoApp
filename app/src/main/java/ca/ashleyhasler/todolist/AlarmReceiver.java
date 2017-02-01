package ca.ashleyhasler.todolist;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Receives the message android sends when alarm goes off.
 */

public class AlarmReceiver extends BroadcastReceiver{

    public void onReceive(Context context, Intent intent) {
        String taskTitle = intent.getStringExtra("title");
        int taskId = intent.getIntExtra("id", 0);

        //get notification builder
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(context.getString(R.string.notification_content_beginning) + " " + taskTitle + " " + context.getString(R.string.notification_content_ending));

        //what happens when you click on notification
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        //associates the above ^ with notification
        notificationBuilder.setContentIntent(resultPendingIntent);

        NotificationManager notifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        notifyMgr.notify(taskId, notificationBuilder.build());
    }
}
