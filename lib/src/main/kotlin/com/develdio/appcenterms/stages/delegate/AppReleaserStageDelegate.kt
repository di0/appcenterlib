package com.develdio.appcenterms.stages.delegate

import com.develdio.appcenterms.client.spi.ReleaserClient
import com.develdio.appcenterms.client.api.AppCenterOpenApi
import com.develdio.appcenterms.stages.delegate.interfaces.IAppReleaserStageDelegate

class AppReleaserStageDelegate(
    private val releaseRequester: ReleaserClient
) : IAppReleaserStageDelegate {

    override fun orderReleaseListById(): MutableList<AppCenterOpenApi.ReleaseInfo>? {
        val releases: MutableList<AppCenterOpenApi.ReleaseInfo>? = releaseRequester.allVersion()
        releases?.sortBy { release ->
            release.id.toInt()
        }
        return releases
    }

    override fun getHighestIdFromReleaseList(): String? {
        val releases: MutableList<AppCenterOpenApi.ReleaseInfo>? = releaseRequester.allVersion()
        releases?.sortBy { release ->
            release.id.toInt()
        }
        return releases?.get(0)?.id
    }

    override fun correlateReleaseNoteWithSameVersion(releaseId: String): String? {
        val correlateReleaseNote = StringBuilder()

        val versionRelease: AppCenterOpenApi.ReleaseInfo? = releaseRequester.versionById(releaseId)
        val releases: MutableList<AppCenterOpenApi.ReleaseInfo>? =
            releaseRequester.allVersionByReleaseId(versionRelease?.id)
        releases?.forEach() { release ->
            correlateReleaseNote.appendLine(correlateReleaseNote, release.releaseNotes)
        }
        return correlateReleaseNote.toString().trimIndent()
    }

    private fun StringBuilder.appendLine(sb: StringBuilder, s: String) {
        sb.append(s + System.getProperty("line.separator"))
    }
}
