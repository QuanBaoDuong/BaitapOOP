package game.object;

import game.manager.GameManager;
import game.manager.GameStateManager;

import java.awt.*;
import java.util.Random;

/**
 * Lớp trừu tượng đại diện cho một Power-Up (vật phẩm rơi ra từ Brick).
 */
public abstract class PowerUp extends GameObject {
    protected double speedY = 2;
    protected String type;
    protected boolean active = false;
    protected long duration; // thời gian hiệu lực (ms)
    protected long startTime;

    public PowerUp(int x, int y, int width, int height, String type, long duration) {
        super(x, y, width, height);
        this.type = type;
        this.duration = duration;
    }

    // Các phương thức bắt buộc
    public abstract void applyEffect(GameManager gm, Paddle paddle, Ball ball);
    public abstract void removeEffect(GameManager gm, Paddle paddle, Ball ball);
    public abstract void draw(Graphics2D g2d);

    @Override
    public void update() {
        y += speedY;
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        draw(g2d);
    }

    public void activate(GameManager gm, Paddle paddle, Ball ball) {
        if (!active) {
            applyEffect(gm, paddle, ball);
            active = true;
            startTime = System.currentTimeMillis();
        }
    }

    public boolean isExpired() {
        return active && duration > 0 && System.currentTimeMillis() - startTime > duration;
    }

    public String getType() {
        return type;
    }

    // ================== Random sinh PowerUp ==================
    public static PowerUp generateFromBrick(Brick brick) {
        Random rand = new Random();
        // 15% tỉ lệ rơi
        if (rand.nextInt(100) < 40) {
            int x = brick.x + brick.width / 2 - 10;
            int y = brick.y + brick.height / 2 - 10;

            switch (rand.nextInt(6)) {
                case 0: return new ExpandPaddlePowerUp(x, y);
                case 1: return new ShrinkPaddlePowerUp(x, y);
                case 2: return new ExtraLifePowerUp(x, y);
                case 3: return new QuickPaddlePowerUp(x, y);
                case 4: return new QuickBallPowerUp(x, y);
                case 5: return new MultiBallPowerUp(x, y);
            }
        }
        return null;
    }
}

// ==================== Các lớp con ====================

// 1️⃣ Mở rộng paddle
class ExpandPaddlePowerUp extends PowerUp {
    private final int expandAmount = 50;

    public ExpandPaddlePowerUp(int x, int y) {
        super(x, y, 20, 20, "expand", 8000);
    }

    @Override
    public void applyEffect(GameManager gm, Paddle paddle, Ball ball) {
        paddle.width = Math.min(paddle.width + expandAmount, 300);
    }

    @Override
    public void removeEffect(GameManager gm, Paddle paddle, Ball ball) {
        paddle.width = Math.max(paddle.width - expandAmount, 50);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.GREEN);
        g2d.fillOval(x, y, width, height);
        g2d.setColor(Color.BLACK);
        g2d.drawString("E", x + 6, y + 14);
    }
}

// 2️⃣ Thu nhỏ paddle
class ShrinkPaddlePowerUp extends PowerUp {
    private final int shrinkAmount = 30;

    public ShrinkPaddlePowerUp(int x, int y) {
        super(x, y, 20, 20, "shrink", 8000);
    }

    @Override
    public void applyEffect(GameManager gm, Paddle paddle, Ball ball) {
        paddle.width = Math.max(paddle.width - shrinkAmount, 50);
    }

    @Override
    public void removeEffect(GameManager gm, Paddle paddle, Ball ball) {
        paddle.width = Math.min(paddle.width + shrinkAmount, 300);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.ORANGE);
        g2d.fillOval(x, y, width, height);
        g2d.setColor(Color.BLACK);
        g2d.drawString("S", x + 6, y + 14);
    }
}

// 3️⃣ Thêm mạng
class ExtraLifePowerUp extends PowerUp {

    public ExtraLifePowerUp(int x, int y) {
        super(x, y, 20, 20, "extraLife", 0);
    }

    @Override
    public void applyEffect(GameManager gm, Paddle paddle, Ball ball) {
        gm.setLives(gm.getLives() + 1);
    }

    @Override
    public void removeEffect(GameManager gm, Paddle paddle, Ball ball) {
        // Không cần xóa hiệu ứng
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.PINK);
        g2d.fillOval(x, y, width, height);
        g2d.setColor(Color.BLACK);
        g2d.drawString("+", x + 8, y + 14);
    }
}

// 4️⃣ Tăng tốc paddle
class QuickPaddlePowerUp extends PowerUp {
    private final int boost = 8;

    public QuickPaddlePowerUp(int x, int y) {
        super(x, y, 20, 20, "quickpaddle", 5000);
    }

    @Override
    public void applyEffect(GameManager gm, Paddle paddle, Ball ball) {
        paddle.setSpeed((int) (paddle.getSpeed() + boost));
    }

    @Override
    public void removeEffect(GameManager gm, Paddle paddle, Ball ball) {
        paddle.setSpeed((int) Math.max(paddle.getSpeed() - boost, 4));
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.CYAN);
        g2d.fillOval(x, y, width, height);
        g2d.setColor(Color.BLACK);
        g2d.drawString("P", x + 6, y + 14);
    }
}

// 5️⃣ Tăng tốc bóng
class QuickBallPowerUp extends PowerUp {
    private final int boost = 8;

    public QuickBallPowerUp(int x, int y) {
        super(x, y, 20, 20, "quickball", 8000);
    }

    @Override
    public void applyEffect(GameManager gm, Paddle paddle, Ball ball) {
        ball.setSpeed(ball.getSpeed() + boost);
    }

    @Override
    public void removeEffect(GameManager gm, Paddle paddle, Ball ball) {
        ball.setSpeed(Math.max(ball.getSpeed() - boost, 3));
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.fillOval(x, y, width, height);
        g2d.setColor(Color.WHITE);
        g2d.drawString("B", x + 6, y + 14);
    }

    public boolean isActive() {
        return active;
    }
}

class MultiBallPowerUp extends PowerUp {
    private final int extraBalls = 2;

    public MultiBallPowerUp(int x, int y) {
        super(x,y,20,20,"multiball",0);
    }

    @Override
    public void applyEffect(GameManager gm, Paddle paddle, Ball ball) {
        int currentBallcount = gm.getBalls().size();

        for (int i=0;i<currentBallcount;i++) {
            Ball orginal = gm.getBalls().get(i);

            for (int j=0;j<extraBalls;j++) {
                Ball newBall = new Ball(orginal.getX(), orginal.getY(),
                        orginal.getWidth(), orginal.getHeight(),
                        orginal.getDirectionX(),orginal.getDirectionY(),
                        orginal.getSpeed());

                double angle = Math.toRadians(new Random().nextInt(120)-60);
                double dx = orginal.getDirectionX();
                double dy = orginal.getDirectionY();

                double minDy = 0.2;
                double newDx = dx * Math.cos(angle) - dy * Math.sin(angle);
                double newDy = dx * Math.sin(angle) + dy * Math.cos(angle);

                if (Math.abs(newDy) < minDy) {
                    newDy = Math.signum(newDy) * minDy; // giữ dấu
                }

                newBall.setDirectionX(newDx);
                newBall.setDirectionY(newDy);

                gm.getBalls().add(newBall);
            }
        }
    }

    @Override
    public void removeEffect(GameManager gm, Paddle paddle, Ball ball) {}

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.MAGENTA);
        g2d.fillOval(x, y, width, height);
        g2d.setColor(Color.BLACK);
        g2d.drawString("x3", x + 6, y + 14);
    }
}
