package game.map;

import game.object.Brick;
import game.object.NormalBrick;

public class Map1 {

    public static Brick[][] createMap(int rows, int cols, int brickWidth, int brickHeight) {
        Brick[][] bricks = new Brick[rows][cols];

        int startX = 0;
        int startY = 100;
        int hGap = 0;
        int vGap = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = startX + col * (brickWidth + hGap);
                int y = startY + row * (brickHeight + vGap);
                bricks[row][col] = new NormalBrick(x, y, brickWidth, brickHeight);
            }
        }

        return bricks;
    }
}
