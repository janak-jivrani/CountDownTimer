package com.zw.countdowntimer.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.zw.countdowntimer.ui.theme.CountDownTimerTheme
import com.zw.countdowntimer.ui.theme.Purple40
import com.zw.countdowntimer.ui.timer.TimerView
import com.zw.countdowntimer.ui.timer.viewmodel.CountDownTimerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	private val requestNotificationPermissions =
		registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissions -> }




	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			val viewModel = hiltViewModel<CountDownTimerViewModel>()
			CountDownTimerTheme {
				Surface(
					modifier = Modifier.fillMaxSize(), color = Color.Black
				) {
					Box(
						contentAlignment = Alignment.Center
					) {
						TimerView(
							totalTime = 60000L,
							inactiveBarColor = Color.DarkGray,
							activeBarColor = Purple40,
							modifier = Modifier.size(300.dp),
							isTimerRunning = viewModel.isPlay.collectAsState().value,
							callback = {viewModel.onConsume(it)},
							stop = viewModel::stop,
							currentTime = viewModel.currentTime
						)
					}
				}
			}
		}

		if (ActivityCompat.checkSelfPermission(
				this, Manifest.permission.POST_NOTIFICATIONS
			) != PackageManager.PERMISSION_GRANTED
		) {
			requestNotificationPermissions.launch(Manifest.permission.POST_NOTIFICATIONS)
		}
	}
}
