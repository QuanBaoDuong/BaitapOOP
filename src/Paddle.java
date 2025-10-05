import java.awt.*;
import javax.swing.*;

public class Paddle extends MovableObject {
    private int speed = 8;
    private Image paddleImage;
    private boolean moveLeft, moveRight;

    public Paddle(int x, int y) {
        super(x, y, 100, 20, 0, 0);
        Image original = new ImageIcon(getClass().getResource("/image/paddle.png")).getImage();
        paddleImage = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    @Override
    public void move() {
        if (moveLeft && x > 0) x -= speed;
        if (moveRight && x < GameJframe.SCREEN_WIDTH - width) x += speed;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(paddleImage, x, y, width, height, null);
    }

    @Override
    public void update() {
        move();
    }

    public int getY() { return y; }

    public void setMoveLeft(boolean moveLeft) { this.moveLeft = moveLeft; }

    public void setMoveRight(boolean moveRight) { this.moveRight = moveRight; }

    public void setWidth(int width) {
        this.width = width;
        updatePaddleImage();
    }

    public void setSpeed(int speed) { this.speed = speed; }

    public void applyPowerUp(PowerUp powerUp) {
        if (powerUp != null) {
            powerUp.applyToPaddle(this);
        }
    }

    private void updatePaddleImage() {
        Image original = new ImageIcon(getClass().getResource("/image/paddle.png")).getImage();
        paddleImage = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
}
