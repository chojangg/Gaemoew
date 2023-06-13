import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class showRanking extends JFrame {
    public showRanking() {
        // 이미지 파일 경로
        String ruleimagePath = "src/image/top5.png";

        // 창 생성
        JFrame frame = new JFrame("Rank");
        frame.setSize(1920, 1080);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  // 전체 화면으로 설정
        frame.setUndecorated(true);  // 타이틀 바 숨김
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 이미지 패널 생성
        ImagePanel imagePanel = new ImagePanel(ruleimagePath);
        frame.add(imagePanel);

        // 이미지 버튼 생성
        String skipBtnPath = "src/image/button/skip_button-01.png";
        String skipBtnPath2 = "src/image/button/skip_button-02.png";

        ImageIcon startBtnIcon = new ImageIcon(skipBtnPath);
        ImageIcon startBtnIcon2 = new ImageIcon(skipBtnPath2);

        JButton skipbtn = new JButton(startBtnIcon);

        skipbtn.setOpaque(false);  // 배경 투명 설정
        skipbtn.setContentAreaFilled(false);  // Content 영역 배경 투명 설정
        skipbtn.setBorderPainted(false);  // 테두리 제거

        skipbtn.setBorder(BorderFactory.createEmptyBorder(1190 , 2150, 0 , 0));
        skipbtn.setRolloverIcon(startBtnIcon2); // 버튼에 마우스가 올라갈떄 이미지 변환
        skipbtn.setBorderPainted(false); // 버튼 테두리 설정해제
        skipbtn.setContentAreaFilled(false);

        // skipbtn에 이벤트 리스너 추가
        skipbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 홈으로 가기
                Main.main(new String[0]);
                // 현재 프레임 종료
                frame.dispose();
            }
        });

        // 이미지 패널에 버튼 추가 및 정렬 설정
        imagePanel.setLayout(new GridBagLayout());
        imagePanel.add(skipbtn, new GridBagConstraints());

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
        SwingUtilities.invokeLater(() -> new showRanking());
    }
}
