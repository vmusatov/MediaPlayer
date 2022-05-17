package com.example.mediaplayer.ui.search.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.mediaplayer.model.Track

class TrackItemDiffCallback(
    private val oldData: List<Track>,
    private val newData: List<Track>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldData.size

    override fun getNewListSize(): Int = newData.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].trackId == newData[newItemPosition].trackId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData[oldItemPosition]
        val newItem = newData[newItemPosition]

        return oldItem.artistName == newItem.artistName
                && oldItem.trackName == newItem.trackName
    }
}