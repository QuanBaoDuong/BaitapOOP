import java.awt.*;

public class Brick extends GameObject {
    protected int hitPoints;
    protected String type;

    Brick (int x ,int y,int width,int height) {
        super(x,y,width,height);
    }
   /* public void takeHit(){

    }
    public void isDestroyed() {

    }*/
    public void render(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }
    public void update(){

    }
}
/*public class NormalBrick extends Brick {

}*/

