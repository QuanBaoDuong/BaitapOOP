import javax.swing.*;
public class GameJframe extends JFrame {

    public static final int SCREEN_WIDTH = 1000;
    public static final int SCREEN_HEIGHT = 800;

    private GameJpanel gamePanel = new GameJpanel();

    public GameJframe() {
        this.setTitle("ARKANOID");
        this.setResizable(false);
        setContentPane(gamePanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

    }

    public static void main(String[] args) {
        new GameJframe();
    }
}
