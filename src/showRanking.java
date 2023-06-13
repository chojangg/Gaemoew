import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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

                rankingBuilder.append(rank).append(". ").append(playerName).append(": ").append(score).append("\n");
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
        frame.setSize(1920, 1080);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  // 전체 화면으로 설정
        frame.setUndecorated(true);  // 타이틀 바 숨김
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 이미지 패널 생성
        ImagePanel imagePanel = new ImagePanel(ruleimagePath);
        frame.add(imagePanel);

        // 데이터베이스 연결
        try {
            String url = "jdbc:mysql://localhost:3308/sys";
            String username = "root";
            String password = "alflarhkgkrrh1!";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 랭킹 텍스트 영역 생성
        rankingTextArea = new JTextArea(10, 20);
        rankingTextArea.setEditable(false);
        rankingTextArea.setFont(new Font("Malgun Gothic", Font.PLAIN, 16)); // 한국어를 지원하는 폰트로 변경

        // 랭킹 텍스트 영역을 스크롤 가능한 패널에 추가
        JScrollPane scrollPane = new JScrollPane(rankingTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // 이미지 패널에 스크롤 패널 추가 및 정렬 설정
        imagePanel.setLayout(new GridBagLayout());
        imagePanel.add(scrollPane, new GridBagConstraints());

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
