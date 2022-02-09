package com.develdio.appcenterms.client.spi

import com.develdio.appcenterms.client.api.AppCenterOpenApi
import com.develdio.appcenterms.client.api.UploadApi
import com.develdio.appcenterms.data.AppCenterParameters
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UploaderClient : AbstractClient {
    private var appCenterParameters: AppCenterParameters? = null
    private var uploadService: UploadApi = createService(UploadApi::class.java)
    private var uploadHasStarted: Boolean = false
    private var uploadHasCompleted: Boolean = false

    private constructor(
        appCenterParameters: AppCenterParameters
    ) : super(appCenterParameters.url) {
        this.appCenterParameters = appCenterParameters
    }

    private constructor(
        appCenterParameters: AppCenterParameters, oldClient: AbstractClient
    ) : super(oldClient) {
        this.appCenterParameters = appCenterParameters
    }

    var releaseId: String? = null
        set(value) {
            field = value!!
        }

    var distributeInfo: AppCenterOpenApi.DistributeInfo? = null
        set(value) {
            field = value!!
        }

    var uploadId: String? = null
        set(value) {
            field = value!!
        }

    fun environmentName() = appCenterParameters?.environmentName

    fun destinations() = appCenterParameters
        ?.uploadParameters?.distributeParameters?.destinations!!

    override fun getToken(): String = appCenterParameters!!.token!!

    fun initialUpload() = runBlocking {
        launch {
            val infoBeforeUpload = uploadService.prepareInitialUploadApk(
                appCenterParameters!!.ownerName!!,
                appCenterParameters!!.appName!!
            ).invokeOrThrow()!!

            val fileUploader = createFileUploader(infoBeforeUpload)
            val infoAboutMetadata = fileUploader.configureMetadata()
            doFileUploader(fileUploader, infoAboutMetadata)

            uploadId = infoBeforeUpload.id
            commitReleaseUpload()
            uploadHasStarted = true
        }
    }

    fun commitReleaseUpload() {
        uploadService.commitReleaseUpload(
            appCenterParameters!!.ownerName!!,
            appCenterParameters!!.appName!!,
            uploadId!!,
            AppCenterOpenApi.CommitReleaseUploadInfo("uploadFinished")
        ).execute()
    }

    fun distribute() = runBlocking {
        launch {
            uploadService.distribute(
                appCenterParameters!!.ownerName!!,
                appCenterParameters!!.appName!!,
                releaseId!!,
                distributeInfo!!
            ).execute()
        }
    }

    fun delete(specifiedReleaseId: String? = null) {
        uploadService.delete(
            appCenterParameters!!.ownerName!!,
            appCenterParameters!!.appName!!,
            specifiedReleaseId ?: releaseId!!
        ).invokeOrThrow()
    }

    fun getInfoAboutUpload(): AppCenterOpenApi.AfterUploadInfo {
        return uploadService.getInfoAboutUploaded(
            appCenterParameters!!.ownerName!!,
            appCenterParameters!!.appName!!,
            uploadId!!
        ).invokeOrThrow()!!
    }

    fun uploadHasStarted(): Boolean = this.uploadHasStarted

    fun uploadHasCompleted(): Boolean = this.uploadHasCompleted

    internal fun waitByUploadEnd(): AppCenterOpenApi.AfterUploadInfo {
        var afterUploadInfo: AppCenterOpenApi.AfterUploadInfo?
        do {
            afterUploadInfo = getInfoAboutUpload()
            Thread.sleep(2000) // TODO remove hard value.
        } while(afterUploadInfo?.uploadStatus != "readyToBePublished")
        uploadHasCompleted = true
        return afterUploadInfo
    }

    internal fun newBuilder(
        newAppCenterParameters: AppCenterParameters
    ): Builder {
        return Builder(newAppCenterParameters, this)
    }

    private fun createFileUploader(
        infoBeforeUpload: AppCenterOpenApi.BeforeUploadInfo): FileUploaderClient {
        return (FileUploaderClient.Builder()
            .urlFromDomain(infoBeforeUpload.uploadDomain)
            .packageAssetId(infoBeforeUpload.packageAssetId)
            .token(appCenterParameters!!.token!!)
            .tokenFromUploadUrl(infoBeforeUpload.token)
            .file(appCenterParameters!!.uploadParameters.file!!)
            .build())
    }

    private fun doFileUploader(fileUploader: FileUploaderClient,
                               infoAboutMetadata: AppCenterOpenApi.MetaDataInfo) {
        fileUploader.configureMetadata()
        fileUploader.id = infoAboutMetadata.id
        fileUploader.chunkList = infoAboutMetadata.chunkList
        fileUploader.chunkSize = infoAboutMetadata.chunkSize
        fileUploader.uploadWithChunk()
        fileUploader.finishUpload()
    }

    data class Builder(
        private var appCenterParameters: AppCenterParameters,
        private var oldClient: AbstractClient? = null
    ) {
        fun registerAppCenterParameters(appCenterParameters: AppCenterParameters)
            = apply { this.appCenterParameters = appCenterParameters }

        fun build(): UploaderClient {
            if (oldClient == null) {
                return UploaderClient(this.appCenterParameters)
            }
            else {
                return UploaderClient(this.appCenterParameters, oldClient!!)
            }
        }
    }
}
