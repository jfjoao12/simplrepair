package com.example.project_simplrepair.Models

import com.squareup.moshi.Json

/**
 * Represents the response from the API containing detailed phone specifications.
 *
 * @property phoneDetails     Contains the phone's details such as year, brand, and model.
 * @property launchDetails    Contains the launch date information for the phone.
 * @property gsmPlatformDetails Contains details about the phone's chipset, CPU, and GPU.
 * @property gsmMainCameraDetails Contains information about the main camera, including features, specs, and video capabilities.
 */
data class PhoneSpecsResponse(
    @Json(name = "phoneDetails") val phoneDetails: PhoneDetails,
    @Json(name = "gsmLaunchDetails") val launchDetails: LaunchDetails,
    @Json(name = "gsmPlatformDetails") val gsmPlatformDetails: GsmPlatformDetails,
    @Json(name = "gsmMainCameraDetails") val gsmMainCameraDetails: GsmMainCameraDetails,
)

/**
 * Represents the basic details of a phone including its year, brand, and model name.
 *
 * @property year      The year the phone was released.
 * @property brand     The brand name of the phone (e.g., "Samsung", "Apple").
 * @property modelName The model name of the phone (e.g., "Galaxy S21").
 */
data class PhoneDetails(
    @Json(name = "yearValue") val year: String?,
    @Json(name = "brandValue") val brand: String?,
    @Json(name = "modelValue") val modelName: String?
)

/**
 * Represents launch-related details of a phone.
 *
 * @property launchDate The launch date of the phone.
 */
data class LaunchDetails(
    @Json(name = "launchAnnounced") val launchDate: String?
)

/**
 * Represents the platform details of a phone including its chipset, CPU, and GPU.
 *
 * @property chipset The chipset used in the phone (e.g., "Snapdragon 888").
 * @property cpu     The CPU model of the phone (e.g., "Octa-core").
 * @property gpu     The GPU used in the phone (e.g., "Adreno 650").
 */
data class GsmPlatformDetails(
    @Json(name = "platformChipset") val chipset: String?,
    @Json(name = "platformCpu") val cpu: String?,
    @Json(name = "platformGpu") val gpu: String?
)

/**
 * Represents the main camera details of a phone.
 *
 * @property mainCamFeatures   Features of the main camera (e.g., "Quad camera").
 * @property mainCameraTriple   Information about a triple-lens camera if available.
 * @property mainCameraQuad     Information about a quad-lens camera if available.
 * @property mainCameraVideo    Video capabilities of the main camera (e.g., "4K@30fps").
 */
data class GsmMainCameraDetails(
    @Json(name = "mainCameraFeatures") val mainCamFeatures: String,
    @Json(name = "mainCameraTriple") val mainCameraTriple: String? = null,
    @Json(name = "mainCameraQuad") val mainCameraQuad: String? = null,
    @Json(name = "mainCameraVideo") val mainCameraVideo: String
)

/**
 * Represents the selfie camera details of a phone.
 *
 * @property selfieCamFeatures   Features of the selfie camera (e.g., "Wide angle").
 * @property selfieCameraSpecs   Specifications of the selfie camera (e.g., "12 MP").
 * @property selfieCameraVideo   Video capabilities of the selfie camera (e.g., "1080p").
 */
data class GsmSelfieCameraDetails(
    @Json(name = "selfieCameraFeatures") val selfieCamFeatures: String?,
    @Json(name = "selfieCameraSingle") val selfieCameraSpecs: String?,
    @Json(name = "selfieCameraVideo") val selfieCameraVideo: String?
)
