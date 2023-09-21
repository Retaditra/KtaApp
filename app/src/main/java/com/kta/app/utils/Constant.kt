package com.kta.app.utils

import java.util.concurrent.Executors

const val NOTIFY_CHANNEL_NAME = "Schedule Channel"
const val NOTIFY_CHANNEL_ID = "notify-schedule"
const val NOTIFY_ID = 32
const val ID_REPEATING = 101

private val SINGLE_EXECUTOR = Executors.newSingleThreadExecutor()

fun executeThread(f: () -> Unit) {
    SINGLE_EXECUTOR.execute(f)
}
