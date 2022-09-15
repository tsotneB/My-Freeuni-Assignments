package ge.tbabunashvili.alarm

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.*
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import kotlin.collections.HashMap


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {

            val alarmId = intent?.getLongExtra(MainActivity.ID, -1)
            val minute = intent?.getIntExtra(MainActivity.MINUTE, -1)
            val hour = intent?.getIntExtra(MainActivity.HOUR, -1)
            if (intent?.action == ALARM_ACTION_NAME) {
                var notificationManager = NotificationManagerCompat.from(context)
               val notificationClickPendingIntent = PendingIntent.getActivity(
                    context,
                    MainActivity.ALARM_REQUEST_CODE,
                    Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE,
                )
                val cancelClickIntent = PendingIntent.getBroadcast(
                    context,
                    200,
                    Intent("ge.tbabunashvili.alarm.NOTIFICATION_CANCEL").apply {
                        `package` = context.packageName
                    },
                    PendingIntent.FLAG_IMMUTABLE,
                )

                val snoozeButtonClick = PendingIntent.getBroadcast(
                    context,
                    MainActivity.ALARM_REQUEST_CODE + (alarmId?.toInt() ?: 1),
                    Intent("ge.tbabunashvili.alarm.NOTIFICATION_SNOOZE").apply {
                        `package` = context.packageName
                        putExtra(MainActivity.ID, alarmId)
                        putExtra(MainActivity.HOUR, hour)
                        putExtra(MainActivity.MINUTE, minute)
                    },
                    PendingIntent.FLAG_IMMUTABLE,
                    )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createChannel(notificationManager)
                }

                var notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.alarm_clock)
                    .setContentTitle("Alarm message!")
                    .setContentText("Alarm set on " + hour?.let { it1 -> AlarmAdapter.getString(it1) }
                            + ":" + minute?.let { it1 -> AlarmAdapter.getString(it1) })
                    .setContentIntent(notificationClickPendingIntent)
                    .addAction(R.mipmap.ic_launcher, "Cancel", cancelClickIntent)
                    .addAction(R.mipmap.ic_launcher, "Snooze", snoozeButtonClick)
                    .build()
                notificationManager.notify(NOTIFICATION_ID, notification)
            } else {
                var notificationManager = NotificationManagerCompat.from(context)
                notificationManager.cancel(NOTIFICATION_ID)
                if (intent?.action == "ge.tbabunashvili.alarm.NOTIFICATION_SNOOZE") {
                    var pi = PendingIntent.getBroadcast(
                        context,
                        MainActivity.ALARM_REQUEST_CODE + (alarmId?.toInt() ?: 1),
                        Intent(AlarmReceiver.ALARM_ACTION_NAME).apply {
                            `package` = context.packageName
                            putExtra(MainActivity.ID, alarmId)
                            putExtra(MainActivity.HOUR, hour)
                            putExtra(MainActivity.MINUTE, minute)
                        },
                        PendingIntent.FLAG_IMMUTABLE
                    )
                    val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
                    val rc = MainActivity.ALARM_REQUEST_CODE + (alarmId?.toInt() ?: 1)
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pi);
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(notificationManager: NotificationManagerCompat) {
        var notificationChannel = NotificationChannel(CHANNEL_ID, "channel_name", IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    companion object {
        const val TAG = "ALARM_RECEIVER"
        const val ALARM_ACTION_NAME = "ge.tbabunashvili.alarm.ALARM_ACTION"

        const val NOTIFICATION_ID = 20
        const val CHANNEL_ID = "ge.tbabunashvili.alarm.CHANNEL_1"
    }
}