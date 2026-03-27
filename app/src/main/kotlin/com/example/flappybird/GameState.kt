package com.example.flappybird

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.random.Random
import com.example.flappybird.ai.FlappyAI

class GameState {
    var bird by mutableStateOf(Bird())
    val pipes = mutableStateListOf<Pipe>()
    var score by mutableStateOf(0)
    var status by mutableStateOf(GameStatus.IDLE)
    var isAiEnabled by mutableStateOf(false)
    
    // Khởi tạo AI và cấu hình thông số tự động nhảy dựa trên kích thước game (Bird = 90f, GapHeight = 500f)
    val aiController = FlappyAI().apply {
        offset = 120f
        toleranceVelocity = -8f
    }
    
    // Physics constants
    private val gravity = 0.8f
    private val jumpForce = -15f
    private val pipeSpeed = 10f // Faster speed for larger scale
    private val pipeSpawnInterval = 95 // Increased interval for more distance between pipes
    private var frameCount = 0

    fun reset() {
        bird = Bird()
        pipes.clear()
        score = 0
        status = GameStatus.PLAYING
        frameCount = 0
    }

    fun jump() {
        if (status == GameStatus.PLAYING) {
            bird = bird.copy(velocity = jumpForce)
        } else if (status == GameStatus.GAME_OVER || status == GameStatus.IDLE) {
            reset()
        }
    }

    fun update(width: Float, height: Float) {
        if (status != GameStatus.PLAYING) return

        // --- Tích hợp FlappyAI ---
        val birdX = 100f
        // Lấy ống gần con chim nhất
        val nextPipe = pipes.firstOrNull { it.x + it.width > birdX }
        if (nextPipe != null) {
            // Tính toán tâm của khoảng trống (mép trên gapY + nửa chiều cao ống)
            val gapCenterY = nextPipe.gapY + (nextPipe.gapHeight / 2)
            
            // Lấy quyết định từ AI
            val shouldJump = aiController.get_action(
                bird_y = bird.y,
                gap_y = gapCenterY,
                bird_v = bird.velocity,
                gap_x = nextPipe.x
            )
            
            // Thực thi Jump nếu AI yêu cầu
            if (shouldJump && isAiEnabled) {
                jump()
            }
        } else {
            // Khi màn hình chưa có cột, AI giữ chim thăng bằng ở xấp xỉ giữa màn hình
            val screenCenterY = height / 2
            val shouldJump = aiController.get_action(
                bird_y = bird.y,
                gap_y = screenCenterY,
                bird_v = bird.velocity,
                gap_x = width
            )
            if (shouldJump && isAiEnabled) {
                jump()
            }
        }
        // --- Kết thúc FlappyAI ---

        // Update Bird
        val newVelocity = bird.velocity + gravity
        val newY = bird.y + newVelocity
        bird = bird.copy(y = newY, velocity = newVelocity)

        // Collision with ground or ceiling
        if (newY < 0 || newY + bird.size > height) {
            status = GameStatus.GAME_OVER
        }

        // Update Pipes
        frameCount++
        if (frameCount % pipeSpawnInterval == 0) {
            spawnPipe(width, height)
        }

        val iterator = pipes.listIterator()
        while (iterator.hasNext()) {
            val pipe = iterator.next()
            val nextPipeX = pipe.x - pipeSpeed
            
            // Check collision
            if (checkCollision(bird, pipe)) {
                status = GameStatus.GAME_OVER
            }

            // Check if passed for scoring
            // Bird is at X=100
            if (!pipe.passed && nextPipeX + pipe.width < 100) {
                pipe.passed = true
                score++
            }

            if (nextPipeX + pipe.width < 0) {
                iterator.remove()
            } else {
                iterator.set(pipe.copy(x = nextPipeX))
            }
        }
    }

    private fun spawnPipe(width: Float, height: Float) {
        val minGapY = 200f
        val maxGapY = height - 800f
        
        val lastPipe = pipes.lastOrNull()
        val gapY = if (lastPipe == null) {
            // Nếu là cột đầu tiên, lấy vị trí ngẫu nhiên bất kỳ
            Random.nextFloat() * (maxGapY - minGapY) + minGapY
        } else {
            // Nếu đã có cột trước đó, giới hạn độ chênh lệch tối đa là 90f
            val lastGapY = lastPipe.gapY
            val preferredMin = maxOf(minGapY, lastGapY - 700f)
            val preferredMax = minOf(maxGapY, lastGapY + 700f)
            Random.nextFloat() * (preferredMax - preferredMin) + preferredMin
        }

        pipes.add(Pipe(x = width, gapY = gapY))
    }

    private fun checkCollision(bird: Bird, pipe: Pipe): Boolean {
        val birdX = 100f
        val birdRight = birdX + bird.size
        val birdTop = bird.y
        val birdBottom = bird.y + bird.size

        val pipeLeft = pipe.x
        val pipeRight = pipe.x + pipe.width

        // Horizontal check
        if (birdRight > pipeLeft && birdX < pipeRight) {
            // Vertical check: hit top pipe or bottom pipe?
            if (birdTop < pipe.gapY || birdBottom > pipe.gapY + pipe.gapHeight) {
                return true
            }
        }
        return false
    }
}
