package game.state;

import game.manager.GameStateManager;
import game.sound.SoundManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class SettingState implements GameState, MouseListener,
        MouseMotionListener {
    private final GameStateManager gsm;
    private final JPanel panel;
    private final PlayState playState;

    private final Rectangle[] buttons;
    private final String[] buttonTexts = {"NEW LEVEL", "QUIT",
            "SOUND: ON", "CLOSE"};
    private int hoveredButton = -1;
    private int clickedButton = -1;

    private final SoundManager soundManager;
    private boolean soundEnabled;

    private static final Color BACKGROUND_COLOR = new Color(0,0,0,180);
    private static final Color BUTTON_COLOR = new Color(70,130,180);
    private static final Color BUTTON_HOVER = new Color(100,160,210);
    private static final Color BUTTON_CLICK = new Color(50,110,160);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 36);

    public SettingState(GameStateManager gsm, JPanel panel,
                        PlayState playState, SoundManager soundManager) {
        this.gsm = gsm;
        this.panel = panel;
        this.playState = playState;
        this.soundManager = soundManager;

        buttons = new Rectangle[4];
        int buttonWidth = 250, buttonHeight = 50;
        int startX = (1000 - buttonWidth)/2;
        int startY = 300, gap = 70;

        for (int i=0;i<buttons.length;i++) {
            buttons[i] = new Rectangle(startX, startY + i*gap,
                    buttonWidth, buttonHeight);
        }

        soundEnabled = soundManager.isSoundEnabled();
        buttonTexts[2] = "SOUND: " + (soundEnabled ? "ON":"OFF");

        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics g) {
        if (playState != null) playState.draw(g);

        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0,0,1000,800);

        g.setColor(Color.WHITE);
        g.setFont(TITLE_FONT);
        String title = "SETTINGS";
        g.drawString(title, (1000 - g.getFontMetrics().
                stringWidth(title))/2, 250);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(BUTTON_FONT);

        for (int i=0;i<buttons.length;i++) {
            Rectangle r = buttons[i];
            if (i==clickedButton) g2d.setColor(BUTTON_CLICK);
            else if (i==hoveredButton) g2d.setColor(BUTTON_HOVER);
            else g2d.setColor(BUTTON_COLOR);

            g2d.fillRoundRect(r.x,r.y,r.width,r.height,25,25);
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(r.x,r.y,r.width,r.height,25,25);

            String text = buttonTexts[i];
            FontMetrics fm = g2d.getFontMetrics();
            int textX = r.x + (r.width - fm.stringWidth(text))/2;
            int textY = r.y + (r.height - fm.getHeight())/2 + fm.getAscent();
            g2d.drawString(text, textX, textY);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        for (int i=0;i<buttons.length;i++) {
            if (buttons[i].contains(p)) {
                handleButtonClick(i);
                break;
            }
        }
    }

    private void handleButtonClick(int idx) {
        switch (idx) {
            case 0: // NEW LEVEL - Khởi động lại level hiện tại
                if (playState != null) {
                    playState.restartCurrentLevel();
                }
                cleanup();
                gsm.pop();
                break;

            case 1: // QUIT - Quay về menu chính
                soundManager.stopAll();
                cleanup();
                gsm.setState(new MenuState(panel, gsm));
                break;

            case 2: // SOUND - Bật/tắt âm thanh
                soundEnabled = !soundEnabled;
                soundManager.setSoundEnabled(soundEnabled);
                if (soundEnabled) soundManager.playBackground("bgm.wav", true);
                buttonTexts[2] = "SOUND: " + (soundEnabled ? "ON":"OFF");
                panel.repaint();
                break;

            case 3: // CLOSE - Đóng setting và tiếp tục game
                cleanup();
                gsm.pop();
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (int i=0;i<buttons.length;i++) {
            if (buttons[i].contains(e.getPoint())) {
                clickedButton = i;
                panel.repaint();
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        clickedButton = -1;
        panel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int newHover = -1;
        for (int i=0;i<buttons.length;i++) {
            if (buttons[i].contains(e.getPoint())) {
                newHover = i;
                break;
            }
        }
        if (newHover != hoveredButton) {
            hoveredButton = newHover;
            panel.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {
        hoveredButton = -1;
        panel.repaint();
    }

    @Override
    public void keyPressed(int keyCode) {}
    @Override
    public void keyReleased(int keyCode) {}

    private void cleanup() {
        panel.removeMouseListener(this);
        panel.removeMouseMotionListener(this);
    }
}
