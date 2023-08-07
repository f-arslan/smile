package com.smile.util

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

fun getCurrentTimestamp(): Long {
    val zoneId = ZoneId.systemDefault()
    val now = ZonedDateTime.now(zoneId)
    return now.toInstant().toEpochMilli()
}

fun timestampToDate(timestamp: Long): String {
    val zoneId = ZoneId.systemDefault()
    val instant = Instant.ofEpochMilli(timestamp)
    val zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId)
    return zonedDateTime.toOffsetDateTime().toString()
}