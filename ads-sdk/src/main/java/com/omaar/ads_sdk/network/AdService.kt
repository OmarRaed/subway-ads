package com.omaar.ads_sdk.network

import android.app.Application
import android.content.Intent
import android.util.Log
import com.omaar.ads_sdk.model.AdMeta
import com.omaar.ads_sdk.view.AdViewActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class AdService(private val application: Application, private var token: String) {

    //declare a lateinit var that will holds the ad
    private lateinit var _ad: AdMeta

    //initialize a job and coroutine to handle background processes
    private val adServiceJob = Job()
    private val coroutineScope = CoroutineScope(adServiceJob + Dispatchers.Main)

    //a flag to check if ad is loaded or not
    private var isAdLoaded : Boolean = false

    init {
        //concatenate token with 'Bearer'
        token = "Bearer $token"
    }

    /**
     * The method responsible for requesting a new ad in a new background process (coroutine)
     */
    fun requestAd(adListener : AdMetaApiListener = object : AdMetaApiListener {
        override fun onAdReady() {
            // just empty body for onAdReady but initialized here to let the 
            // listener be an optional parameter when requestAd() is called
        }
    }
    ) {

        //launch coroutine
        coroutineScope.launch {
            try {
                //request the new ad
                _ad = AdMetaApi.retrofitService.requestAdAsync(token)
                //set isAdLoaded flag to true
                isAdLoaded = true
                //fire onAdReady() listener method
                adListener.onAdReady()
            } catch (e: Exception) {
                //if some error happened log it
                Log.e("SubwayAdService (REQ)", e.message!!)
            }
        }

    }

    /**
     * The method responsible fpr showing the received ad
     */
    fun playAd() {

        //first check if the ad is loaded or not
        if (!isAdLoaded)
            return

        //request a new ad
        requestAd()

        //initialize the intent that will start the ad Activity
        val intent = Intent(application.baseContext, AdViewActivity::class.java)

        //put extras
        intent.putExtra(
            "MEDIA_LINK",
            "https://subway-ticketing-system.herokuapp.com/ads/media/${_ad.id}"
        )
        intent.putExtra("AD_LINK", _ad.adLink)
        intent.putExtra("TOKEN", token)
        intent.putExtra("CATEGORY", _ad.category)
        intent.putExtra("BRAND", _ad.brandName)
        intent.putExtra("TYPE", _ad.type)

        //set ad activity flag and start it
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        application.baseContext.startActivity(intent)

    }

    /**
     * The method responsible for canceling the job
     */
    fun cancelJob() {
        adServiceJob.cancel()
    }

    /**
     * The method used to check if there is a loaded ad or not
     */
    fun isAdReady() : Boolean{
        return isAdLoaded
    }

}