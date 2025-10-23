package game.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {

    public static void playSound(String fileName, boolean loop) {
        new Thread(() -> {
            try {
                // Tìm file trong classpath (hỗ trợ chạy từ bất kỳ package nào)
                URL soundURL = Sound.class.getResource("/sound/" + fileName);


                if (soundURL == null) {
                    System.out.println("⚠️ Không tìm thấy file âm thanh: sound/" + fileName);
                    return;
                }

                System.out.println("🎵 Đã tìm thấy file: " + soundURL);

                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);

                if (loop) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.start();
                }

                System.out.println("✅ Đang phát âm thanh: " + fileName);

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
