package com.cmdv.screen.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.core.helpers.loadCategoryImageFromStorage
import com.cmdv.domain.model.CategoryModel
import com.cmdv.screen.R
import java.lang.ref.WeakReference


class CategoryRecyclerAdapter(private val context: Context) : RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder>() {

	private var data: List<CategoryModel> = listOf()

	private var clickListener: ((CategoryModel) -> Unit)? = null

	fun setData(data: List<CategoryModel>) {
		this.data = data
		notifyDataSetChanged()
	}

	fun setListener(clickListener: (CategoryModel) -> Unit) {
		this.clickListener = clickListener
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
		val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
		return CategoryViewHolder(itemView)
	}

	override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
		if (clickListener == null) throw IllegalAccessError("${CategoryRecyclerAdapter::class.java.simpleName} :: clickListener can't be null.")
		holder.bindView(context, data[position], clickListener!!)
	}

	override fun getItemCount(): Int = data.size

	inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		private val cvImage: CardView = itemView.findViewById(R.id.cv_image)
		private val ivCategoryBgr: AppCompatImageView = itemView.findViewById(R.id.iv_category_bgr)
		private val ivCategoryImage: AppCompatImageView = itemView.findViewById(R.id.iv_category_image)
		private val tvCategoryName: AppCompatTextView = itemView.findViewById(R.id.tv_category_name)

		fun bindView(context: Context, category: CategoryModel, clickListener: (CategoryModel) -> Unit) {
			tvCategoryName.text = category.name
			loadCategoryImageFromStorage(
				WeakReference(context),
				category.imageName,
				ivCategoryImage
			)

			itemView.setOnClickListener { clickListener(category) }

			colorBackground(category.colorTop, category.colorBottom)
		}

		private fun colorBackground(colorTop: String, colorBottom: String) {
			cvImage.setCardBackgroundColor(Color.parseColor(colorTop))
			ivCategoryBgr.drawable.setTint(Color.parseColor(colorBottom))
		}

	}

}