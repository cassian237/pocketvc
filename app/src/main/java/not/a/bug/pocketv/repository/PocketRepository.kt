package not.a.bug.pocketv.repository

import not.a.bug.pocketv.SessionManager
import not.a.bug.pocketv.api.ApiResponseHandler
import not.a.bug.pocketv.api.PocketApi
import not.a.bug.pocketv.model.AccessTokenNotFoundException
import not.a.bug.pocketv.model.ApiResponse
import not.a.bug.pocketv.model.NetworkResult
import not.a.bug.pocketv.model.PocketAction
import not.a.bug.pocketv.model.PocketArticle
import not.a.bug.pocketv.model.PocketUser
import not.a.bug.pocketv.model.RequestTokenResponse

class PocketRepository(
    private val apiService: PocketApi,
    private val sessionManager: SessionManager
) {
    private val responseHandler = ApiResponseHandler()

    suspend fun requestToken(redirectUri: String): NetworkResult<RequestTokenResponse> {
        return responseHandler.safeApiCall {
            val consumerKey = sessionManager.getConsumerKey()
            apiService.requestToken(consumerKey, redirectUri)
        }
    }

    suspend fun authorize(code: String): NetworkResult<PocketUser> {
        return responseHandler.safeApiCall {
            val consumerKey = sessionManager.getConsumerKey()
            val result = apiService.authorize(consumerKey, code)
            result.body()?.let {
                sessionManager.saveAccessToken(it.access_token)
            }
            result
        }
    }

    suspend fun addItem(
        article: PocketArticle
    ): NetworkResult<PocketArticle> {
        return responseHandler.safeApiCall {
            val consumerKey = sessionManager.getConsumerKey()
            val accessToken =
                sessionManager.getAccessToken() ?: throw AccessTokenNotFoundException()
            apiService.addItem(article, consumerKey, accessToken)
        }
    }

    suspend fun modifyItem(
        action: PocketAction
    ): NetworkResult<String> {
        return responseHandler.safeApiCall {
            val consumerKey = sessionManager.getConsumerKey()
            val accessToken =
                sessionManager.getAccessToken() ?: throw AccessTokenNotFoundException()
            apiService.modifyItem(action, consumerKey, accessToken)
        }
    }

    suspend fun getItems(
        state: String? = null,
        sort: String? = null,
        search: String? = null,
        tag: String? = null,
        favorite: Int? = null,
        detailType: String? = null,
        contentType: String? = null,
        count: Int? = null,
    ): NetworkResult<ApiResponse<PocketArticle>> {
        return responseHandler.safeApiCall {
            val consumerKey = sessionManager.getConsumerKey()
            val accessToken =
                sessionManager.getAccessToken() ?: throw AccessTokenNotFoundException()
            apiService.getItems(
                consumerKey, accessToken, state, sort, search, detailType,
                tag,
                favorite,
                contentType,
                count
            )
        }
    }
}