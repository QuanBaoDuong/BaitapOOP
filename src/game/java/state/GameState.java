package game.state;

import java.awt.*;

public interface GameState {
    void update();
    void draw(Graphics g);
    void keyPressed (int keyCode);
    void keyReleased (int keyCode);
}
