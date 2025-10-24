package game.state;

import game.manager.GameStateManager;
import game.manager.HighScoreManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HighScoreState implements GameState, MouseListener {
    private JPanel panel;
    private GameStateManager gsm;

    public HighScoreState(JPanel panel, GameStateManager gsm) {
        this.panel = panel;
        this.gsm = gsm;
        panel.addMouseListener(this);
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, panel.getWidth(), panel.getHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("HIGH SCORES", 380, 200);

        g.setFont(new Font("Arial", Font.PLAIN, 36));
        g.drawString("Điểm cao nhất: " + HighScoreManager.getHighScore(), 380, 300);

        // Nút quay lại
        g.setColor(new Color(70, 130, 180));
        g.fillRoundRect(440, 500, 140, 40, 15, 15);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("QUAY LẠI", 465, 528);
    }

    @Override public void keyPressed(int keyCode) {}
    @Override public void keyReleased(int keyCode) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        Rectangle backButton = new Rectangle(440, 500, 140, 40);
        if (backButton.contains(e.getPoint())) {
            gsm.setStates(new MenuState(panel, gsm));
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
