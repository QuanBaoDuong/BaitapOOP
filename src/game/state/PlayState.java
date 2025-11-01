package game.state;

import game.manager.GameManager;
import game.manager.GameStateManager;
import game.manager.HighScoreManager;
import game.render.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayState implements GameState {
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

    private Image transitionGif;

    // ✅ Cho phép khởi tạo PlayState với level tùy chọn (phục vụ "Select Map")
    private final int startLevel;

    public PlayState(JPanel panel, GameStateManager gameStateManager) {
        this(panel, gameStateManager, 1, false); // mặc định level 1
    }

    public PlayState(JPanel panel, GameStateManager gameStateManager, int startLevel, boolean singleLevelMode) {
        this.panel = panel;
        this.gameStateManager = gameStateManager;
        this.startLevel = startLevel;
        this.singleLevelMode = singleLevelMode;

        Image bg = new ImageIcon(getClass().getResource("/image/BackGroundgame.png")).getImage();
        Image gameOverImg = new ImageIcon(getClass().getResource("/image/GameOver.png")).getImage();
        Image winImg = new ImageIcon(getClass().getResource("/image/GameWin.jpg")).getImage();

        gameManager = new GameManager(startLevel,singleLevelMode);
        renderer = new Renderer(bg, gameOverImg, winImg, null);

        startTransition(startLevel);
    }

    // -------------------------------
    // TRANSITION HANDLING
    // -------------------------------
    private void loadTransitionGif(int level) {
        String path = "/image/LV" + level + ".gif";
        transitionGif = new ImageIcon(getClass().getResource(path)).getImage();
        renderer.setTransitionGif(transitionGif);
    }

    private void startTransition(int level) {
        loadTransitionGif(level);
        isTransitioning = true;
        transitionStartTime = System.currentTimeMillis();
        renderer.restartTransition(); // ✅ reset frame đầu của GIF
    }

    // -------------------------------
    // MAIN LOOP
    // -------------------------------
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
                // chỉ chơi 1 màn → thắng luôn
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
            return;
        }

    }

    // -------------------------------
    // RENDERING
    // -------------------------------
    @Override
    public void draw(Graphics g) {
        renderer.render(g, gameManager, isGameOver, isGameWin, isTransitioning);
    }

    // -------------------------------
    // INPUT
    // -------------------------------
    @Override
    public void keyPressed(int keyCode) {
        if ((isGameOver || isGameWin) && keyCode == KeyEvent.VK_ENTER) {
            if (gameManager.isSingleLevelMode()) {
                // Quay lại chọn map
                gameStateManager.setState(new SelectMapState(panel, gameStateManager));
            } else {
                // Quay về bảng điểm
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

    // -------------------------------
    // UTILITY
    // -------------------------------
    private void restart() {
        gameManager.restartGame();
        isGameOver = false;
        isGameWin = false;
        gameStarted = false;
        startTransition(startLevel);
    }

    public void setTransitionGif(Image img) {
        this.transitionGif = img;
        renderer.setTransitionGif(img);
    }
}
