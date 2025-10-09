import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class PlayState implements GameState {

    private Ball ball;
    private Paddle paddle;
    private Brick[][] bricks;
    private java.util.List<PowerUp> powerUps = new ArrayList<>();

    private boolean isGameOver = false;

    private Image background;
    private Image gameOverImage;
    private JPanel panel;
    private GameStateManager gameStateManager;

    public PlayState(JPanel panel, GameStateManager gameStateManager) {
        this.panel = panel;
        this.gameStateManager = gameStateManager;
        background = new ImageIcon(getClass().getResource("/image/BackGroundgame.png")).getImage();
        gameOverImage = new ImageIcon(getClass().getResource("/image/GameOver.png")).getImage();
        resetGame();
    }

    // === Khởi tạo lại game ===
    public void resetGame() {
        ball = new Ball(400, 400, 30, 30, 1, 1, 8);
        paddle = new Paddle(400, 700);
        bricks = Map1.createMap(5, 10, 100, 60);
        powerUps.clear();
        isGameOver = false;
    }

    // === Cập nhật logic game ===
    @Override
    public void update() {
        if (isGameOver) return;

        ball.update();
        paddle.update();

        if (ball.y > GameJframe.SCREEN_HEIGHT) {
            isGameOver = true;
            return;
        }

        // Va chạm paddle
        if (ball.checkCollision(paddle)) {
            ball.bounceOff(paddle);
        }

        // Va chạm brick
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
            ball.x += ball.getDirectionX() * 2;
            ball.y += ball.getDirectionY() * 2;

            PowerUp newPower = PowerUp.generateFromBrick(nearestBrick);
            if (newPower != null) powerUps.add(newPower);
        }

        // Cập nhật powerup
        Iterator<PowerUp> it = powerUps.iterator();
        while (it.hasNext()) {
            PowerUp p = it.next();
            p.update();
            if (p.getBounds().intersects(paddle.getBounds())) {
                p.activate(paddle, ball);
                it.remove();
            } else if (p.y > GameJframe.SCREEN_HEIGHT) {
                it.remove();
            }
        }
    }

    // Vẽ game
    @Override
    public void draw(Graphics g) {
        g.drawImage(background, 0, 0, null);

        ball.render(g);
        paddle.render(g);

        for (Brick[] row : bricks)
            for (Brick b : row)
                if (!b.isDestroyed())
                    b.render(g);

        for (PowerUp p : powerUps)
            p.render(g);

        if (isGameOver) {
            g.drawImage(
                    gameOverImage,
                    (GameJframe.SCREEN_WIDTH - gameOverImage.getWidth(null)) / 2,
                    (GameJframe.SCREEN_HEIGHT - gameOverImage.getHeight(null)) / 2,
                    null
            );

            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.setColor(Color.WHITE);
            g.drawString("Press ENTER to Restart",
                    (GameJframe.SCREEN_WIDTH / 2) - 180,
                    (GameJframe.SCREEN_HEIGHT / 2) + 300);
        }
    }

    // === Xử lý phím nhấn ===
    @Override
    public void keyPressed(int keyCode) {
        if (isGameOver && keyCode == KeyEvent.VK_ENTER) {
            resetGame();
            return;
        }

        if (keyCode == KeyEvent.VK_LEFT) paddle.setMoveLeft(true);
        else if (keyCode == KeyEvent.VK_RIGHT) paddle.setMoveRight(true);
    }

    // === Xử lý phím thả ===
    @Override
    public void keyReleased(int keyCode) {
        if (keyCode == KeyEvent.VK_LEFT) paddle.setMoveLeft(false);
        else if (keyCode == KeyEvent.VK_RIGHT) paddle.setMoveRight(false);
    }
}
