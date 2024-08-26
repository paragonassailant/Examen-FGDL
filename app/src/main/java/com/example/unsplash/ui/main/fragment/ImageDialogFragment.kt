package com.example.unsplash.ui.main.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.unsplash.R
import com.squareup.picasso.Picasso

class ImageDialogFragment : DialogFragment() {

    private lateinit var imageUrl: String

    companion object {
        fun newInstance(imageUrl: String): ImageDialogFragment {
            val fragment = ImageDialogFragment()
            fragment.imageUrl = imageUrl
            return fragment
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_dialog_image, null)
        val image = dialogView.findViewById<ImageView>(R.id.imageView)
        imageUrl.let {
            Picasso.get()
                .load(it)
                .fit()
                .centerCrop()
                .into(image)

        }

        builder.setView(dialogView)
        return builder.create()

    }
}