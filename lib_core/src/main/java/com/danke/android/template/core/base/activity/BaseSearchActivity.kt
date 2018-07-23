package com.danke.android.template.core.base.activity

import android.app.SearchManager
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.danke.android.template.core.R
import com.danke.android.template.core.utils.ViewUtil


/**
 * @author danke
 * @date 2018/6/13
 */
abstract class BaseSearchActivity : BaseToolbarActivity() {

    private var searchView: SearchView? = null
    private var searchAutoComplete: SearchView.SearchAutoComplete? = null
    private var mQuery = ""

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        mQuery = intent.getStringExtra(SearchManager.QUERY) ?: ""

        if (searchView != null) {
            searchView!!.setQuery(mQuery, false)
            if (autoRequestFocus()) {
                searchView!!.requestFocus()
                searchView!!.requestFocusFromTouch()
            } else {
                searchView!!.clearFocus()
            }
        }

        title = ""

        overridePendingTransition(0, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_search_view, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView

            initSearchView()

            if (!TextUtils.isEmpty(mQuery)) {
                searchView!!.setQuery(mQuery, false)
                if (autoRequestFocus()) {
                    searchView!!.requestFocus()
                    searchView!!.requestFocusFromTouch()
                } else {
                    searchView!!.clearFocus()
                }
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.itemId == R.id.menu_search || super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            overridePendingTransition(0, 0)
        }
    }

    private fun initSearchView() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        //max width match toolbar
        searchView!!.maxWidth = toolbar.measuredWidth

        searchView!!.setIconifiedByDefault(true)
        searchView!!.isIconified = false
        searchView!!.isSubmitButtonEnabled = true
        searchView!!.queryHint = getSearchViewQueryHint()

        // search button
        val searchButton = searchView!!.findViewById<ImageView>(android.support.v7.appcompat.R.id.search_button)
        searchButton.setImageResource(R.drawable.ic_search_dark)

        // close button
        val searchCloseButton = searchView!!.findViewById<ImageView>(android.support.v7.appcompat.R.id.search_close_btn)
        searchCloseButton.setImageResource(R.drawable.ic_close_dark)

        // go button
        val searchGoButton = searchView!!.findViewById<ImageView>(android.support.v7.appcompat.R.id.search_go_btn)
        searchGoButton.setImageResource(R.drawable.ic_search_dark)
        searchGoButton.layoutParams = LinearLayout.LayoutParams(0, 0)

        // text color
        searchAutoComplete = searchView!!.findViewById(android.support.v7.appcompat.R.id.search_src_text)
        ViewUtil.of(searchAutoComplete!!).height(ViewGroup.LayoutParams.MATCH_PARENT).commit()
        searchAutoComplete!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.toolbarTitle).toFloat())
        searchAutoComplete!!.hint = getSearchViewQueryHint()
        searchAutoComplete!!.setTextColor(ContextCompat.getColor(this, R.color.titlePrimary))
        searchAutoComplete!!.setHintTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        searchAutoComplete!!.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                manualSearch(s)
                return true
            }

            override fun onQueryTextChange(s: String): Boolean {
                if (autoSearchEnable()) {
                    autoSearch(s)
                }
                return true
            }
        })

        searchView!!.setOnCloseListener {
            finish()
            false
        }

        if (autoRequestFocus()) {
            searchView!!.requestFocus()
            searchView!!.requestFocusFromTouch()
        } else {
            searchView!!.clearFocus()
        }
    }

    /**
     * 搜索框的hint
     *
     * @return
     */
    protected open fun getSearchViewQueryHint(): String = ""

    /**
     * 打开时获取焦点
     *
     * @return
     */
    protected open fun autoRequestFocus(): Boolean = true

    /**
     * 监听输入框自动搜索，默认打开
     *
     * @return
     */
    protected open fun autoSearchEnable(): Boolean = true

    /**
     * 点击搜索后的动作，包括搜索框的搜索和键盘的搜索
     *
     * @param query
     */
    protected abstract fun goSearch(query: String)

    private fun manualSearch(query: String) {
        searchView!!.clearFocus()
        // getToolbar().requestFocus();
        // getToolbar().requestFocusFromTouch();

        goSearch(query)
    }

    private fun autoSearch(query: String) {
        goSearch(query)
    }
}
