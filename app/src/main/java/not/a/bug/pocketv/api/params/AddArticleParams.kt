package not.a.bug.pocketv.api.params

data class AddArticleParams(
    val url: String,
    val title: String?,
    val tags: String?,
    val consumer_key: String,
    val access_token: String
)