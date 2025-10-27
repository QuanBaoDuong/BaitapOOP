package game.manager;

import game.main.GameJframe;
import game.map.Map;
import game.object.Ball;
import game.object.Brick;
import game.object.Paddle;
import game.object.PowerUp;
import game.sound.Sound;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameManager {
    private Ball ball;
    private Paddle paddle;
    private List<Brick> bricks;
    private List<PowerUp> powerUps = new ArrayList<>();
    private int lives;
    private int score;
    private int currentLevel = 1;
    private final int max_level = 2;

    private boolean isGameOver = false;
    private boolean isGameWin = false;
    private boolean levelComplete = false;

    public GameManager() {
        reset();
    }

    public void reset() {
        ball = new Ball(400, 400, 30, 30, 1 / Math.sqrt(2), 1 / Math.sqrt(2), 8);
        paddle = new Paddle(400, 700, 150, 30);
        loadLevel(currentLevel);
        powerUps.clear();
        isGameOver = false;
        lives = 3;
        score = 0;

    }

    public void restartGame() {
        currentLevel = 1;
        reset();
    }

    private void resetBall() {
        ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
        ball.setY(paddle.getY() - ball.getHeight());
        ball.setSpeed(8);
        ball.setDirectionX(1);
        ball.setDirectionY(1);
    }

    private void resetPaddle() {
        // Đặt paddle ở giữa màn hình (theo chiều ngang)
        int newX = GameJframe.SCREEN_WIDTH / 2 - paddle.getWidth() / 2;
        int newY = 700; // vị trí chuẩn của paddle, có thể điều chỉnh tùy game bạn
        paddle.setX(newX);
        paddle.setY(newY);

        // Dừng di chuyển
        paddle.setMoveLeft(false);
        paddle.setMoveRight(false);
    }

    private void loadLevel(int level) {
        String path = "/FileDesignMap/MapLv"+level+".txt";
        InputStream is = getClass().getResourceAsStream(path);
        if (is != null) {
            bricks = Map.createMap(is, 100, 60, 0, 100);
        }
        else {
            bricks = new ArrayList<>();
        }
    }

    public void update() {
        if (isGameOver) return;
        if (isGameWin) return;

        ball.update();
        paddle.update();

        // Game over nếu bóng rơi ra ngoài
        if (ball.getY() > GameJframe.SCREEN_HEIGHT) {
            lives--;
            if (lives <= 0) {
                isGameOver = true;
                Sound.playSound("gameover.wav", false);
            } else {
                resetBall();
            }
            return;
        }

        // game.object.Paddle collision
        if (ball.checkCollision(paddle)) {
            ball.bounceOff(paddle);
        }

        // game.object.Brick collision
        Brick nearestBrick = null;
        int maxArea = -1;
        for (Brick b : bricks) { // bricks là List<Brick>
            if (!b.isDestroyed() && ball.checkCollision(b)) {
                Rectangle inter = ball.getBounds().intersection(b.getBounds());
                int area = inter.width * inter.height;
                if (area > maxArea) {
                    maxArea = area;
                    nearestBrick = b;
                }
            }
        }

        if (nearestBrick != null) {
            ball.bounceOff(nearestBrick);
            nearestBrick.takeHit();
            ball.setHasBounced(true);
            ball.setX((int) (ball.getX() + ball.getDirectionX() * 2));
            ball.setY((int) (ball.getY() + ball.getDirectionY() * 2));
            Sound.playSound("break.wav", false);

            if (nearestBrick.isDestroyed()) {
                PowerUp newPower = PowerUp.generateFromBrick(nearestBrick);
                score += 100;
                if (newPower != null) powerUps.add(newPower);
            }
        }

        // game.object.PowerUp update
        Iterator<PowerUp> it = powerUps.iterator();
        while (it.hasNext()) {
            PowerUp p = it.next();
            p.update();
            if (p.getBounds().intersects(paddle.getBounds())) {
                p.activate(this,paddle, ball);
                Sound.playSound("powerup.wav", false);
                it.remove();
            } else if (p.getY() > GameJframe.SCREEN_HEIGHT) {
                it.remove();
            }
        }

        // --- Kiểm tra qua màn ---
        boolean allDestroyed = true;
        for (Brick b : bricks) {
            if (!b.isDestroyed()) {
                allDestroyed = false;
                break;
            }
        }
        if (allDestroyed) {
            levelComplete=true;
        }

    }

    public void nextLevel() {
        currentLevel++;
        if(currentLevel>max_level) {
            isGameWin=true;
        }
        else {
            loadLevel(currentLevel);
            resetBall();
            resetPaddle();
            levelComplete=false;
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


    public void drawInfo(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));

        // Hiển thị số mạng (ở góc trái)
        g.drawString("Lives: " + lives, 20, 40);

        // Hiển thị điểm (ở góc phải)
        g.drawString("Score: " + score, GameJframe.SCREEN_WIDTH - 200, 40);
    }
    public void setLives(int newLives){
        if (newLives >= 3) {
            newLives = 3;
        }
        this.lives = newLives;
    }

    public Ball getBall() {
        return ball;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isGameWin() {
        return isGameWin;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }
    public int getCurrentLevel() {
        return this.currentLevel;
    }

    public boolean isLevelComplete() {
        return levelComplete;
    }

    public void setLevelComplete(boolean levelComplete) {
        this.levelComplete = levelComplete;
    }
}
