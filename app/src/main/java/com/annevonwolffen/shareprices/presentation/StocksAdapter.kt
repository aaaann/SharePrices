package com.annevonwolffen.shareprices.presentation

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.annevonwolffen.shareprices.R
import com.annevonwolffen.shareprices.models.domain.StockModel
import kotlin.math.abs

/**
 * @author Terekhova Anna
 */
class StocksAdapter() : ListAdapter<StockModel, StocksAdapter.ViewHolder>(DiffUtilCallback()) {

    class DiffUtilCallback : DiffUtil.ItemCallback<StockModel>() {

        override fun areItemsTheSame(oldItem: StockModel, newItem: StockModel): Boolean =
            oldItem.ticker == newItem.ticker

        override fun areContentsTheSame(oldItem: StockModel, newItem: StockModel): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.stock_item_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tickerTextView: TextView = view.findViewById(R.id.ticker)
        private val nameTextView: TextView = view.findViewById(R.id.companyName)
        private val logoImageView: ImageView = view.findViewById(R.id.logo_image_view)
        private val currentPriceTextView: TextView = view.findViewById(R.id.currentPrice)
        private val priceChangeTextView: TextView = view.findViewById(R.id.priceChange)

        fun bind(stockModel: StockModel) {
            tickerTextView.text = stockModel.ticker
            nameTextView.text = stockModel.name
            currentPriceTextView.text =
                currentPriceTextView.context.getString(R.string.current_price_pattern, stockModel.currentPrice)
            val priceChange = calculatePriceChange(stockModel.currentPrice, stockModel.previousClosePrice)
            priceChangeTextView.apply {
                if (priceChange > 0) {
                    text = priceChangeTextView.context.getString(
                        R.string.price_change_up_pattern,
                        abs(priceChange),
                        priceChangeInPercent(priceChange, stockModel.previousClosePrice)
                    )
                    setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorGreen)))
                } else {
                    text = priceChangeTextView.context.getString(
                        R.string.price_change_down_pattern,
                        abs(priceChange),
                        priceChangeInPercent(priceChange, stockModel.previousClosePrice)
                    )
                    setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorRed)))
                }
            }
        }

        private fun calculatePriceChange(currentPrice: Double, previousPrice: Double): Double {
            return currentPrice - previousPrice
        }

        private fun priceChangeInPercent(difference: Double, previousPrice: Double): Double {
            return (abs(difference) / previousPrice) * 100
        }
    }
}