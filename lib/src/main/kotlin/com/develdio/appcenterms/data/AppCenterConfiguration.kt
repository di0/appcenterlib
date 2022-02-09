package com.develdio.appcenterms.data

import com.google.gson.annotations.SerializedName

data class AppCenterConfiguration(
    @SerializedName("globalConfiguration")
    var globalConfiguration: GlobalConfiguration,

    @SerializedName("environment")
    var environment: HashMap<String, Environment>? = null
)

data class GlobalConfiguration(
    @SerializedName("appCenterUrl")
    var appCenterUrl: String? = null,

    @SerializedName("template")
    val releaseNotetemplate: Array<String>?
)

data class Environment(
    @SerializedName("apiToken")
    var apiToken: String? = null,

    @SerializedName("ownerName")
    var ownerName: String? = null,

    @SerializedName("appName")
    var appName: String? = null,

    @SerializedName("file")
    var file: String? = null,

    @SerializedName("token")
    var token: String? = null,

    @SerializedName("template")
    val releaseNoteTemplate: Array<String>?,

    @SerializedName("destinations")
    val destinations: ArrayList<String>?
)
