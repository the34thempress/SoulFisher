import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Arrays;
import java.util.List;

public class RiverStyxFishingGame extends JPanel {
    private JFrame frame;
    private Timer gameTimer;
    private int redBoxY, blueBoxY, progress;
    private final int barHeight = 300;
    private final int barX = 800;
    private final int barWidth = 20;
    private int redBoxSpeed = 0;
    private boolean isFishing = true;
    private int attemptsLeft = 5;
    private HashSet<String> caughtSouls = new HashSet<>();
    private String currentSoul = "";
    private Image backgroundImage;
    private List<String> checklist;

    private final String[] soulNames = {"Snickerbloom", "Mournox", "Vexilith", "Skittershade", "Freakazoid"};
    private final String[] soulImages = {"resource/snickerbloom.png", "resource/mournox.png", "resource/vexilith.png", "resource/skittershade.png", "resource/freakazoid.png"};

    private Font customFont;

    public RiverStyxFishingGame(JFrame frame) {
        this.frame = frame;
        setLayout(null);
        setPreferredSize(new Dimension(960, 540));

        redBoxY = 200;
        blueBoxY = (int) (Math.random() * (barHeight - 50));
        progress = 50;

        loadCustomBackground("resource/gamebg.png");
        generateChecklist();
        loadCustomFont(); // Load the Daydream font

        gameTimer = new Timer(50, e -> {
            updateGame();
            repaint();
        });
        gameTimer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    redBoxSpeed = -5;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    redBoxSpeed = 5;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    redBoxSpeed = 0;
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }

    private void loadCustomFont() {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("resource/Daydream.ttf")).deriveFont(15f);
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Serif", Font.PLAIN, 20);
        }
    }

    private void generateChecklist() {
        List<String> shuffled = Arrays.asList(soulNames.clone());
        java.util.Collections.shuffle(shuffled);
        checklist = shuffled.subList(0, 3);
    }

    private void loadCustomBackground(String filePath) {
        try {
            backgroundImage = ImageIO.read(new File(filePath));
            backgroundImage = backgroundImage.getScaledInstance(960, 540, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            System.err.println("resource/Error loading background image: " + e.getMessage());
        }
    }

    private void updateGame() {
        if (!isFishing) return;

        redBoxY += redBoxSpeed;
        redBoxY = Math.max(100, Math.min(barHeight - 50, redBoxY));

        blueBoxY += (int) (Math.random() * 7) - 3;
        blueBoxY = Math.max(100, Math.min(barHeight - 50, blueBoxY));

        if (redBoxY >= blueBoxY && redBoxY <= blueBoxY + 50) {
            progress += 2;
        } else {
            progress -= 1;
        }

        progress = Math.max(0, Math.min(100, progress));

        if (progress == 0) {
            attemptsLeft--;
            if (attemptsLeft <= 0) {
                endGame(false);
            } else {
                resetFishingAttempt();
            }
        }

        if (progress == 100) {
            catchSoul();
        }
    }

    private void catchSoul() {
        Random rand = new Random();
        currentSoul = checklist.get(rand.nextInt(checklist.size()));
        String soulImageFile = soulImages[Arrays.asList(soulNames).indexOf(currentSoul)];

        ImageIcon soulIcon = loadSoulIcon(soulImageFile);

        isFishing = false;
        gameTimer.stop();

        if (!caughtSouls.contains(currentSoul)) {
            caughtSouls.add(currentSoul);
        } else {
            attemptsLeft--;
        }

        JOptionPane.showMessageDialog(this, "You caught a " + currentSoul + "!", "Soul Caught", JOptionPane.INFORMATION_MESSAGE, soulIcon);

        if (caughtSouls.containsAll(checklist)) {
            JOptionPane.showMessageDialog(this, "You can keep your job!", "Success", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (attemptsLeft > 0) {
            int choice = JOptionPane.showOptionDialog(this,
                    "Do you want to try again?",
                    "Retry?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, new Object[]{"Retry", "Exit"}, "Retry");

            if (choice == 0) {
                resetFishingAttempt();
            }
        } else {
            JOptionPane.showMessageDialog(this, "You're fired!", "Game Over", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ImageIcon loadSoulIcon(String filePath) {
        try {
            Image image = ImageIO.read(new File(filePath));
            image = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        } catch (IOException e) {
            System.err.println("Error loading soul image: " + e.getMessage());
            return null;
        }
    }

    private void resetFishingAttempt() {
        progress = 50;
        redBoxY = 200;
        blueBoxY = (int) (Math.random() * (barHeight - 50));
        isFishing = true;
        gameTimer.start();
    }

    private void endGame(boolean success) {
        if (!success) {
            JOptionPane.showMessageDialog(this, "Game Over! You're fired.", "Game Over", JOptionPane.ERROR_MESSAGE);
        }
        gameTimer.stop();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this);
        }

        g.setColor(Color.GRAY);
        g.fillRect(barX, 100, barWidth, barHeight);

        g.setColor(Color.BLUE);
        g.fillRect(barX, blueBoxY, barWidth, 50);

        g.setColor(Color.RED);
        g.fillRect(barX, redBoxY, barWidth, 30);

        g.setColor(Color.GREEN);
        g.fillRect(50, 450, 2 * progress, 20);

        //draw checklist
        g.setFont(customFont);
        g.setColor(Color.WHITE);
        g.drawString("Souls to Catch:", 50, 50);
        int yOffset = 70;
        for (String soul : checklist) {
            g.drawString((caughtSouls.contains(soul) ? "✔" : "✘") + " " + soul, 50, yOffset);
            yOffset += 20;
        }

        g.drawString("Attempts Left: " + attemptsLeft, 50, yOffset);
    }
}
