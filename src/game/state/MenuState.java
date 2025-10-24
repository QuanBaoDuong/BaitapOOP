package game.state;

import game.manager.GameStateManager;
import game.sound.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MenuState implements GameState, MouseListener, MouseMotionListener {
    private GameStateManager gameStateManager;
    private JPanel panel;

    private String[] options = {"NEW GAME", "HƯỚNG DẪN", "EXIT GAME"}; // THÊM HƯỚNG DẪN
    private Rectangle[] optionBounds;
    private int currentChoice = -1;

    private Image backGroundMenu;
    private Image newGameBig;
    private Image newGame;
    private Image guideBig;    // THÊM
    private Image guide;       // THÊM
    private Image exitGameBig;
    private Image exitGame;

    // Biến để kiểm tra đang ở màn hình hướng dẫn
    private boolean showingGuide = false;
    private Image guideImage;  // Ảnh hướng dẫn

    public MenuState(JPanel panel, GameStateManager gameStateManager) {
        this.panel = panel;
        this.gameStateManager = gameStateManager;

        optionBounds = new Rectangle[options.length];

        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);

        backGroundMenu = new ImageIcon(getClass().getResource("/image/BackGround.jpg")).getImage();
        newGameBig = new ImageIcon(getClass().getResource("/image/NewGame.png")).getImage();
        guideBig = new ImageIcon(getClass().getResource("/image/Guide.png")).getImage(); // THÊM
        exitGameBig = new ImageIcon(getClass().getResource("/image/ExitGame.png")).getImage();

        int buttonWidth = 140;
        int buttonHeight = 40;
        newGame = newGameBig.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        guide = guideBig.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH); // THÊM
        exitGame = exitGameBig.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);

        // Load ảnh hướng dẫn
        guideImage = new ImageIcon(getClass().getResource("/image/GuideScreen.png")).getImage();

        // tạo mảng bounds (3 phần tử)
        optionBounds = new Rectangle[options.length];

        // xác định vị trí nút
        int x = 440;
        int y = 300;
        optionBounds[0] = new Rectangle(x, y, buttonWidth, buttonHeight);           // NEW GAME
        optionBounds[1] = new Rectangle(x, y + 60, buttonWidth, buttonHeight);      // HƯỚNG DẪN
        optionBounds[2] = new Rectangle(x, y + 120, buttonWidth, buttonHeight);     // EXIT GAME
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics g) {
        if (showingGuide) {
            // Vẽ màn hình hướng dẫn
            g.drawImage(guideImage, 0, 0, null);

            // Vẽ nút "Quay lại"
            g.setColor(new Color(70, 130, 180));
            g.fillRoundRect(440, 750, 140, 40, 15, 15);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("QUAY LẠI", 465, 775);
        } else {
            // Vẽ menu chính
            g.drawImage(backGroundMenu, 0, 0, null);
            g.drawImage(newGame, 440, 300, null);
            g.drawImage(guide, 440, 360, null);     // THÊM
            g.drawImage(exitGame, 440, 420, null);  // ĐÃ DI CHUYỂN XUỐNG
        }
    }

    @Override public void keyPressed(int keyCode) {}
    @Override public void keyReleased(int keyCode) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        if (showingGuide) {
            // Kiểm tra click vào nút "Quay lại" (vị trí cố định)
            Rectangle backButton = new Rectangle(440, 750, 140, 40);
            if (backButton.contains(e.getPoint())) {
                showingGuide = false;
            }
        } else {
            for (int i = 0; i < optionBounds.length; i++) {
                if (optionBounds[i].contains(e.getPoint())) {
                    executeOption(i);
                }
            }
        }
    }

    private void executeOption(int index) {
        switch (index) {
            case 0: // NEW GAME
                gameStateManager.setStates(new PlayState(panel, gameStateManager));
                Sound.playSound("bgm.wav", false);
                break;
            case 1: // HƯỚNG DẪN - THÊM
                showingGuide = true;
                break;
            case 2: // EXIT GAME
                System.exit(0);
                break;
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        currentChoice = -1;
        if (!showingGuide) {
            for (int i = 0; i < optionBounds.length; i++) {
                if (optionBounds[i].contains(e.getPoint())) {
                    currentChoice = i;
                }
            }
        }
    }

    @Override public void mouseDragged(MouseEvent e) {}
}
