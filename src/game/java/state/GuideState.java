package game.state;

import game.manager.GameStateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GuideState implements GameState, MouseListener {
    private final GameStateManager gsm;
    private final JPanel panel;
    private final Image guideImage;
    private final Rectangle backButtonBounds = new Rectangle(400, 750, 200, 50);
    private boolean isHoveringBack = false;

    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_HOVER = new Color(100, 160, 210);

    public GuideState(GameStateManager gsm, JPanel panel) {
        this.gsm = gsm;
        this.panel = panel;
        this.guideImage = new ImageIcon(
                getClass().getResource("/game/resources/image/GuideScreen.png")
        ).getImage();

        panel.addMouseListener(this);
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics g) {
        // Hiển thị ảnh hướng dẫn
        g.drawImage(guideImage, 0, 0, null);

        // Vẽ nút “Quay lại”
        g.setColor(isHoveringBack ? BUTTON_HOVER : BUTTON_COLOR);
        g.fillRoundRect(backButtonBounds.x, backButtonBounds.y,
                backButtonBounds.width, backButtonBounds.height, 25, 25);

        g.setColor(Color.WHITE);
        g.setFont(BUTTON_FONT);
        g.drawString("QUAY LẠI", backButtonBounds.x + 45, backButtonBounds.y + 35);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (backButtonBounds.contains(e.getPoint())) {
            cleanup();
            gsm.setState(new MenuState(panel, gsm)); // Đảm bảo đúng tên hàm
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {
        isHoveringBack = false;
        panel.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (backButtonBounds.contains(e.getPoint())) {
            isHoveringBack = true;
            panel.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isHoveringBack = backButtonBounds.contains(e.getPoint());
        panel.repaint();
    }

    private void cleanup() {
        panel.removeMouseListener(this);
    }

    @Override public void keyPressed(int keyCode) {}
    @Override public void keyReleased(int keyCode) {}
}
