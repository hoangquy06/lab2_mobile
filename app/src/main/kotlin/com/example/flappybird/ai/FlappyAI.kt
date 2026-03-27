package com.example.flappybird.ai

/**
 * FlappyAI là module AI dựa trên luật (Rule-based) đơn giản để điều khiển chim.
 */
class FlappyAI {
    
    // Offset quyết định lúc nào chim bắt đầu thả rớt quá mức an toàn và cần nhảy.
    // Cách tính chuẩn: offset = (Chiều cao khoảng trống / 2) - (Chiều cao chim / 2) - Khoảng an toàn
    // Ví dụ: Gap cao 200px, Chim cao 40px, Khoảng an toàn 20px
    // -> offset = (200 / 2) - (40 / 2) - 20 = 100 - 20 - 20 = 60f
    // Nghĩa là chim tụt xuống dưới tâm ống 60px thì AI sẽ bấm nhảy.
    var offset: Float = 60f

    // Tolerance (độ trễ) giúp chim không nhảy quá liên tục.
    // Dựa trên vận tốc hiện tại (bird_v), mếu chim đang lao mạnh lên (bird_v < -10)
    // thì chưa cho nhảy tiếp, nhằm tránh việc bay quá cao đụng mép ống trên.
    // Vận tốc nhảy thường rơi vào khoảng -15 đến -20 tuỳ game.
    var toleranceVelocity: Float = -10f
    
    // Khoảng cách theo phương Y bổ sung (Tolerance) cho vị trí.
    var tolerancePosition: Float = 10f

    /**
     * get_action quyết định chim có nên nhảy (nhấn màn hình) hay không.
     * 
     * @param bird_y Toạ độ $y$ của con chim (Y trong Android tăng từ trên xuống dưới màn hình).
     * @param gap_y Toạ độ $y$ tâm khoảng trống giữa 2 ống cống gần nhất.
     * @param bird_v Vận tốc của chim (tuỳ chọn): âm khi đang đi lên, dương khi đang rớt xuống.
     * @param gap_x Toạ độ $(x, y)$ của khoảng trống giữa hai ống (tuỳ chọn thêm để tính toán độ dốc sau này).
     * @return True (nhảy) hoặc False (không nhảy).
     */
    fun get_action(bird_y: Float, gap_y: Float, bird_v: Float = 0f, gap_x: Float = 0f): Boolean {
        
        // Điều kiện 1: Tọa độ y của chim thấp hơn khoảng offset.
        // Chú ý: trong Android, Y càng lớn thì vật càng nằm bên dưới. Nghĩa là chim đang rớt.
        val shouldJumpPosition = bird_y > (gap_y + offset)
        
        // Điều kiện 2: Kiểm tra độ trễ (Tolerance) qua vận tốc (bird_v).
        // Nếu chim chưa lao lên quá mạnh, AI mới cho phép nhảy tiếp.
        val notAscendingTooFast = bird_v > toleranceVelocity

        return shouldJumpPosition && notAscendingTooFast
    }
}
