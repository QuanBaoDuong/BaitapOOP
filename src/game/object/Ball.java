package game.object;

import game.main.GameJframe;

import javax.swing.*;
import java.awt.*;

public class Ball extends MovableObject {
    private double directionX;
    private double directionY;
    private boolean hasBouncedThisFrame = false;
    private int speed;
    private Image Ball_image;

    public Ball(int x, int y, int width, int height, double directionX, double directionY, int speed) {
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
        y+=speed*directionY;
        if(x+width>= GameJframe.SCREEN_WIDTH){
            x = GameJframe.SCREEN_WIDTH - width;
            directionX*=-1;
        }
        if(x<=0) {
            x = 0;
            directionX*=-1;
        }
        if(y<=0) {
            y = 0;
            directionY*=-1;
        }
           /* if(y+height>=game.main.GameJframe.SCREEN_HEIGHT) {
                directionY*=-1;
            }*/

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
        hasBouncedThisFrame = false;
    }
    public boolean hasBounced() {
        return hasBouncedThisFrame;
    }
    public void setHasBounced(boolean value) {
        hasBouncedThisFrame = value;
    }
    public void bounceOff(GameObject other) {
        Rectangle ballRect = this.getBounds();
        Rectangle otherRect = other.getBounds();

        if(!ballRect.intersects(otherRect)) return ;

        Rectangle intersection = ballRect.intersection(otherRect);

        if (other instanceof Paddle) {

            // Giả sử paddle nằm ngang, ưu tiên phản xạ theo mặt va chạm
            if (y + height <= other.y + intersection.height) {
                // Va mặt trên của paddle → dùng góc phản xạ
                double ballCenterX = this.x + this.width / 2.0;
                double paddleCenterX = other.x + other.width / 2.0;
                double relativeIntersect = (ballCenterX - paddleCenterX) / (other.width / 2.0);
                relativeIntersect = Math.max(-1.0, Math.min(1.0, relativeIntersect)); // Clamp

                double maxBounceAngle = Math.toRadians(75 );
                double bounceAngle = relativeIntersect * maxBounceAngle;

                directionX = Math.sin(bounceAngle);
                directionY = -Math.cos(bounceAngle); // luôn đi lên

                // Chuẩn hóa
                double len = Math.sqrt(directionX * directionX + directionY * directionY);
                directionX /= len;
                directionY /= len;
                if (Math.abs(directionX) < 0.1) {
                    directionX = (directionX >= 0 ? 0.1 : -0.1);
                }
            } else {
                // Va cạnh bên trái hoặc phải của paddle → phản xạ ngang
                directionX *= -1;
            }

        }
        else {
            if (intersection.width < intersection.height) {
                if (x < other.x) x -= intersection.width;
                else x += intersection.width;
                directionX *= -1;
            } else if (intersection.width > intersection.height) {
                if (y < other.y) y -= intersection.height;
                else y += intersection.height;
                directionY *= -1;
            } else {
                // Va góc
                if (x < other.x) x -= intersection.width;
                else x += intersection.width;
                if (y < other.y) y -= intersection.height;
                else y += intersection.height;
                directionX *= -1;
                directionY *= -1;
            }


        }
    }
    public boolean checkCollision(GameObject other) {
        Rectangle ballBounds = this.getBounds();
        Rectangle otherBounds = other.getBounds();
        return ballBounds.intersects(otherBounds);
    }

    public void setSpeed(int speed) {
        if(this.speed>=40){
            this.speed=40;
        }
        this.speed=speed;
    }
    public double getDirectionX() {
        return directionX;
    }
    public void setDirectionX(double directionX) {
        this.directionX = directionX;
    }

    public double getDirectionY() {
        return directionY;
    }

    public void setDirectionY(double directionY) {
        this.directionY = directionY;
    }

    public int getSpeed() {
        return speed;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
