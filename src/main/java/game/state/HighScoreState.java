package game.state;

import game.manager.GameStateManager;
import game.manager.HighScoreManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HighScoreState implements GameState, MouseListener {
    private final JPanel panel;
    private final GameStateManager gsm;
    private final Rectangle backButton = new Rectangle(440, 500, 140, 40);
    private boolean isHovering = false;

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 48);
    private static final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 36);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_HOVER = new Color(100, 160, 210);

    public HighScoreState(JPanel panel, GameStateManager gsm) {
        this.panel = panel;
        this.gsm = gsm;
        panel.addMouseListener(this);
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics g) {
        // Nền
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, panel.getWidth(), panel.getHeight());

        // Tiêu đề
        g.setColor(Color.WHITE);
        g.setFont(TITLE_FONT);
        g.drawString("HIGH SCORES", 330, 200);

        // Điểm cao
        g.setFont(TEXT_FONT);
        g.drawString("Điểm cao nhất: " +
                HighScoreManager.getHighScore(), 340, 300);

        // Nút quay lại
        g.setColor(isHovering ? BUTTON_HOVER : BUTTON_COLOR);
        g.fillRoundRect(backButton.x, backButton.y, backButton.width,
                backButton.height, 15, 15);
        g.setColor(Color.WHITE);
        g.setFont(BUTTON_FONT);
        g.drawString("QUAY LẠI", backButton.x + 25, backButton.y + 28);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (backButton.contains(e.getPoint())) {
            cleanup();
            gsm.setState(new MenuState(panel, gsm)); // sửa lại tên nếu cần
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {
        isHovering = false;
        panel.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (backButton.contains(e.getPoint())) {
            isHovering = true;
            panel.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isHovering = backButton.contains(e.getPoint());
        panel.repaint();
    }

    private void cleanup() {
        panel.removeMouseListener(this);
    }
    @Override
    public void keyPressed(int keyCode) {}

    @Override
    public void keyReleased(int keyCode) {}

}
