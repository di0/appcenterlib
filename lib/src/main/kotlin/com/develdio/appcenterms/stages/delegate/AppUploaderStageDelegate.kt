package com.develdio.appcenterms.stages.delegate

import com.develdio.appcenterms.client.api.AppCenterOpenApi
import com.develdio.appcenterms.client.spi.UploaderClient
import com.develdio.appcenterms.data.AppCenterParameters
import com.develdio.appcenterms.stages.delegate.interfaces.IAppUploaderStageDelegate

class AppUploaderStageDelegate(
    private val uploaderClients: MutableList<UploaderClient>
) : IAppUploaderStageDelegate {

    override fun upload() {
        uploaderClients.forEach { uploaderClient ->
            Thread { uploaderClient.initialUpload() }.start()
        }
    }

    override fun distribute(distributeInfo: AppCenterOpenApi.DistributeInfo?) {
        uploaderClients.forEach { uploaderClient ->
            Thread {
                do {
                    if (uploaderClient.uploadHasStarted()) {
                        waitByInitialUploadFinish(uploaderClient)
                        uploaderClient.distributeInfo = distributeInfo
                        // Destinations specified, otherwise, comes from AppProperties ones.
                        uploaderClient.distributeInfo?.destinations =
                            distributeInfo?.destinations ?: uploaderClient.destinations()
                        uploaderClient.distribute()
                    } else {
                        Thread.sleep(2000)
                    }
                } while (!uploaderClient.uploadHasCompleted())
            }.start()
        }
    }

    override fun delete(specifiedReleaseId: String?) =
        if (specifiedReleaseId != null) {
            uploaderClients.find { it.releaseId == specifiedReleaseId }?.delete()
        } else {
            uploaderClients.forEach { it.delete() }
        }

    override fun justDelete(specifiedReleaseId: String, appCenterParameters: AppCenterParameters) =
        UploaderClient.Builder(appCenterParameters).build().delete(specifiedReleaseId)

    private fun waitByInitialUploadFinish(uploaderClient: UploaderClient) {
        val r = uploaderClient.waitByUploadEnd()
        uploaderClient.releaseId = r.releaseId // FIXME - obscure logic.
    }
}
