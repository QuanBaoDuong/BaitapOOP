package game.object;

public class ToughBrick extends Brick {

    private int hitCount = 0; // số lần bóng đập vào
    private final int hitsToBreak = 2; // cần 2 lần mới vỡ
    private String crackedImagePath = "/game/resources/image/ToughBrick_Cracked.png";

    public ToughBrick(int x, int y, int width, int height) {
        super(x, y, width, height, "/game/resources/image/ToughBrick.png", true);
    }

    @Override
    public void takeHit() {
        hitCount++;
        if (hitCount == 1) {
            // đổi hình sang gạch nứt
            try {
                java.awt.Image crackedImg = new javax.swing.ImageIcon(
                        getClass().getResource(crackedImagePath)
                ).getImage();
                java.lang.reflect.Field field = Brick.class.getDeclaredField("brickImage");
                field.setAccessible(true);
                field.set(this, crackedImg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (hitCount >= hitsToBreak) {
            destroyed = true;
        }
    }
}
