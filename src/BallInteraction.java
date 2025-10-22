/*
import java.awt.*;

public class BallInteraction {
    private game.object.Ball ball;
    private game.object.Paddle paddle;

    public BallInteraction(game.object.Ball ball, game.object.Paddle paddle) {
        this.ball = ball;
        this.paddle = paddle;
    }

    public void checkAllInteractions() {
        checkWallInteraction();
        checkPaddleInteraction();
    }

    private void checkWallInteraction() {
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= game.main.GameJframe.SCREEN_WIDTH) {
            ball.reverseX();
        }
        if (ball.getY() <= 0) {
            ball.reverseY();
        }
    }

    private void checkPaddleInteraction() {
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.reverseY();
            ball.setY(paddle.getY() - ball.getHeight());
        }
    }

    public boolean isBallOutOfBounds() {
        return ball.getY() > game.main.GameJframe.SCREEN_HEIGHT;
    }
}
*/
