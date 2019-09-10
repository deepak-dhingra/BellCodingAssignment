package com.challenge.tweetsviewerapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.challenge.tweetsviewerapp.repo.TwitterRepository
import com.google.android.gms.maps.model.LatLng
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.models.Tweet
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var twitterSession: TwitterSession
    var tweet= MutableLiveData<Tweet>()

    var searchTweetsList = MutableLiveData<List<Tweet>>()
    var tweetsList = MutableLiveData<List<Tweet>>()

    var twitterRepository = TwitterRepository()


    fun getTwitterSuccess(result: Result<TwitterSession>?) {
        twitterSession = TwitterCore.getInstance().sessionManager.activeSession
    }

    fun getTweetsByLocation(latLng: LatLng, radius: Int) {

        twitterRepository.getTweetsByLocation(latLng,radius)
        twitterRepository.tweets.observeForever{
            tweetsList.postValue(getFilteredList(it))
        }
    }


    fun searchTweets(query:String ,latLng: LatLng, radius: Int) {
        twitterRepository.searchTweets(query,latLng,radius)
        twitterRepository.tweets.observeForever{
            searchTweetsList.postValue(it)
        }
    }


    fun favroties(tweetObj: Tweet) {
        twitterRepository.favorite(tweetObj)
        twitterRepository.tweet.observeForever{
            tweet.postValue(it)
        }
    }

    fun unfavroties(tweetObj: Tweet) {
        twitterRepository.unfavorite(tweetObj)
        twitterRepository.tweet.observeForever{
            tweet.postValue(it)
        }
    }

    fun retweet(tweetObj: Tweet) {
        twitterRepository.retweet(tweetObj)
        twitterRepository.tweet.observeForever{
            tweet.postValue(it)
        }
    }

    fun unretweet(tweetObj: Tweet) {
        twitterRepository.unretweet(tweetObj)
        twitterRepository.tweet.observeForever{
            tweet.postValue(it)
        }
    }


    private fun getFilteredList(list:List<Tweet>):List<Tweet>
    {
        val filteredList = ArrayList<Tweet>()
        for (tweet in list.iterator()) {
            if (tweet.coordinates != null) {
                filteredList.add(tweet)
            }
        }
        return filteredList
    }


}