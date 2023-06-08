package not.a.bug.pocketv.api.params

data class ModifyArticleParams(
    val actions: List<ModifyActionParams>,
    val consumer_key: String,
    val access_token: String
)