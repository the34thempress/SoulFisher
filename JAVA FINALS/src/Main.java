import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Soul Fisher");
        frame.setSize(960, 540);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Start with the HomeScreen
        HomeScreen homeScreen = new HomeScreen(frame);
        frame.add(homeScreen);
        frame.setVisible(true);

        homeScreen.setPlayButtonPosition(630, 400);
    }
}






