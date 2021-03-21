package com.annevonwolffen.shareprices.models.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "COMPANY_PROFILE")
class CompanyInfoModel(
    @PrimaryKey val ticker: String,
    val name: String? = "",
    val phone: String? = "",
    val logoUrl: String? = "",
    val webUrl: String? = "",
    val industry: String? = ""
)