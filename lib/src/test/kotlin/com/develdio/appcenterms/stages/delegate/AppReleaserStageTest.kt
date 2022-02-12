package com.develdio.appcenterms.stages.delegate

import com.develdio.appcenterms.client.spi.ReleaserClient
import com.develdio.appcenterms.client.api.AppCenterOpenApi
import com.develdio.appcenterms.stages.AppReleaserStage
import com.develdio.appcenterms.stages.delegate.AppReleaserStageDelegate
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class AppReleaserStageTest {
    @MockK
    private lateinit var releaseClient: ReleaserClient

    private lateinit var appReleaser: AppReleaserStage

    @BeforeEach
    internal fun setUp() {
        appReleaser = AppReleaserStage(AppReleaserStageDelegate(releaseClient))
    }

    @Test
    fun `should order by release id an unordered list of releases`() {
        every { releaseClient.allVersion() } returns unorderedReleaseList()

        val orderedList = appReleaser.orderReleaseListById()
        assertEquals(orderedList?.get(0)?.id, "58")
        assertEquals(orderedList?.get(1)?.id, "100")
        assertEquals(orderedList?.get(2)?.id, "190")
    }

    @Test
    @DisplayName("The AppCenter API response comes with ordered list by releases ID and not by versions.")
    fun `should get the last higher id of an list of releases`() {
        every { releaseClient.allVersion() } returns unorderedReleaseList()

        val lastRelease = appReleaser.getHighestIdFromReleaseList()
        assertEquals(lastRelease, "58")
    }

    @Test
    @DisplayName("When a version exists, should be possible correlate its releases notes.")
    fun `should correlate release note between same versions`() {
        every { releaseClient.allVersionByReleaseId(any()) } returns releaseNotesList()
        every { releaseClient.versionById(any()) } returns releaseInfoFake()

        val expectedReleaseNote = """Foo modified.
Bar modified.
Baz modified.""".trimIndent()

        val currentReleaseNote = appReleaser.correlateReleaseNoteWithSameVersion("anyId")
        assertEquals(expectedReleaseNote, currentReleaseNote)
    }

    private fun unorderedReleaseList(): MutableList<AppCenterOpenApi.ReleaseInfo> {
        return mutableListOf(
            AppCenterOpenApi.ReleaseInfo("", "190"),
            AppCenterOpenApi.ReleaseInfo("", "100"),
            AppCenterOpenApi.ReleaseInfo("", "58")
        )
    }

    private fun releaseNotesList(): MutableList<AppCenterOpenApi.ReleaseInfo> {
        return mutableListOf(
            AppCenterOpenApi.ReleaseInfo("", "", "1.1.0", "", "Foo modified."),
            AppCenterOpenApi.ReleaseInfo("", "", "1.1.0", "", "Bar modified."),
            AppCenterOpenApi.ReleaseInfo("", "", "1.1.0", "", "Baz modified.")
        )
    }

    private fun releaseInfoFake(): AppCenterOpenApi.ReleaseInfo {
        return AppCenterOpenApi.ReleaseInfo("", "10", "1.1.0", "Qux modified.")
    }
}
