package game.state;

import game.manager.GameManager;
import game.manager.GameStateManager;
import game.manager.HighScoreManager;
import game.render.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PlayState implements GameState, MouseListener {
    private final GameManager gameManager;
    private final Renderer renderer;
    private final JPanel panel;
    private final GameStateManager gameStateManager;

    private boolean isTransitioning = false;
    private long transitionStartTime;
    private boolean gameStarted = false;

    private boolean isGameOver = false;
    private boolean isGameWin = false;

    private final boolean singleLevelMode;
    private final int startLevel;

    private Image transitionGif;

    private final Rectangle settingButton = new Rectangle(920, 2, 80, 80);
    private Image settingIcon;

    public PlayState(JPanel panel, GameStateManager gameStateManager) {
        this(panel, gameStateManager, 1, false);
    }

    public PlayState(JPanel panel, GameStateManager gameStateManager, int startLevel, boolean singleLevelMode) {
        this.panel = panel;
        this.gameStateManager = gameStateManager;
        this.startLevel = startLevel;
        this.singleLevelMode = singleLevelMode;

        Image bg = new ImageIcon(getClass().getResource("/image/BackGroundgame.png")).getImage();
        Image gameOverImg = new ImageIcon(getClass().getResource("/image/GameOver.png")).getImage();
        Image winImg = new ImageIcon(getClass().getResource("/image/GameWin.jpg")).getImage();

        gameManager = new GameManager(startLevel, singleLevelMode);
        renderer = new Renderer(bg, gameOverImg, winImg, null);

        settingIcon = new ImageIcon(getClass().getResource("/image/setting_icon.png")).getImage();

        panel.addMouseListener(this);

        startTransition(startLevel);
    }

    private void loadTransitionGif(int level) {
        String path = "/image/LV" + level + ".gif";
        transitionGif = new ImageIcon(getClass().getResource(path)).getImage();
        renderer.setTransitionGif(transitionGif);
    }

    private void startTransition(int level) {
        loadTransitionGif(level);
        isTransitioning = true;
        transitionStartTime = System.currentTimeMillis();
        renderer.restartTransition();
    }

    @Override
    public void update() {
        if (isGameOver || isGameWin) return;

        if (isTransitioning) {
            long elapsed = System.currentTimeMillis() - transitionStartTime;
            if (elapsed >= 3300) {
                isTransitioning = false;
                if (!gameStarted) {
                    gameStarted = true;
                } else {
                    if (gameManager.getCurrentLevel() < gameManager.getMaxLevel()) {
                        gameManager.nextLevel();
                    } else {
                        isGameWin = true;
                    }
                }
            }
            return;
        }

        gameManager.update();

        if (gameManager.isOutOfLives()) {
            isGameOver = true;
            HighScoreManager.checkNewScore(gameManager.getScore());
            return;
        }

        if (gameManager.isLevelComplete()) {
            if (gameManager.isSingleLevelMode()) {
                isGameWin = true;
                HighScoreManager.checkNewScore(gameManager.getScore());
            } else {
                int next = gameManager.getCurrentLevel() + 1;
                if (next <= gameManager.getMaxLevel()) {
                    startTransition(next);
                } else {
                    isGameWin = true;
                    HighScoreManager.checkNewScore(gameManager.getScore());
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        renderer.render(g, gameManager, isGameOver, isGameWin, isTransitioning);

        if (!isTransitioning && !isGameOver && !isGameWin) {
            g.drawImage(settingIcon, settingButton.x, settingButton.y, settingButton.width, settingButton.height, null);
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        if ((isGameOver || isGameWin) && keyCode == KeyEvent.VK_ENTER) {
            if (gameManager.isSingleLevelMode()) {
                gameStateManager.setState(new SelectMapState(panel, gameStateManager));
            } else {
                gameStateManager.setState(new HighScoreState(panel, gameStateManager));
            }
            return;
        }

        if (isTransitioning) return;
        gameManager.handleKeyPressed(keyCode);
    }

    @Override
    public void keyReleased(int keyCode) {
        gameManager.handleKeyReleased(keyCode);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!isTransitioning && !isGameOver && !isGameWin) {
            if (settingButton.contains(e.getPoint())) {
                gameStateManager.push(new SettingState(gameStateManager, panel, this));
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public void cleanup() {
        panel.removeMouseListener(this);
    }
}
