package com.anfema.breadcrumbsample

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

class Layout2Adapter// Provide a suitable constructor (depends on the kind of dataset)
(private val mDataset: Array<String>) : RecyclerView.Adapter<Layout2Adapter.ViewHolder>()
{

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder(// each data item is just a string in this case
            var mTextView: TextView) : RecyclerView.ViewHolder(mTextView)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): Layout2Adapter.ViewHolder
    {
        // create a new view
        val v = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.text = mDataset[position]

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int
    {
        return mDataset.size
    }
}
