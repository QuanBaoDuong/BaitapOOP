package game.map;

import game.object.Brick;
import game.object.NormalBrick;
import game.object.ToughBrick;
import game.object.UnbreakableBrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Map {
    public static List<Brick> createMap(InputStream is, int brickWidth,
                                        int brickHeight, int StartX, int StartY) {

        List<Brick>bricks = new ArrayList<>();
        int horizontalSpacing = 0;
        int verticalSpacing = 0;

        try(BufferedReader file = new BufferedReader(new InputStreamReader(is))) {
            String line;
            int row = 0;

            while ((line=file.readLine())!=null) {
                for (int col=0; col<line.length(); col++) {
                    int x = StartX + col*(brickWidth+horizontalSpacing);
                    int y = StartY + row*(brickHeight+verticalSpacing);
                    int type = Character.getNumericValue(line.charAt(col));

                    switch (type) {
                        case 0:
                            // ô trống, không tạo gạch
                            break;
                        case 1:
                            bricks.add(new NormalBrick(x, y, brickWidth, brickHeight));
                            break;
                        case 2:
                            bricks.add(new ToughBrick(x, y, brickWidth, brickHeight));
                            break;
                        case 3:
                            bricks.add(new UnbreakableBrick(x, y, brickWidth, brickHeight));
                            break;
                    }
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bricks;

    }
}
