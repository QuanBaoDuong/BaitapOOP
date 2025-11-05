package game.object;

import game.java.main.GameJframe;
import javax.swing.*;
import java.awt.*;

public class Paddle extends MovableObject {
    private int speed = 16;
    private boolean moveLeft, moveRight;
    private Image paddleImage;

    public Paddle(int x, int y, int width, int height) {
        super(x, y, width, height, 0, 0);

        // Thử tải ảnh paddle, nếu không có thì sẽ tô màu xanh dương
        try {
            Image original = new ImageIcon(getClass().getResource("/image/paddle.png")).getImage();
            paddleImage = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            paddleImage = null;
            System.out.println("Không tìm thấy ảnh paddle, dùng màu xanh dương thay thế.");
        }
    }

    @Override
    public void update() {
        move();
    }

    @Override
    public void move() {
        if (moveLeft && x > 0) x -= speed;
        if (moveRight && x < GameJframe.SCREEN_WIDTH - width-3) x += speed;
    }

    @Override
    public void render(Graphics g) {
        if (paddleImage != null) {
            g.drawImage(paddleImage, x, y, width, height, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRoundRect(x, y, width, height, 10, 10);
        }
    }

    //  Gọi trong game.main.GameJpanel keyPressed / keyReleased
    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }

    public void setSpeed(int speed) {
        if (speed >= 30) {
            this.speed = 30;
        } else {
            this.speed = speed;
        }
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSpeed() {
        return speed;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
