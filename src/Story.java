import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Story {
    private static final int GIF_DURATION = 500; // GIF 지속 시간
    private static final int COUNTDOWN_DURATION = 2000; // 카운트 다운 지속 시간

    public static void main(String[] args) {
        // GIF를 보여줄 JFrame 생성
        JFrame frame = new JFrame();
        frame.setUndecorated(true);  // 타이틀 바 숨김

        // 기본 그래픽 디바이스 가져오기
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        // 프레임 크기를 화면 크기와 동일하게 설정
        frame.setSize(1920, 1080);

        // 프레임 위치를 (0, 0)으로 설정
        frame.setLocation(0, 0);

        // 커스텀 패널 생성
        CustomPanel panel = new CustomPanel("src/image/story_text.png");

        // 커스텀 패널 크기를 프레임 크기와 동일하게 설정
        panel.setSize(1920, 1080);

        // 커스텀 패널의 레이아웃을 중앙 정렬로 설정하여 배경 이미지를 가운데로 정렬
        panel.setLayout(new BorderLayout());

        // 카운트 다운 레이블 생성
        JLabel countdownLabel = new JLabel();
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 150));
        countdownLabel.setVerticalAlignment(SwingConstants.TOP);
        countdownLabel.setForeground(Color.WHITE);
        countdownLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(countdownLabel, BorderLayout.CENTER);

        // 카운트 다운 시작
        Timer countdownTimer = new Timer(900, new ActionListener() {
            private int count = 3;

            @Override
            public void actionPerformed(ActionEvent e) {
                countdownLabel.setText(String.valueOf(count));
                if (count == 0) {
                    ((Timer) e.getSource()).stop(); // 카운트 다운 종료
                    panel.remove(countdownLabel); // 레이블 제거

                    // 일정 시간 후에 프레임을 닫고 Play 화면 시작
                    Timer gifTimer = new Timer(GIF_DURATION, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // 프레임 닫기
                            frame.dispose();
                            WriteName2.main(new String[0]);
                        }
                    });

                    // 타이머 시작
                    gifTimer.setRepeats(false);
                    gifTimer.start();
                }
                count--;
            }
        });

        // 카운트 다운 시작을 COUNTDOWN_DURATION(ms) 이후로 지연
        Timer startTimer = new Timer(COUNTDOWN_DURATION, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countdownTimer.start(); // 카운트 다운 시작
                try {
                    File file = new File("src/bgm/countdown.wav");
                    Clip clip = AudioSystem.getClip();
                    clip.open(AudioSystem.getAudioInputStream(file));
                    clip.start();
                } catch (Exception f) {
                    System.err.println("Put the music.wav file in the sound folder if you want to play background music, only optional!");
                }
            }
        });

        // 타이머 시작
        startTimer.setRepeats(false);
        startTimer.start();

        // 커스텀 패널을 프레임의 콘텐트 패널로 설정
        frame.setContentPane(panel);

        // 프레임 표시
        frame.setVisible(true);
    }

    static class CustomPanel extends JPanel {
        private Image backgroundImage;

        public CustomPanel(String imagePath) {
            // 배경 이미지 로드
            ImageIcon icon = new ImageIcon(imagePath);
            backgroundImage = icon.getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // 배경 이미지 그리기
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
