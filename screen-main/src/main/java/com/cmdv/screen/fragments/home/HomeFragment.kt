package com.cmdv.screen.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.cmdv.core.Constants.Companion.CATEGORIES_ROW_QUANTITY
import com.cmdv.core.SimpleAnimationListener
import com.cmdv.core.helpers.logWarningMessage
import com.cmdv.core.helpers.showShortToast
import com.cmdv.data.repository.CategoryRepositoryImpl
import com.cmdv.domain.model.CategoryModel
import com.cmdv.screen.MainActivityViewModel
import com.cmdv.screen.R
import com.cmdv.screen.adapters.CategoryRecyclerAdapter
import com.cmdv.screen.databinding.FragmentHomeBinding
import com.cmdv.screen.itemdecorators.CategoryItemDecoration
import com.google.gson.Gson

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeFragmentViewModel

    private lateinit var binding: FragmentHomeBinding

    private lateinit var categoryAdapter: CategoryRecyclerAdapter

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_main)

        setupViewModel()
        setupRecyclerView()
        observeForCutoutLeftScreen()
        fetchCategories()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, HomeFragmentViewModelFactory(CategoryRepositoryImpl()))
            .get(HomeFragmentViewModel::class.java)
    }

    private fun setupRecyclerView() {
        categoryAdapter = CategoryRecyclerAdapter(requireContext())
        val layoutAnimController: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.layout_anim_item_category_in
            )

        binding.recyclerCategories.apply {
            layoutManager = GridLayoutManager(
                activity,
                CATEGORIES_ROW_QUANTITY,
                GridLayoutManager.VERTICAL,
                false
            )
            adapter = categoryAdapter
            addItemDecoration(CategoryItemDecoration())
            layoutAnimation = layoutAnimController
        }
    }

    private fun observeForCutoutLeftScreen() {
        ViewModelProvider(requireActivity())
            .get(MainActivityViewModel::class.java)
            .displayCutoutLeft
            .observe(viewLifecycleOwner, Observer {
                binding.recyclerCategories.layoutParams =
                    (binding.recyclerCategories.layoutParams as ConstraintLayout.LayoutParams).apply {
                        leftMargin = it
                    }
            })
    }

    private fun fetchCategories() {
        viewModel.fetchCategories()
        viewModel.categoriesLiveData.observe(
            viewLifecycleOwner,
            Observer { categories: List<CategoryModel> ->
                if (categories.isNotEmpty()) {
                    categoryAdapter.apply {
                        setListener(::onCategoryClick)
                        setData(categories)
                        binding.recyclerCategories.scheduleLayoutAnimation()
                    }
                } else {
                    logWarningMessage("There are not categories to display")
                }
            })
    }


    private fun onCategoryClick(category: CategoryModel) {
        activity?.let { showShortToast(it, "Clicked on ${category.name}") }

        val slideDownAnim =
            AnimationUtils.loadAnimation(requireContext(), R.anim.anim_home_fragment_footer_out)

        with(slideDownAnim) {
            setAnimationListener(object : SimpleAnimationListener() {
                override fun onAnimationStart(p0: Animation?) {
                    binding.fragmentHomeFooter.root.visibility = View.INVISIBLE
                    animCategoriesOut()
                    binding.recyclerCategories.layoutAnimationListener =
                        object : SimpleAnimationListener() {
                            override fun onAnimationEnd(p0: Animation?) {
                                binding.recyclerCategories.visibility = View.GONE
                                val extraInfoForSharedElement = FragmentNavigatorExtras()
                                val toPregameFragment =
                                    HomeFragmentDirections.toPreGameFragment(Gson().toJson(category))
                                naviagate(toPregameFragment, extraInfoForSharedElement)
                            }
                        }
                }
            })
            binding.fragmentHomeFooter.root.startAnimation(this)
        }
    }

    private fun animCategoriesOut() {
        val layoutAnimController =
            AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.layout_anim_item_category_out
            )

        binding.recyclerCategories.apply {
            layoutAnimation = layoutAnimController
            scheduleLayoutAnimation()
        }
    }

    @Suppress("SpellCheckingInspection")
    private fun naviagate(destination: NavDirections, extraInfo: FragmentNavigator.Extras) =
        with(findNavController()) {
            currentDestination?.getAction(destination.actionId)
                ?.let { navigate(destination, extraInfo) }
        }

}