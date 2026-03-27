package com.example.flappybird

data class Bird(
    val y: Float = 500f,
    val velocity: Float = 0f,
    val size: Float = 90f // Increased from 80f
)

data class Pipe(
    val x: Float,
    val gapY: Float, // Top of the gap
    val gapHeight: Float = 500f, // Increased from 450f
    val width: Float = 190f, // Increased from 180f
    var passed: Boolean = false
)

enum class GameStatus {
    IDLE, PLAYING, GAME_OVER
}
