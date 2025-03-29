package com.example.project_simplrepair.Models

import com.squareup.moshi.Json

data class PhoneSpecsResponse(
    @Json(name = "phoneDetails") val phoneDetails: PhoneDetails,
    @Json(name = "gsmLaunchDetails") val launchDetails: LaunchDetails,
    @Json(name = "gsmPlatformDetails") val gsmPlatformDetails: GsmPlatformDetails,
    @Json(name = "gsmMainCameraDetails") val gsmMainCameraDetails: GsmMainCameraDetails,
)

data class PhoneDetails(
    @Json(name = "yearValue") val year: String?,
    @Json(name = "brandValue") val brand: String?,
    @Json(name = "modelValue") val modelName: String?
)

data class LaunchDetails(
    @Json(name = "launchAnnounced") val launchDate: String?
)

data class GsmPlatformDetails (
    @Json(name = "platformChipset") val chipset: String??,
    @Json(name = "platformCpu") val cpu: String?,
    @Json(name = "platformGpu") val gpu: String?
)

data class GsmMainCameraDetails(
    @Json(name = "mainCameraFeatures") val mainCamFeatures: String,
    @Json(name = "mainCameraTriple") val mainCameraTriple: String? = null,
    @Json(name = "mainCameraQuad") val mainCameraQuad: String? = null,
    @Json(name = "mainCameraVideo") val mainCameraVideo: String
)

data class GsmSelfieCameraDetails (
    @Json(name = "selfieCameraFeatures") val selfieCamFeatures: String?,
    @Json(name = "selfieCameraSingle") val selfieCameraSpecs: String?,
    @Json(name = "selfieCameraVideo") val selfieCameraVideo: String?
)



