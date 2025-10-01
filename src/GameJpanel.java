import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameJpanel extends JPanel implements ActionListener {
    private Image backGroundGame = new ImageIcon(getClass().getResource("/image/BackGroundgame.png")).getImage();
    private Brick brick = new Brick(750,740,60,40);
    private Ball ball=new Ball(400,400,30,30,1,1,10);
    private Timer timer;

    public Dimension getPreferredSize() {
        return new Dimension(GameJframe.SCREEN_WIDTH,GameJframe.SCREEN_HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backGroundGame,0,0,this);
        ball.render(g);
        brick.render(g);
    }
    public GameJpanel () {

        timer = new Timer(16, this); // ~60 FPS
        timer.start();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        ball.update();   // cập nhật vị trí bóng
        if(ball.checkCollision(brick)) {
            ball.bounceOff(brick);

        }
        repaint();       // vẽ lại panel
    }

}
