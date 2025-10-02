import java.awt.*;

public class PowerUp extends GameObject {
    public int speedY;
    private Color color;
    private String type;
    public PowerUp(int x, int y, int width, int height, int speedY, String type) {
        super(x, y, width, height); // gọi constructor cha
        this.speedY = speedY;
        this.type = type;
        this.color = Color.RED;
    }

    public void update() {
        this.y += speedY;
    }
    @Override
    public void render(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    public String getType() {
        return type;
    }
}
