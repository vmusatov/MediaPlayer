package com.example.mediaplayer.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediaplayer.R
import com.example.mediaplayer.appComponent
import com.example.mediaplayer.databinding.FragmentSearchBinding
import com.example.mediaplayer.ui.search.adapter.TrackListAdapter
import javax.inject.Inject

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding get() = checkNotNull(_binding)

    @Inject
    lateinit var factory: SearchViewModel.Factory
    private val viewModel: SearchViewModel by viewModels { factory }

    private var trackListAdapter: TrackListAdapter? = null

    private val searchTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val text = s.toString().trim()
            if (text.isEmpty()) {
                binding.errorText.visibility = View.GONE
                binding.trackList.visibility = View.GONE
            } else {
                viewModel.searchTracks(text)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trackListAdapter = TrackListAdapter {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        setupObservers()
        setupUi()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        trackListAdapter = null
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it.uiState) {

                UiState.READY_TO_SHOW -> {
                    trackListAdapter?.update(it.searchResult)
                    binding.errorText.visibility = View.GONE
                    binding.trackList.visibility = View.VISIBLE
                }

                UiState.LOAD_ERROR -> handleError(it.searchErrorType ?: SearchErrorType.UNDEFINED)

                else -> { }
            }
        }
    }

    private fun handleError(searchErrorType: SearchErrorType) = with(binding) {
        trackList.visibility = View.GONE

        errorText.text = when (searchErrorType) {
            SearchErrorType.INVALID_REQUEST -> getString(R.string.request_min_length_notice)
            SearchErrorType.NETWORK_ERROR -> getString(R.string.network_error)
            else -> getString(R.string.undefined_error)
        }
        errorText.visibility = View.VISIBLE

    }

    private fun setupUi() = with(binding) {
        trackList.adapter = trackListAdapter
        trackList.layoutManager = LinearLayoutManager(requireContext())
        trackSearchText.addTextChangedListener(searchTextWatcher)
    }
}