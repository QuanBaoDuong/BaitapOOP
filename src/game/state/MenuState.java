package game.state;

import game.manager.GameStateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MenuState implements GameState, MouseListener, MouseMotionListener {
        private GameStateManager gameStateManager;
        private JPanel panel ;

        private String[] options = {"NEW GAME"};
        private Rectangle[] optionBounds;
        private int currentChoice = -1;

        private Image backGroundMenu ;
        private Image newGameBig;
        private Image newGame;

        public MenuState (JPanel panel, GameStateManager gameStateManager) {
            this.panel = panel;
            this.gameStateManager = gameStateManager;

            optionBounds = new Rectangle[options.length];

            panel.addMouseListener(this);
            panel.addMouseMotionListener(this);

            backGroundMenu = new ImageIcon(getClass().getResource("/image/BackGround.jpg")).getImage();
            newGameBig = new ImageIcon(getClass().getResource("/image/NewGame.png")).getImage();

            int buttonWidth = 140;
            int buttonHeight = 40;
            newGame = newGameBig.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);

            // tạo mảng bounds (1 phần tử)
            optionBounds = new Rectangle[options.length];

            // xác định vị trí nút
            int x = 440;
            int y = 300;
            optionBounds[0] = new Rectangle(x, y, buttonWidth, buttonHeight);
        }
        @Override
        public void update() {}

        @Override
        public void draw (Graphics g) {
            g.drawImage(backGroundMenu,0,0,null);
            g.drawImage(newGame,440,300,null);

        }

    @Override public void keyPressed(int keyCode) {}

    @Override public void keyReleased(int keyCode) {}

    @Override
        public void mouseClicked (MouseEvent e) {
            for (int i = 0; i < optionBounds.length; i++) {
                if (optionBounds[i].contains(e.getPoint())) {
                    executeOption(i); // xử lý action tùy nút
                }
            }
        }
    private void executeOption(int index) {
        switch (index) {
            case 0: // NEW GAME
                gameStateManager.setStates(new PlayState(panel, gameStateManager));
                break;
        }
    }
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    // --- MouseMotionListener ---
    @Override
    public void mouseMoved(MouseEvent e) {
        currentChoice = -1;
        for (int i = 0; i < optionBounds.length; i++) {
            if (optionBounds[i].contains(e.getPoint())) {
                currentChoice = i; // hover nút
            }
        }
    }

    @Override public void mouseDragged(MouseEvent e) {}

}
