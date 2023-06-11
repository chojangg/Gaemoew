import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class WriteName extends JFrame {
    public WriteName() {
        // 이미지 파일 경로
        String ruleimagePath = "src/image/name.png";

        // 창 생성
        JFrame frame = new JFrame("Write Your Name");
        frame.setSize(1920, 1080);
//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  // 전체 화면으로 설정
        frame.setUndecorated(true);  // 타이틀 바 숨김
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 이미지 패널 생성
        ImagePanel imagePanel = new ImagePanel(ruleimagePath);
        frame.add(imagePanel);

        // 이미지 패널에 버튼 추가 및 정렬 설정
        imagePanel.setLayout(new GridBagLayout());

        // 창 표시
        frame.setVisible(true);

        try {
            File file = new File("bgm/Little-Samba-Quincas-Moreira.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
        } catch (Exception e) {
            System.err.println("Put the music.wav file in the sound folder if you want to play background music, only optional!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WriteName());
    }
}
