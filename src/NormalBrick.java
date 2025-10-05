public class NormalBrick extends Brick {

    public NormalBrick(int x, int y, int width, int height) {
        super(x, y, width, height, "/image/NormalBrick_red.png");
    }

    @Override
    public void takeHit() {
        destroyed = true;
    }
}
