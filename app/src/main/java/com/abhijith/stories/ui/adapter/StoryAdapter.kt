package com.abhijith.stories.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abhijith.stories.R
import com.abhijith.stories.StoryModel

class StoryAdapter(val context: Context,val sm: StoryModel) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv:TextView = view.findViewById(R.id.tvTimeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_story_view_holder,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv.text = sm.getAllStory()[position].id.toString()
    }

    override fun getItemCount(): Int {
        return sm.getAllStory().size
    }
}