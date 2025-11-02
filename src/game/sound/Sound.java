package game.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {
    private static Clip bgmClip;

    public static void playSound(String fileName, boolean loop) {

        new Thread(() -> {
            try {
                // Tìm file trong classpath (hỗ trợ chạy từ bất kỳ package nào)
                URL soundURL = Sound.class.getResource("/sound/" + fileName);


                if (soundURL == null) {
                    System.out.println("⚠️ Không tìm thấy file âm thanh: sound/" + fileName);
                    return;
                }

                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);

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
    public static void stopBGM() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
            bgmClip.close();
        }
    }
}
