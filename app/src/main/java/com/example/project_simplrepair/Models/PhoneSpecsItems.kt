package com.example.project_simplrepair.Models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents the response from the API containing detailed phone specifications.
 *
 * @property phoneDetails     Contains the phone's details such as year, brand, and model.
 * @property launchDetails    Contains the launch date information for the phone.
 * @property gsmPlatformDetails Contains details about the phone's chipset, CPU, and GPU.
 * @property gsmMainCameraDetails Contains information about the main camera, including features, specs, and video capabilities.
 */
@JsonClass(generateAdapter = true)
data class PhoneSpecsItems(
    val id: Int,
    val name: String,
    val brand: BrandInfo?,
    val battery: BatteryInfo?,
    val platform: PlatformInfo?,
    val network: NetworkInfo?,
    val display: DisplayInfo?,
    val launch: LaunchInfo?,
    val memory: MemoryInfo?,
    val comms: CommsInfo?,
    val features: FeaturesInfo?,
    val colors: List<String>?,
    val models: List<String>?,
    val cameras: CameraInfo?,
)

@JsonClass(generateAdapter = true)
data class BrandInfo(
    val id: Int,
    val name: String?
)

@JsonClass(generateAdapter = true)
data class BatteryInfo(
    val type: String?,
    val charging: List<String>?
)

@JsonClass(generateAdapter = true)
data class PlatformInfo(
    val os: String?,
    val chipset: String?,
    val cpu: String?,
    val gpu: String?
)

@JsonClass(generateAdapter = true)
data class NetworkInfo(
    val technology: String?,
    val speed: String?,
    @Json(name = "2g") val twoG: String?,
    @Json(name = "3g") val threeG: String?,
    @Json(name = "4g") val fourG: String?,
    @Json(name = "5g") val fiveG: String?
)

@JsonClass(generateAdapter = true)
data class DisplayInfo(
    val type: String?,
    val size: String?,
    val resolution: String?,
    val protection: String?
)

@JsonClass(generateAdapter = true)
data class LaunchInfo(
    val released: String?,
    val status: String?,
)

@JsonClass(generateAdapter = true)
data class MemoryInfo(
    val cardSlot: String?,
    val internal: String?
)

@JsonClass(generateAdapter = true)
data class CommsInfo(
    val wlan: String?,
    val bluetooth: String?,
    val positioning: String?,
    val nfc: String?,
    val radio: String?,
    val usb: String?
)

@JsonClass(generateAdapter = true)
data class FeaturesInfo(
    val sensors: String?
)


// Camera info stuff
@JsonClass(generateAdapter = true)
data class CameraInfo(
    val mainCamera: SubCamera?,
    val selfieCamera: SubCamera?
)

@JsonClass(generateAdapter = true)
data class SubCamera(
    val type: String?,
    val cameraSpecs: List<String>?,
    val features: List<String>?,
    val video: List<String>?
)

@JsonClass(generateAdapter = true)
data class PhoneListResponseDTO (
    @Json(name="items")
    val items: List<PhoneSpecsItems>
)
