import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameJpanel extends JPanel implements ActionListener {
    private Ball ball = new Ball(400, 400, 30, 30, 0.5, 0.5, 10);
    private Timer timer;
    public java.util.List<Wall> walls = new ArrayList<>();
    private java.util.List<PowerUp> powerUps = new ArrayList<>();
    private Random rand = new Random();


    public Dimension getPreferredSize() {
        return new Dimension(GameJframe.SCREEN_WIDTH, GameJframe.SCREEN_HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ball.render(g);
        brick.render(g);
        for (PowerUp p : powerUps) {
            p.render(g);
        }

    }

    public GameJpanel() {
        this.setBackground(Color.BLACK);
        timer = new Timer(16, this); // ~60 FPS
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ball.update();   // cập nhật vị trí bóng
        /*if (ball.checkCollision(brick)) {
            ball.bounceOff(brick);
        }*/
        repaint();       // vẽ lại panel
        for (int i = 0; i < walls.size(); i++) {
            if (ball.checkCollision(walls.get(i)) == true) {
                walls.remove(i);
                String[] types = {"expand", "shrink", "extraLife", "quickpaddle", "quickball"};
                String randomType = types[rand.nextInt(types.length)];
                if (rand.nextInt(100) < 30) {
                    powerUps.add(new PowerUp(walls.get(i).x, walls.get(i).y, 20, 20, 3, randomType));
                }
            }
        }
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp p = powerUps.get(i);
            p.update();

        }
    }

    private void activatePowerUp(String type) {
        switch (type) {
            case "expand":
                paddle.setWidth(paddle.getWidth() + 50);
                break;
            case "shrink":
                paddle.setWidth(Math.max(30, paddle.getWidth() - 30));
                break;
            case "extraLife":
                lives++;
                break;
            case "quickpaddle":
                paddle.speed *= 2;
                break;
            case "quickball":
                ball.Speed *= 2;
                break;
        }
    }
}
