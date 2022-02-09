package com.develdio.appcenterms.data

import com.develdio.appcenterms.client.api.AppCenterOpenApi
import java.io.File

data class AppCenterParameters(
    var url: String? = null,
    var ownerName: String? = null,
    var appName: String? = null,
    var token: String? = null,
    var environmentName: String? = null,

    var uploadParameters: UploadParameters = UploadParameters()
)

data class UploadParameters(
    var file: File? = null,
    var distributeParameters: DistributeParameters = DistributeParameters()
)

data class DistributeParameters(
    var destinations: MutableList<AppCenterOpenApi.DestinationInfo>? = null
)