import javax.swing.*;

public class GameJframe extends JFrame {

    public static final int SCREEN_WIDTH = 1000;
    public static final int SCREEN_HEIGHT = 600;

    private GameJpanel gameJpanel = new GameJpanel();

    public GameJframe() {
        this.add(gameJpanel);
        this.pack();
        this.setTitle("ARKANOID");
        this.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

    }

    public static void main(String[] args) {
        new GameJframe();
    }
}
