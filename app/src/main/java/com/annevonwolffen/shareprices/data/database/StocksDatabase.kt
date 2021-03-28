package com.annevonwolffen.shareprices.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.annevonwolffen.shareprices.models.domain.CompanyInfoModel
import com.annevonwolffen.shareprices.models.domain.StockModel

@Database(entities = [StockModel::class, CompanyInfoModel::class], version = 1, exportSchema = false)
abstract class StocksDatabase : RoomDatabase() {

    abstract fun stocksDao(): StocksDao
    abstract fun companyProfileDao(): CompanyProfileDao
}

const val DB_NAME = "Stocks.db"