package com.develdio.appcenterms.client.api

import retrofit2.Call
import retrofit2.http.*

interface UploadApi {
    @GET("apps/{ownerName}/{appName}/uploads/releases/{uploadId}")
    fun getInfoAboutUploaded(
        @Path("ownerName") ownerName: String,
        @Path("appName") appName: String,
        @Path("uploadId") uploadId: String
    ): Call<AppCenterOpenApi.AfterUploadInfo>

    @POST("apps/{ownerName}/{appName}/uploads/releases/")
    fun prepareInitialUploadApk(
        @Path("ownerName") ownerName: String,
        @Path("appName") appName: String
    ): Call<AppCenterOpenApi.BeforeUploadInfo>

    @PATCH("apps/{ownerName}/{appName}/uploads/releases/{uploadId}")
    fun commitReleaseUpload(
        @Path("ownerName") ownerName: String,
        @Path("appName") appName: String,
        @Path("uploadId") uploadId: String,
        @Body status: AppCenterOpenApi.CommitReleaseUploadInfo
    ): Call<AppCenterOpenApi.CommitReleaseUploadInfo>

    @PATCH("apps/{ownerName}/{appName}/releases/{releaseId}")
    fun distribute(
        @Path("ownerName") ownerName: String,
        @Path("appName") appName: String,
        @Path("releaseId") releaseId: String,
        @Body request: AppCenterOpenApi.DistributeInfo
    ): Call<Void>

    @DELETE("apps/{ownerName}/{appName}/releases/{releaseId}")
    fun delete(
        @Path("ownerName") ownerName: String,
        @Path("appName") appName: String,
        @Path("releaseId") releaseId: String
    ): Call<Void>
}
