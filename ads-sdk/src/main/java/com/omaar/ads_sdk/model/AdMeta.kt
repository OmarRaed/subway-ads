package com.omaar.ads_sdk.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class AdMeta(
    @SerializedName("id") val id : String,
    @SerializedName("category") val category : String,
    @SerializedName("type") val type : String,
    @SerializedName("brandName") val brandName : String,
    @SerializedName("minAge") val minAge : Int,
    @SerializedName("maxAge") val maxAge : Int,
    @SerializedName("gender") val gender : Int,
    @SerializedName("adLink") val adLink : String
)