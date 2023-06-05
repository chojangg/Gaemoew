import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        // 이미지 파일 경로
        String homeimagePath = "src/image/home.png";

        // 창 생성
        JFrame frame = new JFrame();
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 이미지 패널 생성
        ImagePanel imagePanel = new ImagePanel(homeimagePath);
        frame.add(imagePanel);

        // 이미지 버튼 생성
        String startBtnPath = "src/image/button/start_button-01.png";
        String ruleBtnPath = "src/image/button/rule_button-01.png";
        ImageIcon startBtnIcon = new ImageIcon(startBtnPath);
        ImageIcon ruleBtnIcon = new ImageIcon(ruleBtnPath);

        JButton startbtn = new JButton(startBtnIcon);
        JButton rulebtn = new JButton(ruleBtnIcon);

        startbtn.setOpaque(false);  // 배경 투명 설정
        startbtn.setContentAreaFilled(false);  // Content 영역 배경 투명 설정
        startbtn.setBorderPainted(false);  // 테두리 제거

        rulebtn.setOpaque(false);  // 배경 투명 설정
        rulebtn.setContentAreaFilled(false);  // Content 영역 배경 투명 설정
        rulebtn.setBorderPainted(false);  // 테두리 제거

        startbtn.setBounds(300, 700, startBtnIcon.getIconWidth(), startBtnIcon.getIconHeight());
        rulebtn.setBounds(0, 0, ruleBtnIcon.getIconWidth(), ruleBtnIcon.getIconHeight());

        // startBtn에 이벤트 리스너 추가
        startbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // play.java 파일 실행
                ProcessBuilder pb = new ProcessBuilder("java", "Game");
                try {
                    pb.start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        imagePanel.add(startbtn);
        imagePanel.add(rulebtn);

        // 창 표시
        frame.setVisible(true);
    }
}
