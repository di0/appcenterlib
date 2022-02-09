package com.develdio.appcenterms

import com.develdio.appcenterms.data.AppCenterConfiguration
import com.develdio.appcenterms.data.Environment
import com.develdio.appcenterms.data.GlobalConfiguration
import com.google.gson.Gson
import java.io.File

class AppCenterProperties(val fileNameAbsolutePath: String) {
    val appCenterConfiguration: AppCenterConfiguration

    init {
        val file = File(fileNameAbsolutePath).inputStream().readBytes().toString(Charsets.UTF_8)
        appCenterConfiguration = Gson().fromJson(file, AppCenterConfiguration::class.java)
    }

    fun getEnvironment(): HashMap<String, Environment>? {
        return appCenterConfiguration.environment
    }

    fun getGlobalConfiguration(): GlobalConfiguration {
        return appCenterConfiguration.globalConfiguration
    }
}
