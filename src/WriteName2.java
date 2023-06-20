import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WriteName2 extends JDialog {

    private static final String DB_URL = "jdbc:mysql://localhost:3308/sys"; // 데이터베이스 URL
    private static final String DB_USER = "root"; // 데이터베이스 사용자 이름
    private static final String DB_PASSWORD = "alflarhkgkrrh1!"; // 데이터베이스 비밀번호

    private String name;

    public WriteName2() {
        // Font 설정
        Font font = null;
        try {
            File fontFile = new File("src/font/font.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JTextField nameField = new JTextField();
        nameField.setColumns(20); // 입력 필드의 칼럼 수 설정
        nameField.setForeground(Color.darkGray);
        nameField.setFont(font.deriveFont(Font.PLAIN, 40)); // 입력하는 글씨의 크기 설정

        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/image/name.png"); // 배경 이미지 파일 경로를 적절히 수정

                // 패널의 크기에 맞게 이미지 크기를 조정
                Image image = background.getImage();
                Image scaledImage = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                background = new ImageIcon(scaledImage);

                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        // 현재 사용자의 화면 크기로 패널 크기 설정
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        panel.setPreferredSize(screenSize);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(20, 20, 20, 20);

        JLabel nameLabel = new JLabel("이름을 입력해주세요.");
        nameLabel.setFont(font.deriveFont(Font.PLAIN, 45));
        panel.add(nameLabel, constraints);

        constraints.gridy = 1;
        panel.add(nameField, constraints);

        setTitle("Write Your Name");
        setUndecorated(true); // 타이틀 바 숨기기
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(panel);

        setBounds(0, 0, screenSize.width, screenSize.height); // 전체 화면으로 설정

        setVisible(true); // 다이얼로그를 표시

        nameField.addActionListener(e -> {
            String name = nameField.getText();
            if (!name.isEmpty()) {
                System.out.println("입력된 이름: " + name);
                insertNameToDatabase(name); // 데이터베이스에 이름 삽입
            } else {
                System.out.println("이름이 입력되지 않았습니다.");
            }

            try {
                File file = new File("src/bgm/start.wav");
                Clip clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(file));
                clip.start();
            } catch (Exception ee) {
                System.err.println("Put the music.wav file in the sound folder if you want to play background music, only optional!");
            }
            Play.main(new String[]{name}); // Play.java 파일 실행
            dispose(); // 현재 창 닫기
        });
    }

    private void insertNameToDatabase(String name) {
        this.name = name;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO gaemoewtbl (uname) VALUES (?)"; // 테이블 이름 및 컬럼명에 맞게 수정
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.executeUpdate();
            }
            System.out.println("이름이 데이터베이스에 삽입되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WriteName2 writeName2 = new WriteName2();
            writeName2.setVisible(true); // 다이얼로그를 표시
        });
    }
}
