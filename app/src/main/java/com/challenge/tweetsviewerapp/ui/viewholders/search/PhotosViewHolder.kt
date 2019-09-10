package com.challenge.tweetsviewerapp.ui.viewholders.search

import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.challenge.tweetsviewerapp.R
import com.challenge.tweetsviewerapp.constants.Constants
import com.challenge.tweetsviewerapp.databinding.TweetPhotoBinding
import com.challenge.tweetsviewerapp.interfaces.IItemClickListener
import com.challenge.tweetsviewerapp.utils.formatTime
import com.challenge.tweetsviewerapp.utils.getImageUrl
import com.twitter.sdk.android.core.models.Tweet

class PhotosViewHolder(
    var binding: TweetPhotoBinding,
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
            .asBitmap()
            .load(item.getImageUrl())
            .error(R.mipmap.ic_launcher)
            .dontAnimate()
            .into(object : BitmapImageViewTarget(binding.ivTweetPhoto) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    super.onResourceReady(resource, transition)
                }

            })
        binding.cvTweetPhotoItem.setOnClickListener{
            iItemClickListener.onItemClick(item, Constants.BASIC)
        }
        binding.ivTweetPhoto.setOnClickListener {
            iItemClickListener.onItemClick(arrayListOf(item.getImageUrl()),Constants.PHOTO)
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