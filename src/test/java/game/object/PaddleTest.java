package game.object;

import game.java.main.GameJframe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaddleTest {

    private Paddle paddle;

    @BeforeEach
    void setUp() {
        paddle = new Paddle((GameJframe.SCREEN_WIDTH / 2) - 75,
                700, 150, 30);
    }

    @Test
    void testMoveLeft() {
        paddle.setMoveLeft(true);
        paddle.setMoveRight(false);

        int initialX = paddle.x;
        paddle.update(); // update sẽ gọi move()
        assertEquals(initialX - paddle.getSpeed(), paddle.x);

        // Kiểm tra không đi ra ngoài màn hình
        paddle.x = 0;
        paddle.update();
        assertEquals(0, paddle.x);
    }

    @Test
    void testMoveRight() {
        paddle.setMoveRight(true);
        paddle.setMoveLeft(false);

        int initialX = paddle.x;
        paddle.update();
        assertEquals(initialX + paddle.getSpeed(), paddle.x);

        // Kiểm tra không vượt quá màn hình
        paddle.x = GameJframe.SCREEN_WIDTH - paddle.getWidth();
        paddle.update();
        assertEquals(GameJframe.SCREEN_WIDTH - paddle.getWidth(), paddle.x);
    }

    @Test
    void testSpeed() {
        paddle.setSpeed(20);
        assertEquals(20, paddle.getSpeed());

        paddle.setSpeed(50); // vượt max 30
        assertEquals(30, paddle.getSpeed());
    }

    @Test
    void testWidthHeight() {
        paddle.setWidth(150);
        paddle.setHeight(25);

        assertEquals(150, paddle.getWidth());
        assertEquals(25, paddle.getHeight());
    }

}
