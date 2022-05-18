package com.example.mediaplayer.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mediaplayer.data.repository.TracksRepository
import com.example.mediaplayer.model.Track
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import javax.inject.Inject

class SearchViewModel(
    private val tracksRepository: TracksRepository
) : ViewModel() {

    private val MIN_REQEUST_LENGTH = 5

    val uiState: LiveData<SearchScreenUiState> get() = _uiState
    private val _uiState = MutableLiveData<SearchScreenUiState>()

    private val disposeBag: CompositeDisposable = CompositeDisposable()

    fun searchTracks(query: String) {
        val request = parseRequest(query)

        if (!isValidRequest(request)) {
            updateUiState {
                it.copy(
                    uiState = UiState.LOAD_ERROR,
                    searchErrorType = SearchErrorType.INVALID_REQUEST
                )
            }
            return
        }

        val dispose = tracksRepository.searchTracks(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { handleSuccessResult(it) },
                { handleErrorResult(it) }
            )

        disposeBag.add(dispose)
    }

    private fun handleSuccessResult(result: List<Track>) {
        if (result.isEmpty()) {
            updateUiState {
                it.copy(
                    uiState = UiState.LOAD_ERROR,
                    searchErrorType = SearchErrorType.NO_RESULTS
                )
            }
        } else {
            updateUiState {
                it.copy(
                    uiState = UiState.READY_TO_SHOW,
                    searchResult = result
                )
            }
        }
    }

    private fun handleErrorResult(exception: Throwable?) {
        when (exception) {
            is IOException -> updateUiState {
                it.copy(
                    uiState = UiState.LOAD_ERROR,
                    searchErrorType = SearchErrorType.NETWORK_ERROR
                )
            }
            else -> updateUiState {
                it.copy(
                    uiState = UiState.LOAD_ERROR,
                    searchErrorType = SearchErrorType.UNDEFINED
                )
            }
        }
    }

    private fun parseRequest(request: String): String {
        return request.trim()
    }

    private fun isValidRequest(request: String): Boolean {
        return request.length > MIN_REQEUST_LENGTH
    }

    private fun updateUiState(updater: (SearchScreenUiState) -> SearchScreenUiState) {
        _uiState.postValue(
            _uiState.value?.let { updater(it) }
                ?: updater(SearchScreenUiState())
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposeBag.clear()
    }

    class Factory @Inject constructor(
        private val tracksRepository: TracksRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SearchViewModel(
                tracksRepository
            ) as T
        }
    }
}

data class SearchScreenUiState(
    val uiState: UiState = UiState.NOTHING,
    val searchResult: List<Track> = emptyList(),
    val searchErrorType: SearchErrorType? = null
)

enum class UiState {
    NOTHING,
    READY_TO_SHOW,
    LOAD_ERROR
}

enum class SearchErrorType {
    NO_RESULTS,
    INVALID_REQUEST,
    NETWORK_ERROR,
    UNDEFINED
}