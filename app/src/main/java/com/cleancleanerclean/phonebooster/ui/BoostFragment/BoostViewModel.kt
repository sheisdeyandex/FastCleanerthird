package com.cleancleanerclean.phonebooster.ui.BoostFragment

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import androidx.lifecycle.ViewModel
import java.io.File

class BoostViewModel : ViewModel() {
    val timerTime = 3000L
    val timerPeriod =1000L
    fun usedMemory (activity:Activity):String{
       val mi = ActivityManager.MemoryInfo()
       val activityManager =
           activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
       activityManager.getMemoryInfo(mi)
       val availableMegs = (mi.availMem / 0x100000L).toDouble().toString()+"MB"
        return availableMegs
   }
     fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

}