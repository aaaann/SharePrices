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
import javax.inject.Inject

/**
 * Фрагмент вкладки Stocks
 *
 * @author Terekhova Anna
 */
open class StocksPageFragment : Fragment(), StocksAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProvider.Factory
    protected lateinit var adapter: StocksAdapter
    private lateinit var stocksViewModel: StocksViewModel
    private lateinit var shimmerLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var scrollObserver: RecyclerView.AdapterDataObserver

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

    override fun onDestroy() {
        try {
            adapter.unregisterAdapterDataObserver(scrollObserver)
        } catch (ex: IllegalStateException) {
        }
        super.onDestroy()
    }

    private fun createViewModel() {
        stocksViewModel = activity?.let {
            ViewModelProvider(it, viewModelProviderFactory)[StocksViewModel::class.java]
        } ?: throw Exception("Activity is null")
        // если вьюмодель взята не из стора, т. е. не при смене конфигурации,
        // запустить загрузку данных
        if (stocksViewModel.isNewInstance) {
            stocksViewModel.loadData()
            stocksViewModel.isNewInstance = false
        }
    }

    private fun initViews(view: View) {
        initRecyclerView(view)
        initRefreshLayout(view)
    }

    private fun initRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.stocks_recycler_view)
        adapter = StocksAdapter(stocksViewModel, this)
        recyclerView.adapter = adapter
        scrollObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                recyclerView.scrollToPosition(0)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                recyclerView.scrollToPosition(0)
            }
        }
        adapter.registerAdapterDataObserver(scrollObserver)
    }

    private fun initRefreshLayout(view: View) {
        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            stocksViewModel.loadData()
            isRefreshing = true
        }
        refreshLayout = view.findViewById(R.id.refresh_layout)
        refreshLayout.setOnRefreshListener(refreshListener)
    }

    private fun initObservers() {
        stocksViewModel.stocks.observe(this, Observer { updateStocksList(it) })
        stocksViewModel.isLoading.observe(
            this,
            Observer { isLoading ->
                shimmerLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
                recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
                refreshLayout.isRefreshing = isLoading && isRefreshing
            })
    }

    protected open fun updateStocksList(stocks: List<StockPresentationModel>) {
        adapter.submitList(stocks.toMutableList())
    }

    override fun onItemClicked(stockModel: StockPresentationModel) {
        startActivity(context?.let { StockDetailsActivity.newIntent(it, stockModel) })
    }
}