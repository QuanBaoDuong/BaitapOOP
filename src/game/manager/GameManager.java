package game.manager;

import game.main.GameJframe;
import game.map.Map1;
import game.object.Ball;
import game.object.Brick;
import game.object.Paddle;
import game.object.PowerUp;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameManager {
    private Ball ball;
    private Paddle paddle;
    private Brick[][] bricks;
    private List<PowerUp> powerUps = new ArrayList<>();

    private boolean isGameOver = false;

    public GameManager() {
        reset();
    }

    public void reset() {
        ball = new Ball(400, 400, 30, 30, 1, 1, 8);
        paddle = new Paddle(400, 700);
        bricks = Map1.createMap(5, 10, 100, 60);
        powerUps.clear();
        isGameOver = false;
    }

    public void update() {
        if (isGameOver) return;

        ball.update();
        paddle.update();

        // Game over nếu bóng rơi ra ngoài
        if (ball.getY() > GameJframe.SCREEN_HEIGHT) {
            isGameOver = true;
            return;
        }

        // game.object.Paddle collision
        if (ball.checkCollision(paddle)) {
            ball.bounceOff(paddle);
        }

        // game.object.Brick collision
        Brick nearestBrick = null;
        int maxArea = -1;
        for (Brick[] row : bricks) {
            for (Brick b : row) {
                if (!b.isDestroyed() && ball.checkCollision(b)) {
                    Rectangle inter = ball.getBounds().intersection(b.getBounds());
                    int area = inter.width * inter.height;
                    if (area > maxArea) {
                        maxArea = area;
                        nearestBrick = b;
                    }
                }
            }
        }

        if (nearestBrick != null) {
            ball.bounceOff(nearestBrick);
            nearestBrick.takeHit();
            ball.setHasBounced(true);
            ball.setX((int)(ball.getX() + ball.getDirectionX() * 2));
            ball.setY((int)(ball.getY() + ball.getDirectionY() * 2));


            PowerUp newPower = PowerUp.generateFromBrick(nearestBrick);
            if (newPower != null) powerUps.add(newPower);
        }

        // game.object.PowerUp update
        Iterator<PowerUp> it = powerUps.iterator();
        while (it.hasNext()) {
            PowerUp p = it.next();
            p.update();
            if (p.getBounds().intersects(paddle.getBounds())) {
                p.activate(paddle, ball);
                it.remove();
            } else if (p.getY() > GameJframe.SCREEN_HEIGHT) {
                it.remove();
            }
        }
    }

    // --- Getter & Key control ---
    public void handleKeyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_LEFT) paddle.setMoveLeft(true);
        else if (keyCode == KeyEvent.VK_RIGHT) paddle.setMoveRight(true);
    }

    public void handleKeyReleased(int keyCode) {
        if (keyCode == KeyEvent.VK_LEFT) paddle.setMoveLeft(false);
        else if (keyCode == KeyEvent.VK_RIGHT) paddle.setMoveRight(false);
    }

    public Ball getBall() { return ball; }
    public Paddle getPaddle() { return paddle; }
    public Brick[][] getBricks() { return bricks; }
    public List<PowerUp> getPowerUps() { return powerUps; }
    public boolean isGameOver() { return isGameOver; }
}
