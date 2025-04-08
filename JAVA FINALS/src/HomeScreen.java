import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeScreen extends JPanel {
    private JFrame frame;
    private Image backgroundImage;
    private JButton playButton;

    public HomeScreen(JFrame frame) {
        this.frame = frame;
        backgroundImage = new ImageIcon("resource/RiverStyxFishingGame.png").getImage();

        AudioPlayer.playBackgroundMusic("resource/bgm.wav");

        setLayout(null);
        playButton = new JButton("Play");
        playButton.setBounds(100, 980, 150, 70);
        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.setFocusPainted(false);

        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("resource/Alucrads.ttf")).deriveFont(50f);
            playButton.setFont(customFont);
        } catch (Exception e) {
            playButton.setFont(new Font("Serif", Font.BOLD, 36));
        }

        playButton.setForeground(Color.decode("#abff01"));

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        add(playButton);
    }

    public void setPlayButtonPosition(int x, int y) {
        playButton.setBounds(x, y, 150, 70);
    }

    public void setPlayButtonColor(Color color) {
        playButton.setForeground(color);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private void startGame() {
        frame.getContentPane().removeAll();
        DialogueSystem dialoguePanel = new DialogueSystem(frame);
        frame.add(dialoguePanel);
        frame.revalidate();
        frame.repaint();

        dialoguePanel.requestFocusInWindow();
    }
}
