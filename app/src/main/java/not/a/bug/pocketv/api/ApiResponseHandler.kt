package not.a.bug.pocketv.api

import not.a.bug.pocketv.model.NetworkResult
import retrofit2.Response

class ApiResponseHandler {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        return try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
}