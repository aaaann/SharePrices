package com.annevonwolffen.shareprices.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.annevonwolffen.shareprices.App
import com.annevonwolffen.shareprices.R
import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel
import com.annevonwolffen.shareprices.presentation.viewmodel.StocksViewModel
import com.annevonwolffen.shareprices.utils.ImageManager
import javax.inject.Inject

/**
 * Фрагмент вкладки Stocks
 *
 * @author Terekhova Anna
 */
open class StocksPageFragment : Fragment(), StocksAdapter.OnItemClickListener, StocksAdapter.FavoriteClickListener {

    @Inject
    lateinit var imageManager: ImageManager

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProvider.Factory
    private lateinit var pagedAdapter: StocksPagedListAdapter
    protected lateinit var stocksViewModel: StocksViewModel
    private lateinit var shimmerLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView

    private lateinit var refreshLayout: SwipeRefreshLayout
    private var isRefreshing = false

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_tab, container, false)

        createViewModel()
        shimmerLayout = view.findViewById(R.id.shimmer_layout)
        initViews(view)
        return view
    }

    override fun onStart() {
        super.onStart()
        initObservers()
    }

    protected open fun createViewModel() {
        stocksViewModel = activity?.let {
            ViewModelProvider(it, viewModelProviderFactory)[StocksViewModel::class.java]
        } ?: throw Exception("Activity is null")
    }

    private fun initViews(view: View) {
        initRecyclerView(view)
        initRefreshLayout(view)
    }

    private fun initRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.stocks_recycler_view)
        pagedAdapter = StocksPagedListAdapter(this, this, imageManager)
        recyclerView.adapter = pagedAdapter
    }

    private fun initRefreshLayout(view: View) {
        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            stocksViewModel.invalidateDataSource()
            isRefreshing = true
            pagedAdapter.setPageLoading(false)
        }
        refreshLayout = view.findViewById(R.id.refresh_layout)
        refreshLayout.setOnRefreshListener(refreshListener)
    }

    private fun initObservers() {
        stocksViewModel.stocksPagedList.observe(this, Observer {
            pagedAdapter.submitList(it)
        })
        stocksViewModel.getIsInitialLoading().observe(
            this,
            Observer { isLoading ->
                shimmerLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
                recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
                refreshLayout.isRefreshing = isLoading && isRefreshing
            })
        stocksViewModel.getIsFurtherLoading().observe(this, Observer { isLoading ->
            pagedAdapter.setPageLoading(isLoading)
        })
    }

    override fun onItemClicked(stockModel: StockPresentationModel) {
        startActivity(context?.let { StockDetailsActivity.newIntent(it, stockModel) })
    }

    override fun onFavClick(ticker: String) {
        pagedAdapter.setPageLoading(false)
        stocksViewModel.onFavClick(ticker)
    }
}