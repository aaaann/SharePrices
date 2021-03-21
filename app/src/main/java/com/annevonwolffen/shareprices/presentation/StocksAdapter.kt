package com.annevonwolffen.shareprices.presentation

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.annevonwolffen.shareprices.R
import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel

/**
 * @author Terekhova Anna
 */
class StocksAdapter(
    private val favoriteClickListener: FavoriteClickListener,
    private val onItemClickListener: OnItemClickListener
) :
    ListAdapter<StockPresentationModel, StocksAdapter.ViewHolder>(DiffUtilCallback()) {

    class DiffUtilCallback : DiffUtil.ItemCallback<StockPresentationModel>() {

        override fun areItemsTheSame(oldItem: StockPresentationModel, newItem: StockPresentationModel): Boolean =
            oldItem.ticker == newItem.ticker

        override fun areContentsTheSame(oldItem: StockPresentationModel, newItem: StockPresentationModel): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.stock_item_layout, parent, false)
        return ViewHolder(itemView, favoriteClickListener, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        view: View,
        private val clickListener: FavoriteClickListener,
        private val onItemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(view) {

        private val itemContainer: ConstraintLayout = view.findViewById(R.id.stock_item_container)
        private val tickerTextView: TextView = view.findViewById(R.id.ticker)
        private val nameTextView: TextView = view.findViewById(R.id.companyName)
        private val logoImageView: ImageView = view.findViewById(R.id.logo_image_view)
        private val currentPriceTextView: TextView = view.findViewById(R.id.currentPrice)
        private val priceChangeTextView: TextView = view.findViewById(R.id.priceChange)
        private val addToFavoriteBtn: ImageButton = view.findViewById(R.id.add_to_fav_btn)

        fun bind(stockModel: StockPresentationModel) {
            tickerTextView.text = stockModel.ticker
            nameTextView.text = stockModel.name
            currentPriceTextView.text = stockModel.currentPrice
            priceChangeTextView.apply {
                text = stockModel.priceChange
                if (stockModel.isPriceUp) {
                    setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorGreen)))
                } else {
                    setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorRed)))
                }
            }
            addToFavoriteBtn.apply {
                colorFilter = if (stockModel.isFavorite) {
                    PorterDuffColorFilter(
                        ContextCompat.getColor(context, R.color.colorYellow),
                        PorterDuff.Mode.SRC_ATOP
                    )
                } else {
                    PorterDuffColorFilter(
                        ContextCompat.getColor(context, R.color.colorGrey),
                        PorterDuff.Mode.SRC_ATOP
                    )
                }
                setOnClickListener { clickListener.onFavClick(stockModel.ticker) }
            }
            itemContainer.setOnClickListener { onItemClickListener.onItemClicked(stockModel.ticker) }
        }
    }

    interface FavoriteClickListener {
        fun onFavClick(ticker: String)
    }

    interface OnItemClickListener {
        fun onItemClicked(ticker: String)
    }
}