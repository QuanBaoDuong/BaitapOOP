import java.awt.*;
import javax.swing.*;

public class Ball extends MovableObject {
    private int directionX;
    private int directionY;
    private int speed;
    private Image Ball_image;

    public Ball(int x, int y, int width, int height, int directionX, int directionY, int speed) {
        super(x, y, width, height, 0, 0);
        this.directionX = directionX;
        this.directionY = directionY;
        this.speed = speed;

        try {
            Image original = new ImageIcon(getClass().getResource("/image/ball.png")).getImage();
            Ball_image = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            Ball_image = null;
        }
    }

    @Override
    public void move() {
            x+=speed*directionX;
        System.out.print(x+" ");
            y+=speed*directionY;
        System.out.print(y);
        System.out.println();
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
    public void bounceOff(GameObject other) {
            Rectangle ballRect = this.getBounds();
            Rectangle otherRect = other.getBounds();

            if(!ballRect.intersects(otherRect)) return ;

            Rectangle intersection = ballRect.intersection(otherRect);

            if (other instanceof Brick) {
                float paddleTop = other.y;
                float ballBottom = this.y+this.height;
                int someThreshold = Math.max(5, speed);

                if (ballBottom>=paddleTop ) {
                    float ballCenterX = this.x+this.width/2f;
                    float paddleCenterX = other.x+other.width/2f;
                    float relativeIntersectX = (ballCenterX-paddleCenterX)/(other.width/2f);
                    if (relativeIntersectX<=-1) relativeIntersectX=-1;
                    else if (relativeIntersectX>=1) relativeIntersectX=1;

                    double maxBounceAngle = Math.toRadians(60);
                    double bounceAngle = relativeIntersectX * maxBounceAngle;

                    x += (int)(speed *Math.sin(bounceAngle));
                    y += -(int)(speed *Math.cos(bounceAngle));
                }
                else {
                    if (intersection.width<intersection.height) {
                        directionX*=-1;
                    }
                    else if (intersection.width>intersection.height){
                        directionY*=-1;
                    }
                    else {
                        directionX*=-1;
                        directionY*=-1;
                    }
                }

            }
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
    public boolean checkCollision(GameObject other) {
            Rectangle ballBounds = this.getBounds();
            Rectangle otherBounds = other.getBounds();
            return ballBounds.intersects(otherBounds);
    }

    public void setSpeed(int speed) {
        this.speed=speed;
    }

}
