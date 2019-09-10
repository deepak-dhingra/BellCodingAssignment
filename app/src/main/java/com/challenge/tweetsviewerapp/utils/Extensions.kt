package com.challenge.tweetsviewerapp.utils

import com.twitter.sdk.android.core.models.Tweet


fun Tweet.hasSingleImage(): Boolean {
    extendedEntities?.media?.size?.let { return it == 1 && extendedEntities.media[0].type == "photo" }
    return false
}

fun Tweet.hasSingleVideo(): Boolean {
    extendedEntities?.media?.size?.let { return it == 1 && extendedEntities.media[0].type != "photo" }
    return false
}

fun Tweet.hasMultipleMedia(): Boolean {
    extendedEntities?.media?.size?.let { return it > 1 }.run { return false }
}

fun Tweet.hasQuotedStatus(): Boolean {
    return quotedStatus != null
}

fun Tweet.hasLinks() : Boolean {
    return extendedEntities?.urls?.isNotEmpty() ?: false
}

fun Tweet.getImageUrl(): String {
    if (hasSingleImage() || hasMultipleMedia())
        return entities.media[0]?.mediaUrl ?: ""
    throw RuntimeException("This Tweet does not have a photo")
}

fun Tweet.getVideoCoverUrl(): String {
    if (hasSingleVideo() || hasMultipleMedia())
        return entities.media[0]?.mediaUrlHttps ?: (entities.media[0]?.mediaUrl ?: "")
    throw RuntimeException("This Tweet does not have a video")
}

fun Tweet.getVideoUrlType(): Pair<String, String> {
    if (hasSingleVideo() || hasMultipleMedia()) {
        val variant =  extendedEntities.media[0].videoInfo.variants
        return Pair(variant[0].url, variant[0].contentType)
    }
    throw RuntimeException("This Tweet does not have a video")
}