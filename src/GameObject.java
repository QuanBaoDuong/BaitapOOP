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
}
