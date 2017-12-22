package com.anfema.breadcrumbsample

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

class Layout2Adapter(private val mDataset: Array<String>) : RecyclerView.Adapter<Layout2Adapter.ViewHolder>()
{

    class ViewHolder(var mTextView: TextView) : RecyclerView.ViewHolder(mTextView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Layout2Adapter.ViewHolder
    {
        val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.mTextView.text = mDataset[position]
    }

    override fun getItemCount() = mDataset.size
}
