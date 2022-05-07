/*
 * Copyright 2021 Hunter J Drum
 */
package com.cleancleanerclean.phonebooster.ui.activities

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts.OpenDocumentTree
import androidx.appcompat.app.AppCompatActivity
import com.cleancleanerclean.phonebooster.R
import com.cleancleanerclean.phonebooster.databinding.ActivityWhitelistBinding
import com.cleancleanerclean.phonebooster.ui.CleanFragment.CleanFragment.Companion.prefs

class WhitelistActivity : AppCompatActivity() {
    lateinit var binding: ActivityWhitelistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whitelist)
        binding = ActivityWhitelistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.newButton.setOnClickListener { addToWhiteList() }
        getWhiteList(prefs)
        loadViews()
    }

    private fun loadViews() {
        binding.pathsLayout.removeAllViews()
        val layout = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        layout.setMargins(0, 20, 0, 20)


    }



    /**
     * Creates a dialog asking for a file/folder name to add to the whitelist
     */
    private fun addToWhiteList() {
        mGetContent.launch(Uri.fromFile(Environment.getDataDirectory()))
    }

    private var mGetContent = registerForActivityResult(
        OpenDocumentTree()
    ) { uri: Uri? ->
        if (uri != null) {
            whiteList.add(uri.path!!.substring(uri.path!!.indexOf(":") + 1)) // TODO create file from uri, then just add its path once sd card support is finished
            prefs!!.edit().putStringSet("whitelist", HashSet(whiteList)).apply()
            loadViews()
        }
    }

    companion object {
        private var whiteList: ArrayList<String> = ArrayList()
        fun getWhiteList(prefs: SharedPreferences?): List<String?> {
            if (whiteList.isNullOrEmpty()) {
                if (prefs != null) {
                    whiteList = ArrayList(prefs.getStringSet("whitelist", emptySet()))
                }
                whiteList.remove("[")
                whiteList.remove("]")
            }
            return whiteList
        }
    }
}