package game.manager;

import game.main.GameJframe;
import game.map.Map;
import game.object.*;
import game.object.PowerUp;
import game.sound.Sound;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameManager {
    private Ball ball;
    private Paddle paddle;
    private List<Brick> bricks = new ArrayList<>();
    private final List<PowerUp> powerUps = new ArrayList<>();

    private int lives;
    private int score;
    private int currentLevel = 1;
    private final int maxLevel = 5;

    private boolean levelComplete = false;
    private boolean outOfLives = false;

    public GameManager() {
        restartGame();
    }

    // --- game lifecycle ---
    public void restartGame() {
        currentLevel = 1;
        score = 0;
        lives = 3;
        outOfLives = false;
        levelComplete = false;
        powerUps.clear();
        loadLevel(currentLevel);
        resetPositions();
    }

    public void loadLevel(int level) {
        // load map and reset powerups for the level
        String path = "/FileDesignMap/MapLv" + level + ".txt";
        InputStream is = getClass().getResourceAsStream(path);
        if (is != null) {
            bricks = Map.createMap(is, 100, 60, 0, 100);
        } else {
            bricks = new ArrayList<>();
        }
        powerUps.clear();
        resetPositions(); // place ball/paddle suitably for level
        levelComplete = false;
    }

    public void nextLevel() {
        currentLevel++;
        if (currentLevel > maxLevel) {
            // reaching beyond max level considered victory;
            // PlayState will check gameManager.isLevelComplete() and then set isGameWin
            levelComplete = false;
        } else {
            loadLevel(currentLevel);
        }
    }

    private void resetPositions() {
        // Ball placed above paddle center, speed default
        paddle = new Paddle( (GameJframe.SCREEN_WIDTH / 2) - 75, 700, 150, 30);
        ball = new Ball(paddle.getX() + paddle.getWidth() / 2 - 15,
                paddle.getY() - 30,
                30, 30, 1 / Math.sqrt(2), 1 / Math.sqrt(2), 8);
    }

    // --- main update ---
    public void update() {
        if (outOfLives || levelComplete) return;

        ball.update();
        paddle.update();

        checkBallOutOfBounds();
        checkPaddleCollision();
        checkBrickCollisions();
        updatePowerUps();
        checkLevelComplete();
    }

    private void checkBallOutOfBounds() {
        if (ball.getY() > GameJframe.SCREEN_HEIGHT) {
            lives--;
            if (lives <= 0) {
                outOfLives = true;
                Sound.playSound("gameover.wav", false);
            } else {
                resetBallPosition();
            }
        }
    }

    private void resetBallPosition() {
        ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
        ball.setY(paddle.getY() - ball.getHeight());
        ball.setSpeed(8);
        ball.setDirectionX(1);
        ball.setDirectionY(1);
    }

    private void checkPaddleCollision() {
        if (ball.checkCollision(paddle)) {
            ball.bounceOff(paddle);
        }
    }

    private void checkBrickCollisions() {
        Brick nearest = null;
        int maxArea = -1;
        for (Brick b : bricks) {
            if (!b.isDestroyed() && ball.checkCollision(b)) {
                Rectangle inter = ball.getBounds().intersection(b.getBounds());
                int area = inter.width * inter.height;
                if (area > maxArea) {
                    maxArea = area;
                    nearest = b;
                }
            }
        }

        if (nearest != null) {
            ball.bounceOff(nearest);
            nearest.takeHit();
            ball.setHasBounced(true);

            // nudge ball a bit so it doesn't sticky-collide
            ball.setX((int) (ball.getX() + ball.getDirectionX() * 2));
            ball.setY((int) (ball.getY() + ball.getDirectionY() * 2));

            Sound.playSound("break.wav", false);

            if (nearest.isDestroyed()) {
                score += 100;
                PowerUp pu = PowerUp.generateFromBrick(nearest);
                if (pu != null) powerUps.add(pu);
            }
        }
    }

    private void updatePowerUps() {
        Iterator<PowerUp> it = powerUps.iterator();
        while (it.hasNext()) {
            PowerUp p = it.next();
            p.update();

            if (p.getBounds().intersects(paddle.getBounds())) {
                p.activate(this, paddle, ball);
                Sound.playSound("powerup.wav", false);
                it.remove();
                continue;
            }
            if (p.getY() > GameJframe.SCREEN_HEIGHT) {
                it.remove();
            }
        }
    }

    private void checkLevelComplete() {
        // only consider breakable bricks for completion
        boolean allBreakableDestroyed = true;
        for (Brick b : bricks) {
            if (b.isBreakable() && !b.isDestroyed()) {
                allBreakableDestroyed = false;
                break;
            }
        }
        if (allBreakableDestroyed) {
            levelComplete = true;
        }
    }

    // --- utility setters / getters used by PlayState / PowerUps ---
    public void setLives(int newLives) {
        this.lives = Math.max(0, newLives);
    }

    public Ball getBall() { return ball; }
    public Paddle getPaddle() { return paddle; }
    public List<Brick> getBricks() { return bricks; }
    public List<PowerUp> getPowerUps() { return powerUps; }
    public int getLives() { return lives; }
    public int getScore() { return score; }
    public int getCurrentLevel() { return currentLevel; }
    public int getMaxLevel() { return maxLevel; }

    public boolean isOutOfLives() { return outOfLives; }
    public boolean isLevelComplete() { return levelComplete; }

    // key control passthrough
    public void handleKeyPressed(int keyCode) {
        if (keyCode == java.awt.event.KeyEvent.VK_LEFT) paddle.setMoveLeft(true);
        else if (keyCode == java.awt.event.KeyEvent.VK_RIGHT) paddle.setMoveRight(true);
    }

    public void handleKeyReleased(int keyCode) {
        if (keyCode == java.awt.event.KeyEvent.VK_LEFT) paddle.setMoveLeft(false);
        else if (keyCode == java.awt.event.KeyEvent.VK_RIGHT) paddle.setMoveRight(false);
    }
}
