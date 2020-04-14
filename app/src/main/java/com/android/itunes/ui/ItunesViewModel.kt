package com.android.itunes.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.itunes.R
import com.android.itunes.api.ApiRepository
import com.android.itunes.api.NetworkResult
import com.android.itunes.model.Album
import com.android.itunes.model.Albums
import com.android.itunes.util.Event
import kotlinx.coroutines.launch
import javax.inject.Inject

class ItunesViewModel @Inject constructor(
    application: Application,
    private val apiRepository: ApiRepository
) : AndroidViewModel(application) {

    //LiveData
    private val _showLoading = MutableLiveData<Event<Unit>>()
    val showLoading: LiveData<Event<Unit>>
        get() = _showLoading

    private val _hideLoading = MutableLiveData<Event<Unit>>()
    val hideLoading: LiveData<Event<Unit>>
        get() = _hideLoading

    private val _hideText = MutableLiveData<Event<Unit>>()
    val hideText: LiveData<Event<Unit>>
        get() = _hideText

    private val _showText = MutableLiveData<Event<String>>()
    val showText: LiveData<Event<String>>
        get() = _showText

    private val _showErrorText = MutableLiveData<Event<String>>()
    val showErrorText: LiveData<Event<String>>
        get() = _showErrorText

    private val _showAlbumList = MutableLiveData<Event<Unit>>()
    val showAlbumList: LiveData<Event<Unit>>
        get() = _showAlbumList

    private val _showAlbumDetails = MutableLiveData<Event<Album>>()
    val showAlbumDetails: LiveData<Event<Album>>
        get() = _showAlbumDetails

    private var albumList : List<Album>? = null

    fun onSearchSelected(query: String) {
        viewModelScope.launch {
            _hideText.value = Event(Unit)
            _showLoading.value = Event(Unit)
            try {
                when(val response : NetworkResult<Albums> = apiRepository.fetchAlbums(query)) {
                    is NetworkResult.Success -> {
                        _hideLoading.value = Event(Unit)
                        if (response.body.results.size > 0) {
                            albumList = response.body.results
                            _showAlbumList.value = Event(Unit)
                        } else {
                            _showText.value = Event(getApplication<Application>().resources.getString(
                                R.string.unknown_artist,query))
                        }
                    }
                    is NetworkResult.Failure -> {
                        _hideLoading.value = Event(Unit)
                        _showErrorText.value = Event(query)
                    }
                }
            } catch (exception : Exception) {
                _hideText.value = Event(Unit)
                _hideLoading.value = Event(Unit)
                _showErrorText.value = Event(query)
            }
        }
    }

    fun onRetryButtonClicked(query : String) {
        onSearchSelected(query)
    }

    fun getAlbumList() : List<Album>? {
        return albumList
    }

    fun onAlbumClicked(album: Album) {
        _showAlbumDetails.value = Event(album)
    }
}
