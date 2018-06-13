package com.danke.android.template.launcher.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.danke.android.template.launcher.R
import com.danke.android.template.launcher.model.FastItem

/**
 * @author danke
 * @date 2018/6/13
 */
class FastAdapter(private val fastItems: List<FastItem>,
                  private val onItemClickListener: OnItemClickListener?) : RecyclerView.Adapter<FastAdapter.FastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FastViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FastViewHolder(inflater.inflate(R.layout.module_launcher_item_fast_launcher, parent, false))
    }

    override fun onBindViewHolder(holder: FastViewHolder, position: Int) {
        val fastItem = fastItems[position]

        holder.fastItem.text = fastItem.name
        holder.fastItem.setOnClickListener {
            onItemClickListener?.onItemClick(fastItem)
        }
    }

    override fun getItemCount(): Int = fastItems.size

    class FastViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fastItem: TextView = itemView.findViewById(R.id.fastItem)
    }

    interface OnItemClickListener {
        fun onItemClick(fastItem: FastItem)
    }
}