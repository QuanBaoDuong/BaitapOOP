package game.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class SoundManager {
    private boolean soundEnabled = true;
    private Clip backgroundClip;
    private final Map<String, Clip> activeEffects = new HashMap<>();

    /** Bật hoặc tắt toàn bộ âm thanh */
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        if (!enabled) stopAll();
    }

    /** Kiểm tra trạng thái âm thanh */
    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    /** Phát nhạc nền (có thể lặp) */
    public void playBackground(String fileName, boolean loop) {
        if (!soundEnabled) return;
        stopBackground();

        try {
            URL url = getClass().getResource("/sound/" + fileName);
            if (url == null) {
                System.out.println(" Không tìm thấy file âm thanh: " + fileName);
                return;
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audio);
            if (loop)
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            else
                backgroundClip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /** Dừng nhạc nền */
    public void stopBackground() {
        if (backgroundClip != null) {
            backgroundClip.stop();
            backgroundClip.close();
            backgroundClip = null;
        }
    }

    /** Phát hiệu ứng âm thanh (1 lần) */
    public void playEffect(String fileName) {
        if (!soundEnabled) return;

        try {
            URL url = getClass().getResource("/sound/" + fileName);
            if (url == null) {
                System.out.println(" Không tìm thấy file âm thanh: " + fileName);
                return;
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
            activeEffects.put(fileName, clip);

            // Tự động xóa sau khi phát xong
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    activeEffects.remove(fileName);
                }
            });

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopAll() {
        stopBackground();
        for (Clip c : activeEffects.values()) {
            if (c.isRunning()) c.stop();
            c.close();
        }
        activeEffects.clear();
    }

    /** Hủy mọi tài nguyên khi thoát game */
    public void dispose() {
        stopAll();
    }
}
