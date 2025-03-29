

package conchimbay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ConChimBay extends JPanel implements KeyListener, Runnable {

    private int WIDTH ;
    private int HEIGHT;
    //vi tri bat dau theo trục X Y
    private final int INITIAL_X = 100;
    private final int INITIAL_Y = HEIGHT / 2;
    //trọng lực khi chim rơi xuống
    private final int GRAVITY = 1;
    //tăng tốc độ game sau khi qua cột thứ 10
    private int checkPipe = 0;
    private int gameSpeed = 5; 
    
    private Random random;
    
    //ChuongNgaiVat pipe;   
    //NguoiChoi player;
    //Dan dan;
    //MucTieu target;
    
    
    
    //diem
    private int scorePlayer1;
    private int scorePlayer2;
    private int highScore;

    
    //trạng thái mục tiêu
    private boolean targetActive;
    
    //trạng thái gane
    private boolean gameOver;   
    private boolean paused = false;


    public ConChimBay() {
        JFrame frame = new JFrame("Con Chim Bay - 2 người chơi");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        WIDTH = (int) screenSize.getWidth();
        HEIGHT = (int) screenSize.getHeight();
        frame.setSize(WIDTH, HEIGHT);
        //đặt hành động mặt định khi người chơi đóng ứng dụng
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //thay đổi kích thước cửa sổ
        frame.setResizable(false);
        frame.addKeyListener(this);
        //thêm vào khung
        frame.add(this);
        //hiển thị
        frame.setVisible(true);
        highScore = loadHighScore(); // Gọi phương thức để đọc điểm cao nhất từ tệp
        playBackgroundMusic();


        
        pipe = new ChuongNgaiVat();
        player = new NguoiChoi();
        dan = new Dan();
        target = new MucTieu();

        player.player1Y = INITIAL_Y;
        player.player2Y = INITIAL_Y;
        player.player1Velocity = 0;
        player.player2Velocity = 0;
        //vị trí đầu tiên của ống nằm ở chiều rộng
        pipe.pipeX = WIDTH;
        

        scorePlayer1 = 0;
        scorePlayer2 = 0;
        
        //random chiều cao ống
        random = new Random();
        pipe.pipeHeight = randomChieuCaoPipe();

        //dan
        //check người chơi bắn
        dan.player1BulletFired = false;
        dan.player2BulletFired = false;
        //trị trí đầu viên đạn
        dan.player1BulletX = 0;
        dan.player1BulletY = 0;
        dan.player2BulletX = 0;
        dan.player2BulletY = 0;
        dan.bulletSpeed = 10;
        dan.bulletWidth = 10;
        dan.bulletHeight = 10;
        //anh nguoi choi
          // Tải ảnh người chơi từ tệp "chim.jpg"
        player.playerImage1  = new ImageIcon(getClass().getClassLoader().getResource("anh/chim1.png")).getImage();
        player.playerImage2  = new ImageIcon(getClass().getClassLoader().getResource("anh/chim2.png")).getImage();
        target.imageTarget  = new ImageIcon(getClass().getClassLoader().getResource("anh/gold.png")).getImage();
        //muc tieu
        target.targetWidth = 30;
        target.targetHeight = 30;
        targetActive = false;
        
        // tạo ra run vẽ giao diện
        Thread thread = new Thread(this);
        thread.start();
        //hinh
        player.player1X=1;
        player.player2X=INITIAL_X+100;
    }

public static void main(String[] args) {
    //Trong trò chơi, sử dụng một luồng chính để vẽ giao diện và xử lý sự kiện người dùng
    //để tạo luồng người chơi 2 và bắt đầu nó.
    SwingUtilities.invokeLater(() -> {
        new ConChimBay();
    });
   }
}
