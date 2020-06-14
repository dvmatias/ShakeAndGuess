package com.cmdv.screen.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.cmdv.core.logWarningMessage
import com.cmdv.core.showShortToast
import com.cmdv.data.repository.CategoryRepositoryImpl
import com.cmdv.domain.model.CategoryModel
import com.cmdv.screen.R
import com.cmdv.screen.adapters.CategoryRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

	private lateinit var viewModel: HomeFragmentViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_home, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupViewModel()
		fetchCategories()
	}

	private fun setupViewModel() {
		viewModel = ViewModelProvider(this, HomeFragmentViewModelFactory(CategoryRepositoryImpl()))
			.get(HomeFragmentViewModel::class.java)
	}

	private fun fetchCategories() {
		viewModel.fetchCategories()
		viewModel.categoriesLiveData.observe(viewLifecycleOwner, Observer { categories: List<CategoryModel> ->
			if (categories.isNotEmpty()) {
				val categoryAdapter = CategoryRecyclerAdapter()
				recycler_categories.apply {
					layoutManager = GridLayoutManager(activity, 4, GridLayoutManager.VERTICAL, false)
					adapter = categoryAdapter
				}
				categoryAdapter.apply {
					setListener(::onCategoryClick)
					setData(categories)
				}

			} else {
				logWarningMessage("There are not categories to display")
			}
		})
	}


	private fun onCategoryClick(category: CategoryModel) {
		activity?.let { showShortToast(it, "Clicked on ${category.name}") }
	}

}