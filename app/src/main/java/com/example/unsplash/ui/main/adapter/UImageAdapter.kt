package com.example.unsplash.ui.main.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.unsplash.R
import com.example.unsplash.data.entities.UImages
import com.example.unsplash.databinding.ItemImageBinding
import com.example.unsplash.ui.main.interfaces.IAUImage
import com.squareup.picasso.Picasso

class UImageAdapter(private val listener: IAUImage) :
    PagingDataAdapter<UImages, UImageAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UImages>() {
            override fun areItemsTheSame(oldItem: UImages, newItem: UImages): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UImages, newItem: UImages): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemImageBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        item?.let {
            holder.binding.tvId.text = it.id

            holder.binding.ivThumb.setOnClickListener {
                listener.viewImage(item)
            }

            holder.binding.cvUnsplash.setOnClickListener {
                listener.details(item)
            }

            if (item.urls != null) {
                Picasso.get()
                    .load(item.urls!!.thumb)
                    .into(holder.binding.ivThumb)
            } else {
                Picasso.get()
                    .load(R.drawable.ic_launcher_background)
                    .into(holder.binding.ivThumb)
            }
        }
    }

    inner class ViewHolder(val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root)
}
