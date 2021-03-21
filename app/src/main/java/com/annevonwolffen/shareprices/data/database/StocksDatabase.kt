package com.annevonwolffen.shareprices.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.annevonwolffen.shareprices.models.domain.CompanyInfoModel
import com.annevonwolffen.shareprices.models.domain.StockModel

@Database(entities = [StockModel::class, CompanyInfoModel::class], version = 1, exportSchema = false)
abstract class StocksDatabase : RoomDatabase() {

    abstract fun stocksDao(): StocksDao
    abstract fun companyProfileDao(): CompanyProfileDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: StocksDatabase? = null

        fun getDatabase(context: Context): StocksDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StocksDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

const val DB_NAME = "Stocks.db"