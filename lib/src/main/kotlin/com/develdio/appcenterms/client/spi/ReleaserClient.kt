package com.develdio.appcenterms.client.spi

import com.develdio.appcenterms.client.api.AppCenterOpenApi
import com.develdio.appcenterms.client.api.ReleaseHistoryApi


class ReleaserClient private constructor(
    private val url: String,
    private val ownerName: String?,
    private val appName: String?,
    private val token: String?
) : AbstractClient(url) {

    private var releaseService: ReleaseHistoryApi = createService(ReleaseHistoryApi::class.java)

    override fun getToken(): String = this.token!!

    fun allVersion(): MutableListReleases = releaseService.all(ownerName!!, appName!!).invokeOrThrow()

    fun allVersionByReleaseId(id: String?): MutableListReleases {
        return null // TODO implements it
    }

    fun versionById(id: String): AppCenterOpenApi.ReleaseInfo? {
        return releaseService.byId(ownerName!!, appName!!, id).execute().body()
    }

    data class Builder(
        private var url: String,
        private var ownerName: String? = null,
        private var appName: String? = null,
        private var token: String? = null
    ) {
        fun ownerName(ownerName: String) = apply { this.ownerName = ownerName }

        fun appName(appName: String) = apply { this.appName = appName }

        fun token(token: String) = apply { this.token = token }

        fun build() = ReleaserClient(url, ownerName, appName, token)
    }
}

typealias MutableListReleases = MutableList<AppCenterOpenApi.ReleaseInfo>?
