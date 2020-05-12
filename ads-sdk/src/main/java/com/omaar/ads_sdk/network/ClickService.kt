package com.omaar.ads_sdk.network

import android.util.Log
import com.omaar.ads_sdk.network.AdMetaApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

internal class ClickService{

    //initialize a job and coroutine to handle background processes
    private val clickJob = Job()
    private val coroutineScope = CoroutineScope(clickJob + Dispatchers.Main)

    /**
     * The method responsible for incrementing ad
     */
    fun incrementClick(token: String, category: String, brandName: String) {

        //launch coroutine
        coroutineScope.launch {
            try {
                //call increment click service
                AdMetaApi.retrofitService.incrementClickAsync(token, category, brandName)
                //cancel the job
                clickJob.cancel()
            } catch (e: Exception) {
                //if some error happened log it
                Log.e("SubwayAdService (CLICK)", e.message!!)
                //cancel the job
                clickJob.cancel()
            }
        }
    }

}