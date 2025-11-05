package game.object;

import game.manager.GameManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class PowerUpTest {

    private GameManager gm;
    private Paddle paddle;
    private Ball ball;

    @BeforeEach
    void setUp() {
        gm = new GameManager(1, false, null); // null SoundManager vì không test âm thanh
        gm.setLives(3);

        paddle = new Paddle((game.java.main.GameJframe.SCREEN_WIDTH / 2) - 75,
                700, 150, 30);
        ball = new Ball(100, 100, 30, 30, 1 / Math.sqrt(2), 1 / Math.sqrt(2), 8);
    }

    @Test
    void testExpandPaddlePowerUp() {
        ExpandPaddlePowerUp pu = new ExpandPaddlePowerUp(0,0);
        int initialWidth = paddle.getWidth();

        pu.activate(gm, paddle, ball);
        assertTrue(pu.active);
        assertEquals(Math.min(initialWidth + 50, 300), paddle.getWidth());

        pu.removeEffect(gm, paddle, ball);
        assertEquals(initialWidth, paddle.getWidth());
    }

    @Test
    void testShrinkPaddlePowerUp() {
        ShrinkPaddlePowerUp pu = new ShrinkPaddlePowerUp(0,0);
        int initialWidth = paddle.getWidth();

        pu.activate(gm, paddle, ball);
        assertTrue(pu.active);
        assertEquals(Math.max(initialWidth - 30, 50), paddle.getWidth());

        pu.removeEffect(gm, paddle, ball);
        assertEquals(initialWidth, paddle.getWidth());
    }

    @Test
    void testExtraLifePowerUp() {
        ExtraLifePowerUp pu = new ExtraLifePowerUp(0,0);
        int initialLives = gm.getLives();

        pu.activate(gm, paddle, ball);
        assertTrue(pu.active);
        assertEquals(initialLives + 1, gm.getLives());
    }

    @Test
    void testQuickPaddlePowerUp() {
        QuickPaddlePowerUp pu = new QuickPaddlePowerUp(0,0);
        int initialSpeed = paddle.getSpeed();

        pu.activate(gm, paddle, ball);
        assertTrue(pu.active);
        assertEquals(initialSpeed + 8, paddle.getSpeed());

        pu.removeEffect(gm, paddle, ball);
        assertEquals(initialSpeed, paddle.getSpeed());
    }

    @Test
    void testQuickBallPowerUp() {
        QuickBallPowerUp pu = new QuickBallPowerUp(0,0);
        int initialSpeed = ball.getSpeed();

        pu.activate(gm, paddle, ball);
        assertTrue(pu.active);
        assertEquals(initialSpeed + 8, ball.getSpeed());

        pu.removeEffect(gm, paddle, ball);
        assertEquals(initialSpeed, ball.getSpeed());
    }

    @Test
    void testUpdatePosition() {
        ExpandPaddlePowerUp pu = new ExpandPaddlePowerUp(10, 20);
        double initialY = pu.y;
        pu.update();
        assertEquals(initialY + pu.speedY, pu.y);
    }

    @Test
    void testIsExpired() throws InterruptedException {
        ExpandPaddlePowerUp pu = new ExpandPaddlePowerUp(0,0);
        pu.activate(gm, paddle, ball);

        assertFalse(pu.isExpired());
        Thread.sleep(pu.duration + 100);
        assertTrue(pu.isExpired());
    }
}
