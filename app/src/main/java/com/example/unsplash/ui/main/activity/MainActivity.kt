package com.example.unsplash.ui.main.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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
import com.example.unsplash.ui.main.viewmodel.ImagesViewModel
import com.example.unsplash.ui.main.adapter.UImageAdapter
import com.example.unsplash.ui.main.fragment.ImageDialogFragment
import com.example.unsplash.ui.main.interfaces.IAUImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), IAUImage {

    private lateinit var viewModel: ImagesViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var uImageAdapter: UImageAdapter
    private lateinit var dialogFragment: ImageDialogFragment
    private lateinit var manager: StaggeredGridLayoutManager
    private var search: String = "cats"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ImagesViewModel::class.java]

        setUpRecycler()
        initConfig()
        loadImages(search)
    }

    private fun initConfig() {
        binding.sView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                search = query
                loadImages(search)
                hideKeyBoard()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun setUpRecycler() {
        manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvImages.layoutManager = manager
        binding.rvImages.setHasFixedSize(true)
        uImageAdapter = UImageAdapter(this)
        binding.rvImages.adapter = uImageAdapter
    }

    private fun loadImages(query: String) {
        lifecycleScope.launch {
            viewModel.getImagesPaging(query).collectLatest { pagingData ->
                binding.pbL.visibility = View.GONE
                uImageAdapter.submitData(pagingData)

            }
        }

    }

    override fun viewImage(item: UImages) {
        dialogFragment = ImageDialogFragment.newInstance(item.urls?.full!!)
        dialogFragment.show(supportFragmentManager, "Image Dialog")
        hideKeyBoard()
    }

    override fun details(item: UImages) {
        val i = Intent(this, DetailItemActivity::class.java).apply {
            putExtra(SLUG, item.slug)
            putExtra(CREATED_AT, item.created_at)
            putExtra(UPDATED_AT, item.updated_at)
            putExtra(WIDTH, item.width)
            putExtra(HEIGHT, item.height)
            putExtra(COLOR, item.color)
            putExtra(BLUR_HASH, item.blur_hash)
            putExtra(ALT_DESCRIPTION, item.alt_description)
            putExtra(LIKES, item.likes)
            putExtra(IMG_FULL, item.urls!!.full)
        }
        startActivity(i)
    }

    private fun hideKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val m = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            m.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    @Deprecated("Deprecated in Java", ReplaceWith("onBackPressedDispatcher.onBackPressed()"))
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }
}
