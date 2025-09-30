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

            if(x+width>=GameJframe.SCREEN_WIDTH){
                directionX*=-1;
            }
            if(x<=0) {
                directionX*=-1;
            }
            if(y<=0) {
                directionY*=-1;
            }
            if(y+height>=GameJframe.SCREEN_HEIGHT) {
                directionY*=-1;
            }

    }

    public void render(Graphics g) {
        g.drawImage(Ball_image,x,y,width,height,null);
    }

    public void update() {
            move();
    }
    public void bounceOff(GameObject other) {
            Rectangle ballRect = this.getBounds();
            Rectangle otherRect = other.getBounds();

            if(!ballRect.intersects(otherRect)) return ;

            Rectangle intersection = ballRect.intersection(otherRect);
            if (intersection.width<intersection.height) {
                directionX*=-1;
            } else if (intersection.width>intersection.height) {
                directionY*=-1;
            }
            else {
                directionX*=-1;
                directionY*=-1;
            }
    }

}
