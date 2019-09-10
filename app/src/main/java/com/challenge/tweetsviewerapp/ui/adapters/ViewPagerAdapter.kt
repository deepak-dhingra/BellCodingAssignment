package com.challenge.tweetsviewerapp.ui.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.challenge.tweetsviewerapp.R
import com.challenge.tweetsviewerapp.databinding.ItemViewPagerBinding
import com.challenge.tweetsviewerapp.utils.getImageUrl

class ViewPagerAdapter(var context: Context, var imagesUrl: List<String>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_view_pager, container, false)
        val ivImages = view.findViewById<ImageView>(R.id.ivImages)
        val imageProgressBar = view.findViewById<ProgressBar>(R.id.imageLoader)
        Glide.with(context)
            .asBitmap()
            .load(imagesUrl[position])
            .error(R.mipmap.ic_launcher)
            .dontAnimate()
            .into(object : BitmapImageViewTarget(ivImages) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    super.onResourceReady(resource, transition)
                    imageProgressBar.visibility = View.GONE
                }
            })
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = imagesUrl.size
}