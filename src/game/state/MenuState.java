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

    private String[] options = {"NEW GAME", "HƯỚNG DẪN", "HIGH SCORES", "EXIT GAME"};
    private Rectangle[] optionBounds;
    private int currentChoice = -1;

    private Image backGroundMenu;
    private Image newGameBig;
    private Image newGame;
    private Image guideBig;
    private Image guide;
    private Image exitGameBig;
    private Image exitGame;
    private Image highScoreBig;
    private Image highScore;

    public MenuState(JPanel panel, GameStateManager gameStateManager) {
        this.panel = panel;
        this.gameStateManager = gameStateManager;

        optionBounds = new Rectangle[options.length];

        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);

        backGroundMenu = new ImageIcon(getClass().getResource("/image/BackGround.jpg")).getImage();
        newGameBig = new ImageIcon(getClass().getResource("/image/NewGame.png")).getImage();
        guideBig = new ImageIcon(getClass().getResource("/image/Guide.png")).getImage();
        exitGameBig = new ImageIcon(getClass().getResource("/image/ExitGame.png")).getImage();
        highScoreBig = new ImageIcon(getClass().getResource("/image/High Scores.png")).getImage();

        int buttonWidth = 140;
        int buttonHeight = 40;
        newGame = newGameBig.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        guide = guideBig.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        exitGame = exitGameBig.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        highScore = highScoreBig.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);

        // tạo mảng bounds (4 phần tử)
        optionBounds = new Rectangle[options.length];

        // xác định vị trí nút
        int x = 440;
        int y = 300;
        optionBounds[0] = new Rectangle(x, y, buttonWidth, buttonHeight);           // NEW GAME
        optionBounds[1] = new Rectangle(x, y + 60, buttonWidth, buttonHeight);      // HƯỚNG DẪN
        optionBounds[2] = new Rectangle(x, y + 120, buttonWidth, buttonHeight);     // HIGH SCORES
        optionBounds[3] = new Rectangle(x, y + 180, buttonWidth, buttonHeight);     // EXIT GAME
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics g) {
        // Vẽ menu chính
        g.drawImage(backGroundMenu, 0, 0, null);
        g.drawImage(newGame, 440, 300, null);
        g.drawImage(guide, 440, 360, null);
        g.drawImage(highScore, 440, 420, null);
        g.drawImage(exitGame, 440, 480, null);
    }

    @Override public void keyPressed(int keyCode) {}
    @Override public void keyReleased(int keyCode) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        for (int i = 0; i < optionBounds.length; i++) {
            if (optionBounds[i].contains(e.getPoint())) {
                executeOption(i);
            }
        }
    }

    private void executeOption(int index) {
        switch (index) {
            case 0: // NEW GAME
                panel.removeMouseListener(this);
                panel.removeMouseMotionListener(this);
                gameStateManager.setStates(new PlayState(panel, gameStateManager));
                Sound.playSound("bgm.wav", true);
                break;
            case 1: // HƯỚNG DẪN
                panel.removeMouseListener(this);
                panel.removeMouseMotionListener(this);
                gameStateManager.push(new GuideState(gameStateManager, panel));
                break;
            case 2: // HIGH SCORES
                panel.removeMouseListener(this);
                panel.removeMouseMotionListener(this);
                gameStateManager.setStates(new HighScoreState(panel, gameStateManager));
                break;
            case 3: // EXIT GAME
                panel.removeMouseListener(this);
                panel.removeMouseMotionListener(this);
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
        for (int i = 0; i < optionBounds.length; i++) {
            if (optionBounds[i].contains(e.getPoint())) {
                currentChoice = i;
            }
        }
    }

    @Override public void mouseDragged(MouseEvent e) {}
}
