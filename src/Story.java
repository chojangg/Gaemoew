import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Story {
    public static void main(String[] args) {
        // GIF를 보여줄 JFrame 생성
        JFrame frame = new JFrame();
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 기본 그래픽 디바이스 가져오기
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        // 프레임 크기를 화면 크기와 동일하게 설정
        frame.setSize(graphicsDevice.getDisplayMode().getWidth(), graphicsDevice.getDisplayMode().getHeight());

        // 프레임 위치를 (0, 0)으로 설정
        frame.setLocation(0, 0);

        // 커스텀 패널 생성
        CustomPanel panel = new CustomPanel("src/image/story_text.png");

        // 커스텀 패널 크기를 프레임 크기와 동일하게 설정
        panel.setSize(frame.getSize());

        // 커스텀 패널의 레이아웃을 중앙 정렬로 설정하여 배경 이미지를 가운데로 정렬
        panel.setLayout(new BorderLayout());

        // 일정 시간 후에 프레임을 닫고 Play 화면 시작
        int gifDuration = 4000; // GIF 지속 시간 (4초)
        Timer timer = new Timer(gifDuration, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 프레임 닫기
                frame.dispose();
                // Play 화면 시작
                WriteName2.main(new String[0]);
            }
        });

        // 타이머 시작
        timer.setRepeats(false);
        timer.start();

        // 프레임을 전체 화면으로 설정
        graphicsDevice.setFullScreenWindow(frame);

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
