package game.manager;

import game.state.GameState;

import java.awt.*;
import java.util.Stack;

public class GameStateManager {

    private Stack<GameState> states;

    public GameStateManager() {
        states = new Stack<>();
    }

    // Thêm state mới lên stack (ví dụ popup, setting)
    public void push(GameState state) {
        states.push(state);
    }

    // Xóa state hiện tại
    public void pop() {
        if (!states.isEmpty()) {
            states.pop();
        }
    }

    // Thay state hiện tại bằng state mới
    public void setStates(GameState state) {
        if (!states.isEmpty()) states.pop();
        states.push(state);
    }

    public void update() {
        if (!states.isEmpty()) states.peek().update();
    }

    public void draw(Graphics g) {
        if (!states.isEmpty()) states.peek().draw(g);
    }

    public void keyPressed(int keyCode) {
        if (!states.isEmpty()) states.peek().keyPressed(keyCode);
    }

    public void keyReleased(int keyCode) {
        if (!states.isEmpty()) states.peek().keyReleased(keyCode);
    }

    public GameState getCurrentState() {
        return states.isEmpty() ? null : states.peek();
    }
}
