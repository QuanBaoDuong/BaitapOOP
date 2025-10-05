
import java.awt.*;
import javax.swing.*;

public class Paddle extends MovableObject {
    private boolean moveLeft, moveRight;
    private Image paddleImage;

    public Paddle(int x, int y,int speed) {
        super(x, y, 200, 40, 0, 0);

        try {
            Image original = new ImageIcon(getClass().getResource("/image/paddle.png")).getImage();
            paddleImage = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            paddleImage = null;
        }
    }

    @Override
    public void move() {
        if (moveLeft && x > 0) x -= 8;
        if (moveRight && x < GameJframe.SCREEN_WIDTH - width) x += 8;
    }

    @Override
    public void render(Graphics g) {
        if (paddleImage != null) {
            g.drawImage(paddleImage, x, y, width, height, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, width, height);
        }
    }

    @Override
    public void update() {
        move();
    }

    // THÊM METHOD getY() Ở ĐÂY
    public int getY() {
        return y;
    }

    public void setMoveLeft() {
        x-=20;
    }

    public void setMoveRight() {
        x+=20;
    }
}

