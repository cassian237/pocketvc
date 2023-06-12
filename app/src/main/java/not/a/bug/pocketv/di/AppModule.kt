package not.a.bug.pocketv.di

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import not.a.bug.pocketv.SessionManager
import not.a.bug.pocketv.api.MercuryParserApi
import not.a.bug.pocketv.api.PocketApi
import not.a.bug.pocketv.repository.MercuryParserRepository
import not.a.bug.pocketv.repository.PocketRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePocketApiService(): PocketApi {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val interceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val modifiedRequest = originalRequest.newBuilder()
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("X-Accept", "application/json")
                .build()
            chain.proceed(modifiedRequest)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://getpocket.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PocketApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMercuryParserApi(): MercuryParserApi {
        return Retrofit.Builder()
            .baseUrl("http://54.36.101.28:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MercuryParserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSessionManager(
        @ApplicationContext context: Context
    ): SessionManager {
        return SessionManager(context)
    }

    @Provides
    @Singleton
    fun providePocketRepository(
        apiService: PocketApi,
        sessionManager: SessionManager
    ): PocketRepository {
        return PocketRepository(apiService, sessionManager)
    }
    @Provides
    @Singleton
    fun provideMercuryParserRepository(
        apiService: MercuryParserApi,
        sessionManager: SessionManager
    ): MercuryParserRepository {
        return MercuryParserRepository(apiService)
    }
}