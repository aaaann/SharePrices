package com.annevonwolffen.shareprices.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.annevonwolffen.shareprices.models.domain.CompanyInfoModel

@Dao
interface CompanyProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCompanyProfile(companyInfoModel: CompanyInfoModel)

    @Query("SELECT * FROM COMPANY_PROFILE WHERE ticker = :ticker")
    fun getCompanyProfileByTicker(ticker: String): CompanyInfoModel
}
