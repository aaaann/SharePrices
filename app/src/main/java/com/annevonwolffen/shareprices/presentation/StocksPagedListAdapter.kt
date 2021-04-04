package com.annevonwolffen.shareprices.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.annevonwolffen.shareprices.R
import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel
import com.annevonwolffen.shareprices.utils.ImageManager

class StocksPagedListAdapter(
    private val favoriteClickListener: StocksAdapter.FavoriteClickListener,
    private val onItemClickListener: StocksAdapter.OnItemClickListener,
    private val imageManager: ImageManager
) : PagedListAdapter<StockPresentationModel, RecyclerView.ViewHolder>(DiffUtilCallback()) {

    private var isPageLoading: Boolean = false

    class DiffUtilCallback : DiffUtil.ItemCallback<StockPresentationModel>() {

        override fun areItemsTheSame(oldItem: StockPresentationModel, newItem: StockPresentationModel): Boolean =
            oldItem.ticker == newItem.ticker

        override fun areContentsTheSame(oldItem: StockPresentationModel, newItem: StockPresentationModel): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            STOCK_VIEW_TYPE -> StockViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.stock_item_layout, parent, false),
                favoriteClickListener, onItemClickListener, imageManager
            )
            FOOTER_VIEW_TYPE -> FooterViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.stocks_load_footer, parent, false)
            )
            else -> StockViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.stock_item_layout, parent, false),
                favoriteClickListener, onItemClickListener, imageManager
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == STOCK_VIEW_TYPE)
            (holder as StockViewHolder).bind(getItem(position))
        else (holder as FooterViewHolder).bind(isPageLoading)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) STOCK_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val progressBar: ProgressBar = view.findViewById(R.id.progress_bar)

        fun bind(isLoading: Boolean) {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && isPageLoading
    }

    fun setPageLoading(isLoading: Boolean) {
        this.isPageLoading = isLoading
        notifyItemChanged(super.getItemCount())
    }

    private companion object {
        const val STOCK_VIEW_TYPE = 0
        const val FOOTER_VIEW_TYPE = 1
    }
}