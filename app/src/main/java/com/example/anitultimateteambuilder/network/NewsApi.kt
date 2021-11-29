package com.example.anitultimateteambuilder.network

import android.content.Context
import android.util.Log
import com.example.anitultimateteambuilder.MyApplication
import com.example.anitultimateteambuilder.domain.NewsApiAnswer
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface NewsApiClient {
    @GET("everything")
    fun getNewsAsync(@Query("q") q:String,@Query("from") from:String,@Query("apiKey") apiKey:String,@Query("page") page:Int): Deferred<Response<NewsApiAnswer>>
}

object WebAccess {
    var usingCache = false
    private const val cacheSize = (5 * 1024 * 1024).toLong()
    private val cache = Cache(MyApplication.appContext.cacheDir, cacheSize)

    val okHttpClient = OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor { chain ->
            var request = chain.request()
            request = if (!usingCache)
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
            else
                request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
            chain.proceed(request)
        }
        .build()
    val partsApi : NewsApiClient by lazy {
        Log.d("WebAccess", "Creating retrofit client")
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            // Moshi maps JSON to classes
            .addConverterFactory(MoshiConverterFactory.create())
            // The call adapter handles threads
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()

        // Create Retrofit client
        return@lazy retrofit.create(NewsApiClient::class.java)
    }

}