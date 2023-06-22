import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class Game extends JFrame implements KeyListener, Runnable {
    int width = 1920;    //프레임 가로
    int height = 1080;    //프레임 높이
    int x = 250, y = 490;  //플레이어 좌표 변수
    int score = 0;    //게임 점수
    int life = 3;
    private int coin_random;    // 코인 랜덤 y좌표
    Random random = new Random();

    Game() {
        getimg();  //사진 불러오기
        gogo();
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // 전체 화면으로 설정
        setUndecorated(true);  // 타이틀 바 숨김
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("계묘년을 지켜라!");  //프레임 이름
        setResizable(false);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());  // 프레임 크기를 디스플레이 크기로 설정
        pack();  // 컴포넌트의 크기에 맞게 프레임 크기 조정
        setVisible(true);

    }

    Image rabbit_img;       //플레이어 이미지
    Image background_img;   //배경화면 이미지
    Image explode_img;      //폭발 이미지
    Image sparkle_img;      //반짝이
    Image bat_img;          //방망이 이미지
    Image rock_img;         //장애물 이미지
    Image coin_img;         // 코인 이미지
    Image heart1;           // 하트 1개

    public void getimg() {
        try {
            bat_img = new ImageIcon("src/image/bat.png").getImage();
            rock_img = new ImageIcon("src/image/rock-1.png").getImage();
            coin_img = new ImageIcon("src/image/coin.gif").getImage();
            rabbit_img = new ImageIcon("src/image/rabbit.gif").getImage();
            background_img = new ImageIcon("src/image/background.png").getImage();
            explode_img = new ImageIcon("src/image/explode.png").getImage();
            sparkle_img = new ImageIcon("src/image/sparkle.png").getImage();
            heart1 = new ImageIcon("src/image/heart/heart.png").getImage();
        } catch (Exception e){
            System.out.println("파일을 열 수 없습니다. ");
        }
    }

    Thread th;  //스레드 생성
    public void gogo() {
        addKeyListener(this);       //키보드 이벤트 실행
        th = new Thread(this);  //스레드 생성
        th.start();  //스레드 실행
    }

    Bat bat;           //방망이 클래스
    Rock rock;           //장애물 클래스
    Coin coin;           //코인 클래스
    Heart heart;         //하트 클래스
    Explode explosion;   //폭발 클래스
    Spark spark;   //폭발 클래스

    class Bat {
        int x;  //화살 좌표
        int y;

        Bat(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move() {
            x += 10;   //화살 10만큼 오른쪽으로 쏘기
        }
    }

    class Rock {
        int x;  //장애물 좌표
        int y;

        Rock(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move() {
            x -= rock_speed;     //장애물 7만큼 왼쪽으로 움직이기
        }
    }

    class Coin {
        int x;
        int y;

        Coin(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move(){
            x -= coin_speed;
        }
    }

    class Heart {
        int x;
        int y;

        Heart(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move(){
            x -= heart_speed;
        }
    }

    class Explode {
        int x;  //폭발 효과 좌표
        int y;
        int maintain_int; //폭발 효과가 유지되는 시간을 위한 변수
        int situation;    //폭발이 일어나는 상황
        //0:장애물이 화살에 맞았을 때, 1:플레이어와 장애물이 충했을 때

        Explode(int x, int y, int situation) {
            this.x = x;
            this.y = y;
            this.situation = situation;
            maintain_int = 0;
        }

        public void maintain() {
            maintain_int++;     //유지될 타임 변수 카운트
        }
    }

    class Spark {
        int x;  //폭발 효과 좌표
        int y;
        int maintain_int; //폭발 효과가 유지되는 시간을 위한 변수

        Spark(int x, int y) {
            this.x = x;
            this.y = y;
            maintain_int = 0;
        }

        public void maintain() {
            maintain_int++;     //유지될 타임 변수 카운트
        }
    }

    int rock_speed =20;
    int coin_speed = 25;
    int heart_speed = 35;
    int round=1;
    int shoot=0;    //연속으로 발사 조절하기 위한 카운트 변수
    int appear=1;   //장애물 등장 간격 카운트 변수
    int coin_appear=1;  //코인 등장 간격 카운트 변수
    int heart_appear=1;  //하트 등장 간격 카운트 변수
    int ch_break=0;
    public void run() {  //스레드 무한루프되는 부분
        try {   //예외옵션 설정으로 에러 방지
            while (true) {  //무한루프
                if(ch_break==1){
                    break;
                }
                if(score>=100){     //일정 점수가 될때마다 난이도 높이기 (외계인 속도 빠르게 하기)
                    round=2;
                    rock_speed =25;
                }
                else if(score>=200){     //일정 점수가 될때마다 난이도 높이기 (외계인 속도 빠르게 하기)
                    round=3;
                    rock_speed =30;
                }
                else if(score>=300){     //일정 점수가 될때마다 난이도 높이기 (외계인 속도 빠르게 하기)
                    round=4;
                    rock_speed =35;
                }
                else if(score>=400){     //일정 점수가 될때마다 난이도 높이기 (외계인 속도 빠르게 하기)
                    round=5;
                    rock_speed =40;
                }
                KeyWok();           //키보드 입력으로 x, y갱신
                WorkGame();         //게임 동작 메소드
                repaint();          //갱신된 값으로 이미지 새로 그리기
                Thread.sleep(20);  //20mill sec의 속도로 스레드 돌리기
                shoot++;    //연속 발사 위해 횟수 카운트
                appear++;   //장애물 등장 간격 위해 횟수 카운트
                coin_appear++;  //코인 등장 간격 위해 횟수 카운트
                heart_appear++;  //하트 등장 간격 위해 횟수 카운트
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("오류가 발생하였습니다. ");
        }
    }

    ArrayList arr_bat = new ArrayList();
    ArrayList arr_rock = new ArrayList();
    ArrayList arr_coin = new ArrayList();
    ArrayList arr_heart = new ArrayList<>();
    ArrayList arr_explosion = new ArrayList();
    ArrayList arr_spark = new ArrayList();
    public void WorkGame() {
        if(life != 0){
            //장애물 동작
            for (int i = 0; i < arr_rock.size(); ++i) {
                rock = (Rock) (arr_rock.get(i));   //배열에 장애물이 만들어져있을 때 해당되는 장애물
                rock.move();  //해당 장애물 움직이기

                if (Crash_check(x, y, rock.x-20, rock.y-20, rabbit_img, rock_img)==1) {    //플레이어가 장애물과 충돌했을 때
                    life--;    //생명 하나 줄기
                    try {
                        File file = new File("src/bgm/hurt.wav");
                        Clip clip = AudioSystem.getClip();
                        clip.open(AudioSystem.getAudioInputStream(file));
                        clip.start();
                    } catch (Exception e) {
                        System.err.println("life minus bgm missing");
                    }
                    arr_rock.remove(i);   //해당 장애물 삭제


                }
            }

            int xmin = 100; // 최소값
            int xmax = 1000; // 최대값
            int interval = 200; // 간격
            int xrandomNumber = random.nextInt((xmax - xmin + interval) / interval) * interval + xmin;

            int ymin = 200;
            int ymax = 990;
            int yrandomNumber = random.nextInt((ymax - ymin + interval) / interval) * interval + ymin;

            if (appear == 100) {   //무한 루프 150마다 장애물 등장
                for(int i=0; i<5; i++){
                    rock = new Rock(width + random.nextInt((xmax - xmin + interval) / interval) * interval + xmin,
                            random.nextInt((ymax - ymin + interval) / interval) * interval + ymin);
                    arr_rock.add(rock);
                }

                appear=0;   //appear 초기화
            }

            //화살쏘기
            if (Space) {
                if (shoot > 25) {   //화살 연속 발사 간격 조절  //간격을 25번 쯤으로 맞추고 발사
                    bat = new Bat(x + 200, y + 30);
                    arr_bat.add(bat);
                    shoot = 0;
                    try {
                        File file = new File("src/bgm/space.wav");
                        Clip clip = AudioSystem.getClip();
                        clip.open(AudioSystem.getAudioInputStream(file));
                        clip.start();
                    } catch (Exception e) {
                        System.err.println("bat bgm missing");
                    }
                }
            }

            for (int i = 0; i < arr_bat.size(); ++i) {
                bat = (Bat) arr_bat.get(i);
                bat.move();

                for (int j = 0; j < arr_rock.size(); ++j) {
                    rock = (Rock) arr_rock.get(j);
                    if (Crash_check(bat.x, bat.y, rock.x, rock.y, bat_img, rock_img)==1) {
                        //장애물에 맞을 경우
                        arr_rock.remove(j);
                        score += 10;  //점수 +10
                        explosion = new Explode(rock.x + rock_img.getWidth(null) / 2, rock.y + rock_img.getHeight(null) / 2, 0);
                        arr_explosion.add(explosion);
                        try {
                            File file = new File("src/bgm/hit.wav");
                            Clip clip = AudioSystem.getClip();
                            clip.open(AudioSystem.getAudioInputStream(file));
                            clip.start();
                        } catch (Exception e) {
                            System.err.println("rock bgm missing");
                        }
                        arr_bat.remove(i);
                        break;

                    }
                }
            }

            // 코인 동작
            for (int i = 0; i < arr_coin.size(); ++i) {
                coin = (Coin) (arr_coin.get(i));   //배열에 장애물이 만들어져있을 때 해당되는 장애물
                coin.move();  //해당 장애물 움직이기

                if (Crash_check(x, y, coin.x, coin.y, rabbit_img, coin_img)==1) {    //플레이어가 장애물과 충돌했을 때
                    score += 15;
                    spark = new Spark(coin.x + coin_img.getWidth(null) / 2, coin.y + coin_img.getHeight(null) / 2);
                    arr_spark.add(spark);
                    try {
                        File file = new File("src/bgm/plus.wav");
                        Clip clip = AudioSystem.getClip();
                        clip.open(AudioSystem.getAudioInputStream(file));
                        clip.start();
                    } catch (Exception e) {
                        System.err.println("coin bgm missing");
                    }
                    arr_coin.remove(i);
                }
            }

            if (coin_appear == 250) {   //무한 루프 250마다 코인 등장
                coin_random = random.nextInt(850 - 180 + 1) + 180;
                coin = new Coin(width + 100, coin_random);
                arr_coin.add(coin);

                coin_appear=0;   //appear 초기화
            }

            // 하트
            for (int i = 0; i < arr_heart.size(); ++i) {
                heart = (Heart) (arr_heart.get(i));   //배열에 장애물이 만들어져있을 때 해당되는 장애물
                heart.move();  //해당 장애물 움직이기

                if (Crash_check(x, y, heart.x, heart.y, rabbit_img, heart1)==1) {    //플레이어가 장애물과 충돌했을 때
                    life ++;
                    arr_heart.remove(i);
                }
            }

            if (heart_appear == 500 & life!=3) {   //무한 루프 250마다 코인 등장
                coin_random = random.nextInt(850 - 180 + 1) + 180;
                heart = new Heart(width + 100, coin_random);
                arr_heart.add(heart);

                heart_appear=0;   //appear 초기화
            }



            //폭발 효과
            for (int i = 0; i < arr_explosion.size(); ++i) {
                explosion = (Explode) arr_explosion.get(i);
                explosion.maintain();
            }

            for (int i = 0; i < arr_spark.size(); ++i) {
                spark = (Spark) arr_spark.get(i);
                spark.maintain();
            }
        }

    }

    public int Crash_check(int x1, int y1, int x2, int y2, Image img1, Image img2) {  //충돌 메소드
        //두 사각형 이미지의 충돌 여부 확인
        int check;
        if (Math.abs((x1 + img1.getWidth(null) / 2) - (x2 + img2.getWidth(null) / 2)) < (img2.getWidth(null) / 2 + img1.getWidth(null) / 2)
                && Math.abs((y1 + img1.getHeight(null) / 2) - (y2 + img2.getHeight(null) / 2)) < (img2.getHeight(null) / 2 + img1.getHeight(null) / 2)) {
            check = 1;
        } else {
            check = 0;
        }
        return check;
    }

    Image bufferimg;
    Graphics bufferg;
    public void paint(Graphics g) {
        bufferimg = createImage(width, height);
        bufferg = bufferimg.getGraphics();
        update(g);
    }

    public void update(Graphics g) {

        String url = "jdbc:mysql://localhost:3306/sys";
        String username = "root";
        String password = "alflarhkgkrrh1!";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, username, password);
            // Connection established
            // You can perform database operations using the "connection" object
        } catch (SQLException e) {
            // Connection failed
            e.printStackTrace();
        }

        Print_Background(); // 배경 이미지 그리기
        bufferg.drawImage(rabbit_img, x, y, this);   // 토끼 그리기
        Print_Rock();
        Print_Bat();
        Print_Coin();
        Print_heart();
        Print_Explode();
        Print_Spark();
        Print_Text();

        if (life == 0 && score < 500) {        // 생명을 다 썼을 경우 게임 오버 창 출력
            try {
                String query = "UPDATE gaemoewtbl SET uscore = ? WHERE uname = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, score);
                statement.setString(2, Play.getName());
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dispose();
            EndingFail.main(new String[0]);
        } else if (life == 0 && score >= 500) {
            try {
                String query = "UPDATE gaemoewtbl SET uscore = ? WHERE uname = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, score);
                statement.setString(2, Play.getName());
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dispose();
            EndingSuccess.main(new String[0]);
        }
        g.drawImage(bufferimg, 0, 0, this);   // 화면에 버퍼에 그린 그림을 가져와 그리기

        // 데이터베이스 연결 종료
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    int move_background = 0;   //배경 움직이는 효과 위한 변수
    public void Print_Background() {    //배경 이미지 출력
        bufferg.clearRect(0, 0, width, height);  //화면 지우기
        bufferg.drawImage(background_img, move_background, 0, this);
        bufferg.drawImage(background_img, move_background + width, 0, this);
        move_background -= 2;
        // 배경이 화면 왼쪽으로 벗어나면 초기화
        if (move_background <= -width) {
            move_background = 0;
        }
    }

    public void Print_Bat() {    //방망이 이미지 출력
        for (int i = 0; i < arr_bat.size(); ++i) {
            bat = (Bat) (arr_bat.get(i));
            bufferg.drawImage(bat_img, bat.x, bat.y, this);
        }
    }

    public void Print_Rock() {  //장애물 이미지 출력
        for (int i = 0; i < arr_rock.size(); ++i) {
            rock = (Rock) (arr_rock.get(i));
            bufferg.drawImage(rock_img, rock.x, rock.y-35, this);
        }
    }

    public void Print_Coin() {  // 코인 이미지 출력
        for (int i = 0; i < arr_coin.size(); ++i) {
            coin = (Coin) (arr_coin.get(i));
            bufferg.drawImage(coin_img, coin.x, coin.y, this);
        }
    }

    public void Print_heart(){     // 하트 출력
        int x=50;
        for(int i=1; i<=life; i++){
            bufferg.drawImage(heart1, x,30,this);
            x+=80;
        }

        for (int i = 0; i < arr_heart.size(); ++i) {
            heart = (Heart) (arr_heart.get(i));
            bufferg.drawImage(heart1, heart.x, heart.y, this);
        }

    }

    public void Print_Explode() { //폭발 효과 출력
        for (int i = 0; i < arr_explosion.size(); ++i) {
            explosion = (Explode) arr_explosion.get(i);
            if (explosion.situation == 0) {    //폭발 상황 "0"이면
                if (explosion.maintain_int < 10) {  //10번 반복될 동안 폭발 효과 유지시키기
                    bufferg.drawImage(explode_img, explosion.x - explode_img.getWidth(null) / 2, explosion.y - explode_img.getHeight(null) / 2, this);
                }
            } else {    //폭발 상황 "1"이면
                if (explosion.maintain_int < 8) {   //8번 반복될 동안 폭발 효과 유지시키기
                    bufferg.drawImage(explode_img, explosion.x + 120, explosion.y + 15, this);
                }
            }
        }
    }

    public void Print_Spark() { //폭발 효과 출력
        for (int i = 0; i < arr_spark.size(); ++i) {
            spark = (Spark) arr_spark.get(i);
            if (spark.maintain_int < 8) {   //8번 반복될 동안 폭발 효과 유지시키기
                bufferg.drawImage(sparkle_img, spark.x - 100, spark.y - 50, this);
            }
        }
    }

    public void Print_Text() {
        // 폰트 설정
        Font font = null;
        try {
            File fontFile = new File("src/font/engfont.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        bufferg.setColor(Color.WHITE);  // 폰트 색상 설정

        bufferg.setFont(font.deriveFont(Font.PLAIN, 50));  // 폰트 설정
        bufferg.drawString("<" + round + " ROUND>", 1550, 110);  // 라운드 표시

        bufferg.setFont(font.deriveFont(Font.PLAIN, 45));  // 폰트 설정
        bufferg.drawString("SCORE : " + score, 1570, 170);  // 점수 표시
    }


    // 키보드 입력
    boolean Up = false;
    boolean Down = false;
    boolean Space = false;
    public void KeyWok() {
        //키보드 입력 방향으로 플레이어 5씩 이동
        if (Up == true && y>100) {
            y -= 15;
        }
        if (Down == true && y<900) {
            y += 15;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { } //키보드가 타이핑될 때 이벤트 처리하는 곳

    public void keyPressed(KeyEvent e) {
        //키보드가 눌러졌을때 이벤트 처리하는 곳
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                Up = true;
                break;
            case KeyEvent.VK_DOWN:
                Down = true;
                break;
            case KeyEvent.VK_SPACE:
                Space = true;
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        //키보드가 눌러졌다가 떼어졌을때 이벤트 처리하는 곳
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                Up = false;
                break;
            case KeyEvent.VK_DOWN:
                Down = false;
                break;
            case KeyEvent.VK_SPACE:
                Space = false;
                break;
        }
    }
}

public class Play {
    private static String name;

    public static void main(String[] args) {
        if (args.length > 0) {
            name = args[0];
            System.out.println("이름: " + name);
        }
        Game game = new Game();
        // 이후에 name 변수를 사용할 수 있음
    }

    public static String getName() {
        return name;
    }
}
