package com.android.itunes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.android.itunes.R
import com.android.itunes.model.Album
import com.bumptech.glide.Glide
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.album_details_fragment.view.*
import javax.inject.Inject


class AlbumDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var itunesViewModel: ItunesViewModel

    companion object {
        private const val ALBUM = "album"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        itunesViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(
            ItunesViewModel::class.java
        )
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        val view = inflater.inflate(R.layout.album_details_fragment, container, false)
        arguments?.getParcelable<Album>(ALBUM)?.let { album ->
            updateDetailsScreen(view, album)
        }
        return view
    }

    private fun updateDetailsScreen(view: View, album: Album) {
        album.listImage?.let {
            /*The image from tne API had the max resolution of 100x100 which was stretching in detail view
            opening the url in the browser showed that there are multiple sizes,
            picked one to show non-stretching image in detail view*/

            val url = album.listImage.replace("100x100bb", "276x0w")
            Glide.with( view.detail_image.context).load(url).into(view.detail_image)
        }
        album.collectionName?.let {
            view.detail_album_title.visibility = View.VISIBLE
            view.detail_album_title.text = view.resources.getText(R.string.album).toString().plus(album.collectionName)
        } ?: run {
            view.detail_album_title.visibility = View.GONE
        }
        album.trackName?.let {
            view.detail_song_title.visibility = View.VISIBLE
            view.detail_song_title.text = view.resources.getText(R.string.song).toString().plus(album.trackName)
        } ?: run {
            view.detail_song_title.visibility = View.GONE
        }
        album.collectionArtistName?.let {
            view.detail_artist_name.visibility = View.VISIBLE
            view.detail_artist_name.text = view.resources.getText(R.string.by).toString().plus(album.collectionArtistName)
        } ?: run {
            album.artistName?.let {
                view.detail_artist_name.visibility = View.VISIBLE
                view.detail_artist_name.text =
                    view.resources.getText(R.string.by).toString().plus(album.artistName)
            }
        } ?: run {
            view.detail_artist_name.visibility = View.GONE
        }

        album.genre?.let {
            view.detail_song_genre.visibility = View.VISIBLE
            view.detail_song_genre.text = view.resources.getText(R.string.genre).toString().plus(album.genre)
        } ?: run {
            view.detail_song_genre.visibility = View.GONE
        }
    }
}