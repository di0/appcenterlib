package com.develdio.appcenterms.stages.factory

import com.develdio.appcenterms.AppCenterProperties
import com.develdio.appcenterms.client.api.AppCenterOpenApi
import com.develdio.appcenterms.client.spi.UploaderClient
import com.develdio.appcenterms.data.AppCenterParameters
import java.io.File

object AppUploaderClientFactory {
    fun createClientsFromAppCenterProperties(
        appCenterProperties: AppCenterProperties
    ): MutableList<UploaderClient> {

        val uploaderClients: MutableList<UploaderClient> = mutableListOf()
        var uploaderClient: UploaderClient?

        appCenterProperties.getEnvironment()?.forEach { (environmentName, configuration) ->
            val appCenterParameters = AppCenterParameters(
                environmentName = environmentName,
                ownerName = configuration.ownerName,
                appName = configuration.appName,
                token = configuration.token,
                url = appCenterProperties.getGlobalConfiguration().appCenterUrl!!
            )

            val destinations = ArrayList<AppCenterOpenApi.DestinationInfo>()
            configuration.destinations?.forEach {
                destinations.add(AppCenterOpenApi.DestinationInfo(name = it))
            }
            appCenterParameters.uploadParameters.file = File(configuration.file)
            appCenterParameters.uploadParameters.distributeParameters.destinations = destinations

            // TODO Reuse the current Thread Connection Pool.
//            if (uploaderClient != null && uploaderClients.isNotEmpty()) {
//                uploaderClients.add(uploaderClient!!.newBuilder(appCenterParameters).build())
//            } else {
                uploaderClient = UploaderClient.Builder(appCenterParameters).build()
                uploaderClients.add(
                    uploaderClient!!
                )
            //}
        }
        return uploaderClients
    }
}
