import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameJpanel extends JPanel implements ActionListener {

    private Ball ball=new Ball(400,400,30,30,1,1,5);
    private Timer timer;

    public Dimension getPreferredSize() {
        return new Dimension(GameJframe.SCREEN_WIDTH,GameJframe.SCREEN_HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ball.render(g);
    }
    public GameJpanel () {
        this.setBackground(Color.BLACK);
        timer = new Timer(16, this); // ~60 FPS
        timer.start();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        ball.update();   // cập nhật vị trí bóng
        repaint();       // vẽ lại panel
    }

}
