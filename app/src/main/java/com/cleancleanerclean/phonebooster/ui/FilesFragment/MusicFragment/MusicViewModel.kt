package com.cleancleanerclean.phonebooster.ui.FilesFragment.MusicFragment

import android.app.Activity
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import com.cleancleanerclean.phonebooster.ui.CleanFragment.CleanFragment.Companion.convertSize
import com.cleancleanerclean.phonebooster.ui.FilesFragment.MusicFragment.model.MusicModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicViewModel : ViewModel() {
     lateinit var musicList: ArrayList<MusicModel>
    private var uniqueId = 0

    private fun getUniqueId(): Int {
        return uniqueId++
    }
     fun queryMusic(activity:Activity) {
        musicList = ArrayList()
        val cursor = activity.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )
        if (cursor != null) {
            musicList.clear()
            CoroutineScope(Dispatchers.IO).launch {
                while (cursor.moveToNext()) {
                    val stringBuilder = StringBuilder()
                    val id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    val title =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    val path =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                    val time =
                        cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    val size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                    var hours: Int
                    var minutes: Int
                    var seconds = time / 1000
                    hours = seconds / 3600
                    minutes = seconds / 60 % 60
                    seconds %= 60
                    if (hours in 1..9) stringBuilder.append("0").append(hours)
                        .append(":") else if (hours > 0) stringBuilder.append(hours).append(":")
                    if (minutes < 10) stringBuilder.append("0").append(minutes)
                        .append(":") else stringBuilder.append(minutes).append(":")
                    if (seconds < 10) stringBuilder.append("0")
                        .append(seconds) else stringBuilder.append(
                        seconds
                    )
                    val myMusic = MusicModel(
                        title,
                        stringBuilder.toString(),
                        convertSize(size.toLong())!!,
                        path,
                        getUniqueId()
                    )
                    musicList.add(myMusic)
                }
            }
        }
    }
}