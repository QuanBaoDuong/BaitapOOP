import javax.swing.*;
import java.awt.*;


public class Brick extends GameObject {

    private Image brickImage;
    private boolean destroyed = false;

    public Brick(int x, int y, int width, int height) {
        super(x, y, width, height);
        // load ảnh
        brickImage = new ImageIcon(getClass().getResource("/image/brick_red.png")).getImage();

    }

    @Override
    public void render(Graphics g) {
        if (!destroyed) {
            g.drawImage(brickImage, x, y, width, height, null);
        }
    }

    @Override
    public void update() {
        // gạch không di chuyển
    }

    public void takeHit() {
        destroyed = true; // 1 hit vỡ
    }

    public boolean isDestroyed() {
        return destroyed;
    }
    public static Brick[][] createMap(int rows, int cols, int brickWidth, int brickHeight) {
        Brick[][] bricks = new Brick[rows][cols];

        int startX = 0;
        int startY = 100;
        int hGap = 0;
        int vGap = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = startX + col * (brickWidth + hGap);
                int y = startY + row * (brickHeight + vGap);
                bricks[row][col] = new Brick(x, y, brickWidth, brickHeight);
            }
        }

        return bricks;
    }
}
