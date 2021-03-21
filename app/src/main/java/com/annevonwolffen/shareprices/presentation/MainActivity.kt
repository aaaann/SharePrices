package com.annevonwolffen.shareprices.presentation

import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.SearchAutoComplete
import androidx.viewpager2.widget.ViewPager2
import com.annevonwolffen.shareprices.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setLightStatusBar()
        initViewPager()
        initSearchView()
    }

    private fun setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun initViewPager() {
        val pagerAdapter =
            PagerAdapter(
                supportFragmentManager,
                lifecycle
            )
        val viewPager: ViewPager2 = findViewById(R.id.pager)
        viewPager.adapter = pagerAdapter
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(PAGE_TITLES[position])
        }.attach()
    }

    private fun initSearchView() {
        val searchView: SearchView = findViewById(R.id.search_view)
        val searchEditText: SearchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        searchEditText.apply {
            inputType = InputType.TYPE_NULL
            isFocusable = false
            isCursorVisible = false
        }

        searchEditText.setOnClickListener {
            startActivity(SearchActivity.newIntent(this))
        }
    }

    private companion object {
        const val TAG = "MainActivity"
        val PAGE_TITLES = listOf(
            R.string.stocks_page_title,
            R.string.favorite_page_title
        )
    }
}