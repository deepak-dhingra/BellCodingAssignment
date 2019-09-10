package com.challenge.tweetsviewerapp.ui.viewholders.search

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.challenge.tweetsviewerapp.R
import com.challenge.tweetsviewerapp.constants.Constants
import com.challenge.tweetsviewerapp.databinding.TweetVideoBinding
import com.challenge.tweetsviewerapp.interfaces.IItemClickListener
import com.challenge.tweetsviewerapp.utils.formatTime
import com.challenge.tweetsviewerapp.utils.getVideoCoverUrl
import com.twitter.sdk.android.core.models.Tweet

class VideoViewHolder(
    var binding: TweetVideoBinding,
    var iItemClickListener: IItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Tweet) {
        binding.tweet = item
        binding.executePendingBindings()

        binding.tvTime.text = formatTime(item.createdAt)
        Glide.with(binding.root.context)
            .load(item.user.profileImageUrlHttps)
            .error(R.mipmap.ic_launcher)
            .dontAnimate()
            .into(binding.ivProfilePic)
        Glide.with(binding.root.context)
            .load(item.getVideoCoverUrl())
            .error(R.mipmap.ic_launcher)
            .dontAnimate()
            .into(binding.ivTweetVideo)
        binding.cvTweetVideoItem.setOnClickListener{
            iItemClickListener.onItemClick(item, Constants.BASIC)
        }
        binding.ibPlayVideo.setOnClickListener {
            iItemClickListener.onItemClick(item, Constants.VIDEO)
        }


        if (item.favorited) {
            binding.ibFavourite.setImageResource(R.drawable.ic_favorite_blue)
        } else {
            binding.ibFavourite.setImageResource(R.drawable.ic_favorite)
        }
        binding.ibFavourite.setOnClickListener {
            if (item.favorited) {
                binding.ibFavourite.setImageResource(R.drawable.ic_favorite)
                iItemClickListener.unfavorite(item)
            } else {

                binding.ibFavourite.setImageResource(R.drawable.ic_favorite_blue)
                iItemClickListener.favorite(item)
            }
        }

        if (item.favorited) {
            binding.retweetImageButton.setImageResource(R.drawable.ic_retweet_blue)
        } else {
            binding.retweetImageButton.setImageResource(R.drawable.ic_retweet)
        }
        binding.retweetImageButton.setOnClickListener {
            if (item.retweeted) {
                binding.retweetImageButton.setImageResource(R.drawable.ic_retweet)
                iItemClickListener.unretweet(item)
            } else {
                binding.retweetImageButton.setImageResource(R.drawable.ic_retweet_blue)
                iItemClickListener.retweet(item)
            }
        }
    }
}