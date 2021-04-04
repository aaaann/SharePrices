package com.annevonwolffen.shareprices.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.annevonwolffen.shareprices.R
import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel
import com.annevonwolffen.shareprices.utils.ImageManager

/**
 * @author Terekhova Anna
 */
class StocksAdapter(
    private val favoriteClickListener: FavoriteClickListener,
    private val onItemClickListener: OnItemClickListener,
    private val imageManager: ImageManager
) :
    ListAdapter<StockPresentationModel, StockViewHolder>(DiffUtilCallback()) {

    class DiffUtilCallback : DiffUtil.ItemCallback<StockPresentationModel>() {

        override fun areItemsTheSame(oldItem: StockPresentationModel, newItem: StockPresentationModel): Boolean =
            oldItem.ticker == newItem.ticker

        override fun areContentsTheSame(oldItem: StockPresentationModel, newItem: StockPresentationModel): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.stock_item_layout, parent, false)
        return StockViewHolder(itemView, favoriteClickListener, onItemClickListener, imageManager)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: MutableList<StockPresentationModel>?) {
        list?.mapIndexed { index, stockPresentationModel ->
            list[index] = stockPresentationModel.copyWithChangeListPosition(index)
        }
        super.submitList(list)
    }

    interface FavoriteClickListener {
        fun onFavClick(ticker: String)
    }

    interface OnItemClickListener {
        fun onItemClicked(stockModel: StockPresentationModel)
    }
}