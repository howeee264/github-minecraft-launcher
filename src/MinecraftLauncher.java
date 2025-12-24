import javax.swing.*;
import java.awt.*;

public class MinecraftLauncher {

    static void main() {

    }public static void main(String[] args) {

        // Frame
        JFrame frame = new JFrame("Minecraft Launcher");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Panel
        JPanel panel = new JPanel() {
            Image bg = new ImageIcon("src/resources/background.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JLabel title = new JLabel("Minecraft Launcher", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle (THIS IS THE NEW PART YOU ASKED FOR)
        JLabel subtitle = new JLabel("Fan-made launcher", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();

        // Version selector
        JLabel versionLabel = new JLabel("Version:");
        String[] versions = {"1.21.1", "1.21", "1.20.1"};
        JComboBox<String> versionBox = new JComboBox<>(versions);

        // Play button
        JButton playButton = new JButton("PLAY");
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Status label
        JLabel statusLabel = new JLabel(" ");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button action
        playButton.addActionListener(e -> {
            String username = usernameField.getText();
            String version = (String) versionBox.getSelectedItem();
            statusLabel.setText("Launching " + version + " as " + username);
        });

        // Add everything to panel (ORDER MATTERS)
        panel.add(title);
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(10));
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(versionLabel);
        panel.add(versionBox);
        panel.add(Box.createVerticalStrut(10));
        panel.add(playButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(statusLabel);

        // Add panel to frame
        frame.add(panel);
        frame.setVisible(true);
    }
}
