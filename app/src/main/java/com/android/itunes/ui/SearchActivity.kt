package com.android.itunes.ui

import android.os.Bundle
import com.android.itunes.R
import dagger.android.support.DaggerAppCompatActivity

class SearchActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)
    }
}
