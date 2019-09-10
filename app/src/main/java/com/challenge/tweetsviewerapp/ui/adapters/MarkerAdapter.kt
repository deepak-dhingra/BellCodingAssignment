package com.challenge.tweetsviewerapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import com.challenge.tweetsviewerapp.constants.Constants
import com.challenge.tweetsviewerapp.databinding.MarkerItemBinding
import com.challenge.tweetsviewerapp.interfaces.IItemClickListener
import com.challenge.tweetsviewerapp.utils.formatTime
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.twitter.sdk.android.core.models.Tweet

class MarkerAdapter(var context: Context, var iItemClickListener: IItemClickListener) :
    GoogleMap.InfoWindowAdapter,
    GoogleMap.OnInfoWindowClickListener {

    lateinit var tweet: Tweet

    fun setItem(tweet: Tweet) {
        this.tweet = tweet
    }

    companion object {
        fun getTweetInfoList(marker: Marker): List<String> = marker.snippet.split("~")
        fun encodedTweet(it: Tweet): String =
            "${it.user.profileImageUrlHttps}~${it.createdAt}~${it.user.name}~${it.id}~${Constants.getType(
                it)}"

        const val INDEX_PHOTO_URL = 0
        const val INDEX_TIME = 1
        const val INDEX_TWEET_TEXT = 2
        const val INDEX_TWEET_ID = 3
        const val INDEX_TWEET_TYPE = 4
    }

    override fun getInfoContents(marker: Marker?): View {
        val text = getTweetInfoList(marker!!)
        val photoUrl = text[INDEX_PHOTO_URL]
        val timestamp = text[INDEX_TIME]
        val tweet = text[INDEX_TWEET_TEXT]

        val binding = MarkerItemBinding.inflate(LayoutInflater.from(context))
        binding.tvTime.text = formatTime(timestamp)
        Glide.with(context)
            .load(photoUrl)
            .override(100, 100)
            .dontAnimate()
            .into(binding.ivProfilePic)

        binding.tvName.text = marker.title
        binding.tvTweet.text = tweet

        return binding.root
    }

    override fun getInfoWindow(marker: Marker?) = null

    override fun onInfoWindowClick(marker: Marker?) {
        val text = getTweetInfoList(marker!!)
        val id = text[INDEX_TWEET_ID].toLong()
        val type = text[INDEX_TWEET_TYPE]
//        when (Constants.getType(tweet)) {
//            Constants.VIDEO -> {
                iItemClickListener.onItemClick(id, type)
            }
//            Constants.MULTIPLE_PHOTOS -> {
//                iItemClickListener.onItemClick(
//                    getUrlList(tweet.extendedEntities.media),
//                    Constants.PHOTO
//                )
//            }
//            Constants.PHOTO -> {
//                iItemClickListener.onItemClick(arrayListOf(tweet.getImageUrl()), Constants.PHOTO)
//            }
//            else -> {
//                iItemClickListener.onItemClick(tweet, Constants.getType(tweet))
//            }
        }

