package game.state;

import game.manager.GameManager;
import game.manager.GameStateManager;
import game.render.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayState implements GameState {
    private GameManager gameManager;
    private game.render.Renderer renderer;

    private boolean isGameOver = false;
    private Image background;
    private Image gameOverImage;
    private JPanel panel;
    private GameStateManager gameStateManager;

    public PlayState(JPanel panel, GameStateManager gameStateManager) {
        this.panel = panel;
        this.gameStateManager = gameStateManager;

        background = new ImageIcon(getClass().getResource("/image/BackGroundgame.png")).getImage();
        gameOverImage = new ImageIcon(getClass().getResource("/image/GameOver.png")).getImage();

        gameManager = new GameManager();
        renderer = new Renderer(background, gameOverImage);
    }

    @Override
    public void update() {
        if (isGameOver) return;
        gameManager.update();
        isGameOver = gameManager.isGameOver();
    }

    @Override
    public void draw(Graphics g) {
        renderer.render(g, gameManager, isGameOver);
    }

    @Override
    public void keyPressed(int keyCode) {
        if (isGameOver && keyCode == KeyEvent.VK_ENTER) {
            gameManager.reset();
            isGameOver = false;
            return;
        }

        gameManager.handleKeyPressed(keyCode);
    }

    @Override
    public void keyReleased(int keyCode) {
        gameManager.handleKeyReleased(keyCode);
    }
}
