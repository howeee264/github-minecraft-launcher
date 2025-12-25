import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;

public class MinecraftLauncher {
    static void main() {
        SwingUtilities.invokeLater(LauncherUI::new);
    }
}

class LauncherUI extends JFrame {

    private final JComboBox<String> versionBox;
    private final DefaultListModel<File> modModel = new DefaultListModel<>();
    private File modsDir;

    LauncherUI() {
        setTitle("Welcome to Howeees Minecraft Launcher");
        setSize(750, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        applyDarkTheme();

        versionBox = new JComboBox<>(new String[]{
                "1.21.11", "1.21.10", "1.21.9"
        });
        versionBox.addActionListener(_ -> loadProfile());

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Home", buildHome());
        tabs.add("Mods", buildMods());

        add(tabs);
        loadProfile();
        setVisible(true);
    }

    /* ================= HOME ================= */

    private JPanel buildHome() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(25, 25, 25));

        JLabel title = new JLabel("Welcome to Howeees Minecraft Launcher");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("have fun ");
        subtitle.setForeground(Color.LIGHT_GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton play = new JButton("PLAY");
        JButton playSurvival = new JButton("PLAY SURVIVAL");

        play.setAlignmentX(Component.CENTER_ALIGNMENT);
        playSurvival.setAlignmentX(Component.CENTER_ALIGNMENT);

        play.addActionListener(_ -> launchMinecraft());
        playSurvival.addActionListener(_ -> {
            createSurvivalWorld();
            launchMinecraft();
        });

        panel.add(Box.createVerticalGlue());
        panel.add(title);
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(20));
        panel.add(new JLabel("Version:"));
        panel.add(versionBox);
        panel.add(Box.createVerticalStrut(20));
        panel.add(play);
        panel.add(Box.createVerticalStrut(10));
        panel.add(playSurvival);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /* ================= MODS ================= */

    private JPanel buildMods() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));

        JList<File> modList = new JList<>(modModel);
        modList.setBackground(new Color(40, 40, 40));
        modList.setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(modList);

        JButton add = new JButton("Add Mod");
        JButton toggle = new JButton("Enable / Disable");
        JButton download = new JButton("Download Mod (URL)");
        JButton open = new JButton("Open Mods Folder");

        add.addActionListener(_ -> addMod());
        toggle.addActionListener(_ -> toggleMod(modList.getSelectedValue()));
        download.addActionListener(_ -> downloadMod());
        open.addActionListener(_ -> {
            try {
                Desktop.getDesktop().open(modsDir);
            } catch (Exception ignored) {}
        });

        JPanel buttons = new JPanel();
        buttons.setBackground(new Color(30, 30, 30));
        buttons.add(add);
        buttons.add(toggle);
        buttons.add(download);
        buttons.add(open);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    /* ================= LOGIC ================= */

    private void loadProfile() {
        String version = (String) versionBox.getSelectedItem();
        modsDir = new File(System.getenv("APPDATA"),
                ".minecraft/howeees-launcher/" + version + "/mods");

        modsDir.mkdirs();
        modModel.clear();

        File[] files = modsDir.listFiles();
        if (files != null) {
            for (File f : files) {
                modModel.addElement(f);
            }
        }
    }

    private void addMod() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter("Mod (.jar)", "jar"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Files.copy(
                        chooser.getSelectedFile().toPath(),
                        new File(modsDir,
                                chooser.getSelectedFile().getName()).toPath()
                );
                loadProfile();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to add mod");
            }
        }
    }

    private void toggleMod(File mod) {
        if (mod == null) return;

        File renamed;
        if (mod.getName().endsWith(".disabled")) {
            renamed = new File(modsDir,
                    mod.getName().replace(".disabled", ""));
        } else {
            renamed = new File(modsDir,
                    mod.getName() + ".disabled");
        }
        mod.renameTo(renamed);
        loadProfile();
    }

    private void downloadMod() {
        String url = JOptionPane.showInputDialog(this,
                "Paste direct .jar mod URL:");

        if (url == null || !url.endsWith(".jar")) {
            JOptionPane.showMessageDialog(this, "Invalid mod URL");
            return;
        }

        try (InputStream in = new URL(url).openStream()) {
            File out = new File(modsDir,
                    url.substring(url.lastIndexOf("/") + 1));
            Files.copy(in, out.toPath());
            loadProfile();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Download failed");
        }
    }

    private void createSurvivalWorld() {
        File saves = new File(System.getenv("APPDATA"), ".minecraft/saves");
        File world = new File(saves, "HoweeesSurvival");

        if (world.exists()) return;

        try {
            world.mkdirs();
            new File(world, "level.dat").createNewFile();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "World creation failed");
        }
    }

    private void launchMinecraft() {
        try {
            Runtime.getRuntime().exec(
                    "cmd /c start \"\" \"C:\\Program Files (x86)\\Minecraft Launcher\\MinecraftLauncher.exe\""
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Minecraft Launcher not found");
        }
    }

    /* ================= THEME ================= */

    private void applyDarkTheme() {
        UIManager.put("Label.foreground", Color.WHITE);
        UIManager.put("Button.background", new Color(60, 60, 60));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("ComboBox.background", new Color(60, 60, 60));
        UIManager.put("ComboBox.foreground", Color.WHITE);
    }
}
