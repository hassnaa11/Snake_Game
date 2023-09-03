import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
public class StartFrame extends JFrame {
    public StartFrame() {
        addButton();
        addLabel();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1000,577));
        this.setLayout(null);
        this.setResizable(false);
        this.setTitle("Snake Game");
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        ImageIcon logo=new ImageIcon("src/resources/logo.png");
        this.setIconImage(logo.getImage());
    }
    public void addButton(){
        JButton startButton = new JButton("CLASSIC");
        startButton.setFont(new Font("MV Boli", Font.BOLD, 30));
        startButton.setForeground(new Color(0x350E00));
        startButton.setBackground(new Color(0xC8842F));
        startButton.setBounds(293, 416, 220, 50);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GamePanel.DELAY=75;
                try {
                    buttonSound();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                }
                JFrame GameFrame = (JFrame) SwingUtilities.getWindowAncestor(startButton);
                GameFrame.dispose();
                try {
                    new GameFrame();
                } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        JButton adventureButton=new JButton("ADVENTURE");
        adventureButton.setFont(new Font("MV Boli", Font.BOLD, 30));
        adventureButton.setForeground(new Color(0x350E00));
        adventureButton.setBackground(new Color(0xC8842F));
        adventureButton.setBounds(550, 416, 220,50 );
        adventureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GamePanel.DELAY=30;
                try {
                    buttonSound();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                }
                JFrame GameFrame = (JFrame) SwingUtilities.getWindowAncestor(adventureButton);
                GameFrame.dispose();
                try {
                    new GameFrame();
                } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        adventureButton.setVisible(true);
        startButton.setVisible(true);
        this.add(startButton);
        this.add(adventureButton);
    }
    public void addLabel(){
        JLabel label=new JLabel();
        ImageIcon icon=new ImageIcon("src/resources/start.png");
        label.setIcon(icon);
        label.setBounds(0,0,1000,577);
        label.setVisible(true);
        this.add(label);
    }
    public void buttonSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file=new File("src/resources/button-pressed.wav");
        AudioInputStream audioInputStream= AudioSystem.getAudioInputStream(file);
        Clip clip=AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    }
}