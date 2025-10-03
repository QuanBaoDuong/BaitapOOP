import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameJpanel extends JPanel implements ActionListener {

    private Ball ball=new Ball(400,400,40,40,1,1,15);
    Brick[][] bricks = Brick.createMap(5, 10, 100, 60);
    Brick brick;
    private Timer timer;

    public Dimension getPreferredSize() {
        return new Dimension(GameJframe.SCREEN_WIDTH,GameJframe.SCREEN_HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ball.render(g);
        for (int row = 0; row < bricks.length; row++) {
            for (int col = 0; col < bricks[0].length; col++) {
                if (!bricks[row][col].isDestroyed()) {
                    bricks[row][col].render(g);
                }
            }
        }


    }
    public GameJpanel () {
        this.setBackground(Color.BLACK);
        timer = new Timer(16, this); // ~60 FPS
        timer.start();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        ball.update();// cập nhật vị trí bóng
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

        repaint();       // vẽ lại panel
    }

}
