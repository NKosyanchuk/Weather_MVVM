package com.example.weathermvvmapp

import android.annotation.SuppressLint
import android.app.Instrumentation
import android.content.Context
import android.os.SystemClock
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before

abstract class BaseTest {

    companion object {
        const val SHORT_SLEEP_IN_MILIS: Long = 300
        const val AVERAGE_SLEEP_IN_MILIS: Long = 500
        const val LONG_SLEEP_IN_MILIS: Long = 1000
        const val REALLY_LONG_SLEEP_IN_MILIS: Long = 3000

        lateinit var instrumentation: Instrumentation

        @SuppressLint("StaticFieldLeak")
        lateinit var targetContext: Context

        @SuppressLint("StaticFieldLeak")
        lateinit var testContext: Context
    }

    @Before
    open fun setup() {
        instrumentation = InstrumentationRegistry.getInstrumentation()
        targetContext = instrumentation.targetContext
        testContext = instrumentation.context
    }

    protected fun readFileFromAssets(fileName: String): String {
        val inputStreamReader = testContext.assets.open(fileName)
        val size = inputStreamReader.available()
        val bufferArray = ByteArray(size)
        inputStreamReader.read(bufferArray)
        inputStreamReader.close()
        return String(bufferArray)
    }

    /**
     * Sleeps 300 millisecond's
     */
    protected fun sleepShort() {
        SystemClock.sleep(SHORT_SLEEP_IN_MILIS)
    }

    /**
     * Sleeps 500 millisecond's
     */
    protected fun sleepAverage() {
        SystemClock.sleep(AVERAGE_SLEEP_IN_MILIS)
    }

    /**
     * Sleeps 1000 millisecond's
     */
    protected fun sleepLong() {
        SystemClock.sleep(LONG_SLEEP_IN_MILIS)
    }

    /**
     * Sleeps 3000 millisecond's
     */
    protected fun sleepReallyLong() {
        SystemClock.sleep(REALLY_LONG_SLEEP_IN_MILIS)
    }

    /**
     * Sleeps for a time specified by parameter
     * @param timeToSleep Time in milliseconds for how long should test wait
     */
    protected fun sleepSpecificTime(timeToSleep: Long) {
        SystemClock.sleep(timeToSleep)
    }
}