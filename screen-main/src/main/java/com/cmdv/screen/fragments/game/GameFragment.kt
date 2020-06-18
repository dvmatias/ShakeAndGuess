package com.cmdv.screen.fragments.game

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.cmdv.core.Constants
import com.cmdv.core.SimpleAnimationListener
import com.cmdv.core.helpers.logDebugMessage
import com.cmdv.domain.model.CategoryModel
import com.cmdv.screen.R
import com.cmdv.screen.databinding.FragmentGameBinding
import com.devs.vectorchildfinder.VectorChildFinder
import com.google.gson.Gson

class GameFragment : Fragment() {

    private lateinit var viewModel: GameFragmentViewModel
    private lateinit var binding: FragmentGameBinding
    private val args: GameFragmentArgs by navArgs()
    private lateinit var category: CategoryModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        category = Gson().fromJson(args.category, CategoryModel::class.java)

        setupViewModel()
        initCategoryWords()
        setupBackgroundColors()
        prepareGame()
    }

    private fun setupViewModel() {
        this.viewModel = ViewModelProvider(this).get(GameFragmentViewModel()::class.java)
    }

    private fun initCategoryWords() {
        viewModel.category = this.category
        viewModel.setWordsStack()
    }

    private fun setupBackgroundColors() {
        VectorChildFinder(requireContext(), R.drawable.img_fragment_game_top_bgr, binding.ivBackgroundTop)
            .findPathByName(Constants.CATEGORY_BG_PATH_TOP_NAME).fillColor = Color.parseColor(category.colorTop)
        VectorChildFinder(requireContext(), R.drawable.img_fragment_game_bottom_bgr, binding.ivBackgroundBottom)
            .findPathByName(Constants.CATEGORY_BG_PATH_BOTTOM_NAME).fillColor = Color.parseColor(category.colorBottom)
    }

    private fun prepareGame() {
        animateOpenBackground()
        viewModel.gameStartingCountDownFinishedMutableLiveData.observe(
            viewLifecycleOwner,
            Observer {
                binding.tvInitialCountEffect.visibility = View.GONE
                animateCloseBackground()
                startGame()
            })
    }

    private fun animateOpenBackground() {
        // Animate background top image
        val animBackgroundTopOpen = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_background_top_open)
        binding.ivBackgroundTop.startAnimation(animBackgroundTopOpen)
        // Animate background bottom image
        val animBackgroundBottomOpen = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_background_bottom_open)
        binding.ivBackgroundBottom.startAnimation(animBackgroundBottomOpen)
        // Wait for anim to end before start initial countdown-
        animBackgroundBottomOpen.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(p0: Animation?) {
                startStartingCountDown()
            }
        })
    }

    private fun animateCloseBackground() {
        // Animate background top image
        val animTopToTop = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_background_top_close)
        binding.ivBackgroundTop.startAnimation(animTopToTop)
        // Animate background bottom image
        val animBottomToBottom = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_background_bottom_close)
        binding.ivBackgroundBottom.startAnimation(animBottomToBottom)
    }

    private fun startStartingCountDown() {
        with(this.viewModel) {
            startGameStartingCountdown()
            gameStartingCountDownMutableLiveData.observe(
                viewLifecycleOwner,
                Observer { count: Int ->
                    val countText = if (count == 0) "Go!" else count.toString()
                    binding.tvInitialCount.text = countText
                    binding.tvInitialCountEffect.apply {
                        text = countText
                        startAnimation(
                            AnimationUtils.loadAnimation(requireContext(), R.anim.anim_starting_game_count_down)
                        )
                    }
                })
        }
    }

    private fun startGame() {
        viewModel.wordToGuess.observe(viewLifecycleOwner, Observer {
            
        })
        viewModel.getNewWordToGuess()
    }

}