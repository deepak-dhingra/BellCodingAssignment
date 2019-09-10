package com.challenge.tweetsviewerapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.challenge.tweetsviewerapp.R
import com.challenge.tweetsviewerapp.constants.Constants
import com.challenge.tweetsviewerapp.databinding.ItemPhotoBinding
import com.challenge.tweetsviewerapp.interfaces.IItemClickListener
import com.challenge.tweetsviewerapp.utils.getUrlList
import com.twitter.sdk.android.core.models.TweetEntities


class ImagesAdapter(
    var context: Context,
    private val mediaEntities: TweetEntities, var iItemClickListener: IItemClickListener
) :
    RecyclerView.Adapter<ImagesAdapter.PhotoItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoItemViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PhotoItemViewHolder, position: Int) {
        Glide.with(context)
            .load(mediaEntities.media[position].mediaUrlHttps)
            .error(R.mipmap.ic_launcher)
            .dontAnimate()
            .into(holder.ivTweetPhoto)
        holder.ivTweetPhoto.setOnClickListener {
            iItemClickListener.onItemClick(getUrlList(mediaEntities.media), Constants.MULTIPLE_PHOTOS)
        }
    }

    override fun getItemCount() = mediaEntities.media.size

    class PhotoItemViewHolder(binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivTweetPhoto: ImageView = binding.ivTweetPhoto

    }
}
