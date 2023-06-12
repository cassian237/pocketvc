package not.a.bug.pocketv.api

import not.a.bug.pocketv.model.ApiResponse
import not.a.bug.pocketv.model.PocketAction
import not.a.bug.pocketv.model.PocketArticle
import not.a.bug.pocketv.model.PocketUser
import not.a.bug.pocketv.model.RequestTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PocketApi {
    @POST("/v3/oauth/request")
    suspend fun requestToken(
        @Query("consumer_key") consumerKey: String,
        @Query("redirect_uri") redirectUri: String,
    ): Response<RequestTokenResponse>

    @POST("/v3/oauth/authorize")
    suspend fun authorize(
        @Query("consumer_key") consumerKey: String,
        @Query("code") code: String,
    ): Response<PocketUser>

    @POST("/v3/add")
    suspend fun addItem(
        @Body article: PocketArticle,
        @Query("consumer_key") consumerKey: String,
        @Query("access_token") accessToken: String
    ): Response<PocketArticle>

    @POST("/v3/send")
    suspend fun modifyItem(
        @Body action: PocketAction,
        @Query("consumer_key") consumerKey: String,
        @Query("access_token") accessToken: String
    ): Response<String>

    @GET("/v3/get")
    suspend fun getItems(
        @Query("consumer_key") consumerKey: String,
        @Query("access_token") accessToken: String,
        @Query("state") state: String?,
        @Query("sort") sort: String?,
        @Query("detailType") detailType: String?,
        @Query("tag") tag: String?,
        @Query("favorite") favorite: Int?,
        @Query("contentType") contentType: String?,
        @Query("count") count: Int?,
    ): Response<ApiResponse<PocketArticle>>
}