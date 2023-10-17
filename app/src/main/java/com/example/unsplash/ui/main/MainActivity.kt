package com.example.unsplash.ui.main

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.unsplash.MySuggestionProvider
import com.example.unsplash.data.entities.UImages
import com.example.unsplash.databinding.ActivityMainBinding
import com.example.unsplash.sys.util.Constants.Companion.ALT_DESCRIPTION
import com.example.unsplash.sys.util.Constants.Companion.API_KEY
import com.example.unsplash.sys.util.Constants.Companion.BLUR_HASH
import com.example.unsplash.sys.util.Constants.Companion.COLOR
import com.example.unsplash.sys.util.Constants.Companion.CREATED_AT
import com.example.unsplash.sys.util.Constants.Companion.HEIGHT
import com.example.unsplash.sys.util.Constants.Companion.IMG_FULL
import com.example.unsplash.sys.util.Constants.Companion.LIKES
import com.example.unsplash.sys.util.Constants.Companion.PER_PAGE
import com.example.unsplash.sys.util.Constants.Companion.SLUG
import com.example.unsplash.sys.util.Constants.Companion.UPDATED_AT
import com.example.unsplash.sys.util.Constants.Companion.WIDTH
import com.example.unsplash.ui.deatils.DetailItemActivity
import com.example.unsplash.ui.main.adapter.UImageAdapter
import com.example.unsplash.ui.main.fragment.ImageDialogFragment
import com.example.unsplash.ui.main.interfaces.IAUImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), IAUImage {

    private lateinit var viewModel: ImagesViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var uImageAdapter: UImageAdapter
    private lateinit var dialogFragment: ImageDialogFragment




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this)[ImagesViewModel::class.java]
        viewModel.getImages("cats", API_KEY, PER_PAGE)

        setObservers()
        initConfig()

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                SearchRecentSuggestions(this, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
                    .saveRecentQuery(query, null)
            }
        }
    }


    private fun setObservers() {
        viewModel.onSuccess.observe(this) {
            binding.pbItems.visibility = View.GONE
            setUpRecycler(it)
        }
        viewModel.onError.observe(this) {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            Log.d("ERROR", it.message)
        }
    }

    private fun initConfig() {
        binding.sView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getImages(query, API_KEY, PER_PAGE)
                hideKeyBoard()
                return true

            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return
    }


    private fun setUpRecycler(data: List<UImages>) {
        binding.rvImages.layoutManager = GridLayoutManager(this, 2)
        binding.rvImages.setHasFixedSize(true)
        uImageAdapter = UImageAdapter(data, this, this)
        binding.rvImages.adapter = uImageAdapter
    }

    override fun viewImage(item: UImages) {
        dialogFragment = ImageDialogFragment.newInstance(item.urls!!.full)
        dialogFragment.show(supportFragmentManager, "Image Dialog")
        hideKeyBoard()
    }

    override fun details(item: UImages) {
        val i = Intent(this, DetailItemActivity::class.java)
        i.putExtra(SLUG, item.slug)
        i.putExtra(CREATED_AT, item.created_at)
        i.putExtra(UPDATED_AT, item.updated_at)
        i.putExtra(WIDTH,item.width)
        i.putExtra(HEIGHT,item.height)
        i.putExtra(COLOR,item.color)
        i.putExtra(BLUR_HASH, item.blur_hash)
        i.putExtra(ALT_DESCRIPTION, item.alt_description)
        i.putExtra(LIKES,item.likes)
        i.putExtra(IMG_FULL, item.urls!!.full)
        startActivity(i)
    }

    private fun hideKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val m =
                this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            m.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }


}
