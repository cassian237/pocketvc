package not.a.bug.pocketv.model

data class PocketArticle(
    val item_id: String,
    val resolved_id: String,
    val given_url: String,
    val given_title: String,
    val favorite: String,
    val status: String,
    val time_added: String,
    val time_updated: String,
    val time_read: String,
    val time_favorited: String,
    val sort_id: Int,
    val resolved_title: String,
    val resolved_url: String,
    val excerpt: String,
    val is_article: String,
    val is_index: String,
    val has_video: String,
    val has_image: String,
    val word_count: String
)