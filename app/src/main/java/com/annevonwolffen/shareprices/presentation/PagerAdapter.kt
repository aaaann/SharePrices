package com.annevonwolffen.shareprices.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @author Terekhova Anna
 */
class PagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount() =
        PAGES_COUNT

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StocksPageFragment()
            else -> FavoritePageFragment()
        }
    }

    private companion object {
        const val PAGES_COUNT = 2
    }
}