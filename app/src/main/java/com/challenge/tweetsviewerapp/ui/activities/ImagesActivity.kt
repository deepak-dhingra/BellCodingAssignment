package com.challenge.tweetsviewerapp.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.challenge.tweetsviewerapp.R
import com.challenge.tweetsviewerapp.databinding.ActivityImagesBinding
import com.challenge.tweetsviewerapp.ui.adapters.ViewPagerAdapter

class ImagesActivity:AppCompatActivity() {

    companion object {
        const val IMAGE_URLS_EXTRA = "video_url"
    }


    lateinit var imageUrls:ArrayList<String>
    lateinit var binding:ActivityImagesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_images)
        supportActionBar?.hide()
        imageUrls = intent.getStringArrayListExtra(IMAGE_URLS_EXTRA)
        val viewPagerAdaptor = ViewPagerAdapter(this,imageUrls)
        binding.imagesViewPager.adapter = viewPagerAdaptor
    }
}