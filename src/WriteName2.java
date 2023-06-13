import javax.swing.*;
import java.awt.Font;
import java.io.File;

public class WriteName2 extends JFrame {

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
        }

        // Play.java 실행
        Play.main(new String[]{});
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WriteName2());
    }
}
