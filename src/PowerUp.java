import java.awt.*;
import java.util.Random;

public abstract class PowerUp extends GameObject {
    protected double speedY = 2;
    protected String type;
    protected boolean active;
    protected long duration; // thời gian hiệu lực
    protected long startTime;

    public PowerUp(int x, int y, int width, int height, String type, long duration) {
        super(x, y, width, height);
        this.type = type;
        this.duration = duration;
    }

    public abstract void applyEffect(Paddle paddle, Ball ball);
    public abstract void removeEffect(Paddle paddle, Ball ball);

    @Override
    public void update() {
        y += speedY;
    }

    public void activate(Paddle paddle, Ball ball) {
        if (!active) {
            applyEffect(paddle, ball);
            active = true;
            startTime = System.currentTimeMillis();
        }
    }

    public boolean isExpired() {
        return active && duration > 0 && System.currentTimeMillis() - startTime > duration;
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        draw(g2d);
    }

    public abstract void draw(Graphics2D g2d);

    public static PowerUp generateFromBrick(Brick brick) {
        Random rand = new Random();
        if (rand.nextInt(100) < 15) { // 15% rơi
            int x = brick.x + brick.width / 2 - 10;
            int y = brick.y + brick.height / 2 - 10;
            switch (rand.nextInt(5)) {
                case 0: return new ExpandPaddlePowerUp(x, y);
                case 1: return new ShrinkPaddlePowerUp(x, y);
                case 2: return new ExtraLifePowerUp(x, y);
                case 3: return new QuickPaddlePowerUp(x, y);
                case 4: return new QuickBallPowerUp(x, y);
            }
        }
        return null;
    }
}

// ==================== Các lớp con ====================

// Làm dài Paddle
class ExpandPaddlePowerUp extends PowerUp {
    private int expandAmount = 50;

    public ExpandPaddlePowerUp(int x, int y) {
        super(x, y, 20, 20, "expand", 8000);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        paddle.width += expandAmount;
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        paddle.width -= expandAmount;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.GREEN);
        g2d.fillOval(x, y, width, height);
        g2d.setColor(Color.BLACK);
        g2d.drawString("E", x + 6, y + 14);
    }
}

// Làm ngắn Paddle
class ShrinkPaddlePowerUp extends PowerUp {
    private int shrinkAmount = 30;

    public ShrinkPaddlePowerUp(int x, int y) {
        super(x, y, 20, 20, "shrink", 8000);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        paddle.width -= shrinkAmount;
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        paddle.width += shrinkAmount;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.ORANGE);
        g2d.fillOval(x, y, width, height);
        g2d.setColor(Color.BLACK);
        g2d.drawString("S", x + 6, y + 14);
    }
}

// Thêm mạng
class ExtraLifePowerUp extends PowerUp {
    public ExtraLifePowerUp(int x, int y) {
        super(x, y, 20, 20, "extraLife", 0);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        // Giả sử có hàm tăng mạng trong paddle/game
        // paddle.getGamePanel().increaseLives();
        System.out.println("Extra life!");
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        // Không cần
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.PINK);
        g2d.fillOval(x, y, width, height);
        g2d.setColor(Color.BLACK);
        g2d.drawString("+", x + 8, y + 14);
    }
}

// Tăng tốc Paddle
class QuickPaddlePowerUp extends PowerUp {
    public QuickPaddlePowerUp(int x, int y) {
        super(x, y, 20, 20, "quickpaddle", 1000);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        paddle.speed *= 2;
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        paddle.speed /= 2;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.CYAN);
        g2d.fillOval(x, y, width, height);
        g2d.setColor(Color.BLACK);
        g2d.drawString("P", x + 6, y + 14);
    }
}

// Tăng tốc bóng
class QuickBallPowerUp extends PowerUp {
    public QuickBallPowerUp(int x, int y) {
        super(x, y, 20, 20, "quickball", 8000);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        ball.setSpeed(ball.getSpeed()*2);
    }

    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        ball.setSpeed(ball.getSpeed() /2);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.fillOval(x, y, width, height);
        g2d.setColor(Color.WHITE);
        g2d.drawString("B", x + 6, y + 14);
    }
}
