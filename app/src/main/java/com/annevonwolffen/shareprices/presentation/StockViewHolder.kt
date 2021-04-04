package com.annevonwolffen.shareprices.presentation

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.annevonwolffen.shareprices.R
import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel
import com.annevonwolffen.shareprices.utils.ImageManager

class StockViewHolder(
    view: View,
    private val clickListener: StocksAdapter.FavoriteClickListener,
    private val onItemClickListener: StocksAdapter.OnItemClickListener,
    private val imageManager: ImageManager
) : RecyclerView.ViewHolder(view) {

    private val stockCardView: CardView = view.findViewById(R.id.stock_card_view)
    private val itemContainer: ConstraintLayout = view.findViewById(R.id.stock_item_container)
    private val tickerTextView: TextView = view.findViewById(R.id.ticker)
    private val nameTextView: TextView = view.findViewById(R.id.companyName)
    private val logoImageView: ImageView = view.findViewById(R.id.logo_image_view)
    private val currentPriceTextView: TextView = view.findViewById(R.id.currentPrice)
    private val priceChangeTextView: TextView = view.findViewById(R.id.priceChange)
    private val addToFavoriteBtn: ImageButton = view.findViewById(R.id.add_to_fav_btn)

    fun bind(stockModel: StockPresentationModel?) {
        if (stockModel == null) {
            return
        }
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
        itemContainer.setOnClickListener { onItemClickListener.onItemClicked(stockModel) }
        stockCardView.apply {
            stockModel.positionInList?.let { position ->
                if (position % 2 == 0) {
                    setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPaleGreyBlue))
                } else {
                    setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
                }
            }
        }
        stockModel.logo.apply {
            if (isNotEmpty()) {
                setLogoImage(this)
            } else {
                logoImageView.setImageResource(R.drawable.ic_monetization)
            }
        }
    }

    private fun setLogoImage(url: String) {
        imageManager.load(logoImageView, url)
    }
}