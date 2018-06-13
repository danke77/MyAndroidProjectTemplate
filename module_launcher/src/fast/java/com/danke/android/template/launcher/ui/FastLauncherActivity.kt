package com.danke.android.template.launcher.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.danke.android.template.launcher.R
import com.danke.android.template.launcher.adapter.FastAdapter
import com.danke.android.template.launcher.model.FastItem
import kotlinx.android.synthetic.main.module_launcher_activity_fast_launcher.*

/**
 * @author danke
 * @date 2018/6/13
 */
class FastLauncherActivity : AppCompatActivity() {

    companion object {
        private val FAST_ITEM_LIST = arrayListOf(
                FastItem("module_a", "router_module_a"),
                FastItem("module_b", "router_module_b"),
                FastItem("module_c", "router_module_c")
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.module_launcher_activity_fast_launcher)
        init()
    }

    private fun init() {
        title = "FastLauncher"

        val adapter = FastAdapter(FAST_ITEM_LIST, object : FastAdapter.OnItemClickListener {
            override fun onItemClick(fastItem: FastItem) {
                Toast.makeText(this@FastLauncherActivity, fastItem.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        fastItemList.layoutManager = LinearLayoutManager(this@FastLauncherActivity)
        fastItemList.adapter = adapter
    }
}