package com.example.mediaplayer.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.databinding.ItemTrackListBinding
import com.example.mediaplayer.model.Track

typealias OnClickMediaItemListItem = (Track) -> Unit

class TrackListAdapter(
    private val onItemClick: OnClickMediaItemListItem
) : RecyclerView.Adapter<TrackListItemHolder>() {

    private var data: List<Track> = emptyList()

    fun update(data: List<Track>) {
        val diffCallback = TrackItemDiffCallback(this.data, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback, false)

        this.data = data

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTrackListBinding.inflate(inflater, parent, false)
        return TrackListItemHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackListItemHolder, position: Int) {
        holder.bind(data[position], onItemClick)
    }

    override fun getItemCount(): Int = data.size

}


