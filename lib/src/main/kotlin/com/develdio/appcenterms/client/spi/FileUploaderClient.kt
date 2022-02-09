package com.develdio.appcenterms.client.spi

import com.develdio.appcenterms.client.api.AppCenterOpenApi
import com.develdio.appcenterms.client.api.FileUploadApi
import java.io.File

class FileUploaderClient private constructor(
    private val url: String?,
    private val packageAssetId: String?,
    private val token: String?,
    private val tokenFromUploadUrl: String?,
    private val file: File?
) : AbstractClient(url) {

    private val fileUploadService: FileUploadApi = createService(FileUploadApi::class.java)

    var chunkList: Array<String>? = null
        set(value) {
            field = value!!
        }

    var chunkSize: Long? = null
        set(value) {
            field = value!!
        }

    var id: String? = null
        set(value) {
            field = value!!
        }

    override fun getToken(): String = this.token!!

    fun configureMetadata(): AppCenterOpenApi.MetaDataInfo {
        return (
            fileUploadService.configureMetadata(
                packageAssetId!!,
                file?.name!!,
                file.length(),
                tokenFromUploadUrl!!,
                CONTENT_TYPE_PACKAGE_ARCHIVE
            ).invokeOrThrow()!!
        )
    }

    fun uploadWithChunk() {
        validateChunkBeforeUpload()

        chunkList?.forEachIndexed { i, chunkId ->
            val range: LongRange = ((i * chunkSize!! )..((i + 1) * chunkSize!!))
            fileUploadService.uploadChunk(
                packageAssetId!!,
                chunkId,
                tokenFromUploadUrl!!,
                ChunkRequesterClient(file!!, range, CONTENT_TYPE_OCTET_STREAM)
            ).execute()
        }
    }

    fun finishUpload() = fileUploadService.finishUpload(
        id!!,
        tokenFromUploadUrl!!
    ).execute()

    /*
     Sometimes chunk list and chunk size are used. Sometimes it's not
     required(ie: metadata operation). */
    private fun validateChunkBeforeUpload() {
        if (chunkList == null) {
            throw ClientException("Chunk List not found. Please, register them.")
        }

        if (chunkSize == null) {
            throw ClientException("Chunk Size not found. Please, register them.")
        }
    }

    data class Builder(
        var url: String? = null,
        var packageAssetId: String? = null,
        var token: String? = null,
        var tokenFromUploadUrl: String? = null,
        var file: File? = null
    ) {
        fun urlFromDomain(url: String) = apply { this.url = url }

        fun packageAssetId(packageAssetId: String) = apply {
            this.packageAssetId = packageAssetId
        }

        fun token(token: String) = apply { this.token = token }

        fun tokenFromUploadUrl(tokenFromUploadUrl: String) = apply {
            this.tokenFromUploadUrl = tokenFromUploadUrl
        }

        fun file(file: File) = apply { this.file = file }

        fun build() = FileUploaderClient(
            url = url,
            packageAssetId = packageAssetId,
            token = token,
            tokenFromUploadUrl = tokenFromUploadUrl,
            file = file
        )
    }
}
