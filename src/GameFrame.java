import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;
public class GameFrame extends JFrame {
    public GameFrame() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.add(new GamePanel());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Snake Game");
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        ImageIcon logo=new ImageIcon("src/resources/logo.png");
        this.setIconImage(logo.getImage());
    }
}
