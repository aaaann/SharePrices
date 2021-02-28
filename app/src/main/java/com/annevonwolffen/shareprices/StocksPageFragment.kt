package com.annevonwolffen.shareprices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author Terekhova Anna
 */
class StocksPageFragment : BasePageFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_tab, container, false)
        initRecyclerView()
        return view
    }

    private fun initRecyclerView() {

    }
}