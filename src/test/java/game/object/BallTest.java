package game.object;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import game.java.main.GameJframe;

import java.awt.*;

/**
 * Unit test cho class Ball
 */
public class BallTest {

    private Ball ball;

    @BeforeEach
    void setUp() {
        ball = new Ball(100, 100, 30, 30, 1 / Math.sqrt(2), 1 / Math.sqrt(2), 8);
    }

    @Test
    void testMoveChangesPosition() {
        int oldX = ball.getX();
        int oldY = ball.getY();

        ball.move();

        assertNotEquals(oldX, ball.getX(), "Ball phải di chuyển chiều ngang");
        assertNotEquals(oldY, ball.getY(), "Ball phải di chuyển chiều dọc");
    }

    @Test
    void testBounceOffLeftWall() {
        ball.setX(0);
        ball.setDirectionX(-1);

        ball.move();

        assertTrue(ball.getDirectionX() > 0, "Ball nên đổi hướng sang phải khi chạm tường trái");
    }

    @Test
    void testBounceOffRightWall() {
        ball.setX(GameJframe.SCREEN_WIDTH - ball.getWidth());
        ball.setDirectionX(1);

        ball.move();

        assertTrue(ball.getDirectionX() < 0, "Ball nên đổi hướng sang trái khi chạm tường phải");
    }

    @Test
    void testBounceOffTopWall() {
        ball.setY(0);
        ball.setDirectionY(-1);

        ball.move();

        assertTrue(ball.getDirectionY() > 0, "Ball nên đổi hướng xuống khi chạm trần");
    }

    @Test
    void testCollisionWithOtherObject() {
        GameObject other = new GameObject(110, 100, 20, 20) {
            @Override
            public void render(Graphics g) {}
            @Override
            public void update() {}
        };

        assertTrue(ball.checkCollision(other), "Ball nên phát hiện va chạm với object gần đó");
    }

    @Test
    void testSetSpeedLimit() {
        ball.setSpeed(100);
        assertTrue(ball.getSpeed() <= 40, "Tốc độ không nên vượt quá 40");
    }

}
