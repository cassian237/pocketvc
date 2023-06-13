package not.a.bug.pocketv.model

import com.google.gson.annotations.SerializedName

data class ParsedArticle(
    @SerializedName("title")
    val title: String?,

    @SerializedName("author")
    val author: String?,

    @SerializedName("date_published")
    val datePublished: String?,

    @SerializedName("dek")
    val dek: String?,

    @SerializedName("lead_image_url")
    val leadImageUrl: String?,

    @SerializedName("content")
    val content: String?,

    @SerializedName("next_page_url")
    val nextPageUrl: String?,

    @SerializedName("url")
    val url: String,

    @SerializedName("domain")
    val domain: String,

    @SerializedName("excerpt")
    val excerpt: String?,

    @SerializedName("word_count")
    val wordCount: Int,

    @SerializedName("direction")
    val direction: String?,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("rendered_pages")
    val renderedPages: Int
)