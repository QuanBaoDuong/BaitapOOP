package game.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Sound {
    private static final Map<String, Clip> activeClips = new HashMap<>();
    private static Clip backgroundMusic;
    private static boolean soundEnabled = true;

    public static void playSound(String fileName, boolean loop) {
        if (!soundEnabled || fileName.isEmpty()) return;

        new Thread(() -> {
            try {
                URL soundURL = Sound.class.getResource("/sound/" + fileName);
                if (soundURL == null) {
                    System.out.println("⚠️ Không tìm thấy file âm thanh: sound/" + fileName);
                    return;
                }

                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);

                if (loop) {
                    // Lưu clip nhạc nền để có thể dừng sau này
                    if (backgroundMusic != null) {
                        backgroundMusic.stop();
                        backgroundMusic.close();
                    }
                    backgroundMusic = clip;
                } else {
                    // Lưu sound effect
                    activeClips.put(fileName, clip);
                }

                if (loop) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.start();
                }

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Method để dừng tất cả âm thanh
    public static void stopAllSounds() {
        // Dừng nhạc nền
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.close();
            backgroundMusic = null;
        }

        // Dừng tất cả sound effects
        for (Clip clip : activeClips.values()) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }
        }
        activeClips.clear();
    }

    // Bật/tắt âm thanh toàn cục
    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
        if (!enabled) {
            stopAllSounds();
        }
    }

    // Kiểm tra trạng thái âm thanh
    public static boolean isSoundEnabled() {
        return soundEnabled;
    }
}
