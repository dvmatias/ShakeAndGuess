package com.cmdv.screen.fragments.game

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.cmdv.core.Constants
import com.cmdv.core.helpers.AnimationHelper
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
    private var wordToGuessInAnimations: List<Int> = listOf(
        R.anim.anim_word_to_guess_in_1,
        R.anim.anim_word_to_guess_in_2,
        R.anim.anim_word_to_guess_in_3,
        R.anim.anim_word_to_guess_in_4,
        R.anim.anim_word_to_guess_in_5,
        R.anim.anim_word_to_guess_in_6,
        R.anim.anim_word_to_guess_in_7,
        R.anim.anim_word_to_guess_in_8
    )
    private lateinit var animationHelper: AnimationHelper

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
        animationHelper = AnimationHelper(requireActivity())

        setupViewModel()
        initCategoryWords()
        setupBackgroundColors()
        prepareGame()

        binding.btn.setOnClickListener { viewModel.getNewWordToGuess() }
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
            })
    }

    private fun animateOpenBackground() {
        with(animationHelper) {
            startAnimation(
                R.anim.anim_background_top_open, binding.ivBackgroundTop
            )
            startAnimation(
                R.anim.anim_background_bottom_open, binding.ivBackgroundBottom, ::startStartingCountDown
            )
        }
    }

    private fun animateCloseBackground() {
        with(animationHelper) {
            startAnimation(R.anim.anim_background_top_close, binding.ivBackgroundTop)
            startAnimation(R.anim.anim_background_bottom_close, binding.ivBackgroundBottom, ::startGame)
        }
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
                        animationHelper.startAnimation(R.anim.anim_starting_game_count_down, this)
                    }
                })
        }
    }

    private fun startGame() {
        viewModel.wordToGuess.observe(viewLifecycleOwner, Observer {
            binding.tvWordToGuessBack.text = it
            showWordToGuess(it)
        })
        viewModel.getNewWordToGuess()
    }

    private fun showWordToGuess(value: String) {
        with(binding.tvWordToGuessFront) {
            text = value
            animationHelper.startAnimation(wordToGuessInAnimations[(wordToGuessInAnimations.indices).random()], this)
        }
    }

}