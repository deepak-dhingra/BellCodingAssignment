package com.challenge.tweetsviewerapp.utils

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.text.format.DateUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.challenge.tweetsviewerapp.R
import com.google.android.gms.maps.model.Circle
import com.twitter.sdk.android.core.models.MediaEntity
import com.twitter.sdk.android.core.models.Tweet
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.roundToInt


fun getZoomLevel(circle: Circle?): Float {
    if (circle == null) return 0.5f

    val radius = circle.radius
    return getZoomLevel(radius)
}

/**
 * im Meters not KM
 */
fun getZoomLevel(radius: Double): Float {
    val scale = radius / 500
    val zoomLevel = (16 - ln(scale) / ln(2.0)).roundToInt().toFloat()
    return zoomLevel + 0.5f
}
const val INVALID_DATE: Long = -1
private val DATE_TIME_RFC822 : SimpleDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH)

fun formatTime(time: String): CharSequence? {
    val createdAt = apiTimeToLong(time)
    if (createdAt != INVALID_DATE) {
        return DateUtils.getRelativeTimeSpanString(createdAt)
    }

    return null
}

fun apiTimeToLong(apiTime: String?): Long {
    if (apiTime == null) return INVALID_DATE

    try {
        return DATE_TIME_RFC822.parse(apiTime).time
    } catch (e: ParseException) {
        return INVALID_DATE
    }
}

fun openTweet(context: Context,tweet: Tweet) {
    context.startActivity(
        Intent.createChooser(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://twitter.com/${tweet.user.screenName}/status/${tweet.id}")
            ), "tweet"
        )
    )
}

fun getUrlList(mediaList: List<MediaEntity>): List<String> {
    val list = ArrayList<String>()

    for (item in mediaList) {
        list.add(item.mediaUrlHttps)
    }
    return list
}

fun showMessageOKCancel(context: Context,okListener: DialogInterface.OnClickListener) {
    AlertDialog.Builder(context)
        .setMessage(R.string.allow_gps_permission)
        .setPositiveButton(R.string.ok, okListener)
        .setNegativeButton(R.string.cancel, null)
        .create()
        .show()
}