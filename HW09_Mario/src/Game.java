import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game implements Runnable {

    @Override
    public void run() {
     // NOTE : recall that the 'final' keyword notes inmutability
        // even for local variables.

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Super Mario");
        frame.setLocation(300, 200);

        // Status panel
        final JPanel status_panel = new JPanel();
        status_panel.setLayout(new GridLayout());
        frame.add(status_panel, BorderLayout.NORTH);
        final JLabel score_label = new JLabel("Score: 0", SwingConstants.CENTER);
        status_panel.add(score_label);
        final JLabel coins_label = new JLabel("Coins: 0", SwingConstants.CENTER);
        status_panel.add(coins_label);
        final JLabel lives_label = new JLabel("Lives: 3", SwingConstants.CENTER);
        status_panel.add(lives_label);
        
        // Welcome Screen
        final JPanel welcomeScreen = new JPanel();
        welcomeScreen.setLayout(new BoxLayout(welcomeScreen, BoxLayout.Y_AXIS));
        welcomeScreen.setBackground(new Color(107, 140, 255));
        final ImageIcon titleImage = new ImageIcon("TitleScreen.png");
        final JLabel title = new JLabel(titleImage);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeScreen.add(title);
        JLabel enterUsername = new JLabel("Please enter a username with no spaces and no more than 5 characters.");
        welcomeScreen.add(enterUsername);
        enterUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Instructions Screen
        final JPanel instructionsScreen = new JPanel();
        instructionsScreen.setBackground(Color.BLACK);
        
        // Main playing area
        final GameCourt court = new GameCourt(score_label, coins_label, lives_label);
        court.setBackground(new Color(107, 140, 255));
//        frame.add(court, BorderLayout.CENTER);
        
        // High Score Screen
        final JPanel highScoreScreen = new JPanel();
        highScoreScreen.setBackground(Color.WHITE);
        
        // Card Layout
        final CardLayout cl = new CardLayout();
        final JPanel contentPanel = new JPanel(cl);
        contentPanel.add(welcomeScreen, "Welcome Screen");
        contentPanel.add(instructionsScreen, "Instructions Screen");
        contentPanel.add(court, "Game");
        contentPanel.add(highScoreScreen, "High Score Screen");
        frame.add(contentPanel, BorderLayout.CENTER);
        
        // Username text field
        final JTextField usernameBox = new JTextField(10);
        usernameBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameBox.setMaximumSize(usernameBox.getPreferredSize());
        ActionListener submitUsername = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (usernameBox.getText().length() == 0) {
                    GameCourt.username = "silly";
                } else {
                    if (usernameBox.getText().length() >= 5) {
                        GameCourt.username = usernameBox.getText().substring(0, 4);
                    } else {
                        GameCourt.username = usernameBox.getText();
                    }
                }
                cl.show(contentPanel, "Instructions Screen");
            }
        };
        usernameBox.addActionListener(submitUsername);
        welcomeScreen.add(usernameBox);
        
        // Let's Play button
        final JButton letsPlay = new JButton("Let's Play!");
        letsPlay.setAlignmentX(Component.CENTER_ALIGNMENT);
        letsPlay.addActionListener(submitUsername);
        welcomeScreen.add(letsPlay);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.SOUTH);
        
        // To Game button
        final JButton instructionsToGame = new JButton("To Game");
        final JButton highScoresToGame = new JButton("To Game");
        ActionListener toGame = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!(GameCourt.username.equals(""))) {
                    cl.show(contentPanel, "Game");
                    GameCourt.playing = true;
                    court.requestFocus();
                } else {
                    cl.show(contentPanel, "Welcome Screen");
                }
            }
        };
        
        instructionsToGame.addActionListener(toGame);
        highScoresToGame.addActionListener(toGame);
        instructionsScreen.add(instructionsToGame);
        highScoreScreen.add(highScoresToGame);
        
        // Instructions button
        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GameCourt.playing = false;
                cl.show(contentPanel, "Instructions Screen");
            }
        });
        control_panel.add(instructions);
        
        // High Scores button
        final JButton highScores = new JButton("High Scores");
        highScores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GameCourt.playing = false;
                cl.show(contentPanel, "High Score Screen");
            }
        });
        control_panel.add(highScores);

        // Note here that when we add an action listener to the reset
        // button, we define it as an anonymous inner class that is
        // an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed,
        // actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);

        // Start game
        court.reset();
    }

    /*
     * Main method run to start and run the game Initializes the GUI elements
     * specified in Game and runs it IMPORTANT: Do NOT delete! You MUST include
     * this in the final submission of your game.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
