import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

class Game extends JFrame implements KeyListener, Runnable {
    int width = 1920;    //프레임 가로
    int height = 1080;    //프레임 높이
    int x = 250, y = 490;  //플레이어 좌표 변수
    int score = 0;    //게임 점수
    int life = 3;
    private Timer timer;
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
    Image heart0;           // 하트 0개
    Image heart1;           // 하트 1개
    Image heart2;           // 하트 2개
    Image heart3;           // 하트 3개

    public void getimg() {
        try {
            bat_img = new ImageIcon("src/image/bat.png").getImage();
            rock_img = new ImageIcon("src/image/rock-1.png").getImage();
            coin_img = new ImageIcon("src/image/coin.gif").getImage();
            rabbit_img = new ImageIcon("src/image/rabbit.gif").getImage();
            background_img = new ImageIcon("src/image/background.png").getImage();
            explode_img = new ImageIcon("src/image/explode.png").getImage();
            sparkle_img = new ImageIcon("src/image/sparkle.png").getImage();
            heart0 = new ImageIcon("src/image/heart/heart0.png").getImage();
            heart1 = new ImageIcon("src/image/heart/heart1.png").getImage();
            heart2 = new ImageIcon("src/image/heart/heart2.png").getImage();
            heart3 = new ImageIcon("src/image/heart/heart3.png").getImage();
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
    Explode explosion;   //폭발 클래스
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

    int rock_speed =7;
    int coin_speed = 20;
    int round=1;
    int shoot=0;    //연속으로 발사 조절하기 위한 카운트 변수
    int appear=1;   //장애물 등장 간격 카운트 변수
    int coin_appear=1;  //코인 등장 간격 카운트 변수
    int ch_break=0;
    public void run() {  //스레드 무한루프되는 부분
        try {   //예외옵션 설정으로 에러 방지
            while (true) {  //무한루프
                if(ch_break==1){
                    break;
                }
                if(score==100){     //일정 점수가 될때마다 난이도 높이기 (외계인 속도 빠르게 하기)
                    round=2;
                    rock_speed =14;
                }
                else if(score==250){
                    round=3;
                    rock_speed =16;
                }
                else if(score==350){
                    round=4;
                    rock_speed =18;
                }
                else if(score==450){   //난이도는 5단계까지만
                    round=5;
                    rock_speed =20;
                }else if(score<=500 && life==0) {
                    dispose();
                    EndingSuccess.main(new String[0]);
                }
                KeyWok();           //키보드 입력으로 x, y갱신
                WorkGame();         //게임 동작 메소드
                repaint();          //갱신된 값으로 이미지 새로 그리기
                Thread.sleep(20);  //20mill sec의 속도로 스레드 돌리기
                shoot++;    //연속 발사 위해 횟수 카운트
                appear++;   //장애물 등장 간격 위해 횟수 카운트
                coin_appear++;  //코인 등장 간격 위해 횟수 카운트
            }
        } catch (Exception e) {
            System.out.println("오류가 발생하였습니다. ");
        }
    }

    ArrayList arr_bat = new ArrayList();
    ArrayList arr_rock = new ArrayList();
    ArrayList arr_coin = new ArrayList();
    ArrayList arr_explosion = new ArrayList();
    public void WorkGame() {
        //장애물 동작
        for (int i = 0; i < arr_rock.size(); ++i) {
            rock = (Rock) (arr_rock.get(i));   //배열에 장애물이 만들어져있을 때 해당되는 장애물
            rock.move();  //해당 장애물 움직이기

            if (Crash_check(x, y, rock.x, rock.y, rabbit_img, rock_img)==1) {    //플레이어가 장애물과 충돌했을 때
                life--;    //생명 하나 줄기
                try {
                    File file = new File("src/bgm/hurt.wav");
                    Clip clip = AudioSystem.getClip();
                    clip.open(AudioSystem.getAudioInputStream(file));
                    clip.start();
                } catch (Exception e) {
                    System.err.println("Put the music.wav file in the sound folder if you want to play background music, only optional!");
                }
                arr_rock.remove(i);   //해당 장애물 삭제


            }
        }

        if (appear == 150) {   //무한 루프 150마다 장애물 등장
            rock = new Rock(width + 100, 180);
            arr_rock.add(rock);
            rock = new Rock(width + 100, 350);
            arr_rock.add(rock);
            rock = new Rock(width + 100, 520);
            arr_rock.add(rock);
            rock = new Rock(width + 100, 680);
            arr_rock.add(rock);
            rock = new Rock(width + 100, 850);
            arr_rock.add(rock);

            appear=0;   //appear 초기화
        }

        //화살쏘기
        if (Space) {
            if (shoot > 15) {   //화살 연속 발사 간격 조절  //간격을 15번 쯤으로 맞추고 발사
                bat = new Bat(x + 150, y + 30);
                arr_bat.add(bat);
                shoot = 0;
                try {
                    File file = new File("src/bgm/space.wav");
                    Clip clip = AudioSystem.getClip();
                    clip.open(AudioSystem.getAudioInputStream(file));
                    clip.start();
                } catch (Exception e) {
                    System.err.println("Put the music.wav file in the sound folder if you want to play background music, only optional!");
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
                    arr_bat.remove(i);
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
                        System.err.println("Put the music.wav file in the sound folder if you want to play background music, only optional!");
                    }

                }
            }
        }

        // 코인 동작
        for (int i = 0; i < arr_coin.size(); ++i) {
            coin = (Coin) (arr_coin.get(i));   //배열에 장애물이 만들어져있을 때 해당되는 장애물
            coin.move();  //해당 장애물 움직이기

            if (Crash_check(x, y, coin.x, coin.y, rabbit_img, coin_img)==1) {    //플레이어가 장애물과 충돌했을 때
                score += 20;
                try {
                    File file = new File("src/bgm/plus.wav");
                    Clip clip = AudioSystem.getClip();
                    clip.open(AudioSystem.getAudioInputStream(file));
                    clip.start();
                } catch (Exception e) {
                    System.err.println("Put the music.wav file in the sound folder if you want to play background music, only optional!");
                }
                arr_coin.remove(i);
                bufferg.drawImage(rabbit_img, x, y, this);   // 토끼 그리기
            }
        }

        if (coin_appear == 250) {   //무한 루프 250마다 코인 등장
            coin_random = random.nextInt(850 - 180 + 1) + 180;
            coin = new Coin(width + 100, coin_random);
            arr_coin.add(coin);

            coin_appear=0;   //appear 초기화
        }


        //폭발 효과
        for (int i = 0; i < arr_explosion.size(); ++i) {
            explosion = (Explode) arr_explosion.get(i);
            explosion.maintain();
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
        Print_Background(); //배경 이미지 그리기
        bufferg.drawImage(rabbit_img, x, y, this);   // 토끼 그리기
        Print_Rock();
        Print_Bat();
        Print_Coin();
        Print_heart();
        Print_Explode();
        Print_Text();
        if(life==0){        //생명을 다 썼을 경우 게임 오버 창 출력
            dispose();
            EndingFail.main(new String[0]);
        }
        g.drawImage(bufferimg, 0, 0, this);   //화면에 버퍼에 그린 그림을 가져와 그리기
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
        if(life==3){
            bufferg.drawImage(heart3, 50,30,this);
        } else if(life==2){
            bufferg.drawImage(heart2, 50,30,this);
        } else if(life==1){
            bufferg.drawImage(heart1, 50,30,this);
        } else {
            bufferg.drawImage(heart0, 50,30,this);
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

        bufferg.setFont(font.deriveFont(Font.PLAIN, 40));  // 폰트 설정
        bufferg.drawString("<" + round + " ROUND>", 1670, 100);  // 라운드 표시

        bufferg.setFont(font.deriveFont(Font.PLAIN, 37));  // 폰트 설정
        bufferg.drawString("SCORE : " + score, 1685, 150);  // 점수 표시
    }


    // 키보드 입력
    boolean Up = false;
    boolean Down = false;
    boolean Space = false;
    public void KeyWok() {
        //키보드 입력 방향으로 플레이어 5씩 이동
        if (Up == true && y>100) {
            y -= 10;
        }
        if (Down == true && y<900) {
            y += 10;
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
    public static void main(String[] ar) {
        Game game = new Game();
    }
}