package com.challenge.tweetsviewerapp.interfaces

import com.twitter.sdk.android.core.models.Tweet


interface IItemClickListener {
    fun onItemClick(item: Any, type: Any)
    fun favorite(tweet: Tweet)
    fun unfavorite(tweet: Tweet)
    fun retweet(tweet: Tweet)
    fun unretweet(tweet: Tweet)
}