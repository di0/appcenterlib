package com.develdio.appcenterms.client.api

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FileUploadApi {
    @POST("/upload/set_metadata/{packageAssetId}")
    fun configureMetadata(
        @Path("packageAssetId") packageAssetId: String,
        @Query("file_name") fileName: String,
        @Query("file_size") fileSize: Long,
        @Query("token") token: String,
        @Query("content_type") contentType: String
    ): Call<AppCenterOpenApi.MetaDataInfo>

    @POST("/upload/upload_chunk/{packageAssetId}")
    fun uploadChunk(
        @Path("packageAssetId") packageAssetId: String,
        @Query("block_number") chunkId: String,
        @Query("token") token: String,
        @Body chunk: RequestBody
    ): Call<AppCenterOpenApi.UploadChunkInfo>

    @POST("/upload/finished/{packageAssetId}")
    fun finishUpload(
        @Path("packageAssetId") packageAssetId: String,
        @Query("token") token: String
    ): Call<AppCenterOpenApi.FinishUploadInfo>
}
