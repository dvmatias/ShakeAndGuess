package com.cmdv.screen.fragments.game

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.cmdv.core.base.BaseMVVMViewModel
import com.cmdv.core.helpers.logInfoMessage
import com.cmdv.domain.model.CategoryModel
import java.util.*

class GameFragmentViewModel : BaseMVVMViewModel() {
    lateinit var category: CategoryModel
    private var wordsStack = Stack<CategoryModel.CategoryItemModel>()
    var gameStartingCountDownMutableLiveData = MutableLiveData<Int>()
        private set
    var gameStartingCountDownFinishedMutableLiveData = MutableLiveData<Int>()
        private set
    var wordToGuess = MutableLiveData<String>()
        private set

    fun startGameStartingCountdown() {
        CountDown().Builder(6000,1000)
            .startCountingOn(gameStartingCountDownMutableLiveData)
            .finishCountingOn(gameStartingCountDownFinishedMutableLiveData)
            .start()
    }

    fun setWordsStack() {
        for (item in category.items) {
            wordsStack.push(item)
        }
    }

    fun getNewWordToGuess() {
        wordToGuess.postValue(wordsStack.pop().value)
    }

    open class CountDown(
        millisInFuture: Long,
        countDownInterval: Long
    ) : CountDownTimer(millisInFuture, countDownInterval) {

        constructor(): this(0, 0)

        private var startCountingOnMutableLiveData: MutableLiveData<Int>? = null
        private var finishCountingOnMutableLiveData: MutableLiveData<Int>? = null

        override fun onFinish() {
            this.startCountingOnMutableLiveData = null
            this.finishCountingOnMutableLiveData?.postValue(-1)
            this.finishCountingOnMutableLiveData = null
            logInfoMessage("Starting countdown ---> FINISH")
        }

        override fun onTick(millisUntilFinished: Long) {
            startCountingOnMutableLiveData?.postValue((millisUntilFinished / 1000).toInt())
            logInfoMessage("Starting countdown ---> ${(millisUntilFinished / 1000).toInt()}")
        }

        inner class Builder(
            millisInFuture: Long,
            countDownInterval: Long
        ) : CountDown(millisInFuture, countDownInterval) {

            fun startCountingOn(startCountingOnMutableLiveData: MutableLiveData<Int>): Builder {
                super.startCountingOnMutableLiveData = startCountingOnMutableLiveData
                return this
            }

            fun finishCountingOn(finishCountingOnMutableLiveData: MutableLiveData<Int>): Builder {
                super.finishCountingOnMutableLiveData = finishCountingOnMutableLiveData
                return this
            }

        }

    }

}