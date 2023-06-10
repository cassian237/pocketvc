package not.a.bug.pocketv.model

import com.google.gson.annotations.SerializedName

data class PocketArticle(
    @SerializedName("item_id") val itemId: String,
    @SerializedName("resolved_id") val resolvedId: String,
    @SerializedName("given_url") val givenUrl: String,
    @SerializedName("given_title") val givenTitle: String,
    @SerializedName("favorite") val favorite: String,
    @SerializedName("status") val status: String,
    @SerializedName("resolved_title") val resolvedTitle: String,
    @SerializedName("resolved_url") val resolvedUrl: String,
    @SerializedName("excerpt") val excerpt: String,
    @SerializedName("is_article") val isArticle: String,
    @SerializedName("has_video") val hasVideo: String,
    @SerializedName("has_image") val hasImage: String,
    @SerializedName("word_count") val wordCount: String,
    @SerializedName("images") val images: Map<String, PocketImage>,
    @SerializedName("videos") val videos: Map<String, PocketVideo>
)

data class PocketImage(
    @SerializedName("item_id") val itemId: String,
    @SerializedName("image_id") val imageId: String,
    @SerializedName("src") val src: String,
    @SerializedName("width") val width: String,
    @SerializedName("height") val height: String,
    @SerializedName("credit") val credit: String,
    @SerializedName("caption") val caption: String
)

data class PocketVideo(
    @SerializedName("item_id") val itemId: String,
    @SerializedName("video_id") val videoId: String,
    @SerializedName("src") val src: String,
    @SerializedName("width") val width: String,
    @SerializedName("height") val height: String,
    @SerializedName("type") val type: String,
    @SerializedName("vid") val vid: String
)