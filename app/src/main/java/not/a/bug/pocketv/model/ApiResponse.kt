package not.a.bug.pocketv.model

data class ApiResponse<T>(
    val status: Int,
    val list: Map<String, T>
)