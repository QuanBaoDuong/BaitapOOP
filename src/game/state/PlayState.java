package game.state;

import game.manager.GameManager;
import game.manager.GameStateManager;
import game.manager.HighScoreManager;
import game.render.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayState implements GameState {
    private GameManager gameManager;
    private game.render.Renderer renderer;

    private boolean isGameOver = false;
    private boolean isGameWin = false;
    private boolean isTransitioning = false;
    private long transitionStartTime;

    private boolean gameStarted = false;


    private Image background;
    private Image gameOverImage;
    private Image gameWinImage;
    private Image transitionGif;

    private JPanel panel;
    private GameStateManager gameStateManager;

    public PlayState(JPanel panel, GameStateManager gameStateManager) {
        this.panel = panel;
        this.gameStateManager = gameStateManager;

        background = new ImageIcon(getClass().getResource("/image/BackGroundgame.png")).getImage();
        gameOverImage = new ImageIcon(getClass().getResource("/image/GameOver.png")).getImage();
        gameWinImage = new ImageIcon(getClass().getResource("/image/GameWin.jpg")).getImage();

        gameManager = new GameManager();
        loadTransitionGif(gameManager.getCurrentLevel());
        startTransition();
        renderer = new Renderer(background, gameOverImage ,gameWinImage,transitionGif);
    }

    private void loadTransitionGif(int level) {
        String path = "/image/LV"+level+".gif";
        transitionGif = new ImageIcon(getClass().getResource(path)).getImage();
    }

    @Override
    public void update() {
        if (isGameOver||isGameWin) return;

        if(isTransitioning) {
            long elapsed = System.currentTimeMillis()-transitionStartTime;
            if (elapsed >=3300) {
                isTransitioning = false;
                // Nếu là lần đầu tiên -> chỉ bắt đầu chơi chứ không nextLevel
                if (gameManager.getCurrentLevel() == 1 && !gameStarted) {
                    gameStarted = true;
                    System.out.println("✅ Bắt đầu chơi Level 1!");
                } else {
                    // Chuyển sang level kế tiếp
                    gameManager.nextLevel();
                    System.out.println(gameManager.getCurrentLevel());
                    loadTransitionGif(gameManager.getCurrentLevel());
                }
            }
            return;
        }

        gameManager.update();
        if (gameManager.isGameOver()) {
            isGameOver = true;
            HighScoreManager.checkNewScore(gameManager.getScore());
        }
        else if (gameManager.isGameWin()) {
            isGameWin = true;
            HighScoreManager.checkNewScore(gameManager.getScore());
        }
        else if (gameManager.isLevelComplete()) {
            startTransition();
            gameManager.setLevelComplete(false);
        }
    }

    private void startTransition() {
        isTransitioning = true;
        transitionStartTime = System.currentTimeMillis();
    }

    @Override
    public void draw(Graphics g) {
        renderer.render(g, gameManager, isGameOver,isGameWin,isTransitioning);
    }

    @Override
    public void keyPressed(int keyCode) {
        if (isGameOver && keyCode == KeyEvent.VK_ENTER) {
            gameManager.restartGame();
            isGameOver = false;
            return;
        }
        if (isGameWin && keyCode == KeyEvent.VK_ENTER) {
            gameManager.restartGame();
            isGameWin = false;
            return;
        }

        gameManager.handleKeyPressed(keyCode);
    }

    @Override
    public void keyReleased(int keyCode) {
        gameManager.handleKeyReleased(keyCode);
    }
}
