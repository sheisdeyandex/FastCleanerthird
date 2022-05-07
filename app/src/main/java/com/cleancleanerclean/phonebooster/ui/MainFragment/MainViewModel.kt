package com.cleancleanerclean.phonebooster.ui.MainFragment

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import androidx.lifecycle.ViewModel


class MainViewModel : ViewModel() {
   fun updateRam(activity: Activity): Int {
      val mi = ActivityManager.MemoryInfo()
      val activityManager: ActivityManager =
         activity.getSystemService(ACTIVITY_SERVICE) as ActivityManager
      activityManager.getMemoryInfo(mi)
      return (mi.availMem / mi.totalMem.toDouble() * 100.0).toInt()
   }
   fun updateRamString(activity: Activity): String {
      val mi = ActivityManager.MemoryInfo()
      val activityManager: ActivityManager =
         activity.getSystemService(ACTIVITY_SERVICE) as ActivityManager
      activityManager.getMemoryInfo(mi)
      return (mi.availMem / mi.totalMem.toDouble() * 100.0).toInt().toString()+"%"
   }
   var internaltotal: Long = getTotalStorageInfo(Environment.getDataDirectory().path)
   var internalfree: Long = getUsedStorageInfo(Environment.getDataDirectory().path)
   var progress = (100*internalfree/internaltotal)
   val devidedStorage = (100*internalfree/internaltotal).toString()+"%"
   private fun getTotalStorageInfo(path: String?): Long {
      val statFs = StatFs(path)
      return statFs.totalBytes
   }
   private fun getUsedStorageInfo(path: String?): Long {
      val statFs = StatFs(path)
      return statFs.totalBytes - statFs.availableBytes
   }
   fun checkSystemWritePermission(context: Context): Boolean {
      return Settings.System.canWrite(context)
   }
}