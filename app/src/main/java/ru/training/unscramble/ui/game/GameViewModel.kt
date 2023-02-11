package ru.training.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.unscramble.ui.game.MAX_NO_OF_WORDS
import com.example.android.unscramble.ui.game.SCORE_INCREASE
import com.example.android.unscramble.ui.game.allWordsList

class GameViewModel: ViewModel() {
    private val TAG = "GameFragment"
    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    private val _score = MutableLiveData<Int>(0)
    val score: LiveData<Int>
            get() = _score

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String>
            get() = _currentScrambledWord

    private val _currentWordCount = MutableLiveData<Int>(0)
    val currentWordCount: LiveData<Int>
            get() = _currentWordCount

    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
// To avoid equality with currentWord. It will be shuffling until it is not equal
        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordsList.add(currentWord)
        }
    }

    init {
        Log.d(TAG, "View Model created!")
        getNextWord()
    }

    private fun increaseScore() {
        _score.value = _score.value?.plus(SCORE_INCREASE)
    }

    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        return if (playerWord.equals(currentWord, true)) {
            increaseScore()
            true
        } else false
    }

    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "View Model destroyed")
    }

}