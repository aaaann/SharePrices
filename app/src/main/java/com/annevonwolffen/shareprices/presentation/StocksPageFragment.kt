package com.annevonwolffen.shareprices.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.annevonwolffen.shareprices.R
import com.annevonwolffen.shareprices.presentation.viewmodel.StocksViewModel
import com.annevonwolffen.shareprices.presentation.viewmodel.ViewModelProviderFactory

/**
 * @author Terekhova Anna
 */
class StocksPageFragment : BasePageFragment() {

    private val adapter: StocksAdapter = StocksAdapter()
    private lateinit var stocksViewModel: StocksViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_tab, container, false)
        initRecyclerView(view)
        createViewModel()
        return view
    }

    override fun onStart() {
        super.onStart()
        initObservers()
    }

    private fun initRecyclerView(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.stocks_recycler_view)
        recyclerView.adapter = adapter
    }

    private fun createViewModel() {
        stocksViewModel = activity?.let {
            ViewModelProvider(it, ViewModelProviderFactory(it.applicationContext))[StocksViewModel::class.java]
        } ?: throw Exception("Activity is null")
        stocksViewModel.loadData()
    }

    private fun initObservers() {
        stocksViewModel.stocks.observe(this, Observer { adapter.submitList(it) })
    }
}