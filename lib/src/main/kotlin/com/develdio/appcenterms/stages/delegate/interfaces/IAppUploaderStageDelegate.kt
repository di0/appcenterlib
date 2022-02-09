package com.develdio.appcenterms.stages.delegate.interfaces

import com.develdio.appcenterms.client.api.AppCenterOpenApi
import com.develdio.appcenterms.data.AppCenterParameters

interface IAppUploaderStageDelegate {
    fun upload()
    fun distribute(distributeInfo: AppCenterOpenApi.DistributeInfo? = null)
    fun delete(specifiedReleaseId: String? = null): Unit?
    fun justDelete(specifiedReleaseId: String, appCenterParameters: AppCenterParameters)
}
