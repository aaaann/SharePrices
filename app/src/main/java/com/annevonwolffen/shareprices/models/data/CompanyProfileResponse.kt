package com.annevonwolffen.shareprices.models.data

import com.google.gson.annotations.SerializedName

/**
 * @author Terekhova Anna
 */
class CompanyProfileResponse(
    @SerializedName("name") val companyName: String,
    @SerializedName("ticker") val ticker: String,
    @SerializedName("logo") val logo: String
)