import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class EndingSuccess extends JFrame {
    public class ImagePanel extends JPanel {
        private Image image;

        public ImagePanel(String imagePath) {
            this.image = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int imageWidth = image.getWidth(this);
            int imageHeight = image.getHeight(this);
            int x = (panelWidth - imageWidth) / 2;
            int y = (panelHeight - imageHeight) / 2;
            g.drawImage(image, x, y, this);
        }
    }

    public EndingSuccess() {
        // 이미지 파일 경로
        String ruleimagePath = "src/image/ending_success.gif";

        // 창 생성
        JFrame frame = new JFrame("게임방법");
        frame.setSize(1920, 1080);
//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  // 전체 화면으로 설정
        frame.setUndecorated(true);  // 타이틀 바 숨김
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 이미지 패널 생성
        ImagePanel imagePanel = new ImagePanel(ruleimagePath);
        frame.add(imagePanel);

        // 창 표시
        frame.setVisible(true);

        try {
            File file = new File("src/bgm/victory.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
        } catch (Exception e) {
            System.err.println("Put the music.wav file in the sound folder if you want to play background music, only optional!");
        }

        // 3초 후에 showRanking.java 파일 실행
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                showRanking();
                frame.dispose(); // 현재 창 닫기
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000);
    }

    private void showRanking() {
        SwingUtilities.invokeLater(() -> {
            showRanking.main(new String[0]);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EndingSuccess());
    }
}
