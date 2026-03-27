package com.example.flappybird.ai

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FlappyAITest {

    @Test
    fun testFlappyAJumpCondition() {
        val ai = FlappyAI()
        ai.offset = 20f
        ai.toleranceVelocity = -5f
        
        // Kịch bản 1: Chim rơi xuống dưới vùng an toàn (bird_y > gap_y + offset) và đang rơi tiếp (v > 0)
        // Kết quả mong đợi: True (Nên nhảy)
        assertTrue(ai.get_action(bird_y = 100f, gap_y = 50f, bird_v = 10f))
        
        // Kịch bản 2: Chim đang ở mức an toàn bên trên khoảng cách offset
        // Kết quả mong đợi: False (Không cần nhảy)
        assertFalse(ai.get_action(bird_y = 40f, gap_y = 50f, bird_v = 10f))

        // Kịch bản 3: Chim dù ở dưới mức offset nhưng đang bay tạt lên rất nhanh (v < tolerance)
        // Kịch bản này kiểm tra khả năng chống nhảy liên tục (anti-spam jump)
        // Kết quả mong đợi: False (Chưa cần nhảy tiếp vì đang đà bay lên)
        assertFalse(ai.get_action(bird_y = 100f, gap_y = 50f, bird_v = -10f))
    }
}
