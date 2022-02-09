package com.develdio.appcenterms.stages.factory

import com.develdio.appcenterms.AppCenterProperties
import com.develdio.appcenterms.client.spi.AbstractClient
import com.develdio.appcenterms.client.spi.ReleaserClient

object AppReleaserClientFactory {
    fun createSpecificClientFromAppCenterProperties(
        environment: String, appCenterProperties: AppCenterProperties
    ) : ReleaserClient {
        val thatEnvironment = appCenterProperties.getEnvironment()?.get(environment)
            ?: throw AbstractClient.ClientException("Environment $environment not found" +
                    " on AppCenterProperties.")

        val url = appCenterProperties.getGlobalConfiguration().appCenterUrl!!
        return(
            ReleaserClient.Builder(url)
                .ownerName(thatEnvironment.ownerName!!)
                .appName(thatEnvironment.appName!!)
                .token(thatEnvironment.apiToken!!)
                .build()
        )
    }
}
