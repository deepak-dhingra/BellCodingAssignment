package com.challenge.tweetsviewerapp.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.twitter.sdk.android.core.models.Search
import com.twitter.sdk.android.core.models.Tweet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiConnection<T> {

    var tweetsList = MutableLiveData<List<Tweet>>()

    var tweet = MutableLiveData<Tweet>()
    var apiError = MutableLiveData<Boolean>()

    /**
     * Function for making api connection
     */
    fun makeApiConnection(call: Call<T>) {
        call.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                Log.v("Tag", t.message!!)
                apiError.value = true
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                Log.v("Tag", response.body().toString())
                if (response.code() == 200) {
                    if (response.body() is Search)
                        tweetsList.postValue((response.body() as Search).tweets)
                    else if (response.body() is Tweet)
                        tweet.postValue(response.body() as Tweet)
                } else {
                    apiError.value = true
                }
            }
        })
    }
}