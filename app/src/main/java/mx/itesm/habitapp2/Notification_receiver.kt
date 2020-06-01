package mx.itesm.habitapp2

import android.R
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat


class Notification_receiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val builder =
            NotificationCompat.Builder(context, "1")
        val myIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            myIntent,
            FLAG_ONE_SHOT
        )
        builder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.arrow_up_float)
            .setContentTitle("Actualiza tu progreso")
            .setContentIntent(pendingIntent)
            .setContentText("Hola! Es importante que actualices tu progreso todos los días")
            .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
            .setContentInfo("Info")
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())
    }

}
