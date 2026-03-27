package com.example.flappybird

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity

@Composable
fun GameCanvas(gameState: GameState) {
    val birdImage = ImageBitmap.imageResource(id = R.drawable.bird)
    val pipeImage = ImageBitmap.imageResource(id = R.drawable.pipe)
    val backgroundImage = ImageBitmap.imageResource(id = R.drawable.background)

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val widthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val heightPx = with(LocalDensity.current) { maxHeight.toPx() }

        LaunchedEffect(gameState.status) {
            if (gameState.status == GameStatus.PLAYING) {
                while (true) {
                    gameState.update(widthPx, heightPx)
                    delay(16)
                }
            }
        }

        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    gameState.jump()
                }
            }) {
            val width = size.width
            val height = size.height

            // Draw Background
            drawImage(
                image = backgroundImage,
                dstSize = IntSize(width.toInt(), height.toInt())
            )

            // Draw Bird
            val birdX = 100f
            drawImage(
                image = birdImage,
                dstOffset = IntOffset(birdX.toInt(), gameState.bird.y.toInt()),
                dstSize = IntSize(gameState.bird.size.toInt(), gameState.bird.size.toInt())
            )

            // Draw Pipes
            gameState.pipes.forEach { pipe ->
                // Top Pipe (Inverted)
                withTransform({
                    rotate(180f, pivot = Offset(pipe.x + pipe.width / 2, pipe.gapY / 2))
                }) {
                    drawImage(
                        image = pipeImage,
                        dstOffset = IntOffset(pipe.x.toInt(), 0),
                        dstSize = IntSize(pipe.width.toInt(), pipe.gapY.toInt())
                    )
                }

                // Bottom Pipe
                drawImage(
                    image = pipeImage,
                    dstOffset = IntOffset(pipe.x.toInt(), (pipe.gapY + pipe.gapHeight).toInt()),
                    dstSize = IntSize(pipe.width.toInt(), (height - (pipe.gapY + pipe.gapHeight)).toInt())
                )
            }
        }
    }
}
