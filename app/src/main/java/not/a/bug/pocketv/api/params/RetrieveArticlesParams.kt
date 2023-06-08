package not.a.bug.pocketv.api.params

data class RetrieveArticlesParams(
    val state: String?,
    val favorite: Int?,
    val tag: String?,
    val contentType: String?,
    val sort: String?,
    val detailType: String?,
    val search: String?,
    val domain: String?,
    val since: Long?,
    val count: Int?,
    val consumer_key: String,
    val access_token: String
)