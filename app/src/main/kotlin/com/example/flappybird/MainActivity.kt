package com.example.flappybird

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val gameState = remember { GameState() }
            FlappyBirdGame(gameState)
        }
    }
}

@Composable
fun FlappyBirdGame(gameState: GameState) {
    Box(modifier = Modifier.fillMaxSize()) {
        GameCanvas(gameState)
        
        // --- Nút Bật/Tắt AI (Góc Trái Trên) ---
        Button(
            onClick = { gameState.isAiEnabled = !gameState.isAiEnabled },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (gameState.isAiEnabled) Color(0xFF4CAF50) else Color(0xFFF44336)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = if (gameState.isAiEnabled) "🤖 AI: ON" else "🤖 AI: OFF",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        // --- Giao diện đang chơi / Thua (Hiện Điểm Số) ---
        if (gameState.status == GameStatus.PLAYING || gameState.status == GameStatus.GAME_OVER) {
            Text(
                text = "${gameState.score}",
                color = Color.White,
                fontSize = 64.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 80.dp)
            )
        }

        if (gameState.status == GameStatus.GAME_OVER) {
            Text(
                text = "Game Over! Tap to Restart",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (gameState.status == GameStatus.IDLE) {
            // --- Giao diện Màn hình chờ mới (Giống Flappy Bird Classic) ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
            ) {
                // Tiêu đề FlappyBird
                Text(
                    text = "FlappyBird",
                    color = Color.White,
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(bottom = 60.dp)
                )

                // Khoảng trống ở giữa này để hiển thị con chim lơ lửng của GameCanvas
                Spacer(modifier = Modifier.height(70.dp))

                // Nút RATE
                Button(
                    onClick = { /* TODO: Xử lý Đánh Giá */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(bottom = 32.dp).height(50.dp)
                ) {
                    Text(text = "RATE", color = Color(0xFFFFA500), fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                }

                // Hàng nút Play và Leaderboard
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Nút PLAY (Mũi tên Xanh)
                    Button(
                        onClick = { gameState.jump() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(width = 130.dp, height = 65.dp)
                    ) {
                        Text(text = "▶", color = Color(0xFF4CAF50), fontSize = 36.sp)
                    }

                    // Nút LEADERBOARD (Biểu tượng Cúp)
                    Button(
                        onClick = { /* TODO: Xử lý Bảng xếp hạng */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(width = 130.dp, height = 65.dp)
                    ) {
                        Text(text = "🏆", color = Color(0xFFFF9800), fontSize = 32.sp)
                    }
                }
            }
        }
    }
}
