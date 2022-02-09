package com.develdio.appcenterms.client.api

import com.google.gson.annotations.SerializedName

class AppCenterOpenApi {
 data class BeforeUploadInfo(
  @SerializedName("id") var id: String = "",
  @SerializedName("upload_domain") var uploadDomain: String = "",
  @SerializedName("token") var token: String = "",
  @SerializedName("url_encoded_token") var urlEncondedToken: String = "",
  @SerializedName("package_asset_id") var packageAssetId: String = ""
 )

 data class AfterUploadInfo(
  @SerializedName("id") val id: String,
  @SerializedName("upload_status") val uploadStatus: String,
  @SerializedName("error_details") val errorDetails: String?,
  @SerializedName("release_distinct_id") val releaseId: String?
 )

 data class BuildInfo(
  @SerializedName("branch") val branch: String? = null,
  @SerializedName("commit_hash") val commitHash: String? = null,
  @SerializedName("commit_message") val commitMessage: String? = null
 )

 data class CommitReleaseUploadInfo(
  @SerializedName("upload_status") val uploadStatus: String
 )

 data class DestinationInfo(
  @SerializedName("name") val name: String? = null,
  @SerializedName("id") val id: String? = null
 )

 data class DistributeInfo(
  @SerializedName("distribution_group_name") val distributionGroupName: String? = null,
  @SerializedName("distribution_group_id") val distributionGroupId: String? = null,
  @SerializedName("destination_name") val destinationName: String? = null,
  @SerializedName("destination_id") val destinationId: String? = null,
  @SerializedName("destination_type") val destinationType: String? = null,
  @SerializedName("release_notes") val releaseNotes: String? = null,
  @SerializedName("mandatory_update") val mandatoryUpdate: Boolean = false,
  @SerializedName("destinations") var destinations: List<DestinationInfo>? = null,
  @SerializedName("build") val build: BuildInfo? = null,
  @SerializedName("notify_testers") val notifyTesters: Boolean = false
 )

 data class FinishUploadInfo(
  @SerializedName("id") val id: String
 )

 data class DeleteUploadInfo(
  @SerializedName("release_id") val release_id: String
 )

 data class MetaDataInfo(
  @SerializedName("id") val id: String,
  @SerializedName("chunk_size") val chunkSize: Long,
  @SerializedName("chunk_list") val chunkList: Array<String>
 )

 data class ReleaseInfo(
  @SerializedName("origin") var origin: String = "",
  @SerializedName("id") var id: String = "",
  @SerializedName("version") var version: String = "",
  @SerializedName("description") var description: String = "",
  @SerializedName("release_notes") val releaseNotes: String = ""
 )

 data class UploadChunkInfo(
  @SerializedName("packageAssetId") val packageAssetId: String
 )
}
