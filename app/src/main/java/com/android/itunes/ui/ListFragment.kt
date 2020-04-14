package com.android.itunes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.android.itunes.R
import com.android.itunes.model.Album
import com.android.itunes.util.EventObserver
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.list_fragment.view.*
import javax.inject.Inject

class ListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var itunesViewModel: ItunesViewModel
    private lateinit var navController : NavController
    private lateinit var adapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        itunesViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(ItunesViewModel::class.java)
        val view = inflater.inflate(R.layout.list_fragment, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        adapter = AlbumAdapter { album: Album -> onAlbumClicked(album) }
        view.album_list.adapter = adapter
        itunesViewModel.getAlbumList()?.let { list ->
            adapter.setList(list)
        }
        itunesViewModel.showAlbumDetails.observe(requireActivity(), EventObserver { album ->
            val destination = ListFragmentDirections.navigateToDetailsFragment(album)
            navController.navigate(destination)
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
    }

    private fun onAlbumClicked(album: Album) {
        itunesViewModel.onAlbumClicked(album)
    }
}