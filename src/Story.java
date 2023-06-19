import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Story {
    public static void main(String[] args) {
        // Create a JFrame to hold the GIF
        JFrame frame = new JFrame();
        frame.setUndecorated(true);  // 타이틀 바 숨김
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Get the default graphics device
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        // Set the frame size to match the screen size
        frame.setSize(graphicsDevice.getDisplayMode().getWidth(), graphicsDevice.getDisplayMode().getHeight());

        // Set the frame position to (0, 0)
        frame.setLocation(0, 0);

        // Create an ImageIcon from the GIF file
        ImageIcon icon = new ImageIcon("src/image/story_text.png");

        // Create a JLabel to display the GIF
        JLabel label = new JLabel(icon);

        // Add the label to the frame's content pane
        frame.getContentPane().add(label);

        // Set the label's layout to center align the GIF
        label.setHorizontalAlignment(JLabel.CENTER);

        // Create a Timer to close the frame after a certain delay
        int gifDuration = 4000; // GIF duration in milliseconds (4 seconds)
        Timer timer = new Timer(gifDuration, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Start the Play screen
                WriteName2.main(new String[0]);
                // Close the frame
                frame.dispose();
            }
        });

        // Start the timer
        timer.setRepeats(false);
        timer.start();

        // Set the frame to full screen
        graphicsDevice.setFullScreenWindow(frame);

        // Show the frame
        frame.setVisible(true);
    }
}
