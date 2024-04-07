package com.example.guessthenumber

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private var _gameState = MutableLiveData<GameState>()
    val uiState: LiveData<GameState>
        get() = _gameState

    private fun setGameState(state: GameState) {
        _gameState.value = state
    }

    var userGuess by mutableStateOf("")
        private set


    private var guessedNumber: Int = 0
    private var attempts: Int = 0

    init {
        resetGame()
    }

    private fun generateNewRandomValue(){
        guessedNumber = (0..100).random()
    }

    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }

    fun resetGame() {
        attempts = 0
        generateNewRandomValue()
        _gameState.value =
            GameState.AttemptState(attempts = attempts, hintText = "")
    }

    fun checkValue() {
        val parsedUserGuess = userGuess.toIntOrNull()
        val valuesDifference = parsedUserGuess?.minus(guessedNumber)

        if (valuesDifference == null) {
            setGameState(
                GameState.AttemptState(
                    attempts = attempts,
                    hintText = "Your number has incorrect format"
                )
            )
        } else {
            if (valuesDifference > 0) {
                attempts++
                setGameState(
                    GameState.AttemptState(
                        attempts = attempts,
                        hintText = "Your number is more"
                    )
                )
            }
            if (valuesDifference == 0) {
                setGameState(
                    GameState.WinState
                )
            }
            if (valuesDifference < 0) {
                attempts++
                setGameState(
                    GameState.AttemptState(
                        attempts = attempts,
                        hintText = "Your number is less"
                    )
                )
            }
        }
    }
}