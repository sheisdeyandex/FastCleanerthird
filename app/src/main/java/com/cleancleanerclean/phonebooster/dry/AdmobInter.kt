package com.cleancleanerclean.phonebooster.dry

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.cleancleanerclean.phonebooster.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AdmobInter {
    private var mInterstitialAd: InterstitialAd? = null
    var isLoaded = MutableLiveData<Boolean>()
    fun loadInter(context:Context){
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context,context.getString(R.string.admob_inter), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
    }
    fun showInter(activity:Activity){
        mInterstitialAd?.show(activity)
        loadInter(activity)
    }
    }
