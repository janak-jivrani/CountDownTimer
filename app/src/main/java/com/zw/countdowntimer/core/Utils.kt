package com.zw.countdowntimer.core

fun convertMinutesToMilliseconds(minutesString: String): Long {
    try {
        val minutes = minutesString.toLong()
        return minutes * 60 * 1000
    } catch (e: NumberFormatException) {
        e.printStackTrace()
        return -1
    }
}

fun milliToMinutes(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val remainingMilliseconds = milliseconds % 1000

    return String.format("%02d:%02d:%03d", minutes, seconds, remainingMilliseconds)
}