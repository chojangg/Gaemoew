import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class showRanking extends JFrame {
    private Connection connection;
    private JTextArea rankingTextArea;

    private void retrieveRankingFromDatabase() {
        String query = "SELECT * FROM gaemoewtbl ORDER BY uscore DESC LIMIT 5";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            StringBuilder rankingBuilder = new StringBuilder();
            int rank = 1;
            while (resultSet.next()) {
                String playerName = resultSet.getString("uname");
                int score = resultSet.getInt("uscore");

                rankingBuilder.append("      ").append(rank).append(". ").append(playerName).append("\t").append(score).append("점").append("\n\n\n");
                rank++;
            }

            rankingTextArea.setText(rankingBuilder.toString());

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 데이터베이스 연결 종료
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public showRanking() {
        // 이미지 파일 경로
        String ruleimagePath = "src/image/top5.png";

        // 창 생성
        JFrame frame = new JFrame("Rank");


        // 창 크기 설정
        frame.setSize(1920, 1080);
        frame.setUndecorated(true);  // 타이틀 바 숨김
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 이미지 패널 생성
        ImagePanel imagePanel = new ImagePanel(ruleimagePath);
        frame.add(imagePanel);

        // 이미지 버튼 생성
        String startBtnPath = "src/image/button/start_button_small1.png";
        String startBtnPath2 = "src/image/button/start_button_small2.png";

        ImageIcon startBtnIcon = new ImageIcon(startBtnPath);
        ImageIcon startBtnIcon2 = new ImageIcon(startBtnPath2);

        JButton startbtn = new JButton(startBtnIcon);

        startbtn.setOpaque(false);  // 배경 투명 설정
        startbtn.setContentAreaFilled(false);  // Content 영역 배경 투명 설정
        startbtn.setBorderPainted(false);  // 테두리 제거

        startbtn.setBorder(BorderFactory.createEmptyBorder(800 , 1580, 0 , 0));
        startbtn.setRolloverIcon(startBtnIcon2); // 버튼에 마우스가 올라갈떄 이미지 변환
        startbtn.setBorderPainted(false); // 버튼 테두리 설정해제
        startbtn.setContentAreaFilled(false);

        // startBtn에 이벤트 리스너 추가
        startbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.main(new String[0]);
                // 현재 프레임 종료
                frame.dispose();
            }
        });

        // 이미지 패널에 버튼 추가 및 정렬 설정
        imagePanel.setLayout(new GridBagLayout());
        imagePanel.add(startbtn, new GridBagConstraints());

        // 데이터베이스 연결
        try {
            String url = "jdbc:mysql://localhost:3308/sys";
            String username = "root";
            String password = "alflarhkgkrrh1!";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // font 설정
        Font font = null;
        try {
            File fontFile = new File("src/font/font.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // 랭킹 텍스트 영역 생성
        rankingTextArea = new JTextArea(13, 20);
        rankingTextArea.setOpaque(false); // 배경 투명 설정
        rankingTextArea.setEditable(false);
        Font customFont = font.deriveFont(55f); // 원하는 폰트 크기로 설정
        rankingTextArea.setFont(customFont); // 폰트 적용

        // 랭킹 텍스트 영역을 스크롤 불가능한 패널에 추가
        JScrollPane scrollPane = new JScrollPane(rankingTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        // 이미지 패널에 스크롤 패널 추가 및 정렬 설정
        imagePanel.setLayout(new GridBagLayout());
        imagePanel.add(scrollPane, new GridBagConstraints());
        scrollPane.setOpaque(false); // 스크롤 패널 배경을 투명하게 설정
        scrollPane.getViewport().setOpaque(false); // 스크롤 패널의 내용 영역 배경을 투명

        SwingUtilities.invokeLater(() -> {
            // 스크롤바 위치를 최상단으로 설정
            rankingTextArea.setCaretPosition(0);
        });

        // 창 표시
        frame.setVisible(true);

        try {
            // 데이터베이스에서 랭킹 정보 가져오기
            retrieveRankingFromDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new showRanking());
    }
}
