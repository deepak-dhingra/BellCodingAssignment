package com.challenge.tweetsviewerapp.repo

import com.google.android.gms.maps.model.LatLng
import com.twitter.sdk.android.core.models.Tweet

interface Repository
{
    fun getTweetsByLocation(latLng: LatLng, radius: Int)

    fun searchTweets(query: String, latLng: LatLng, radius: Int)

    fun getTweetById(id:Long)

    fun favorite(tweetObj: Tweet)

    fun retweet(tweetObj: Tweet)

    fun unfavorite(tweetObj: Tweet)

    fun unretweet(tweetObj: Tweet)
}