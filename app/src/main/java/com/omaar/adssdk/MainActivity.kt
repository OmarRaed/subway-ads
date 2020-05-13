package com.omaar.adssdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.omaar.ads_sdk.network.AdMetaApiListener
import com.omaar.ads_sdk.network.AdService

class MainActivity : AppCompatActivity() {

    //declare a lateinit var that will holds the ad service
    private lateinit var adService: AdService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialize play button
        val playAdBtn = findViewById<Button>(R.id.playAd)

        //initialize application
        val application = requireNotNull(this).application

        //initialize AdService
        adService = AdService(
            application, "token goes here..."
        )

        //request ad
        //A new ad is automatically requested whenever you play ad so you only need to call requestAd() once
        adService.requestAd(object : AdMetaApiListener {
            //optional listener
            override fun onAdReady() {
                adService.playAd()
            }
        })

        //handle play button clicks
        playAdBtn.setOnClickListener {
            //play ad
            adService.playAd()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        //don't forget to cancel the job
        adService.cancelJob()
    }
}
