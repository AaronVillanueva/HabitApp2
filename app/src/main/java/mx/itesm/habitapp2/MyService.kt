package mx.itesm.habitapp2

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import java.util.*


class MyService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        startAlarm(true, true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    private fun startAlarm(
        isNotification: Boolean,
        isRepeat: Boolean
    ) {
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent: PendingIntent
        //THIS IS WHERE YOU SET NOTIFICATION TIME FOR CASES WHEN THE NOTIFICATION NEEDS TO BE RESCHEDULED
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 17)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 5)
        val myIntent: Intent = Intent(this, Notification_receiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0)
        if (!isRepeat) manager[AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 3000] =
            pendingIntent else manager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}