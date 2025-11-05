package game.object;

import javax.swing.*;
import java.awt.*;


public abstract class Brick extends GameObject {

    private Image brickImage;
    protected boolean destroyed = false;
    private boolean breakable;

    public Brick(int x, int y, int width, int height, String imagePath, boolean breakable) {
        super(x, y, width, height);
        brickImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        this.breakable=breakable;
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

    public abstract void takeHit();

    public boolean isDestroyed() {
        return destroyed;
    }
    public boolean isBreakable() {
        return breakable; // hàm getter
    }
}
