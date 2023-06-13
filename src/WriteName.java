import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WriteName extends JFrame {
    public WriteName() {
        int width = 1920;    // 프레임 가로
        int height = 1080;   // 프레임 높이
        String yourname;         // 이름
        // 이미지 파일 경로
        String ruleImagePath = "src/image/name.png";

        // 창 생성
        JFrame frame = new JFrame("Write Your Name");
        frame.setSize(width, height);
        // frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  // 전체 화면으로 설정
        frame.setUndecorated(true);  // 타이틀 바 숨김
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 이미지 패널 생성
        ImagePanel imagePanel = new ImagePanel(ruleImagePath);
        frame.add(imagePanel);

        // 이미지 패널에 텍스트를 표시할 JLabel 생성
        JLabel label = new JLabel("이름을 입력해주세요.");
        label.setForeground(Color.BLACK);

        // 마진 생성 및 설정
        int margin = 200;  // 원하는 마진 크기 설정
        EmptyBorder marginBorder = new EmptyBorder(margin, margin, margin, margin);
        label.setBorder(marginBorder);

        // 폰트 설정
        Font koreanFont = null;
        try {
            koreanFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/font.ttf")).deriveFont(Font.PLAIN, 80);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(koreanFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        if (koreanFont != null) {
            label.setFont(koreanFont);
        }

        // 이미지 패널에 JLabel 추가
        imagePanel.add(label);

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
        String name = JOptionPane.showInputDialog(null, "이름을 입력하세요:", "이름 입력", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.isEmpty()) {
            System.out.println("입력된 이름: " + name);
        }
        SwingUtilities.invokeLater(() -> new WriteName());
    }
}