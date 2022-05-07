package com.cleancleanerclean.phonebooster.ui.AppsManagerFragment.interfaces

import android.net.Uri


interface DeleteAppsI {
    fun delete(integers:ArrayList<Int>, packages:ArrayList<Uri>, ids:ArrayList<Int> )

    fun checked(i: Int)
}