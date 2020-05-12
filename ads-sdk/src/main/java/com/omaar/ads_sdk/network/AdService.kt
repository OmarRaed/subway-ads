package com.omaar.ads_sdk.network

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val _isAdLoaded = MutableLiveData<Boolean>()
    val isAdLoaded: LiveData<Boolean>
        get() = _isAdLoaded

    init {
        //concatenate token with 'Bearer'
        token = "Bearer $token"

        //initialize isAdLoaded flag to false
        _isAdLoaded.value = false
    }

    /**
     * The method responsible for requesting a new ad in a new background process (coroutine)
     */
    fun requestAd() {

        //launch coroutine
        coroutineScope.launch {
            try {
                //request the new ad
                _ad = AdMetaApi.retrofitService.requestAdAsync(token)
                //set isAdLoaded flag to true
                _isAdLoaded.value = true
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
        if (!isAdLoaded.value!!)
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

        //start the ad activity
        application.baseContext.startActivity(intent)

    }

    /**
     * The method responsible for canceling the job
     */
    fun cancelJob() {
        adServiceJob.cancel()
    }

}