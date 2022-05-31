package com.cleancleanerclean.phonebooster.ui.CoolFragment

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.cleancleanerclean.phonebooster.R
import java.io.BufferedReader
import java.io.InputStreamReader
import android.os.HardwarePropertiesManager

import androidx.core.content.ContextCompat.getSystemService




class CoolViewModel : ViewModel() {
    val timerTime = 3000L
    fun initImageDrawable(context:Context): Drawable? {
      return  ContextCompat.getDrawable(context, R.drawable.ic_ventilator)
    }
//    fun testTemperature(context: Context): Float? {
//        val hardwarePropertiesManager =
//            context.getSystemService(Context.HARDWARE_PROPERTIES_SERVICE) as HardwarePropertiesManager?
//        val temp = hardwarePropertiesManager?.getDeviceTemperatures(
//            HardwarePropertiesManager.DEVICE_TEMPERATURE_CPU,
//            HardwarePropertiesManager.TEMPERATURE_CURRENT
//        )
//
//return temp?.get(1)
//    }
     fun cpuTemperature(): Float {
        val process: Process
        return try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp")
            process.waitFor()
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val line = reader.readLine()
            if (line != null) {
                val temp = line.toFloat()
                temp / 1000.0f
            } else {
                51.0f
            }
        } catch (e: Exception) {
            e.printStackTrace()
            0.0f
        }
    }
    fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }
}