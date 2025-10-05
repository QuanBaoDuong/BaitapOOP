import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class GameJpanel extends JPanel implements ActionListener, KeyListener {

    private Ball ball=new Ball(400,400,40,40,1,1,5);
    private Paddle paddle = new Paddle(450,700,15);
    Brick[][] bricks = Brick.createMap(5, 10, 100, 60);
    Brick brick;
    private Timer timer;

    public Dimension getPreferredSize() {
        return new Dimension(GameJframe.SCREEN_WIDTH, GameJframe.SCREEN_HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
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
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        timer = new Timer(16, this); // ~60 FPS
        timer.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ball.update();// cập nhật vị trí bóng
        paddle.update();
        for (int i=0;i<bricks.length;i++) {
            for (int j=0;j<bricks[0].length;j++) {
                Brick b = bricks[i][j];
                if (!b.isDestroyed() &&ball.checkCollision(bricks[i][j])) {
                    ball.bounceOff(bricks[i][j]);
                    bricks[i][j].takeHit();
                    return;
                }
            }
        }

        if (ball.checkCollision(paddle)) {
            ball.bounceOff(paddle);
        }

        repaint();       // vẽ lại panel

    }
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) {
            paddle.setMoveLeft();
        } else if (code == KeyEvent.VK_RIGHT) {
            paddle.setMoveRight();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
       /* int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT) {
            //paddle.stop();
        }*/
    }
    @Override
    public void keyTyped(KeyEvent e) {
        // Không dùng nhưng vẫn phải override
    }
}
