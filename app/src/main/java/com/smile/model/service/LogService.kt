package com.smile.model.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}
