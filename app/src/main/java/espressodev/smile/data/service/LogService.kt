package espressodev.smile.data.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}
