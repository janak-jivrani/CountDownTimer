package com.zw.countdowntimer.ui.timer.viewmodel

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import com.zw.countdowntimer.R
import com.zw.countdowntimer.ui.timer.MainActivity
import javax.inject.Inject

/**
 * Created by Janak on 27/12/23.
 */
class CountDownTimerViewModel @Inject constructor(): ViewModel() {

	fun postNotification(context: Context) {
		val notificationIntent = Intent(
			context, MainActivity::class.java
		)
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
		val intent = PendingIntent.getActivity(
			context, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
		)


		val notificationManager = NotificationManagerCompat.from(context)
		val CHANNEL_ID = "HEADS_UP_NOTIFICATION"
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(
				CHANNEL_ID, "Heads Up Notification", NotificationManager.IMPORTANCE_HIGH
			)
			notificationManager.createNotificationChannel(channel)
		}

		val notification: NotificationCompat.Builder =
			NotificationCompat.Builder(context, CHANNEL_ID).setContentTitle(context.getString(R.string.app_name)).setContentText("Coun down timer Stopped")
				.setContentIntent(intent).setSmallIcon(R.drawable.notification_icon)
				.setAutoCancel(true)
		if (ActivityCompat.checkSelfPermission(
				context, Manifest.permission.POST_NOTIFICATIONS
			) != PackageManager.PERMISSION_GRANTED
		) {
			return
		}
		notificationManager.notify(0, notification.build())

	}
}