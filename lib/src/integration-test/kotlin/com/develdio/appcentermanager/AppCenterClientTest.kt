package com.develdio.appcenterms

import com.develdio.appcenterms.client.api.AppCenterOpenApi
import com.develdio.appcenterms.distribute.NoteCreator
import com.develdio.appcenterms.stages.delegate.AppUploaderStageDelegate
import com.develdio.appcenterms.stages.factory.AppUploaderClientFactory
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AppCenterClientTest {
    lateinit var uploaderDelegate: AppUploaderStageDelegate

    @BeforeAll
    internal fun setUp() {
        val uploaderClients = AppUploaderClientFactory.createClientsFromAppCenterProperties(
            AppCenterProperties("") // yours file json configuration ...
        )
        uploaderDelegate = AppUploaderStageDelegate(uploaderClients)
    }

    @Test
    @Order(1)
    fun uploadRelease() {
        uploaderDelegate.upload()
    }

    @Test
    @Order(2)
    fun distributeRelease() {
        uploaderDelegate.distribute(prepareDistributeInfo())
    }

    @AfterAll
    @Order(3)
    fun deleteRelease() {
        uploaderDelegate.delete()
    }

    private fun prepareDistributeInfo(): AppCenterOpenApi.DistributeInfo {
        val noteCreator = prepareNoteCreator()
        val distributeInfo = AppCenterOpenApi.DistributeInfo(
            releaseNotes = noteCreator.create(),
            destinations = mutableListOf(
                AppCenterOpenApi.DestinationInfo("Analytics"),
                AppCenterOpenApi.DestinationInfo("Security")
            ).toList()
        )
        return distributeInfo
    }

    private fun prepareNoteCreator(): NoteCreator {
        return(NoteCreator()
            .addAboutIt("Testing version, don't use it." +
                    " Just a integration test.")
            .addReasons("Alter module fake.")
            .addReasons("Modified template fake.")
            .addReasons("Added feature fake.")
            .addAuthor("James Bond")
            .addBranchName("release/1.0.0")
            .addCommitHash("f1a32cfabb4a5d8ba2331ef1f1839aa7fdd95572"))
    }
}
