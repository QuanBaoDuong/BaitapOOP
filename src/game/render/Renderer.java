package game.render;

import game.main.GameJframe;
import game.manager.GameManager;
import game.object.Brick;
import game.object.PowerUp;

import java.awt.*;

public class Renderer {
    private Image background;
    private Image gameOverImage;

    public Renderer(Image background, Image gameOverImage) {
        this.background = background;
        this.gameOverImage = gameOverImage;
    }

    public void render(Graphics g, GameManager gm, boolean isGameOver) {
        // Background
        g.drawImage(background, 0, 0, null);

        // Draw objects
        gm.getBall().render(g);
        gm.getPaddle().render(g);

        for (Brick[] row : gm.getBricks()) {
            for (Brick b : row)
                if (!b.isDestroyed()) b.render(g);
        }

        for (PowerUp p : gm.getPowerUps()) {
            p.render(g);
        }

        // Game Over
        if (isGameOver) {
            g.drawImage(
                    gameOverImage,
                    (GameJframe.SCREEN_WIDTH - gameOverImage.getWidth(null)) / 2,
                    (GameJframe.SCREEN_HEIGHT - gameOverImage.getHeight(null)) / 2,
                    null
            );
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.setColor(Color.WHITE);
            g.drawString(
                    "Press ENTER to Restart",
                    (GameJframe.SCREEN_WIDTH / 2) - 180,
                    (GameJframe.SCREEN_HEIGHT / 2) + 300
            );
        }
    }
}
