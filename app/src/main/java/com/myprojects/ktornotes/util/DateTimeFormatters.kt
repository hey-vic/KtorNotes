package com.myprojects.ktornotes.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.formatByDifferenceFromNow(): String {
    val currentDateTime = LocalDateTime.now()
    val pattern = when {
        currentDateTime.toLocalDate().isEqual(toLocalDate()) -> "HH:mm"
        currentDateTime.minusDays(7).isBefore(this) &&
                currentDateTime.dayOfWeek > dayOfWeek -> "EEE HH:mm"

        currentDateTime.year == year -> "MMMM d"
        else -> "MMMM d yyyy"
    }
    return format(DateTimeFormatter.ofPattern(pattern))
}

fun LocalDateTime.formatWithOptionalYear(): String {
    val currentDateTime = LocalDateTime.now()
    val pattern = if (currentDateTime.year == year) {
        "MMMM d HH:mm"
    } else "MMMM d yyyy HH:mm"
    return format(DateTimeFormatter.ofPattern(pattern))
}