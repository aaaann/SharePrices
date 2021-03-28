package com.annevonwolffen.shareprices.presentation

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.annevonwolffen.shareprices.App
import com.annevonwolffen.shareprices.R
import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel
import com.annevonwolffen.shareprices.presentation.viewmodel.StockDetailsViewModel
import com.annevonwolffen.shareprices.utils.ImageManager
import com.facebook.shimmer.ShimmerFrameLayout
import javax.inject.Inject

class StockDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var imageManager: ImageManager

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProvider.Factory
    private lateinit var detailsViewModel: StockDetailsViewModel
    private var stockPresentationModel: StockPresentationModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        (application as App).appComponent.inject(this)

        setUpAppBar()
        setLightStatusBar()
        createViewModel()
    }

    override fun onStart() {
        super.onStart()
        if (detailsViewModel.isNewInstance) {
            stockPresentationModel?.ticker?.let { detailsViewModel.loadCompanyData(it) }
            detailsViewModel.isNewInstance = false
        }
        initObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        colorFavItemMenu(menu)
        return true
    }

    private fun colorFavItemMenu(menu: Menu) {
        val favMenuItem = menu.findItem(R.id.action_add_to_favorite)
        val drawable = DrawableCompat.wrap(favMenuItem.icon)
        DrawableCompat.setTint(
            drawable,
            if (stockPresentationModel?.isFavorite == true) {
                ContextCompat.getColor(this, R.color.colorYellow)
            } else {
                ContextCompat.getColor(this, R.color.colorBlackGrey)
            }
        )
        favMenuItem.icon = drawable
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add_to_favorite) {
            Log.d(TAG, "on menu item add_to_fav clicked")
            stockPresentationModel?.ticker?.let { detailsViewModel.clickFavorite(it) }
        } else if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpAppBar() {
        stockPresentationModel = intent.getParcelableExtra(TICKER)
        setupToolbar()
        setUpCollapsingToolbar()
        invalidateOptionsMenu()
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.navigationIcon?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(this, R.color.colorBlackGrey),
            PorterDuff.Mode.SRC_ATOP
        )
    }

    private fun setUpCollapsingToolbar() {
        val tickerHeaderTitle: TextView = findViewById(R.id.header_title_ticker)
        val nameHeaderTitle: TextView = findViewById(R.id.header_title_name)
        stockPresentationModel?.let {
            tickerHeaderTitle.text = it.ticker
            nameHeaderTitle.text = it.name
        }
    }

    private fun setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun createViewModel() {
        detailsViewModel =
            ViewModelProvider(this, viewModelProviderFactory)[StockDetailsViewModel::class.java]
    }

    private fun initObservers() {
        val companyInfo: Group = findViewById(R.id.company_info_group)
        val companyInfoLoadingError: TextView = findViewById(R.id.error_details_loading)
        detailsViewModel.companyInfo.observe(this, Observer { model ->
            if (model.phone.isNullOrEmpty() && model.webUrl.isNullOrEmpty()) {
                companyInfoLoadingError.visibility = View.VISIBLE
                companyInfo.visibility = View.GONE
            } else {
                companyInfoLoadingError.visibility = View.GONE
                companyInfo.visibility = View.VISIBLE
                val phoneTextView: TextView = findViewById(R.id.phone_text)
                val webUrlTextView: TextView = findViewById(R.id.web_url_text)
                val industryTextView: TextView = findViewById(R.id.industry_text)
                val logoImage: ImageView = findViewById(R.id.logo)
                phoneTextView.text = model.phone?.substringBefore(".")
                webUrlTextView.text = Html.fromHtml(String.format(getString(R.string.web_url_link, model.webUrl)))
                webUrlTextView.setOnClickListener { model.webUrl?.let { it1 -> openWebPage(it1) } }
                industryTextView.text = getString(R.string.industry_title, model.industry)
                if (model.logoUrl.isNullOrEmpty()) {
                    logoImage.visibility = View.GONE
                } else {
                    imageManager.load(logoImage, model.logoUrl)
                }
            }
        })

        detailsViewModel.isLoading.observe(this, Observer { isLoading ->
            val shimmerLayout: ShimmerFrameLayout = findViewById(R.id.shimmer_layout)
            shimmerLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                companyInfo.visibility = View.GONE
            }
        })

        detailsViewModel.isFavorite.observe(this, Observer {
            stockPresentationModel = stockPresentationModel?.copy(isFavorite = it)
            invalidateOptionsMenu()
        })
    }

    private fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.resolveActivity(packageManager)?.let {
            startActivity(intent)
        }
    }

    companion object {
        private const val TAG = "StockDetailsActivity"
        private const val TICKER = "TICKER"

        @JvmStatic
        fun newIntent(context: Context, stock: StockPresentationModel): Intent {
            val intent = Intent(context, StockDetailsActivity::class.java)
            intent.putExtra(TICKER, stock)
            return intent
        }
    }
}