package espressodev.smile.domain.util

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun getCurrentTimestamp(): Long {
    val zoneId = ZoneId.systemDefault()
    val now = ZonedDateTime.now(zoneId)
    return now.toInstant().toEpochMilli()
}

fun timestampToDate(timestamp: Long): String {
    val zoneId = ZoneId.systemDefault()
    val instant = Instant.ofEpochMilli(timestamp)
    val zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId)
    return zonedDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
}

fun isTodayOrDate(timestamp: Long): String {
    val zoneId = ZoneId.systemDefault()
    val instant = Instant.ofEpochMilli(timestamp)
    val zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId)
    val today = ZonedDateTime.now(zoneId)
    return if (zonedDateTime.dayOfYear == today.dayOfYear && zonedDateTime.year == today.year) {
        "Today"
    } else {
        zonedDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd MMM"))
    }
}