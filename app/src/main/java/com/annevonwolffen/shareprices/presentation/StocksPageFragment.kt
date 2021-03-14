package com.annevonwolffen.shareprices.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.annevonwolffen.shareprices.R
import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel
import com.annevonwolffen.shareprices.presentation.viewmodel.StocksViewModel
import com.annevonwolffen.shareprices.presentation.viewmodel.ViewModelProviderFactory

/**
 * Фрагмент вкладки Stocks
 *
 * @author Terekhova Anna
 */
open class StocksPageFragment : BasePageFragment() {

    protected lateinit var adapter: StocksAdapter
    private lateinit var stocksViewModel: StocksViewModel
    private lateinit var shimmerLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout

    private var isRefreshing = false

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

    private fun createViewModel() {
        stocksViewModel = activity?.let {
            ViewModelProvider(it, ViewModelProviderFactory(it.applicationContext))[StocksViewModel::class.java]
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
        adapter = StocksAdapter(stocksViewModel)
        recyclerView.adapter = adapter
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
        adapter.submitList(stocks)
    }
}