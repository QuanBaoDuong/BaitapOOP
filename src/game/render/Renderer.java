package game.render;

import game.main.GameJframe;
import game.manager.GameManager;
import game.object.Brick;
import game.object.PowerUp;

import javax.swing.*;
import java.awt.*;

public class Renderer {
    private final Image backgroundImage;
    private final Image gameOverImage;
    private final Image winImage;
    private Image transitionGif;

    public Renderer(Image backgroundImg, Image gameOverImg, Image winImage,Image transitionGif) {
        this.backgroundImage = backgroundImg;
        this.gameOverImage = gameOverImg;
        this.winImage = winImage;
        this.transitionGif = transitionGif;
    }

    public void render(Graphics g, GameManager gm, boolean isGameOver, boolean isWin,boolean isTransitioning) {
        if (isTransitioning) {
            // Vẽ ảnh động chuyển cảnh
            drawCenteredImage(g, transitionGif);
            return;
        }

        // Vẽ background
        g.drawImage(backgroundImage, 0, 0, null);

        // Vẽ các đối tượng trong game
        gm.getBall().render(g);
        gm.getPaddle().render(g);

        for (Brick brick : gm.getBricks()) {
            if (!brick.isDestroyed()) {
                brick.render(g);
            }
        }

        for (PowerUp powerUp : gm.getPowerUps()) {
            powerUp.render(g);
        }

        // Hiển thị thông tin
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.WHITE);
        g.drawString("Lives: " + gm.getLives(), 20, 40);
        g.drawString("Score: " + gm.getScore(), GameJframe.SCREEN_WIDTH - 150, 40);

        // Kiểm tra trạng thái Game Over hoặc Win
        if (isGameOver) {
            drawCenteredImage(g, gameOverImage);
            drawCenteredText(g, "Press ENTER to Restart", 300);
        } else if (isWin) {
            drawCenteredImage(g, winImage);
            drawCenteredText(g, "Press ENTER for Next Level", 300);
        }
    }

    private void drawCenteredImage(Graphics g, Image img) {
        int x = (GameJframe.SCREEN_WIDTH - img.getWidth(null)) / 2;
        int y = (GameJframe.SCREEN_HEIGHT - img.getHeight(null)) / 2;
        g.drawImage(img, x, y, null);
    }

    private void drawCenteredText(Graphics g, String text, int offsetY) {
        g.setFont(new Font("Arial", Font.BOLD, 32));
        g.setColor(Color.WHITE);
        int textWidth = g.getFontMetrics().stringWidth(text);
        int x = (GameJframe.SCREEN_WIDTH - textWidth) / 2;
        int y = (GameJframe.SCREEN_HEIGHT / 2) + offsetY;
        g.drawString(text, x, y);
    }
    // Cho phép thay đổi ảnh động khi qua màn
    public void setTransitionGif(Image transitionGif) {
        this.transitionGif = transitionGif;
    }
}
