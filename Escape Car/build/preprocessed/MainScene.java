
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hynra : http://hynra.com
 */


public class MainScene extends GameCanvas implements Runnable {
    
    Timer timer;
    Graphics g = getGraphics();
    Midlet midlet;
    Sprite player, musuh, musuh2, rocket, point, point2, laser, bonus;
    //var posisi player
    int posplayX = 100, posplayY = 230, speed = 1;
    //var posisi musuh
    int posMus2Y = -120, posMusY = -60, posXmus, posXmus2;
    int score =0;
    Image bg, gameStart, gameOver;
    int bgPos=3, randomMusuh, randomMusuh2;
    boolean tembak = false, moveBonus = false, enableTimer = false, enableLaser = false;
    boolean moveMusuh = false, moveMusuh2 = false, addPoin = true, addPoin2 = true, isGameOver = false, isGameStart = true;
    int bonusX, bonusY;
    
    public MainScene(Midlet m) {
        super(true);
        this.midlet = m;
        setFullScreenMode(true);
        try {
            sourceKu();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        new Thread(this).start();
    }

    // fungsi draw player
    public void drawPlayer() {
        // posisi player
        player.setPosition(posplayX, posplayY);
        player.paint(g);
         // jika collide dengan musuh 1 atau 2, call gameOverState()
         if(player.collidesWith(musuh, true) || player.collidesWith(musuh2, true)){
             gameOverState();
         }
         
         if(player.collidesWith(bonus, true)){
             enableLaser = true;
         }
    }
    
    // fungsi draw musuh
    public void drawMusuh() throws IOException{
        // jika bool moveMusuh = false, tentukan random x musuh 1 dan 2
        if(!moveMusuh){
            musuh.setVisible(true);
            // random posisi x musuh
            posXmus = randInt(49, 150);
            // random sprite musuh (1 - 5)
            randomMusuh = randInt(1, 5);
            // jika bool isGameOver = false, randomize sprite
            if(!isGameOver){
                // switch chase dari randomMusuh untuk random sprite
                switch(randomMusuh){
                    case 1:
                        // sprite 1
                        musuh = new Sprite(Image.createImage("/gambar/enemy1.png"));
                        break;
                    case 2:
                        // sprite 2
                        musuh = new Sprite(Image.createImage("/gambar/enemy2.png"));
                        break;
                    case 3:
                        //sprite 3
                        musuh = new Sprite(Image.createImage("/gambar/enemy3.png"));
                        break;
                    case 4:
                        // sprite 4
                        musuh = new Sprite(Image.createImage("/gambar/enemy4.png"));
                        break;
                    case 5:
                        // sprite 5
                        musuh = new Sprite(Image.createImage("/gambar/enemy5.png"));
                        break;
                }
            }
            // set musuh 1 position, x = random, y = -60 (diatas layar)
            musuh.setPosition(posXmus, -60);
            // point digunakan untuk point system musuh 1
            point.setPosition(0, -60);
            // move musuh 1 dan point 1
            moveMusuh = true;
        }
        // jika moveMusuh (1) = true;
        else{
            //move musuh (1) senilai score + speed
            musuh.move(0, speed+(score));
            //move point (1) senilai score + speed
            point.move(0, speed+(score));
            // jika point collide dengan player (berhasil melewati musuh), tambah point
            if(point.collidesWith(player, true))
                //.. hanya jika addPoin = true;
                // ditambahkan boolean addPoin supaya hanya satu point saja yang ditambah
                if(addPoin){
                    score += 1;
                    addPoin = false;
                }
        }
        // paint musuh(1) dan point (2)
        if(!isGameOver){
            musuh.paint(g);
            point.paint(g);
        }
        // Jika musuh melewati y lebihdari samadengan 305
        if(musuh.getY() >= 305){
            // moveMusuh = false, yang berarrti generate posisi dan sprite kembali
            moveMusuh = false;
            // bolehkan scoreSystem (1);
            addPoin = true;
        }
    }

   public void drawMusuh2() throws IOException{
       if(!moveMusuh2){
           musuh2.setVisible(true);
           posMus2Y = -180 + musuh.getY();
           posXmus2 = randInt(49, 150);
           randomMusuh2 = randInt(1, 5);
            if(!isGameOver){
                switch(randomMusuh2){
                    case 1:
                        musuh2 = new Sprite(Image.createImage("/gambar/enemy1.png"));
                        break;
                    case 2:
                        musuh2 = new Sprite(Image.createImage("/gambar/enemy2.png"));
                        break;
                    case 3:
                        musuh2 = new Sprite(Image.createImage("/gambar/enemy3.png"));
                        break;
                    case 4:
                        musuh2 = new Sprite(Image.createImage("/gambar/enemy4.png"));
                        break;
                    case 5:
                        musuh2 = new Sprite(Image.createImage("/gambar/enemy5.png"));
                        break;
                }
            }
           musuh2.setPosition(posXmus2, posMus2Y);
           point2.setPosition(0, posMus2Y);
           moveMusuh2 = true;
        }
        else{
            musuh2.move(0, speed+(score));
            point2.move(0, speed+(score));
            if(point2.collidesWith(player, true))
                if(addPoin2){
                    score += 1;
                    addPoin2 = false;
                }
        }
        if(!isGameOver){
            musuh2.paint(g);
            point2.paint(g);
        }
        if(musuh2.getY() >= 305){
            moveMusuh2 = false;
            addPoin2 = true;
        }
   }
   
   // draw GameOver UI
   public void drawGameOverUI(){
       g.drawImage(gameOver, 0, 60, 0);
   }
   
   // fungsi gameOverState, digunakan jika game dalam keadaan gameOver
   public void gameOverState(){
       // sembunyikan semua sprite
       player.setVisible(false);
       bonus.setVisible(false);
       laser.setVisible(false);
       musuh.setVisible(false);
       musuh2.setVisible(false);
       point.setVisible(false);
       point2.setVisible(false);
       // moveMusuh = false, set posisi awal
       moveMusuh = false;
       moveMusuh2 = false;
       // jika game isGameStart = false, artinya game dalam keadaan berjalan
       if(!isGameStart)
           // state isGameOver = true
        isGameOver = true;
   }
   
   // fungsi yang digunakan untuk menjalankan semua hame objek / reset
   public void startGame(){
       // tampilkan semua sprite
       player.setVisible(true);
       bonus.setVisible(true);
       laser.setVisible(true);
       musuh.setVisible(true);
       musuh2.setVisible(true);
       point.setVisible(true);
       point2.setVisible(true);
       // isGameOver = false, game dalam keadaan jalan
       isGameOver = false;
       // reset score = 0
       score = 0;
       // regenerate posisi awal musuh
       moveMusuh = false;
       moveMusuh2 = false;
       // regenerate posisi awal player
       posplayX = 100;
       posplayY = 230;
       
   }
   
   public  void drawBonus(){
       if(!isGameOver)   
       if(score > 0){  
       {    
            if(!moveBonus){  
                bonusX = randInt(49, 150);
                bonus.setPosition(bonusX, 0);
                moveBonus = true;
            }else{
                bonus.move(0, speed+score);
            }
            if(bonus.getY() >= 305){
                if(enableTimer)
                moveBonus = false;
            }
            bonus.paint(g);
            if(bonus.collidesWith(musuh, true) || bonus.collidesWith(musuh2, true))
                    moveBonus = false;
                
       }
       }
   }
   
   public  void drawLaser(){
    if(score > 0){  
        if(tembak){
             laser.move(0, -speed*score);
            if(laser.getY()<= 0){
               tembak = false;
               enableLaser = false;
            }
       }else{  
            laser.setPosition(posplayX+8, posplayY-3);
        //    laser.setVisible(false);
       }
       laser.paint(g);
       if(laser.collidesWith(musuh, true)){
           musuh.setVisible(false);
           tembak = false;
           enableLaser = false;
       } else if(laser.collidesWith(musuh2, true)){
           musuh2.setVisible(false);
           tembak = false;
           enableLaser = false;
       }
    }
   }
   
   // fungsi definisi source (sprite dan img)
    public void sourceKu() throws IOException {
        bg = Image.createImage("/gambar/bg.png");
        player = new Sprite(Image.createImage("/gambar/player.png"));
        musuh = new Sprite(Image.createImage("/gambar/enemy1.png"));
        musuh2 = new Sprite(Image.createImage("/gambar/enemy2.png"));
        point = new Sprite(Image.createImage("/gambar/point.png"));
        point2 = new Sprite(Image.createImage("/gambar/point.png"));
        gameOver = Image.createImage("/gambar/gameover.png");
        gameStart = Image.createImage("/gambar/start.png");
        laser = new Sprite(Image.createImage("/gambar/laser.png"));
        bonus = new Sprite(Image.createImage("/gambar/bonus.png"));
        
    }
    
    // Fungsi Control
    protected void control(){
        int tombol=getKeyStates();
        // tombol kiri
        if(tombol==RIGHT_PRESSED){
            // batasi pergerakan player sampai x >= 151 (batas jalan)
            if(posplayX >= 151)
            
            posplayX = 151;
            //Jika posisi x player kurang dari 151, boleh pindah
            else if(posplayX < 151)
                // speed bertambah jika score juga bertambah
            posplayX += speed+(score);
        }
        
        // tombol kanan
        else if(tombol==LEFT_PRESSED){
            // batasi pergeraakan player ke kiri sampai x <= 49
            if(posplayX <= 49)
                posplayX = 49;
            // jika posisi x player lebih dari 49, boleh geser kekiri
            else if(posplayX > 49)
                // speed tergantung score (ditambah score)
            posplayX -= speed+(score);
        }
        // tombol atas
        else if(tombol==UP_PRESSED){
            // batasi pergerakan player ke sumbu y jika posisi <= 0
            if(posplayY <= 0);
            // jika posisi y player lebih dari 0, player boleh maju keatas
            else if(posplayY > 0)
                // speed ditambah score
            posplayY -= speed+(score);
        }
        // tombol bawah
        else if(tombol==DOWN_PRESSED){
            // batasi pergerakan y jika >= 250
            if(posplayY >= 250);
            // jika kurang dari 250, boleh gerak kebawah
            else if(posplayY < 250)
                // speed tambah score
            posplayY += speed+(score);
            
            // tombol fire
        }else if(tombol==FIRE_PRESSED){
            //jika gameOver = true, mulai lagi game
            if(isGameOver)
                startGame();
            // jika game baru pertama dimulai
            if(isGameStart){
                // mulai game
                startGame();
                isGameStart = false;
            }
            if(!isGameOver || !isGameStart)
                if(enableLaser)
                    tembak = true;
        }
    }
    
    // fungsi untuk randomize integer
    public static int randInt(int min, int max) {
    Random rand = new Random();
    int randomNum = rand.nextInt((max - min) + 1) + min;
    return randomNum;
}

    // fungsi run
    public void run() {          
        while (true) {
         // jika score kurangdari samadengan 3, cepatkan pergerakan
        if(score <= 3){
            // supaya tidak terlalu lambat diawal, speed = 3
            speed = 3;
            // jika score lebhdari 3, kembalikan nilai speed = 1
        } else if(score > 3){
            speed = 1;
        }
        // jika bukan dalam keadaan game baru dimulai dan gameover, gerakkan background
        if(!isGameOver && !isGameStart)
            // gerakan background, ditambah score
            bgPos+=speed+(score);
        if(bgPos > bg.getHeight()){
            bgPos=0;
        }
        // draw background
        g.drawImage(bg, 0, bgPos, 0);
        g.drawImage(bg, 0, bgPos - bg.getHeight(), 0);
        g.setColor(255,255,255);
        // jika gae baru pertamma dimulai, munculkan UI judul
        if(isGameStart){
            gameOverState();
            g.drawImage(gameStart, 0, 60, 0);
        }
        // call control
        control();
        // draw player
        if(!enableLaser)
            drawBonus();
        drawPlayer();
        if(enableLaser){
            moveBonus = false;
            drawLaser();
        }
        try {
            // draw musuh 1
            drawMusuh();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            // draw musuh 2
            drawMusuh2();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        // jika status gameover, tampilkan UI Gameover
        if(isGameOver){
            drawGameOverUI();
        }
        // hanya tampilkan score setelah status game pertama kali dibuka
        if(!isGameStart)
            g.drawString("Score : " +score, 90, 2, 0);    
        flushGraphics();
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        }
    }
}