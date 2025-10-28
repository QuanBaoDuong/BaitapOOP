package game.state;

import game.manager.GameManager;
import game.manager.HighScoreManager;
import game.render.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayState implements GameState {
    private final GameManager gameManager;
    private final Renderer renderer;

    private boolean isTransitioning = false;
    private long transitionStartTime;
    private boolean gameStarted = false;

    private boolean isGameOver = false;
    private boolean isGameWin = false;

    private Image transitionGif;

    public PlayState(JPanel panel) {
        // load assets
        Image bg = new ImageIcon(getClass().getResource("/image/BackGroundgame.png")).getImage();
        Image gameOverImg = new ImageIcon(getClass().getResource("/image/GameOver.png")).getImage();
        Image winImg = new ImageIcon(getClass().getResource("/image/GameWin.jpg")).getImage();

        gameManager = new GameManager();
        renderer = new Renderer(bg, gameOverImg, winImg, null);

        // show initial level-1 transition
        startTransition(1);
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
    }

    @Override
    public void update() {
        // if game ended, skip updates (wait for player input)
        if (isGameOver || isGameWin) return;

        // transition timer
        if (isTransitioning) {
            long elapsed = System.currentTimeMillis() - transitionStartTime;
            if (elapsed >= 3300) {
                isTransitioning = false;
                if (!gameStarted) {
                    gameStarted = true;
                } else {
                    // now actually move to the next level data
                    if (gameManager.getCurrentLevel() < gameManager.getMaxLevel()) {
                        gameManager.nextLevel();
                    } else {
                        // shouldn't happen because PlayState sets isGameWin earlier,
                        // but safe-guard.
                        isGameWin = true;
                    }
                }
            }
            return;
        }

        // normal update
        gameManager.update();

        // check out of lives
        if (gameManager.isOutOfLives()) {
            isGameOver = true;
            HighScoreManager.checkNewScore(gameManager.getScore());
            return;
        }

        // level complete -> either transition to next or set win
        if (gameManager.isLevelComplete()) {
            int next = gameManager.getCurrentLevel() + 1;
            if (next <= gameManager.getMaxLevel()) {
                startTransition(next);
            } else {
                // final level cleared -> win, no transition
                isGameWin = true;
                HighScoreManager.checkNewScore(gameManager.getScore());
            }
            // clear GameManager levelComplete (GameManager will be reloaded on nextLevel/loadLevel)
            // we rely on gameManager.nextLevel() to clear on actual level change
            return;
        }
    }

    @Override
    public void draw(Graphics g) {
        renderer.render(g, gameManager, isGameOver, isGameWin, isTransitioning);
    }

    @Override
    public void keyPressed(int keyCode) {
        if ((isGameOver || isGameWin) && keyCode == KeyEvent.VK_ENTER) {
            restart();
            return;
        }

        // when transitioning, ignore gameplay keys
        if (isTransitioning) return;

        gameManager.handleKeyPressed(keyCode);
    }

    @Override
    public void keyReleased(int keyCode) {
        gameManager.handleKeyReleased(keyCode);
    }

    private void restart() {
        gameManager.restartGame();
        isGameOver = false;
        isGameWin = false;
        gameStarted = false;
        // show intro transition again
        startTransition(1);
    }

    // optional setter for testing
    public void setTransitionGif(Image img) {
        this.transitionGif = img;
        renderer.setTransitionGif(img);
    }
}
