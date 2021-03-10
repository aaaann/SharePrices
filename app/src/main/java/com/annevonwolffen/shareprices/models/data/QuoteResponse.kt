package com.annevonwolffen.shareprices.models.data

import com.google.gson.annotations.SerializedName

/**
 * @author Terekhova Anna
 */
class QuoteResponse(
    @SerializedName("c") val currentPrice: Double,
    @SerializedName("pc") val previousClosePrice: Double
)