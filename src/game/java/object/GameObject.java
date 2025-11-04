package game.object;

import java.awt.*;

public abstract class GameObject {
    protected int x ,y,width,height;

    public GameObject(int x,int y,int width,int height) {
        this.x =x;
        this.y =y;
        this.width = width;
        this.height = height;
    }
    public abstract void render(Graphics g);

    public abstract void update();

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
