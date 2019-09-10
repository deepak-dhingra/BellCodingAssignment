package com.challenge.tweetsviewerapp.ui.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import com.challenge.tweetsviewerapp.R
import com.challenge.tweetsviewerapp.baseclasses.BaseActivity
import com.challenge.tweetsviewerapp.databinding.ActivityVideoBinding
import com.challenge.tweetsviewerapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_video.*

class VideoActivity : BaseActivity<ActivityVideoBinding,MainViewModel>() {
    override fun getLayoutId(): Int = R.layout.activity_video

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    companion object {
        const val VIDEO_URL_EXTRA = "video_url"
        const val VIDEO_TYPE_EXTRA = "video_type"
    }

    private lateinit var videoUrl: String
    lateinit var videoType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        videoUrl = intent.getStringExtra(VIDEO_URL_EXTRA)!!
        videoType = intent.getStringExtra(VIDEO_TYPE_EXTRA)!!

        val mc = MediaController(this)
        mc.setAnchorView(videoView)
        mc.setMediaPlayer(videoView)

        videoView.setOnPreparedListener { progressBar.visibility = View.GONE }
        if ("animated_gif" == videoType)
            videoView.setOnCompletionListener { videoView.start() }

        videoView.setMediaController(mc)
        videoView.setVideoURI(Uri.parse(videoUrl))
        videoView.requestFocus()
        videoView.start()
    }
}