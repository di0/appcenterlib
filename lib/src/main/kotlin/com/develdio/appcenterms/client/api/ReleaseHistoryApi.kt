package com.develdio.appcenterms.client.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ReleaseHistoryApi {
    @GET("apps/{ownerName}/{appName}/releases")
    fun all(
        @Path("ownerName") ownerName: String,
        @Path("appName") appName: String
    ): Call<MutableList<AppCenterOpenApi.ReleaseInfo>>

    @GET("apps/{ownerName}/{appName}/releases/{id}")
    fun byId(
        @Path("ownerName") ownerName: String,
        @Path("appName") appName: String,
        @Path("id") id: String
    ): Call<AppCenterOpenApi.ReleaseInfo>
}
