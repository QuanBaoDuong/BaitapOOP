import javax.swing.*;
import java.awt.*;

public class Ball extends MovableObject{
        private int directionX;
        private int directionY;
        private int Speed;
        private Image Ball_image;
        public Ball (int x,int y,int width,int height,int directionX,int directionY,int Speed) {
            super(x,y,width,height,0,0);
            this.directionX=directionX;
            this.directionY=directionY;
            this.Speed = Speed;
            Image original = new ImageIcon(Ball.class.getResource("/image/ball.png")).getImage();
            Ball_image = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        }

    public void move() {
            x+=Speed*directionX;
            y+=Speed*directionY;
            if(x+1.5*width>=GameJframe.SCREEN_WIDTH){
                directionX*=-1;
            }
            if(x<=0) {
                directionX*=-1;
            }
            if(y<=0) {
                directionY*=-1;
            }
            if(y+2*width>=GameJframe.SCREEN_HEIGHT) {
                directionY*=-1;
            }
    }

    public void render(Graphics g) {
        g.drawImage(Ball_image,x,y,width,height,null);
    }

    public void update() {
            move();
    }
}
