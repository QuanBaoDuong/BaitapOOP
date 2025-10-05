import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class GameJpanel extends JPanel implements ActionListener, KeyListener {
    private Image backGroundGame = new ImageIcon(getClass().getResource("/image/BackGroundgame.png")).getImage();
    private Ball ball=new Ball(400,400,40,40,1,1,8);
    private Paddle paddle = new Paddle(400,700);
    Brick[][] bricks = Map1.createMap(5, 10, 100, 60);
    Brick brick;
    private Timer timer;

    public Dimension getPreferredSize() {
        return new Dimension(GameJframe.SCREEN_WIDTH, GameJframe.SCREEN_HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backGroundGame,0,0,null);
        ball.render(g);
        paddle.render(g);
        for (int row = 0; row < bricks.length; row++) {
            for (int col = 0; col < bricks[0].length; col++) {
                if (!bricks[row][col].isDestroyed()) {
                    bricks[row][col].render(g);
                }
            }
        }

    }

    public GameJpanel() {

        setFocusable(true);
        addKeyListener(this);
        timer = new Timer(16, this); // ~60 FPS
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ball.update();// cập nhật vị trí bóng
        paddle.update();

        if (ball.checkCollision(paddle)) {
            ball.bounceOff(paddle);
        }
        Brick nearestBrick = null;
        int minArea = Integer.MAX_VALUE;
        for (Brick[] row : bricks) {
            for (Brick b : row) {
                if (!b.isDestroyed() && ball.checkCollision(b)) {
                    // Tính vùng giao nhau
                    Rectangle inter = ball.getBounds().intersection(b.getBounds());
                    int area = inter.width * inter.height;

                    // Lưu viên có diện tích giao nhỏ nhất
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
        }


        repaint();       // vẽ lại panel

    }
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            paddle.setMoveLeft(true);
        } else if (key == KeyEvent.VK_RIGHT) {
            paddle.setMoveRight(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            paddle.setMoveLeft(false);
        } else if (key == KeyEvent.VK_RIGHT) {
            paddle.setMoveRight(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

}
