package com.cleancleanerclean.phonebooster.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.cleancleanerclean.phonebooster.R
import com.cleancleanerclean.phonebooster.Utils.UiUtils
import com.cleancleanerclean.phonebooster.databinding.ActivityMainBinding
import com.google.android.gms.ads.MobileAds
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
     lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initAdmob()
        initNavHostFragment()
        UiUtils.hideSystemUI(this)
    }
    private fun initAdmob(){
        MobileAds.initialize(
            this
        ) { }
    }
    private fun initNavHostFragment(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.navController
    }
    companion object{

        fun convertSize(length: Long): String? {
            val format = DecimalFormat("#.##")
            val mb = (1024 * 1024).toLong()
            val kb: Long = 1024
            if (length > mb) {
                return format.format(length / mb) + " MB"
            }
            return if (length > kb) {
                format.format(length / kb) + " KB"
            } else format.format(length) + " B"
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}