import java.awt.*;

public class BallInteraction {
    private Ball ball;
    private Paddle paddle;

    public BallInteraction(Ball ball, Paddle paddle) {
        this.ball = ball;
        this.paddle = paddle;
    }

    public void checkAllInteractions() {
        checkWallInteraction();
        checkPaddleInteraction();
    }

    private void checkWallInteraction() {
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= GameJframe.SCREEN_WIDTH) {
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
        return ball.getY() > GameJframe.SCREEN_HEIGHT;
    }
}
