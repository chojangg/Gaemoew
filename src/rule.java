import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class rule {
    public static void main(String[] args) {
        // 이미지 파일 경로
        String ruleimagePath = "src/image/rule.png";

        // 창 생성
        JFrame frame = new JFrame("게임성공");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  // 전체 화면으로 설정
        frame.setUndecorated(true);  // 타이틀 바 숨김
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 이미지 패널 생성
        ImagePanel imagePanel = new ImagePanel(ruleimagePath);
        frame.add(imagePanel);

        // 이미지 버튼 생성
        String startBtnPath = "src/image/button/start_button-01.png";
        String startBtnPath2 = "src/image/button/start_button-02.png";

        ImageIcon startBtnIcon = new ImageIcon(startBtnPath);
        ImageIcon startBtnIcon2 = new ImageIcon(startBtnPath2);

        JButton startbtn = new JButton(startBtnIcon);

        startbtn.setOpaque(false);  // 배경 투명 설정
        startbtn.setContentAreaFilled(false);  // Content 영역 배경 투명 설정
        startbtn.setBorderPainted(false);  // 테두리 제거

        startbtn.setBorder(BorderFactory.createEmptyBorder(0 , 0, 0 , 0));
        startbtn.setRolloverIcon(startBtnIcon2); // 버튼에 마우스가 올라갈떄 이미지 변환
        startbtn.setBorderPainted(false); // 버튼 테두리 설정해제
        startbtn.setContentAreaFilled(false);

        // startBtn에 이벤트 리스너 추가
        startbtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new Game();   // 화면 전환
            }
        });

        // 이미지 패널에 버튼 추가 및 정렬 설정
        imagePanel.setLayout(new GridBagLayout());
        imagePanel.add(startbtn, new GridBagConstraints());

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
}