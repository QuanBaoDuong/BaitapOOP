package test.game.manager;

import game.manager.GameManager;
import game.object.Ball;
import game.object.Brick;
import game.object.Paddle;
import game.object.PowerUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import game.object.NormalBrick;

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
        List<Ball> balls = gameManager.getBalls();
    }

    @Test
    void testBallOutOfBoundsReducesLife() {
        int initialLives = gameManager.getLives();

        // Đặt bóng ra ngoài màn hình
        ball.setY(1000);
        gameManager.update();

        assertEquals(initialLives - 1, gameManager.getLives());
        assertFalse(gameManager.isOutOfLives());
    }

    @Test
    void testOutOfLives() {
        gameManager.setLives(1);
        ball.setY(1000);
        gameManager.update();

        assertTrue(gameManager.isOutOfLives());
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
        // Tạo 1 PowerUp từ Brick
        List<Brick> bricks = gameManager.getBricks();
        if (!bricks.isEmpty()) {
            Brick brick = bricks.get(0);
            PowerUp pu = PowerUp.generateFromBrick(brick);
            if (pu != null) {
                int oldPaddleWidth = paddle.getWidth();
                int oldBallSpeed = ball.getSpeed();

                // Đặt PowerUp chạm paddle
                pu.activate(gameManager, paddle, ball);

                // Kiểm tra ít nhất paddle hoặc ball thay đổi
                boolean changed = paddle.getWidth() != oldPaddleWidth || ball.getSpeed() != oldBallSpeed;
                assertTrue(changed, "PowerUp should affect paddle or ball");
            }
        }
    }

    @Test
    void testPowerUpExpiresAfterDuration() throws InterruptedException {
        // Tạo PowerUp thời gian ngắn
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
