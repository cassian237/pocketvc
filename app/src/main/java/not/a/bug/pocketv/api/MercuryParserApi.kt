package not.a.bug.pocketv.api

import not.a.bug.pocketv.model.ApiResponse
import not.a.bug.pocketv.model.ParsedArticle
import not.a.bug.pocketv.model.PocketAction
import not.a.bug.pocketv.model.PocketArticle
import not.a.bug.pocketv.model.PocketUser
import not.a.bug.pocketv.model.RequestTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MercuryParserApi {


    @GET("/parser")
    suspend fun getParsedArticle(
        @Query("url") url: String
    ): Response<ParsedArticle>
}