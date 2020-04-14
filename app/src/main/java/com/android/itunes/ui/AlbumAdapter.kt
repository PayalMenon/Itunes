package com.android.itunes.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.itunes.R
import com.android.itunes.model.Album
import com.bumptech.glide.Glide

class AlbumAdapter (private val clickListener: (Album) -> Unit) : RecyclerView.Adapter<AlbumViewHolder>() {

    var albumList : List<Album> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.album_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bindView(albumList[position], clickListener)
    }

    fun setList(albums : List<Album>) {
        albumList = albums
        notifyDataSetChanged()
    }
}

class AlbumViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val albumTitle : TextView
    private val albumImage : ImageView
    private val songTitle: TextView
    private val artistName : TextView
    private val albumParent : CardView

    init {
        albumTitle = view.findViewById(R.id.album_title)
        albumImage = view.findViewById(R.id.album_image)
        songTitle = view.findViewById(R.id.song_title)
        artistName = view.findViewById(R.id.artist_name)
        albumParent = view.findViewById(R.id.album_parent)
    }

    fun bindView(album : Album, clickListener: (Album) -> Unit) {
        album.collectionName?.let {
            albumTitle.visibility = View.VISIBLE
            albumTitle.text = view.resources.getText(R.string.album).toString().plus(album.collectionName)
        } ?: run {
            albumTitle.visibility = View.GONE
        }
        album.trackName?.let {
            songTitle.visibility = View.VISIBLE
            songTitle.text = view.resources.getText(R.string.song).toString().plus(album.trackName)
        } ?: run {
            songTitle.visibility = View.GONE
        }
        album.collectionArtistName?.let {
            artistName.visibility = View.VISIBLE
            artistName.text = view.resources.getText(R.string.by).toString().plus(album.collectionArtistName)
        } ?: run {
            album.artistName?.let {
                artistName.visibility = View.VISIBLE
                artistName.text =
                    view.resources.getText(R.string.by).toString().plus(album.artistName)
            }
        } ?: run {
            artistName.visibility = View.GONE
        }

        album.listImage?.let {
            albumImage.visibility = View.VISIBLE
            Glide.with(view.context).load(album.listImage).into(albumImage)
        } ?: run {
            albumImage.visibility = View.GONE
        }

        albumParent.setOnClickListener {
            clickListener(album)
        }
    }
}