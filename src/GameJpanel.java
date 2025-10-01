import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameJpanel extends JPanel implements ActionListener, KeyListener {

    // Giữ nguyên tốc độ và hướng từ file cũ: (1, 1, 5)
    private Ball ball = new Ball(400, 400, 30, 30, 1, 1, 5);
    private Paddle paddle = new Paddle(450, 700);
    private BallInteraction ballInteraction;
    private Timer timer;
    private boolean gameRunning = true;

    public Dimension getPreferredSize() {
        return new Dimension(GameJframe.SCREEN_WIDTH, GameJframe.SCREEN_HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ các đối tượng game (giữ nguyên từ cũ)
        ball.render(g);
        paddle.render(g);
    }

    public GameJpanel() {
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        // Khởi tạo BallInteraction
        ballInteraction = new BallInteraction(ball, paddle);

        // Giữ nguyên timer từ file cũ
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameRunning) {
            ball.update();
            paddle.update();
            ballInteraction.checkAllInteractions();

            // Kiểm tra game over
            if (ballInteraction.isBallOutOfBounds()) {
                gameOver();
            }

            repaint();
        }
    }

    private void gameOver() {
        gameRunning = false;
        timer.stop();
        System.out.println("Game Over! Ball fell down.");
    }

    // KeyListener methods (giữ nguyên từ cũ)
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                paddle.setMoveLeft(true);
                break;
            case KeyEvent.VK_RIGHT:
                paddle.setMoveRight(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                paddle.setMoveLeft(false);
                break;
            case KeyEvent.VK_RIGHT:
                paddle.setMoveRight(false);
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Không cần xử lý
    }
}
