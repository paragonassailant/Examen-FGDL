package com.example.unsplash.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unsplash.R
import com.example.unsplash.data.entities.UImages
import com.example.unsplash.databinding.ItemImageBinding
import com.example.unsplash.ui.main.interfaces.IAUImage
import com.squareup.picasso.Picasso

class UImageAdapter(private var uImages: List<UImages>, private var context: Context, private var listener:IAUImage) :
    RecyclerView.Adapter<UImageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = uImages[position]
        holder.binding.tvId.text = item.id

        holder.binding.ivThumb.setOnClickListener {
            listener.viewImage(item)
        }

        holder.binding.cvUnsplash.setOnClickListener {
            listener.details(item)
        }



        if (item.urls != null) {
            Picasso.with(context)
                .load(item.urls!!.thumb)
                .fit()
                .centerCrop()
                .into(holder.binding.ivThumb)
        } else {
            Picasso.with(context)
                .load(R.drawable.ic_launcher_background)
                .fit()
                .centerCrop()
                .into(holder.binding.ivThumb)
        }

    }

    override fun getItemCount() = uImages.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemImageBinding.bind(view)
    }

}