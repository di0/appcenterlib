package com.develdio.appcenterms.client.spi

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.BufferedSink
import okio.buffer
import okio.source
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

abstract class AbstractClient {
    @PublishedApi
    internal lateinit var retrofit: Retrofit // TODO care with internal visibility.

    private var client: OkHttpClient? = null
    private var customLogging: Interceptor? = null
    private var url: String

    constructor(url: String?) {
        this.url = url!!
        createClient()
        bindRetrofit()
    }

    constructor(oldAbstractClient: AbstractClient) {
        client = oldAbstractClient.client
        this.url = oldAbstractClient.url
        customLogging = oldAbstractClient.customLogging
        createClient(false)
        bindRetrofit()
    }

    protected abstract fun getToken(): String

    protected inline fun <reified T : Any> createService(t: Class<T>): T {
        return retrofit.create(t)
    }

    protected fun <T> prepareResponseAfterCall(call: Call<T>) {
        call.enqueue(object: Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    protected fun newBuilder() {
        createClient(false)
        bindRetrofit()
    }

    protected fun <T> Call<T>.invokeOrThrow() = execute().invokeOrThrow()

    private fun <T> Response<T>.invokeOrThrow() =
        if (isSuccessful) {
            body()
        } else {
            throw ClientException("An error occurred at service invoke," +
                    " code=${code()}, reason=${errorBody()?.string()}")
        }

    private fun prepareLogging(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.BASIC)
        logging.redactHeader("Authorization") // Security hidden
        logging.redactHeader("Cookie")
        return logging
    }

    /** FIXME - Due way to shared a connection ... */
    private fun createClient(newConnection: Boolean = true) {
        var bd = if (newConnection) OkHttpClient().newBuilder() else client?.newBuilder()!!

        client = bd
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-API-Token", getToken())
                    .addHeader("Content-Type", CONTENT_TYPE_OR_ACCEPT_JSON)
                    .addHeader("Accept", CONTENT_TYPE_OR_ACCEPT_JSON)
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(customLogging ?: prepareLogging())
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(130, TimeUnit.SECONDS)
            .build()
    }

    private fun bindRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client!!)
            .build()
    }

    companion object {
        const val CONTENT_TYPE_OR_ACCEPT_JSON = "application/json"
        const val CONTENT_TYPE_PACKAGE_ARCHIVE = "application/vnd.android.package-archive"
        const val CONTENT_TYPE_OCTET_STREAM = "application/octet-stream"
    }

    inner class ChunkRequesterClient(
        private val file: File,
        private val range: LongRange,
        private val contentType: String
    ) : RequestBody() {
        private val contentLength: Long by lazy {
            (range.last - range.first).coerceAtMost(file.length() - range.first)
        }

        override fun contentLength(): Long = contentLength

        override fun contentType(): MediaType? = contentType.toMediaTypeOrNull()

        @Throws(IOException::class)
        override fun writeTo(sink: BufferedSink) {
            file.source().buffer().use { source ->
                source.skip(range.first)

                var toRead = contentLength
                while (toRead > 0) {
                    val read = source.read(sink.buffer, toRead)
                    if (read >= 0) {
                        toRead -= read
                    } else {
                        break
                    }
                }
            }
        }
    }

    class ClientException(message: String) : Exception(message)
}
