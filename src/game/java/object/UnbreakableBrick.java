package game.object;

public class UnbreakableBrick extends Brick {
    public UnbreakableBrick(int x, int y, int width, int height) {
        super(x, y, width, height, "/game/resources/image/UnbreakableBrick.jpg",false);
    }

    @Override
    public void takeHit() {}
}
