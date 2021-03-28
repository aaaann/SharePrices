package com.annevonwolffen.shareprices.models.data

import com.google.gson.annotations.SerializedName

class SymbolSearchResponse(
    @SerializedName("result") val result: List<SymbolSearchItem>
) {
    class SymbolSearchItem(
        @SerializedName("symbol") val symbol: String
    )
}