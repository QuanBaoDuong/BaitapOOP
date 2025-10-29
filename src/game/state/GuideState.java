package game.state;

import game.manager.GameStateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GuideState implements GameState, MouseListener, MouseMotionListener {
    private GameStateManager gameStateManager;
    private JPanel panel;
    private Image guideImage;
    private Rectangle backButtonBounds;

    public GuideState(GameStateManager gameStateManager, JPanel panel) {
        this.gameStateManager = gameStateManager;
        this.panel = panel;
        guideImage = new ImageIcon(getClass().getResource("/image/GuideScreen.png")).getImage();

        // Xác định vị trí nút quay lại
        backButtonBounds = new Rectangle(400, 750, 200, 50);

        // Thêm mouse listener
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics g) {
        // Hiển thị ảnh hướng dẫn
        g.drawImage(guideImage, 0, 0, null);

        // Vẽ nút "Quay lại"
        g.setColor(new Color(70, 130, 180));
        g.fillRoundRect(400, 750, 200, 50, 25, 25);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("QUAY LAI", 440, 785);
    }

    @Override
    public void keyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_ENTER) {
            cleanup();
            gameStateManager.pop();
        }
    }

    @Override
    public void keyReleased(int keyCode) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        if (backButtonBounds.contains(e.getPoint())) {
            cleanup();
            gameStateManager.pop();
        }
    }

    private void cleanup() {
        panel.removeMouseListener(this);
        panel.removeMouseMotionListener(this);
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
}
