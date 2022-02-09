package com.develdio.appcenterms.stages

import com.develdio.appcenterms.client.api.AppCenterOpenApi
import com.develdio.appcenterms.data.AppCenterParameters
import com.develdio.appcenterms.stages.delegate.interfaces.IAppUploaderStageDelegate

class AppUploaderStage(
    private val uploderDelegate: IAppUploaderStageDelegate
) {
    fun upload() {
        uploderDelegate.upload()
    }

    fun distribute(distributeInfo: AppCenterOpenApi.DistributeInfo) {
        uploderDelegate.distribute(distributeInfo)
    }

    fun delete(specifiedReleaseId: String? = null) {
        uploderDelegate.delete(specifiedReleaseId)
    }

    fun justDelete(specifiedReleaseId: String, appCenterParameters: AppCenterParameters) {
        uploderDelegate.justDelete(specifiedReleaseId, appCenterParameters)
    }
}
