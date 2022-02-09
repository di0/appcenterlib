package com.develdio.appcenterms.stages.delegate.interfaces

import com.develdio.appcenterms.client.api.AppCenterOpenApi

interface IAppReleaserStageDelegate {
    fun orderReleaseListById(): MutableList<AppCenterOpenApi.ReleaseInfo>?
    fun getHighestIdFromReleaseList(): String?
    fun correlateReleaseNoteWithSameVersion(releaseId: String): String?
}
