package com.annevonwolffen.shareprices.presentation

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.annevonwolffen.shareprices.App
import com.annevonwolffen.shareprices.R
import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel
import com.annevonwolffen.shareprices.presentation.viewmodel.SearchViewModel
import com.annevonwolffen.shareprices.utils.ImageManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    @Inject
    lateinit var imageManager: ImageManager

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProvider.Factory
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StocksAdapter
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var searchView: SearchView
    private lateinit var searchEditText: SearchView.SearchAutoComplete
    private lateinit var recentSearchLayout: LinearLayout
    private lateinit var recentSearchChipGroup: ChipGroup
    private lateinit var recentSearchEmptyTextView: TextView
    private lateinit var searchResultLayout: LinearLayout
    private lateinit var searchResultEmptyTextView: TextView

    private val onQueryTextListener: SearchView.OnQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.takeIf { it.isNotEmpty() }?.let { searchViewModel.runSearch(it) }
            hideDeviceKeyboard(searchView)
            recentSearchLayout.visibility = View.GONE
            searchResultLayout.visibility = View.VISIBLE
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if (newText.isNullOrEmpty()) {
                searchViewModel.getRecentSearch()
                searchResultLayout.visibility = View.GONE
                recentSearchLayout.visibility = View.VISIBLE
            }
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        (application as App).appComponent.inject(this)

        createViewModel()
        initViews()
        setLightStatusBar()
        initSearchView()
        initRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        // если вьюмодель взята не из стора, т. е. не при смене конфигурации,
        // запустить загрузку истории поиска
        if (searchViewModel.isNewInstance) {
            searchViewModel.getRecentSearch()
            searchResultLayout.visibility = View.GONE
            recentSearchLayout.visibility = View.VISIBLE
            searchViewModel.isNewInstance = false
        }
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        searchView.clearFocus()
    }

    private fun createViewModel() {
        searchViewModel =
            ViewModelProvider(this, viewModelProviderFactory)[SearchViewModel::class.java]
    }

    private fun setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun initViews() {
        searchResultLayout = findViewById(R.id.search_result_layout)
        recentSearchLayout = findViewById(R.id.recent_search_layout)
    }

    private fun initSearchView() {
        searchView = findViewById(R.id.search_view)
        searchView.setOnQueryTextListener(onQueryTextListener)
        searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.search_result_stocks)
        adapter = StocksAdapter(searchViewModel, searchViewModel, imageManager)
        recyclerView.adapter = adapter
    }

    private fun initObservers() {
        searchResultEmptyTextView = findViewById(R.id.search_result_empty_text_view)
        searchViewModel.stocks.observe(this, Observer {
            setSearchResults(it)
        })
        val shimmerLayout: LinearLayout = findViewById(R.id.shimmer_layout)
        searchViewModel.isLoading.observe(
            this,
            Observer { isLoading ->
                shimmerLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
                if (isLoading) {
                    searchResultEmptyTextView.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                }
            })
        recentSearchChipGroup = findViewById(R.id.recent_search_chips)
        recentSearchEmptyTextView = findViewById(R.id.recent_search_empty_text_view)
        searchViewModel.recentSearchedTickers.observe(this, Observer {
            setChips(it)
        })

        searchViewModel.itemClicked.observe(
            this,
            Observer { startActivity(StockDetailsActivity.newIntent(this, it)) }
        )
    }

    private fun setSearchResults(searchResults: List<StockPresentationModel>) {
        adapter.submitList(searchResults.toMutableList())
        if (searchResults.isEmpty()) {
            recyclerView.visibility = View.GONE
            searchResultEmptyTextView.visibility = View.VISIBLE
            return
        }
        recyclerView.visibility = View.VISIBLE
        searchResultEmptyTextView.visibility = View.GONE
    }

    private fun setChips(tickers: List<String>) {
        recentSearchChipGroup.removeAllViews()
        if (tickers.isEmpty()) {
            recentSearchEmptyTextView.visibility = View.VISIBLE
            return
        }
        recentSearchEmptyTextView.visibility = View.GONE
        recentSearchChipGroup.visibility = View.VISIBLE
        for (ticker in tickers) {
            val chip = Chip(this)
            chip.text = ticker
            chip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPaleGreyBlue))
            chip.setOnClickListener {
                searchEditText.setText(ticker)
                onQueryTextListener.onQueryTextSubmit(ticker)
            }
            recentSearchChipGroup.addView(chip)
        }
    }

    private fun hideDeviceKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        @JvmStatic
        fun newIntent(context: Context): Intent = Intent(context, SearchActivity::class.java)

        private const val TAG = "SearchActivity"
    }
}