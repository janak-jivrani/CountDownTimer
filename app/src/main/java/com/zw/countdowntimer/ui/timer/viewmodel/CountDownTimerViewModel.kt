package com.zw.countdowntimer.ui.timer.viewmodel

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zw.countdowntimer.core.CountTimer
import com.zw.countdowntimer.R
import com.zw.countdowntimer.ui.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Janak on 27/12/23.
 */


sealed class TimerEvent {
	object IsPlay : TimerEvent()
	object IsPause : TimerEvent()
}
@HiltViewModel
class CountDownTimerViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {

	private var duration = 60000L

	var currentTime by mutableStateOf(duration)
	var event = Lifecycle.Event.ON_ANY

	private val _isPlay: MutableStateFlow<Boolean> = MutableStateFlow(false)
	val isPlay : StateFlow<Boolean> = _isPlay

	fun onConsume(timerEvent: TimerEvent) {
		viewModelScope.launch {
			when (timerEvent) {
				TimerEvent.IsPause -> {
					pause()
					_isPlay.emit(false)
				}

				TimerEvent.IsPlay -> {
					startTimer()
					_isPlay.emit(true)
				}
			}
		}
	}

	private val timeCounter = object : CountTimer() {
		override fun onTimerTick(currentProgress: Long) {
			currentTime = currentProgress
		}

		override fun onTimerFinish() {
			viewModelScope.launch {
				currentTime = duration
				_isPlay.emit(false)
				if (event == Lifecycle.Event.ON_PAUSE || event == Lifecycle.Event.ON_STOP) {
					postNotification(context)
				} else {
					Toast.makeText(context,context.getString(R.string.count_down_timer_stopped),Toast.LENGTH_SHORT).show()
				}
			}
		}

	}


	fun startTimer() {
		timeCounter.setTime(duration)
		timeCounter.start()
	}

	fun stop() {
		viewModelScope.launch {
			timeCounter.stop()
			currentTime = duration
			_isPlay.emit(false)
		}
	}

	fun restart() {
		timeCounter.restart()
	}

	fun pause() {
		timeCounter.pause()
	}

	fun postNotification(context: Context) {
		val notificationIntent = Intent(
			context, MainActivity::class.java
		)
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
		val intent = PendingIntent.getActivity(
			context,
			0,
			notificationIntent,
			PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
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
			NotificationCompat.Builder(context, CHANNEL_ID).setContentTitle(context.getString(R.string.app_name)).setContentText(context.getString(R.string.count_down_timer_stopped))
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