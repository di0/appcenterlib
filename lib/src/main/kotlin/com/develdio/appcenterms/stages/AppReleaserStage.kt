package com.develdio.appcenterms.stages

import com.develdio.appcenterms.client.api.AppCenterOpenApi
import com.develdio.appcenterms.stages.delegate.interfaces.IAppReleaserStageDelegate

internal class AppReleaserStage(private val delegate: IAppReleaserStageDelegate) {
    fun orderReleaseListById(): MutableList<AppCenterOpenApi.ReleaseInfo>? {
        return delegate.orderReleaseListById()
    }

    fun getHighestIdFromReleaseList(): String? {
        return delegate.getHighestIdFromReleaseList()
    }

    fun correlateReleaseNoteWithSameVersion(releaseId: String): String? {
        return delegate.correlateReleaseNoteWithSameVersion(releaseId)
    }
}