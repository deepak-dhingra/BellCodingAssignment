package com.challenge.tweetsviewerapp.application

import android.app.Application
import android.content.Context
import android.util.Log
import com.challenge.tweetsviewerapp.BuildConfig
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig

class TweetApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        enableTwitter(this)
    }
    init {
        instance = this
    }

    companion object {
        private var instance: TweetApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

    }

    fun enableTwitter(context: Context) {
        val config = TwitterConfig.Builder(context)
            .logger(DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(
                TwitterAuthConfig(
                    BuildConfig.TwitterApiKey,
                    BuildConfig.TwitterAppSecret
                )
            )
            .debug(true)
            .build()

        Twitter.initialize(config)
    }

}