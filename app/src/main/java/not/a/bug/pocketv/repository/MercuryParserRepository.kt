package not.a.bug.pocketv.repository

import not.a.bug.pocketv.api.ApiResponseHandler
import not.a.bug.pocketv.api.MercuryParserApi
import not.a.bug.pocketv.model.NetworkResult
import not.a.bug.pocketv.model.ParsedArticle

class MercuryParserRepository(
    private val apiService: MercuryParserApi
) {
    private val responseHandler = ApiResponseHandler()

    suspend fun getParsedArticle(url: String): NetworkResult<ParsedArticle> {
        return responseHandler.safeApiCall {

            apiService.getParsedArticle(url)
        }
    }
}