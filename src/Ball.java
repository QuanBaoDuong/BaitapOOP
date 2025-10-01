import java.awt.*;
import javax.swing.*;

public class Ball extends MovableObject {
    private int directionX;
    private int directionY;
    private int Speed;
    private Image Ball_image;

    public Ball(int x, int y, int width, int height, int directionX, int directionY, int Speed) {
        super(x, y, width, height, 0, 0);
        this.directionX = directionX;
        this.directionY = directionY;
        this.Speed = Speed;

        try {
            Image original = new ImageIcon(getClass().getResource("/image/ball.png")).getImage();
            Ball_image = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            Ball_image = null;
        }
    }

    @Override
    public void move() {
        x += Speed * directionX;
        y += Speed * directionY;
    }

    @Override
    public void render(Graphics g) {
        if (Ball_image != null) {
            g.drawImage(Ball_image, x, y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillOval(x, y, width, height);
        }
    }

    @Override
    public void update() {
        move();
    }

    public void reverseX() { directionX *= -1; }
    public void reverseY() { directionY *= -1; }
    public void setY(int y) { this.y = y; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
