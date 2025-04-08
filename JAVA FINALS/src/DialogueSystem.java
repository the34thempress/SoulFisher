import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class DialogueSystem extends JPanel {
    private JFrame frame;
    private JTextArea dialogueBox;
    private JLabel spriteLabel;
    private JLabel backgroundLabel;
    private String[] dialogue;
    private int dialogueIndex = 0;
    private JLabel npcNameLabel;
    private NPC npc;

    public DialogueSystem(JFrame frame) {
        this.frame = frame;
        setLayout(null);

        // Set up background
        ImageIcon backgroundImage = new ImageIcon("resource/background1.png");
        backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, 960, 540);
        add(backgroundLabel);

        // Set up NPC (sprite)
        npc = new NPC();
        npc.setBounds(300, 20, 350, 350);
        backgroundLabel.add(npc);

        npcNameLabel = new JLabel("Kairos"); //sets up NPC Name for Dialgoue
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("resource/Daydream.ttf")).deriveFont(30f);
            npcNameLabel.setFont(customFont);
        } catch (Exception e) {
            npcNameLabel.setFont(new Font("Serif", Font.BOLD, 30));
        }
        npcNameLabel.setForeground(Color.WHITE);
        npcNameLabel.setBounds(50, 320, 200, 40);
        backgroundLabel.add(npcNameLabel);

        // dialogue
        dialogue = new String[] {
                "Oh hello! You must be the new riverkeeper intern I asked Lady Styx for.",
                "Welcome. It's just been me for about 4000 years and with the booming population there... I just can't keep up.",
                "Anyways, my new intern, it's time for me to teach you the ropes! You ready?",
                "Since you're a probationary riverkeeper, we'll keep it slow. Each day, I'll be giving you a checklist of three souls to catch.",
                "What happens if you don't get the quota filled in five tries?",
                "Well, you get thrown into the river like everyone else! Who knows, your replacement might even fish you out one day.",
                "I'm kidding! You should have seen your face! You just lose your job, silly.",
                "Anyways, you'll be seeing the two bars. The one on the lower left is your progress on being able to successfully fish out the soul!",
                "Every soul reacts differently to being fished out! Do your best to keep the red square in the blue square using you up and down arrow keys.",
                "But be careful! If the red square is out of bounds from the blue square, your progress drops!",
                "You get 5 attempts and there are several ways to deplete them.",
                "Don't let your progress bar drop completely! You also lose an attempt if you catch a duplicate in your checklist and if you catch a soul thats not in your checklist.",
                "Well, that would be everything! Good luck out there! Don't fall into the river!"
        };

        dialogueBox = new JTextArea();
        dialogueBox.setBounds(20, 370, 920, 120);
        dialogueBox.setOpaque(true);
        dialogueBox.setBackground(Color.BLACK);
        dialogueBox.setForeground(Color.WHITE);
        dialogueBox.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));

        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("resource/Daydream.ttf")).deriveFont(19f);
            dialogueBox.setFont(customFont);
        } catch (Exception e) {
            dialogueBox.setFont(new Font("Serif", Font.PLAIN, 19));
        }

        dialogueBox.setLineWrap(true);
        dialogueBox.setWrapStyleWord(true);
        dialogueBox.setEditable(false);
        dialogueBox.setText(dialogue[dialogueIndex]);

        dialogueBox.setMargin(new Insets(20, 40, 20, 40));

        backgroundLabel.add(dialogueBox);

        backgroundLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                advanceDialogue();
            }
        });

        // Ensure the new panel is focused
        this.requestFocusInWindow();
    }

    private void advanceDialogue() {
        if (dialogueIndex < dialogue.length - 1) {
            dialogueIndex++;
            dialogueBox.setText(dialogue[dialogueIndex]);
        } else {
            endDialogue();
        }
    }

    private void endDialogue() {
        frame.getContentPane().removeAll();
        RiverStyxFishingGame gamePanel = new RiverStyxFishingGame(frame);
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();

        gamePanel.requestFocusInWindow();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Dialogue System");
        frame.setSize(960, 540);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new DialogueSystem(frame));
        frame.setVisible(true);
    }
}

class NPC extends JLabel implements Runnable {
    private Thread npcThread;
    private boolean running = true;

    public NPC() {
        String happyImagePath = "resource/HAPPY.png";
        File happyImageFile = new File(happyImagePath);
        if (happyImageFile.exists()) {
            setIcon(new ImageIcon(happyImageFile.getAbsolutePath()));
        }

        setOpaque(false);  // Ensure transparency for sprite
        npcThread = new Thread(this);
        npcThread.start();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000);

                // Check if TALK.png exists and set the icon
                String talkImagePath = "resource/TALK.png";
                File talkImageFile = new File(talkImagePath);
                if (talkImageFile.exists()) {
                    SwingUtilities.invokeLater(() -> {
                        setIcon(new ImageIcon(talkImageFile.getAbsolutePath()));  // Change to TALK sprite
                        revalidate();
                        repaint();
                    });
                }

                Thread.sleep(1000);

                // Change back to HAPPY.png
                File happyImageFile = new File("resource/HAPPY.png");
                if (happyImageFile.exists()) {
                    SwingUtilities.invokeLater(() -> {
                        setIcon(new ImageIcon(happyImageFile.getAbsolutePath()));  // Change back to happy sprite
                        revalidate();
                        repaint();
                    });
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopNPC() {
        running = false;
    }
}
