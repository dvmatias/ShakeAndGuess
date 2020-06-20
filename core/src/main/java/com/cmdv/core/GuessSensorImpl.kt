package com.cmdv.core

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.MutableLiveData
import com.cmdv.core.Constants.Companion.GUEST_RESULT_FAIL
import com.cmdv.core.Constants.Companion.GUEST_RESULT_SUCCESS


class GuessSensorImpl(private val context: Context) : GuessSensor, SensorEventListener {

    private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private lateinit var sensor: Sensor
    private var tickCount: Int = 0
    private var lastTickTime: Long = 0
    private var minimumTimeBetweenTicks: Long = 250
    private var previousResult: Int = 0
    private var currentResult: Int = 0
    private val guessResultMutableLiveData = MutableLiveData<Int>()

    init {
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun registerListener() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun unregisterListener() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // Nothing.
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        val x = p0?.values?.get(0)?.toInt() ?: 1000
        val z = p0?.values?.get(2)?.toInt() ?: 1000

        currentResult = when {
            x <= 8 && z >= 5 -> { // Success!
                GUEST_RESULT_SUCCESS
            }
            x <= 9 && z <= -3 -> { // Fail!
                GUEST_RESULT_FAIL
            }
            else -> {
                0
            }
        }
        updateGetGuessResultOnlyIfItChanges()
    }

    override fun updateGetGuessResultOnlyIfItChanges(): MutableLiveData<Int> {
        if (System.currentTimeMillis() >= (lastTickTime + minimumTimeBetweenTicks)) {
            lastTickTime = System.currentTimeMillis()
            if (currentResult != previousResult) {
                previousResult = currentResult
                guessResultMutableLiveData.postValue(currentResult)
            } else {
                guessResultMutableLiveData.postValue(null)
            }
        } else {
            guessResultMutableLiveData.postValue(null)
        }

        return guessResultMutableLiveData
    }
}