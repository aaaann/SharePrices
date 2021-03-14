package com.annevonwolffen.shareprices.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.annevonwolffen.shareprices.models.domain.StockModel

@Dao
interface StocksDao {

    @Query("SELECT * FROM STOCKS WHERE ticker IN (:tickers)")
    fun selectStocksByTickers(tickers: List<String>): List<StockModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<StockModel>)
}