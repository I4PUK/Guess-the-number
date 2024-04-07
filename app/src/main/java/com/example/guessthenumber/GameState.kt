package com.example.guessthenumber

sealed class GameState {
    data object WinState : GameState()
    class AttemptState(val attempts: Int, val hintText: String): GameState()
}
