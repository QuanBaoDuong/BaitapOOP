package game.object;

public class UnbreakableBrick extends Brick {
    public UnbreakableBrick(int x, int y, int width, int height) {
        super(x, y, width, height, "/image/UnbreakableBrick.jpg");
    }

    @Override
    public void takeHit() {}
}
