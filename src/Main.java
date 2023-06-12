import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        // 이미지 파일 경로
        String homeimagePath = "src/image/home.png";

        // 창 생성
        JFrame frame = new JFrame("계묘년을 지켜라!");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  // 전체 화면으로 설정
        frame.setUndecorated(true);  // 타이틀 바 숨김
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 이미지 패널 생성
        ImagePanel imagePanel = new ImagePanel(homeimagePath);
        frame.add(imagePanel);

        // 이미지 버튼 생성
        String startBtnPath = "src/image/button/start_button-01.png";
        String startBtnPath2 = "src/image/button/start_button-02.png";
        String ruleBtnPath = "src/image/button/rule_button-01.png";
        String ruleBtnPath2 = "src/image/button/rule_button-02.png";

        ImageIcon startBtnIcon = new ImageIcon(startBtnPath);
        ImageIcon startBtnIcon2 = new ImageIcon(startBtnPath2);
        ImageIcon ruleBtnIcon = new ImageIcon(ruleBtnPath);
        ImageIcon ruleBtnIcon2 = new ImageIcon(ruleBtnPath2);

        JButton startbtn = new JButton(startBtnIcon);
        JButton rulebtn = new JButton(ruleBtnIcon);

        startbtn.setOpaque(false);  // 배경 투명 설정
        startbtn.setContentAreaFilled(false);  // Content 영역 배경 투명 설정
        startbtn.setBorderPainted(false);  // 테두리 제거

        rulebtn.setOpaque(false);  // 배경 투명 설정
        rulebtn.setContentAreaFilled(false);  // Content 영역 배경 투명 설정
        rulebtn.setBorderPainted(false);  // 테두리 제거

        startbtn.setBorder(BorderFactory.createEmptyBorder(1000 , 0, 0 , 0));
        startbtn.setRolloverIcon(startBtnIcon2); // 버튼에 마우스가 올라갈떄 이미지 변환
        startbtn.setBorderPainted(false); // 버튼 테두리 설정해제
        startbtn.setContentAreaFilled(false);

        // startBtn에 이벤트 리스너 추가
        startbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Story 실행
                Story.main(new String[0]);
                // 현재 프레임 종료
                frame.dispose();
            }
        });

        rulebtn.setBorder(BorderFactory.createEmptyBorder(1000 , 0, 0 , 0));
        rulebtn.setRolloverIcon(ruleBtnIcon2); // 버튼에 마우스가 올라갈떄 이미지 변환
        rulebtn.setBorderPainted(false); // 버튼 테두리 설정해제
        rulebtn.setContentAreaFilled(false);

        // rulebtn에 이벤트 리스너 추가
        rulebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new rule();
            }
        });

        imagePanel.add(rulebtn);
        imagePanel.add(startbtn);

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
