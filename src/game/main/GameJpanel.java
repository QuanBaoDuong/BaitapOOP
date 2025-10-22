package game.main;

import game.manager.GameStateManager;
import game.state.MenuState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameJpanel extends JPanel implements KeyListener {

    private GameStateManager gsm; // quản lý state
    private Timer timer;

    public GameJpanel() {
        setFocusable(true);
        requestFocusInWindow();

        // Khởi tạo game.manager.GameStateManager và menu ban đầu
        gsm = new GameStateManager();
        gsm.setStates(new MenuState(this, gsm)); // game.state.MenuState xử lý chuột riêng

        addKeyListener(this);

        // Timer ~60 FPS
        timer = new Timer(16, e -> {
            gsm.update();
            repaint();
        });
        timer.start();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(GameJframe.SCREEN_WIDTH, GameJframe.SCREEN_HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gsm != null) gsm.draw(g); // vẽ state hiện tại
    }

    // === KeyListener ===
    @Override
    public void keyPressed(KeyEvent e) {
        if (gsm != null) gsm.keyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gsm != null) gsm.keyReleased(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
