package com.cleancleanerclean.phonebooster.ui.BatterySaverFragment

import android.app.ActivityManager
import android.content.ContentResolver
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.res.ColorStateList
import android.graphics.Color
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cleancleanerclean.phonebooster.BuildConfig
import com.cleancleanerclean.phonebooster.R
import kotlinx.coroutines.*

class BatterySaverViewModel : ViewModel() {
    fun checkedColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.white)
    }
    fun uncheckedColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.button_background)
    }
    fun setColorStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_enabled)
            ), intArrayOf(
                R.color.transparent_white,
                Color.WHITE
            )
        )
    }
    var finishFragment = MutableLiveData<Boolean>()
    var countText = MutableLiveData<String>()

    fun countApps(context: Context){
        var appsCount = 0
        val names = ArrayList<Int>()
        val packList: List<PackageInfo> =
            context.packageManager.getInstalledPackages(0)
        packList.forEach {
            val packInfo = it
            if (packInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0 && packInfo.applicationInfo.flags and ApplicationInfo.FLAG_STOPPED == 0) {
                val packageName = packInfo.applicationInfo.packageName
                if (packageName != BuildConfig.APPLICATION_ID) {
                    val am :ActivityManager= context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                    am.killBackgroundProcesses(packageName)
                    appsCount++
                    names.add(appsCount)
                }
            }
        }.also {
            var count=0
            val job  = Job()
            CoroutineScope(Dispatchers.IO+job).launch {
                while(names.size>count){

                    delay(50)
                    count++
                    countText.postValue(context.getString(R.string.countApps)+" "+count+"/"+names.size)
                }
                finishFragment.postValue(true)
                    job.cancel()

            }
        }

    }
    fun setBrightness(brightness: Int, context: Context) {

        //constrain the value of brightness
        var brightnessInline = brightness
        if (brightnessInline < 0) brightnessInline = 0 else if (brightnessInline > 255) brightnessInline = 255
        val cResolver: ContentResolver =
            context.applicationContext.contentResolver
        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightnessInline)
    }



    var colorAnimDuration = 500L
    var timer= 3000L
}