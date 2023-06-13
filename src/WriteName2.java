import javax.swing.*;
import java.awt.Font;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WriteName2 extends JFrame {

    private static final String DB_URL = "jdbc:mysql://localhost:3308/sys"; // 데이터베이스 URL
    private static final String DB_USER = "root"; // 데이터베이스 사용자 이름
    private static final String DB_PASSWORD = "alflarhkgkrrh1!"; // 데이터베이스 비밀번호

    public WriteName2() {
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/font.ttf")); // 폰트 파일 경로를 적절히 수정
            UIManager.put("OptionPane.messageFont", customFont.deriveFont(Font.PLAIN, 12));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String name = JOptionPane.showInputDialog(null, "이름을 입력해주세요.", "Write Your Name", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.isEmpty()) {
            System.out.println("입력된 이름: " + name);
            insertNameToDatabase(name); // 데이터베이스에 이름 삽입
        }

        // Play.java 실행
        Play.main(new String[]{});
    }

    private void insertNameToDatabase(String name) {
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
        SwingUtilities.invokeLater(() -> new WriteName2());
    }
}
