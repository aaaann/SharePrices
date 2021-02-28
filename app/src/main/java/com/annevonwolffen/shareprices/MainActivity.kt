package com.annevonwolffen.shareprices

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setLightStatusBar()
        initViewPager()
    }

    private fun setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun initViewPager() {
        val pagerAdapter = PagerAdapter(supportFragmentManager, lifecycle)
        val viewPager: ViewPager2 = findViewById(R.id.pager)
        viewPager.adapter = pagerAdapter
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(PAGE_TITLES[position])
        }.attach()
    }

    private companion object {
        const val TAG = "MainActivity"
        val PAGE_TITLES = listOf(
            R.string.stocks_page_title,
            R.string.favorite_page_title
        )
    }
}