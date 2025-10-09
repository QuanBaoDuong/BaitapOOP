import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GameJpanel extends JPanel implements ActionListener, KeyListener {
    private Image backGroundGame = new ImageIcon(getClass().getResource("/image/BackGroundgame.png")).getImage();
    private Ball ball = new Ball(400, 400, 40, 40, 1, 1, 8);
    private Paddle paddle = new Paddle(400, 700);
    Brick[][] bricks = Map1.createMap(5, 10, 100, 60);
    Random rand = new Random();
    private javax.swing.Timer timer;
    private java.util.List<PowerUp> powerUps = new ArrayList<>();
    private java.util.List<PowerUp> activePowerUps = new ArrayList<>();

    public GameJpanel() {
        setFocusable(true);
        addKeyListener(this);
        timer = new javax.swing.Timer(16, this); // ~60 FPS
        timer.start();
    }

    public Dimension getPreferredSize() {
        return new Dimension(GameJframe.SCREEN_WIDTH, GameJframe.SCREEN_HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backGroundGame, 0, 0, null);
        ball.render(g);
        paddle.render(g);

        for (Brick[] row : bricks) {
            for (Brick b : row) {
                if (!b.isDestroyed()) {
                    b.render(g);
                }
            }
        }

        for (PowerUp p : powerUps) {
            p.render(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ball.update();
        paddle.update();

        if (ball.checkCollision(paddle)) {
            ball.bounceOff(paddle);
        }

        Brick nearestBrick = null;
        int minArea = Integer.MAX_VALUE;

        for (Brick[] row : bricks) {
            for (Brick b : row) {
                if (!b.isDestroyed() && ball.checkCollision(b)) {
                    Rectangle inter = ball.getBounds().intersection(b.getBounds());
                    int area = inter.width * inter.height;
                    if (area < minArea) {
                        minArea = area;
                        nearestBrick = b;
                    }
                }
            }
        }

        if (nearestBrick != null) {
            ball.bounceOff(nearestBrick);
            nearestBrick.takeHit();

            PowerUp newPower = PowerUp.generateFromBrick(nearestBrick);
            if (newPower != null) powerUps.add(newPower);
        }

        Iterator<PowerUp> iterator = powerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp p = iterator.next();
            p.update();

            if (p.getBounds().intersects(paddle.getBounds())) {
                p.activate(paddle, ball);
                activePowerUps.add(p);
                iterator.remove();
            } else if (p.y > GameJframe.SCREEN_HEIGHT) {
                iterator.remove();
                repaint();
            }
        }
        Iterator<PowerUp> activeIterator = activePowerUps.iterator();
        while (activeIterator.hasNext()) {
            PowerUp p = activeIterator.next();
            if (p.isExpired()) {
                p.removeEffect(paddle, ball);
                activeIterator.remove();
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) paddle.setMoveLeft(true);
        else if (key == KeyEvent.VK_RIGHT) paddle.setMoveRight(true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) paddle.setMoveLeft(false);
        else if (key == KeyEvent.VK_RIGHT) paddle.setMoveRight(false);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
