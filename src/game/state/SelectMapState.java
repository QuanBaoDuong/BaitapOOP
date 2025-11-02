package game.state;

import game.manager.GameStateManager;
import game.sound.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SelectMapState implements GameState, MouseListener, MouseMotionListener {

    private final GameStateManager gameStateManager;
    private final JPanel panel;
    private final Image background;

    private final int startX = 740;
    private final int startY = 262;
    private final int width = 175;
    private final int height = 35;
    private final int gap = 52;

    private final Rectangle[] levelButtons;
    private final Rectangle buttonBack;
    private int hoveredButtonIndex = -1;
    private int clickedButtonIndex = -1;
    private boolean backClicked = false;

    public SelectMapState(JPanel panel, GameStateManager gameStateManager) {
        this.panel = panel;
        this.gameStateManager = gameStateManager;

        background = new ImageIcon(getClass().getResource("/image/BackGround_selectMap.png")).getImage();

        levelButtons = new Rectangle[5];
        for (int i = 0; i < 5; i++) {
            levelButtons[i] = new Rectangle(startX, startY + i * gap, width, height);
        }

        buttonBack = new Rectangle(35,25,195,50);

        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics g) {
        g.drawImage(background, 0, 0, null);
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("Arial", Font.BOLD, 24));

        if (backClicked) {
            g2.setColor(new Color(255, 255, 150, 60));
            g2.fillRoundRect(buttonBack.x, buttonBack.y, buttonBack.width, buttonBack.height, 20, 20);
        }

        for (int i = 0; i < levelButtons.length; i++) {
            Rectangle rect = levelButtons[i];

            if (i == clickedButtonIndex) {
                g2.setColor(new Color(255, 255, 100)); // sáng hơn khi click
            } else if (i == hoveredButtonIndex) {
                g2.setColor(new Color(255, 255, 180)); // vàng sáng khi hover
            } else {
                g2.setColor(new Color(255, 215, 0));   // vàng thường
            }

            String text = "LEVEL " + (i + 1);
            drawCenteredText(g2, text, rect);
        }
    }

    private void drawCenteredText(Graphics2D g2, String text, Rectangle rect) {
        FontMetrics fm = g2.getFontMetrics();
        int textX = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int textY = rect.y + (rect.height - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(text, textX, textY);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();

        if (buttonBack.contains(p)) {
            cleanup();
            gameStateManager.setState(new MenuState(panel,gameStateManager));
        }

        for (int i = 0; i < levelButtons.length; i++) {
            if (levelButtons[i].contains(p)) {
                cleanup();
                //Sound.stopBGM();
                Sound.playSound("bgm.wav", true);
                gameStateManager.setState(new PlayState(panel, gameStateManager, i + 1, true));
                return;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        backClicked = buttonBack.contains(p);

        for (int i = 0; i < levelButtons.length; i++) {
            if (levelButtons[i].contains(p)) {
                clickedButtonIndex = i;
                panel.repaint();
                return;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        clickedButtonIndex = -1;
        backClicked = false;
        panel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        int newHover = -1;
        for (int i = 0; i < levelButtons.length; i++) {
            if (levelButtons[i].contains(p)) {
                newHover = i;
                break;
            }
        }

        if (newHover != hoveredButtonIndex) {
            hoveredButtonIndex = newHover;
            panel.repaint();
        }
    }

    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) { hoveredButtonIndex = -1; panel.repaint(); }
    @Override public void mouseDragged(MouseEvent e) {}

    private void cleanup() {
        panel.removeMouseListener(this);
        panel.removeMouseMotionListener(this);
    }

    @Override public void keyPressed(int keycode) {}
    @Override public void keyReleased(int keycode) {}
}
