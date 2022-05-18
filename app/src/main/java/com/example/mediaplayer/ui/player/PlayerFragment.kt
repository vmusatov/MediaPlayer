package com.example.mediaplayer.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import com.example.mediaplayer.R
import com.example.mediaplayer.databinding.FragmentPlayerBinding
import com.example.mediaplayer.model.Track
import com.example.mediaplayer.util.showShortToast
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding: FragmentPlayerBinding get() = checkNotNull(_binding)

    private lateinit var player: Player

    private val disposeBag = CompositeDisposable()

    private val onPlayPauseToggleListener = { _: CompoundButton, isChecked: Boolean ->
        if (isChecked) {
            play()
        } else {
            pause()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = Player(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)

        val track: Track? = arguments?.getParcelable(TRACK_KEY)
        setupUi(track)

        savedInstanceState?.let { restoreState(it) }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    private fun restoreState(bundle: Bundle) {
        val position = bundle.getLong(POSITION_KEY)
        val duration = bundle.getInt(DURATION_KEY)

        binding.progressBar.max = duration
        binding.progressBar.progress = position.toInt()

        player.seekTo(position)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(POSITION_KEY, player.currentPosition)
        outState.putInt(DURATION_KEY, binding.progressBar.max)
    }

    private fun setupProgressObserver() {
        val dispose = player.progressObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val progress = it.toInt()
                binding.progressBar.progress = progress
                if (binding.progressBar.max <= progress) {
                    pause()
                }
            }
        disposeBag.add(dispose)
    }

    private fun setupUi(track: Track?) = with(binding) {

        if (track == null) {
            showShortToast(getString(R.string.nothing_found))
            playPause.isEnabled = false
            return@with
        }

        if (!player.setTrack(track)) {
            showShortToast(getString(R.string.track_cannot_be_played))
            playPause.isEnabled = false
        } else {
            binding.playPause.tag = false
            playPause.setOnCheckedChangeListener(onPlayPauseToggleListener)
        }

        trackName.text = track.trackName
        trackAuthor.text = track.artistName

        Picasso.get()
            .load(track.previewImage100)
            .placeholder(R.drawable.ic_music_album)
            .error(R.drawable.ic_music_album)
            .into(trackImage)
    }

    private fun play() {
        setupProgressObserver()
        player.play()
        binding.progressBar.max = player.currentDuration.toInt()
    }

    private fun pause() {
        binding.playPause.isChecked = false
        player.pause()
        disposeBag.clear()
    }

    companion object {
        const val TRACK_KEY = "TRACK_KEY"
        const val POSITION_KEY = "POSITION_KEY"
        const val DURATION_KEY = "DURATION_KEY"
    }
}