package game.render;

import game.java.main.GameJframe;
import game.manager.GameManager;
import game.object.Ball;
import game.object.Brick;
import game.object.PowerUp;

import javax.swing.*;
import java.awt.*;

public class Renderer {
    private final Image backgroundImage;
    private final Image gameOverImage;
    private final Image winImage;
    private Image transitionGif;
    private ImageIcon transitionIcon; // Giữ bản gốc GIF để reset dễ hơn

    private Image setting;
    private Image settingMenu;

    private boolean showSettingMenu = false;

    public Renderer(Image backgroundImg, Image gameOverImg, Image winImg, Image transitionGif) {
        this.backgroundImage = backgroundImg;
        this.gameOverImage = gameOverImg;
        this.winImage = winImg;

        setTransitionGif(transitionGif);
    }

    public void render(Graphics g, GameManager gm, boolean isGameOver, boolean isWin, boolean isTransitioning) {
        Graphics2D g2d = (Graphics2D) g;

        if (isTransitioning) {
            // Hiển thị ảnh động chuyển cảnh
            drawCenteredImage(g2d, transitionGif);
            Toolkit.getDefaultToolkit().sync(); // đồng bộ khung hình
            return;
        }

        // Vẽ background
        g2d.drawImage(backgroundImage, 0, 0, GameJframe.SCREEN_WIDTH, GameJframe.SCREEN_HEIGHT, null);

        // Vẽ các đối tượng trong game
        for (Ball b : gm.getBalls()) {
            b.render(g2d);
        }
        gm.getPaddle().render(g2d);

        for (Brick brick : gm.getBricks()) {
            if (!brick.isDestroyed()) brick.render(g2d);
        }

        for (PowerUp powerUp : gm.getPowerUps()) {
            powerUp.render(g2d);
        }

        // Hiển thị thông tin
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.setColor(Color.WHITE);
        g2d.drawString("Lives: " + gm.getLives(), 20, 40);
        g2d.drawString("Score: " + gm.getScore(), GameJframe.SCREEN_WIDTH - 980, 65);

        // Hiển thị các trạng thái kết thúc
        if (isGameOver) {
            drawCenteredImage(g2d, gameOverImage);
            drawCenteredText(g2d, "Your Score: " + gm.getScore(), -280);
            drawCenteredText(g2d, "Press ENTER to Restart", 300);
        }
        else if (isWin) {
            drawCenteredImage(g2d, winImage);
            drawCenteredText(g2d, "Your Score: " + gm.getScore(), -280);
            drawCenteredText(g2d, "Press ENTER to Restart", 300);
        }
    }

    private void drawCenteredImage(Graphics g, Image img) {
        int imgWidth = img.getWidth(null);
        int imgHeight = img.getHeight(null);
        int x = (GameJframe.SCREEN_WIDTH - imgWidth) / 2;
        int y = (GameJframe.SCREEN_HEIGHT - imgHeight) / 2;
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

    /**
     * Cập nhật lại transition GIF — đảm bảo nó luôn bắt đầu từ frame đầu tiên
     */
    public void setTransitionGif(Image gifImage) {
        if (gifImage == null) return;
        // Buộc flush cache cũ
        gifImage.flush();
        this.transitionGif = gifImage;

        // Tạo lại ImageIcon để Java biết đây là animation mới
        this.transitionIcon = new ImageIcon(gifImage);
    }

    /**
     * Được gọi khi bắt đầu transition mới — reset GIF về frame đầu
     */
    public void restartTransition() {
        if (transitionIcon != null) {
            transitionIcon.getImage().flush();
            this.transitionGif = new ImageIcon(transitionIcon.getImage()).getImage();
        }
    }
}
