package com.cmdv.core.helpers

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import java.lang.ref.WeakReference

private const val PATH_CATEGORIES = "categories"

fun loadCategoryImageFromStorage(
    context: WeakReference<Context>,
    imageName: String,
    imageView: AppCompatImageView
) {
    val storageRef = FirebaseStorage.getInstance().reference
    storageRef.child(PATH_CATEGORIES).child(imageName).downloadUrl.addOnSuccessListener { uri ->
        context.get()?.let { context ->
            Glide.with(context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .fitCenter()
                .into(imageView)
        }
    }
}