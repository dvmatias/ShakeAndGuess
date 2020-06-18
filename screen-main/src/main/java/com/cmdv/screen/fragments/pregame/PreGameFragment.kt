package com.cmdv.screen.fragments.pregame

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cmdv.core.Constants
import com.cmdv.domain.model.CategoryModel
import com.cmdv.screen.R
import com.cmdv.screen.databinding.FragmentPreGameBinding
import com.devs.vectorchildfinder.VectorChildFinder
import com.google.gson.Gson

class PreGameFragment : Fragment() {

    private lateinit var binding: FragmentPreGameBinding

    private val args: PreGameFragmentArgs by navArgs()

    private lateinit var category: CategoryModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pre_game, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        category = Gson().fromJson(args.category, CategoryModel::class.java)
        setupBackgroundColors()
        setupPlayButton()
    }

    private fun setupBackgroundColors() {
        val vectorChild =
            VectorChildFinder(
                requireContext(),
                R.drawable.img_fragment_game_bgr,
                binding.ivBackground
            )
        with(vectorChild) {
            findPathByName(Constants.CATEGORY_BG_PATH_TOP_NAME).fillColor =
                Color.parseColor(category.colorTop)
            findPathByName(Constants.CATEGORY_BG_PATH_BOTTOM_NAME).fillColor =
                Color.parseColor(category.colorBottom)
        }
    }

    private fun setupPlayButton() {
        binding.btnStart.setOnClickListener {
            val extraInfo = FragmentNavigatorExtras()
            val destination = PreGameFragmentDirections.toGameFragment(args.category)
            navigate(destination, extraInfo)
        }
    }

    private fun navigate(destination: NavDirections, extraInfo: FragmentNavigator.Extras) {
        with(findNavController()) {
            currentDestination?.getAction(destination.actionId)
                ?.let { navigate(destination, extraInfo) }
        }
    }
}