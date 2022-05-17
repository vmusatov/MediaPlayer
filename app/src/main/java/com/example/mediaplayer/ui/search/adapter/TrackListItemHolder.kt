package com.example.mediaplayer.ui.search.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.R
import com.example.mediaplayer.databinding.ItemTrackListBinding
import com.example.mediaplayer.model.Track
import com.squareup.picasso.Picasso

class TrackListItemHolder(
    private val binding: ItemTrackListBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Track, onClick: OnClickMediaItemListItem) = with(binding) {

        root.setOnClickListener { onClick(item) }

        trackName.text = item.trackName
        trackAuthor.text = item.artistName

        Picasso.get()
            .load(item.previewImage100)
            .placeholder(R.drawable.ic_music_album)
            .into(trackImage)
    }
}