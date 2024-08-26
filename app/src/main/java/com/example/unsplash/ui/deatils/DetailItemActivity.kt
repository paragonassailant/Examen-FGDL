package com.example.unsplash.ui.deatils

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.unsplash.databinding.ActivityDetailItemBinding
import com.example.unsplash.sys.util.Constants.Companion.ALT_DESCRIPTION
import com.example.unsplash.sys.util.Constants.Companion.BLUR_HASH
import com.example.unsplash.sys.util.Constants.Companion.COLOR
import com.example.unsplash.sys.util.Constants.Companion.CREATED_AT
import com.example.unsplash.sys.util.Constants.Companion.FORMAT
import com.example.unsplash.sys.util.Constants.Companion.HEIGHT
import com.example.unsplash.sys.util.Constants.Companion.IMG_FULL
import com.example.unsplash.sys.util.Constants.Companion.LIKES
import com.example.unsplash.sys.util.Constants.Companion.SLUG
import com.example.unsplash.sys.util.Constants.Companion.UPDATED_AT
import com.example.unsplash.sys.util.Constants.Companion.WIDTH
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class DetailItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailItemBinding
    private lateinit var slug: String
    private lateinit var createdAt: String
    private lateinit var updatedAt: String
    private lateinit var blurHash: String
    private lateinit var altDescription: String
    private lateinit var urlFull: String
    private var width: Int = 0
    private var height = 0
    private lateinit var color: String
    private var likes: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailItemBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initConfig()
    }

    private fun initConfig() {
        setSupportActionBar(binding.toolbar)
        title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener(arrowPressed)



        slug = intent.getStringExtra(SLUG).toString()
        createdAt = intent.getStringExtra(CREATED_AT).toString()
        updatedAt = intent.getStringExtra(UPDATED_AT).toString()
        width = intent.getIntExtra(WIDTH, 0)
        height = intent.getIntExtra(HEIGHT, 0)
        color = intent.getStringExtra(COLOR).toString()
        blurHash = intent.getStringExtra(BLUR_HASH).toString()
        altDescription = intent.getStringExtra(ALT_DESCRIPTION).toString()
        likes = intent.getIntExtra(LIKES, 0)
        urlFull = intent.getStringExtra(IMG_FULL).toString()


        binding.tvSlug.text = slug
        binding.tvCreatedAt.text = createdAt(createdAt, FORMAT)
        binding.tvUpdatedAt.text = updatedAt(updatedAt, FORMAT)
        binding.tvWidth.text = width.toString()
        binding.tvHeight.text = height.toString()
        binding.tvColor.text = color
        binding.tvBlur.text = blurHash
        binding.tvAlDescription.text = altDescription
        binding.tvLikes.text = likes.toString()


        Picasso.get()
            .load(urlFull)
            .fit()
            .centerCrop()
            .into(binding.ivFull)

    }

    @SuppressLint("SimpleDateFormat")
    private fun createdAt(date: String, format: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val formatter = SimpleDateFormat(format)
        val fecha = parser.parse(date)
        return formatter.format(fecha!!)
    }

    @SuppressLint("SimpleDateFormat")
    private fun updatedAt(date: String, format: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val formatter = SimpleDateFormat(format)
        val fecha = parser.parse(date)
        return formatter.format(fecha!!)

    }


    private val arrowPressed =
        View.OnClickListener { v: View? -> onBackPressedDispatcher.onBackPressed() }
}