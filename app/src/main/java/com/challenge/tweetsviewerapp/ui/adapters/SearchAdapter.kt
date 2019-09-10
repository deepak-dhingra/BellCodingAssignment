package com.challenge.tweetsviewerapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.challenge.tweetsviewerapp.R
import com.challenge.tweetsviewerapp.databinding.*
import com.challenge.tweetsviewerapp.interfaces.IItemClickListener
import com.challenge.tweetsviewerapp.ui.viewholders.search.*
import com.challenge.tweetsviewerapp.utils.*
import com.twitter.sdk.android.core.models.Tweet

class SearchAdapter(var list: List<Tweet>, var iItemClickListener: IItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.tweet_basic -> BasicTweetViewHolder(
               TweetBasicBinding.inflate(LayoutInflater.from(parent.context),parent, false),iItemClickListener
            )
            R.layout.tweet_photo -> PhotosViewHolder(
                TweetPhotoBinding.inflate(LayoutInflater.from(parent.context),parent, false),iItemClickListener
            )
            R.layout.tweet_quote -> QuoteViewHolder(
                TweetQuoteBinding.inflate(LayoutInflater.from(parent.context),parent, false),iItemClickListener
            )
            R.layout.tweet_multiplephotos -> MultiplePhotosViewHolder(
                TweetMultiplephotosBinding.inflate(LayoutInflater.from(parent.context),parent, false),iItemClickListener
            )
            R.layout.tweet_video -> VideoViewHolder(
                TweetVideoBinding.inflate(LayoutInflater.from(parent.context),parent, false),iItemClickListener
            )
            R.layout.tweet_link -> LinkViewHolder(
                TweetLinkBinding.inflate(LayoutInflater.from(parent.context),parent, false),iItemClickListener
            )
            else -> throw UnsupportedOperationException("No Type found")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val tweet = list[position]
        return when {
            tweet.hasSingleImage() -> R.layout.tweet_photo
            tweet.hasSingleVideo() -> R.layout.tweet_video
            tweet.hasMultipleMedia() -> R.layout.tweet_multiplephotos
            tweet.hasQuotedStatus() -> R.layout.tweet_quote
            tweet.hasLinks() -> R.layout.tweet_link
            else -> R.layout.tweet_basic
        }
    }
    override fun getItemCount(): Int =list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is BasicTweetViewHolder)
        {
            holder.bind(list[position])
        }
        else if(holder is VideoViewHolder)
        {
            holder.bind(list[position])
        }
        else if(holder is QuoteViewHolder)
        {
            holder.bind(list[position])
        }
        else if(holder is PhotosViewHolder)
        {
            holder.bind(list[position])
        }
        else if(holder is LinkViewHolder)
        {
            holder.bind(list[position])
        }
        else if(holder is MultiplePhotosViewHolder)
        {
            holder.bind(list[position])
        }
    }
}