package com.android.itunes.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.android.itunes.R
import com.android.itunes.util.EventObserver
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.search_fragment.view.*
import javax.inject.Inject

class SearchFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var itunesViewModel: ItunesViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        itunesViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(ItunesViewModel::class.java)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        val view = inflater.inflate(R.layout.search_fragment, container, false)
        this.setHasOptionsMenu(true)
        setListener(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchMenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchMenuItem.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    itunesViewModel.onSearchSelected(query)
                }
                return true
            }
        })
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    fun setListener(view: View) {
        itunesViewModel.showAlbumList.observe(requireActivity(), EventObserver {
            val destination = SearchFragmentDirections.navigateToListFragment()
            navController.navigate(destination)
        })

        itunesViewModel.showLoading.observe(requireActivity(), EventObserver {
            view.loading.visibility = View.VISIBLE
            view.loading_text.visibility = View.VISIBLE
        })

        itunesViewModel.hideLoading.observe(requireActivity(), EventObserver {
            view.loading.visibility = View.GONE
            view.loading_text.visibility = View.GONE
        })

        itunesViewModel.hideText.observe(requireActivity(), EventObserver {
            view.info_text.visibility = View.GONE
        })

        itunesViewModel.showErrorText.observe(requireActivity(), EventObserver { query ->
            view.error_text.visibility = View.VISIBLE
            view.retry_button.visibility = View.VISIBLE
            view.retry_button.setOnClickListener {
                itunesViewModel.onRetryButtonClicked(query)
            }
        })

        itunesViewModel.showText.observe(requireActivity(), EventObserver { updateText ->
            view.info_text.visibility = View.VISIBLE
            view.info_text.text = updateText
        })
    }
}