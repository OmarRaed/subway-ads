package com.omaar.ads_sdk.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.omaar.ads_sdk.databinding.ActivityAdViewBinding
import com.omaar.ads_sdk.network.ClickService

internal class AdViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //receive data from intent
        val mediaLink = intent.getStringExtra("MEDIA_LINK")
        val adLink = intent.getStringExtra("AD_LINK")
        val token = intent.getStringExtra("TOKEN")
        val category = intent.getStringExtra("CATEGORY")
        val brandName = intent.getStringExtra("BRAND")
        val type = intent.getStringExtra("TYPE")

        //handle ad according to it's type
        if (type!!.startsWith("image")) {
            showImage(mediaLink!!)
        } else if (type.startsWith("video")) {
            playVideo(mediaLink!!)
        }

        //handle user ad clicks
        binding.clickView.setOnClickListener {
            adClicked(token!!, category!!, brandName!!, adLink!!)
        }

    }

    /**
     * The method responsible for showing images
     */
    private fun showImage(mediaLink: String) {

        //set the video view visibility to GONE
        binding.videoView.visibility = GONE

        //set the image view visibility to VISIBLE
        binding.imageView.visibility = VISIBLE

        //show the image
        Glide.with(this)
            .load(mediaLink)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.imageView).apply {

            }

    }

    /**
     * The method responsible for playing videos
     */
    private fun playVideo(mediaLink: String) {

        //set the image view visibility to GONE
        binding.imageView.visibility = GONE

        //set the video view visibility to VISIBLE
        binding.videoView.visibility = VISIBLE

        //start playing video
        binding.videoView.setVideoURI(Uri.parse(mediaLink))
        binding.videoView.start()
    }

    /**
     * The method responsible for handling user ad clicks
     */
    private fun adClicked(token: String, category: String, brandName: String, adLink: String) {

        //call increment click function
        ClickService()
            .incrementClick(token, category, brandName)

        //start the link of this ad in the browser
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(adLink))
        startActivity(browserIntent)
    }

    override fun onPause() {
        super.onPause()
        //if video is visible pause it
        if (binding.videoView.visibility == VISIBLE)
            binding.videoView.pause()
    }

    override fun onResume() {
        super.onResume()
        //if video is visible resume it
        if (binding.videoView.visibility == VISIBLE)
            binding.videoView.start()
    }
}
