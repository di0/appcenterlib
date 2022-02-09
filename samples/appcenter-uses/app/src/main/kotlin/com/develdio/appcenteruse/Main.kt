package com.develdio.appcenteruse

import com.develdio.appcenterms.AppCenterProperties
import com.develdio.appcenterms.client.api.AppCenterOpenApi
import com.develdio.appcenterms.distribute.NoteCreator
import com.develdio.appcenterms.stages.AppUploaderStage
import com.develdio.appcenterms.stages.delegate.AppUploaderStageDelegate
import com.develdio.appcenterms.stages.factory.AppUploaderClientFactory

private fun createReleaseNote(): String {
    return(NoteCreator()
        .addAboutIt("Testing version, don't use it. Just a integration test.")
        .addReasonWithFile("/foo/my_file") // Replace it by any full path from file name on file system. See
                                                // more about it following the template file example into directory sample.
        .addAuthor("James Bond")
        .addBranchName("release/1.0.0")
        .addCommitHash("f1a32cfabb4a5d8ba2331ef1f1839aa7fdd95572")
        .create())
}

private fun prepareDistributeInfo(): AppCenterOpenApi.DistributeInfo {
    val note = createReleaseNote()
    val distributeInfo = AppCenterOpenApi.DistributeInfo(
        releaseNotes = note
    )
    return distributeInfo
}

fun main() {
    val uploaderClients = AppUploaderClientFactory.createClientsFromAppCenterProperties(
        AppCenterProperties("/foo/appcenter-configuration.json") // See more about it following the template file example into directory sample.
    )

    val delegate = AppUploaderStageDelegate(uploaderClients)
    var appUploader = AppUploaderStage(delegate)
    appUploader.upload()
    appUploader.distribute(prepareDistributeInfo())
}
