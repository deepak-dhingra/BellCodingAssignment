package com.challenge.tweetsviewerapp.constants

import com.challenge.tweetsviewerapp.utils.*
import com.twitter.sdk.android.core.models.Tweet

enum class Constants {

    BASIC, LINK, PHOTO, VIDEO, MULTIPLE_PHOTOS, QUOTE;

    companion object {
        fun getType(tweet: Tweet) = when {
            tweet.hasSingleImage() -> PHOTO
            tweet.hasSingleVideo() -> VIDEO
            tweet.hasMultipleMedia() -> MULTIPLE_PHOTOS
            tweet.hasQuotedStatus() -> QUOTE
            tweet.hasLinks() -> LINK
            else -> BASIC
        }
    }
}