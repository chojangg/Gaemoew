import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class BackMove {
    ImageIcon backIc = new ImageIcon("src/image/background.png");
    Image backImg = backIc.getImage();

    //   1번째 이미지
    int back1X = 0;

    //   2번쨰 이미지가 뒤따라 와야하므로 backImg의 넓이를 가져온다.
    int back2X = backImg.getWidth(null);

    private JFrame frame;


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BackMove window = new BackMove();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class MyPanel extends JPanel{
        public MyPanel() {

            setFocusable(true);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    while(true) {
                        back1X--;
                        back2X--;

//                  이미지가 화면 밖으로 완전히 나가면
//                  X축을 이미지의 넓이 좌표로 다시 옮긴다.
//                  1번 이미지가 먼저 나가서 2번 뒤에 붙고
//                  2번 이미지가 나가면 다시 1번 뒤에 붇는 다.

                        if(back1X < -(backImg.getWidth(null))) {
                            back1X = backImg.getWidth(null);
                        }
                        if(back2X < -(backImg.getWidth(null))) {
                            back2X = backImg.getWidth(null);
                        }

                        repaint();
                        try {
                            Thread.sleep(10);
                        }catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        @Override
        protected void paintComponent(Graphics g) {   //그림 그려주는 메서드
            super.paintComponent(g);   //캔버스를 비워주는 메서드

            g.drawImage(backImg, back1X, 0, this);   //1번 그림
            g.drawImage(backImg, back2X, 0, this);   //2번 그림
        }
    }


    public BackMove() {
        initialize();
    }


    private void initialize() {
        frame = new JFrame();
        frame.setTitle("배경화면 흐르기");
        frame.setBounds(0, 0, 1300, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new MyPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);
    }

}