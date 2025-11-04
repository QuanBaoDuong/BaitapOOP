package game.manager;

import java.io.*;

public class HighScoreManager {
    private static final String FILE_NAME = "highscore.dat";
    private static int highScore = 0;

    // Đọc điểm cao nhất từ file (khi game khởi động)
    static {
        loadHighScore();
    }

    public static int getHighScore() {
        return highScore;
    }

    // Ghi điểm mới nếu cao hơn điểm cũ
    public static void checkNewScore(int score) {
        if (score > highScore) {
            highScore = score;
            saveHighScore();
        }
    }

    private static void loadHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line = reader.readLine();
            if (line != null) {
                highScore = Integer.parseInt(line.trim());
            }
        } catch (IOException | NumberFormatException e) {
            highScore = 0; // nếu chưa có file hoặc lỗi
        }
    }

    private static void saveHighScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write(String.valueOf(highScore));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
