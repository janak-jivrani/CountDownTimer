package com.zw.countdowntimer.ui.timer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.zw.countdowntimer.ui.theme.CountDownTimerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	private val requestNotificationPermissions =
		registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissions -> }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			CountDownTimerTheme {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					Greeting("Android")
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
	Text(
		text = "Hello $name!",
		modifier = modifier
	)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	CountDownTimerTheme {
		Greeting("Android")
	}
}