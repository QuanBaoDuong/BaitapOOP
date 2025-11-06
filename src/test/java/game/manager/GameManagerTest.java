package test.game.manager;

import game.manager.GameManager;
import game.object.Ball;
import game.object.Brick;
import game.object.Paddle;
import game.object.PowerUp;
import game.object.NormalBrick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    private GameManager gameManager;
    private Paddle paddle;
    private Ball ball;

    @BeforeEach
    void setup() {
        gameManager = new GameManager();
        paddle = gameManager.getPaddle();

        // Lấy bóng đầu tiên trong danh sách bóng của GameManager
        List<Ball> balls = gameManager.getBalls();
        if (!balls.isEmpty()) {
            ball = balls.get(0);
        } else {
            // Nếu vì lý do nào đó không có bóng -> tạo tạm 1 quả để test
            ball = new Ball(paddle.getX() + paddle.getWidth() / 2 - 15,
                    paddle.getY() - 30,
                    30, 30,
                    1 / Math.sqrt(2), 1 / Math.sqrt(2),
                    12);
            gameManager.getBalls().add(ball);
        }
    }

    @Test
    void testBallOutOfBoundsReducesLife() {
        int initialLives = gameManager.getLives();

        // Đặt bóng ra ngoài màn hình (giả sử > chiều cao màn hình)
        ball.setY(1000);
        gameManager.update();

        assertEquals(initialLives - 1, gameManager.getLives(), "Lives should decrease when ball goes out of bounds");
        assertFalse(gameManager.isOutOfLives(), "Game should not be over yet");
    }

    @Test
    void testOutOfLives() {
        gameManager.setLives(1);
        ball.setY(1000);
        gameManager.update();

        assertTrue(gameManager.isOutOfLives(), "Game should be over when lives reach zero");
    }

    @Test
    void testLevelCompletion() {
        GameManager gm = new GameManager();

        // Xóa các bricks map mặc định
        gm.getBricks().clear();

        // Thêm 2 NormalBrick
        gm.getBricks().add(new NormalBrick(0, 0, 50, 20));
        gm.getBricks().add(new NormalBrick(60, 0, 50, 20));

        // Phá hết gạch breakable
        for (Brick b : gm.getBricks()) {
            b.takeHit();
        }

        gm.update();

        assertTrue(gm.isLevelComplete(), "Level should be complete when all bricks are destroyed");
    }

    @Test
    void testPowerUpActivationChangesPaddleOrBall() {
        List<Brick> bricks = gameManager.getBricks();
        if (!bricks.isEmpty()) {
            Brick brick = bricks.get(0);
            PowerUp pu = PowerUp.generateFromBrick(brick);

            if (pu != null) {
                int oldPaddleWidth = paddle.getWidth();
                int oldBallSpeed = ball.getSpeed();

                // Kích hoạt PowerUp
                pu.activate(gameManager, paddle, ball);

                // Kiểm tra paddle hoặc ball thay đổi
                boolean changed = paddle.getWidth() != oldPaddleWidth || ball.getSpeed() != oldBallSpeed;
                assertTrue(changed, "PowerUp should affect paddle or ball");
            }
        }
    }

    @Test
    void testPowerUpExpiresAfterDuration() throws InterruptedException {
        PowerUp pu = new PowerUp(0, 0, 20, 20, "dummy", 100) {
            @Override
            public void applyEffect(GameManager gm, Paddle paddle, Ball ball) {}
            @Override
            public void removeEffect(GameManager gm, Paddle paddle, Ball ball) {}
            @Override
            public void draw(Graphics2D g2d) {}
        };

        pu.activate(gameManager, paddle, ball);

        assertFalse(pu.isExpired(), "PowerUp should not be expired immediately");

        Thread.sleep(150);

        assertTrue(pu.isExpired(), "PowerUp should be expired after duration");
    }
}
