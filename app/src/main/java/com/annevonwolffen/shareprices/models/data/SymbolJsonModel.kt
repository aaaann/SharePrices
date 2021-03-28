package com.annevonwolffen.shareprices.models.data

import com.google.gson.annotations.SerializedName

/**
 * @author Terekhova Anna
 */
class SymbolJsonModel(
    @SerializedName("Symbol") val symbol: String
)