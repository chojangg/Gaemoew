import javax.swing.*;
import java.awt.*;

// 이미지를 표시하는 패널 클래스
class ImagePanel extends JPanel {
    private Image image;   // 이미지

    public ImagePanel(String imagePath) {
        // 이미지 파일 로드
        image = new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 이미지를 패널에 그리기
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }
}
