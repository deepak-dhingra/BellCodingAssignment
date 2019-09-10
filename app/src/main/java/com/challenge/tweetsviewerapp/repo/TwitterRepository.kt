package com.challenge.tweetsviewerapp.repo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.challenge.tweetsviewerapp.network.ApiConnection
import com.google.android.gms.maps.model.LatLng
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Search
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.services.params.Geocode

class TwitterRepository : Repository {
    var tweet = MutableLiveData<Tweet>()
    var tweets = MutableLiveData<List<Tweet>>()


    override fun getTweetsByLocation(latLng: LatLng, radius: Int) {
        getData("#music #love", latLng, radius)
    }

    override fun searchTweets(query: String, latLng: LatLng, radius: Int) {
        getData(query, latLng, radius)
    }

    private fun getData(query: String, latLng: LatLng, radius: Int) {
        val geocode =
            Geocode(latLng.latitude, latLng.longitude, radius, Geocode.Distance.KILOMETERS)
        val service = TwitterCore.getInstance().apiClient.searchService
        val call = service.tweets(
            query, geocode, null,
            null, null, 100, null, null, null, null
        )

        val apiConnection: ApiConnection<Search> = ApiConnection()
        apiConnection.makeApiConnection(call)

        apiConnection.tweetsList.observeForever(Observer {
            tweets.postValue(it)
        })
    }

    override fun getTweetById(id: Long) {
        val call = TwitterCore.getInstance().apiClient.statusesService.show(id, null, null, null)

        val apiConnection: ApiConnection<Tweet> = ApiConnection()
        apiConnection.makeApiConnection(call)
        apiConnection.tweet.observeForever(Observer {
            tweet.value = it
        })
    }


    override fun favorite(tweetObj: Tweet) {
        val call = TwitterCore.getInstance().apiClient.favoriteService.create(tweetObj.id, null)
        val apiConnection: ApiConnection<Tweet> = ApiConnection()
        apiConnection.makeApiConnection(call)
        apiConnection.tweet.observeForever(Observer {
            tweet.value = it
        })
    }

    override fun retweet(tweetObj: Tweet) {
        val call = TwitterCore.getInstance().apiClient.statusesService.retweet(tweetObj.id, null)
        val apiConnection: ApiConnection<Tweet> = ApiConnection()
        apiConnection.makeApiConnection(call)
        apiConnection.tweet.observeForever(Observer {
            tweet.value = it
        })
    }

    override fun unfavorite(tweetObj: Tweet) {
        val call = TwitterCore.getInstance().apiClient.favoriteService.destroy(tweetObj.id, null)
        val apiConnection: ApiConnection<Tweet> = ApiConnection()
        apiConnection.makeApiConnection(call)
        apiConnection.tweet.observeForever(Observer {
            tweet.value = it
        })
    }

    override fun unretweet(tweetObj: Tweet) {
        val call = TwitterCore.getInstance().apiClient.statusesService.unretweet(tweetObj.id, null)
        val apiConnection: ApiConnection<Tweet> = ApiConnection()
        apiConnection.makeApiConnection(call)
        apiConnection.tweet.observeForever(Observer {
            tweet.value = it
        })
    }


}
