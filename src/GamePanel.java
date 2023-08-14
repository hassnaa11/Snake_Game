import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
public class GamePanel extends JPanel implements ActionListener {
    static final int WIDTH = 600, HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static int DELAY;
    static final int GAME_UNITS = (WIDTH * HEIGHT) / UNIT_SIZE;
    int[] x = new int[GAME_UNITS];
    int[] y = new int[GAME_UNITS];
    int appleX, appleY;
    Random random;
    Timer timer;
    int snakeSize =3;
    char direction = 'R';
    boolean running=false;
    int score=0;
    Instant start,stop;
    Clip clip;
    public GamePanel() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file=new File("src/resources/GameOverSound.wav");
        AudioInputStream audioStream= AudioSystem.getAudioInputStream(file);
        clip=AudioSystem.getClip();
        clip.open(audioStream);
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame() {
        newApple();
        running=true;
        timer= new Timer(DELAY,this);
        timer.start();
        start = Instant.now();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            draw(g);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException |
                 UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public void draw(Graphics g) throws UnsupportedAudioFileException, LineUnavailableException, IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        if(running) {

            //Draw BackGround
            ImageIcon icon=new ImageIcon("src/resources/greennn.jpg");
            Image green=icon.getImage();
            green.getScaledInstance(WIDTH,HEIGHT, BufferedImage.SCALE_SMOOTH);
            g.drawImage(green,0,0,null);

            //Draw Apple
            ImageIcon iconApple=new ImageIcon("src/resources/apple.png");
            Image apple=iconApple.getImage();
            apple.getScaledInstance(UNIT_SIZE,UNIT_SIZE, BufferedImage.SCALE_SMOOTH);
            g.drawImage(apple,appleX,appleY,null);

            //Draw Snake
            for (int i = 0; i < snakeSize; i++) {
                //Draw the Head
                if (i == 0) {
                    ImageIcon part1 =new ImageIcon("src/resources/1R.png");;
                    switch (direction) {
                        case ('D')-> part1 = new ImageIcon("src/resources/1D.png");
                        case ('U')-> part1 = new ImageIcon("src/resources/1U.png");
                        case ('R')-> part1 = new ImageIcon("src/resources/1R.png");
                        case ('L')-> part1 = new ImageIcon("src/resources/1L.png");
                    }
                    Image head=part1.getImage();
                    g.drawImage(head,x[i], y[i],null);
                }
                else {
                    ImageIcon part2;
                    //Draw the tail
                    if (i==snakeSize-1){
                        if (y[i-1]<y[i] && x[i-1]==x[i])
                            part2=new ImageIcon("src/resources/Utail.png");
                        else if (y[i-1]>y[i] && x[i-1]==x[i])
                            part2=new ImageIcon("src/resources/Dtail.png");
                        else if (y[i-1]==y[i] && x[i-1]>x[i])
                            part2=new ImageIcon("src/resources/Rtail.png");
                        else
                            part2=new ImageIcon("src/resources/Ltail.png");
                    }
                    //Draw corners
                    else if((y[i+1]==y[i] && x[i-1]==x[i]&& x[i+1]<x[i] && y[i-1]>y[i]) || (y[i+1]>y[i] && x[i-1]<x[i]&& x[i+1]==x[i] && y[i-1]==y[i])) {
                        part2 = new ImageIcon("src/resources/RtD.png");
                    }
                    else if((y[i+1]==y[i] && x[i-1]==x[i] && x[i+1]>x[i] && y[i-1]<y[i]) || (y[i+1]<y[i] && x[i-1]>x[i] && x[i+1]==x[i] && y[i-1]==y[i])) {
                        part2 = new ImageIcon("src/resources/DtR.png");
                    }
                    else if((x[i+1]==x[i] && y[i-1]==y[i] && y[i+1]<y[i] && x[i-1]<x[i]) || (x[i+1]<x[i] && y[i-1]<y[i] && y[i+1]==y[i] && x[i-1]==x[i])) {
                        part2 = new ImageIcon("src/resources/DtL.png");
                    }
                    else if((x[i+1]==x[i] && y[i-1]==y[i] && y[i+1]>y[i] && x[i-1]>x[i]) || (x[i+1]>x[i] && y[i-1]>y[i] && y[i+1]==y[i] && x[i-1]==x[i])){
                        part2 = new ImageIcon("src/resources/LtD.png");
                    }
                    //Draw middle parts
                    else if(y[i+1]>y[i] || y[i+1]<y[i]){
                        part2=new ImageIcon("src/resources/2UD.png");
                    }
                    else if(x[i+1]>x[i] || x[i+1]<x[i]) {
                        part2 = new ImageIcon("src/resources/2RL.png");
                    }
                    else {
                        part2 = new ImageIcon("src/resources/2RL.png");

                    }
                    Image snake=part2.getImage();
                    g.drawImage(snake,x[i], y[i],null);
                }
            }
        }
        else{
            gameOver(g);
        }
    }

    public void move() {
        for (int i = snakeSize; i >0 ; i--) {
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
            switch (direction) {
                //Right
                case ('R') ->
                    x[0] += UNIT_SIZE;

                //Left
                case ('L') ->
                    x[0] -= UNIT_SIZE;

                //up
                case ('U') ->
                    y[0] -= UNIT_SIZE;

                //Down
                case ('D') ->
                    y[0] += UNIT_SIZE;
            }

    }
    public void newApple() {
        for (int i = 0; i < snakeSize; i++) {
            while (x[i]==appleX || y[i] ==appleY) {
                appleX = random.nextInt((int) (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
                appleY = random.nextInt((int) (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
            }
        }
    }
    public void checkApple() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if(appleX==x[0] && appleY==y[0]){
            File clickFile=new File("src/resources/interface.wav");
            AudioInputStream clickAudio=AudioSystem.getAudioInputStream(clickFile);
            Clip clickClip=AudioSystem.getClip();
            clickClip.open(clickAudio);
            clickClip.start();

            newApple();
            snakeSize++;
            score++;
        }
    }
    public void checkCollisions() {
        //check if snake touches itself
        for (int i = snakeSize; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                break;
            }
        }
        //check if snake hits the borders
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }
        if (!running){
            timer.stop();
            stop=Instant.now();
        }
    }
    public void gameOver(Graphics g) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if(!running) {
            clip.start();
            //background photo
            this.setLayout(null);
            ImageIcon icon=new ImageIcon("src/resources/gameover.jpg");
            JLabel backgroundLabel=new JLabel();
            backgroundLabel.setBounds(0,0,600,600);
            backgroundLabel.setIcon(icon);
            backgroundLabel.setVisible(true);
            //Game over label 1
            JLabel label = new JLabel("GAME   OVER");
            label.setFont(new Font("Bernard MT Condensed", Font.BOLD, 60));
            label.setForeground(new Color(0x682F16));
            label.setBounds(160, 130, 400, 50);
            label.setVisible(true);
            //Game over label 2
            JLabel label1 = new JLabel("GAME    OVER");
            label1.setFont(new Font("Bernard MT Condensed", Font.BOLD, 55));
            label1.setForeground(new Color(0xAD8473));
            label1.setBounds(160, 130, 410, 60);
            label1.setVisible(true);
            //Score label
            JLabel label2 = new JLabel("Score: (" + score+")");
            label2.setFont(new Font("MV Boli", Font.PLAIN, 30));
            label2.setForeground(new Color(0x682F16));
            label2.setBounds(150, 260, 300, 50);
            label2.setVisible(true);
            //time label
            JLabel label3=new JLabel("Time: ("+ Duration.between(start,stop).toMinutes()+":"+Duration.between(start,stop).toSeconds()%60+")");
            label3.setFont(new Font("MV Boli", Font.PLAIN, 30));
            label3.setForeground(new Color(0x682F16));
            label3.setBounds(300, 310, 300, 50);
            label3.setVisible(true);
            //Exit Button
            JButton exit = new JButton("exit");
            exit.setFont(new Font("MV Boli", Font.BOLD, 30));
            exit.setForeground(new Color(0x350E00));
            exit.setBackground(new Color(0xAD8473));
            exit.setBounds(350, 410, 150, 50);
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clip.stop();
                    try {
                        buttonSound();
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                        throw new RuntimeException(ex);
                    }
                    JFrame GameFrame = (JFrame) SwingUtilities.getWindowAncestor(exit);
                    GameFrame.dispose();
                }
            });
            //Restart Button
            JButton restart = new JButton("restart");
            restart.setFont(new Font("MV Boli", Font.BOLD, 30));
            restart.setForeground(new Color(0x350E00));
            restart.setBackground(new Color(0xAD8473));
            restart.setBounds(130, 410, 150, 50);
            restart.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clip.stop();
                    try {
                        buttonSound();
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                        throw new RuntimeException(ex);
                    }
                    JFrame GameFrame = (JFrame) SwingUtilities.getWindowAncestor(restart);
                    GameFrame.dispose();
                    new StartFrame();
                }
            });
            //Adding all above labels and buttons on the panel
            this.add(label);
            this.add(label1);
            this.add(label2);
            this.add(label3);
            this.add(exit);
            this.add(restart);
            this.add(backgroundLabel);
            this.setVisible(true);
        }
    }
    public void buttonSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file=new File("src/resources/button-pressed.wav");
        AudioInputStream audioInputStream= AudioSystem.getAudioInputStream(file);
        Clip clip=AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkCollisions();
            try {
                checkApple();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            if (e.getKeyCode() == KeyEvent.VK_RIGHT && direction!='L'){
                direction='R';
            }
            else if (e.getKeyCode() == KeyEvent.VK_LEFT && direction!='R'){
                direction='L';
            }
            else if (e.getKeyCode() == KeyEvent. VK_UP && direction!='D'){
                direction='U';
            }
            else if (e.getKeyCode() == KeyEvent.VK_DOWN && direction!='U'){
                direction='D';
            }
        }
    }
}
