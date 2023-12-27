package com.zw.countdowntimer.ui.timer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zw.countdowntimer.core.milliToMinutes
import com.zw.countdowntimer.ui.theme.Purple40
import com.zw.countdowntimer.ui.timer.viewmodel.TimerEvent

@Composable
fun TimerView(
    totalTime: Long,
    inactiveBarColor: Color,
    activeBarColor: Color,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 5.dp,
    currentTime: Long,
    isTimerRunning: Boolean,
    stop:()->Unit,
    callback : (timerEvent :TimerEvent) -> Unit
) {

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    var value = currentTime / totalTime.toFloat()


    Column {

        Box(contentAlignment = Alignment.Center, modifier = modifier.onSizeChanged {
            size = it
        }) {
            Canvas(modifier = modifier) {
                drawArc(
                    color = inactiveBarColor,
                    startAngle = -215f,
                    sweepAngle = 250f,
                    useCenter = false,
                    size = Size(size.width.toFloat(), size.height.toFloat()),
                    style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = activeBarColor,
                    startAngle = -215f,
                    sweepAngle = 250f * value,
                    useCenter = false,
                    size = Size(size.width.toFloat(), size.height.toFloat()),
                    style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                )
            }
            Text(
                text = milliToMinutes(currentTime),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Button(
                onClick = {
                    if (currentTime <= 0L) {
                        callback(TimerEvent.IsPlay)
                    } else {
                        callback(TimerEvent.IsPause)
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple40
                )
            ) {
                Text(
                    text = if (isTimerRunning && currentTime >= 0L) "Pause"
                    else if (!isTimerRunning && currentTime >= 0L) "Start"
                    else "Restart"
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp),
            visible = isTimerRunning
        ) {
            Button(
                onClick = {
                   stop()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple40
                )
            ) {
                Text(
                    text = "Stop"
                )
            }
        }

    }


}


@Preview(showSystemUi = true)
@Composable
private fun TimeViewPre() {
    /*Surface(
        modifier = Modifier.fillMaxSize(), color = Color.Black
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            TimerView(
                totalTime = 60000L,
                inactiveBarColor = Color.DarkGray,
                activeBarColor = Purple40,
                modifier = Modifier.size(300.dp)
            )
        }
    }*/
}
