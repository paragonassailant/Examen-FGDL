package com.example.unsplash.ui.main

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unsplash.MySuggestionProvider
import com.example.unsplash.data.entities.UImages
import com.example.unsplash.databinding.ActivityMainBinding
import com.example.unsplash.sys.util.Constants.Companion.ALT_DESCRIPTION
import com.example.unsplash.sys.util.Constants.Companion.BLUR_HASH
import com.example.unsplash.sys.util.Constants.Companion.COLOR
import com.example.unsplash.sys.util.Constants.Companion.CREATED_AT
import com.example.unsplash.sys.util.Constants.Companion.HEIGHT
import com.example.unsplash.sys.util.Constants.Companion.IMG_FULL
import com.example.unsplash.sys.util.Constants.Companion.LIKES
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
    private lateinit var manager: GridLayoutManager
    private var page: Int = 1
    private var pageSize = 30
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private lateinit var search: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this)[ImagesViewModel::class.java]
        viewModel.getImages("cats", page)

        setObservers()
        initConfig()

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                SearchRecentSuggestions(
                    this, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE
                ).saveRecentQuery(query, null)
            }
        }

        binding.rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItem = manager.childCount
                val totalItem = manager.itemCount
                val firstVisibleItemPos = manager.findFirstVisibleItemPosition()
                if (!isLoading && !isLastPage) {
                    if (visibleItem + firstVisibleItemPos >= totalItem && firstVisibleItemPos >= 0
                        && totalItem >= pageSize
                    ) {
                        page++
                        viewModel.getImages(search, page)
                    }
                }
            }
        })
    }


    private fun initConfig() {
        binding.sView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                isLoading = true
                search = query
                viewModel.getImages(search, page)
                hideKeyBoard()
                return true

            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return
    }

    private fun setObservers() {
        viewModel.onSuccess.observe(this) { list ->
            binding.pbItems.visibility = View.GONE
            setUpRecycler(list)
            isLoading = false
            isLastPage = if (list.isNotEmpty()) {
                list.size < pageSize
            } else {
                true
            }
        }
        viewModel.onError.observe(this) { error ->
            Log.d("ERROR", error.errorCode.toString())
        }
    }

    private fun setUpRecycler(data: List<UImages>) {
        manager = GridLayoutManager(this, 2)
        binding.rvImages.layoutManager = manager
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
        i.putExtra(WIDTH, item.width)
        i.putExtra(HEIGHT, item.height)
        i.putExtra(COLOR, item.color)
        i.putExtra(BLUR_HASH, item.blur_hash)
        i.putExtra(ALT_DESCRIPTION, item.alt_description)
        i.putExtra(LIKES, item.likes)
        i.putExtra(IMG_FULL, item.urls!!.full)
        startActivity(i)
    }

    private fun hideKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val m = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            m.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    @Deprecated("Deprecated in Java", ReplaceWith("onBackPressedDispatcher.onBackPressed()"))
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }

}
