import java.awt.*;
import java.awt.event.*;
import java.util.List;
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

        // Status panel contains the Score/Coins/Lives
        final JPanel status_panel = new JPanel();
        status_panel.setLayout(new GridLayout());
        frame.add(status_panel, BorderLayout.NORTH);
        
        final JLabel score_label = new JLabel("Score: 0", SwingConstants.CENTER);
        status_panel.add(score_label);
        
        final JLabel coins_label = new JLabel("Coins: 0", SwingConstants.CENTER);
        status_panel.add(coins_label);
        
        final JLabel lives_label = new JLabel("Lives: 3", SwingConstants.CENTER);
        status_panel.add(lives_label);

        // Control Panel with Instructions & High Score panel buttons
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.SOUTH);
        
        // Card Layout for the Welcome Screen, Game, High Scores & Instructions
        final CardLayout cl = new CardLayout();
        final JPanel contentPanel = new JPanel(cl);
        frame.add(contentPanel, BorderLayout.CENTER);
        
        // Welcome Screen
        final JPanel welcomeScreen = new JPanel();
        welcomeScreen.setLayout(new BoxLayout(welcomeScreen, BoxLayout.Y_AXIS));
        welcomeScreen.setBackground(new Color(107, 140, 255));
        
        // Welcome Screen: Title Image
        final ImageIcon titleImage = new ImageIcon("TitleScreen.png");
        final JLabel title = new JLabel(titleImage);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeScreen.add(title);
        
        // Welcome Screen: Username instrucions
        JLabel enterUsername = new JLabel("Please enter a username.");
        enterUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeScreen.add(enterUsername);
        JLabel noSpaces = new JLabel("No spaces and no more than 6 characters.");
        noSpaces.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeScreen.add(noSpaces);
        
        // Welcome Screen: Username text box & action listener
        final JTextField usernameBox = new JTextField(10);
        usernameBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameBox.setMaximumSize(usernameBox.getPreferredSize());
        ActionListener submitUsername = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userText = usernameBox.getText();
                userText.replaceAll("\\s", "");
                if (userText.length() == 0) GameCourt.username = "silly";
                else if (userText.length() > 6) GameCourt.username = userText.substring(0, 6);
                else GameCourt.username = userText;
                
                cl.show(contentPanel, "Instructions Screen");
            }
        };
        usernameBox.addActionListener(submitUsername);
        welcomeScreen.add(usernameBox);

        contentPanel.add(welcomeScreen, "Welcome Screen"); // add Welcome Screen to contentPanel
        
        // Instructions Screen
        final JPanel instructionsScreen = new JPanel();
        instructionsScreen.setLayout(new BoxLayout(instructionsScreen, BoxLayout.Y_AXIS));
        instructionsScreen.setBackground(Color.BLACK);
        contentPanel.add(instructionsScreen, "Instructions Screen");
        final JLabel instructionHelp = new JLabel("<html>" + "Welcome to Super Mario!<br>" +
        "You will be controlling Mario using the left and right arrow keys to move him<br>" +
        "left and right (respectively) and the up arrow key to make him jump. Your goal is to<br>" +
        "make it to the end of the game. Pick up coins along the way to add to your score.<br>" +
        "Jump on enemies to kill them and add to your score but don't touch them directly or <br>" +
        "they will kill you. Don't fret if you die though, you'll have 3 chances to get to<br>" +
        "the end of the level. Check out the high scores too and see if you can beat them.<br>" +
        "Good luck!</html>");
        instructionHelp.setForeground(Color.WHITE);
        instructionsScreen.add(instructionHelp);
        
        // Main playing area
        final GameCourt court = new GameCourt(score_label, coins_label, lives_label);
        court.setBackground(new Color(107, 140, 255));
        contentPanel.add(court, "Game");
        
        // High Score Screen
        final JPanel highScoreScreen = new JPanel();
        highScoreScreen.setLayout(new BoxLayout(highScoreScreen, BoxLayout.Y_AXIS));
        highScoreScreen.setBackground(Color.BLACK);
        final HighScores hs = new HighScores();
        contentPanel.add(highScoreScreen, "High Score Screen");
        
        // Go to game buttons for insturcion & high score screens
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
        highScoresToGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionsToGame.addActionListener(toGame);
        highScoresToGame.addActionListener(toGame);
        instructionsScreen.add(instructionsToGame);
        
        // High Scores button & action listener
        final JButton highScores = new JButton("High Scores");
        highScores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GameCourt.playing = false;
                highScoreScreen.removeAll();
                JLabel highScoreIntro = new JLabel("High Scores:");
                highScoreIntro.setForeground(Color.WHITE);
                highScoreIntro.setAlignmentX(Component.CENTER_ALIGNMENT);
                highScoreScreen.add(highScoreIntro);
                List<HighScore> highScoreList = hs.getTopTenHighScores();
                if (highScoreList.size() == 0) {
                    JLabel noHS = new JLabel("None yet.");
                    noHS.setForeground(Color.WHITE);
                    noHS.setAlignmentX(Component.CENTER_ALIGNMENT);
                    highScoreScreen.add(noHS);
                } else {
                    int i = 1;
                    for (HighScore h : highScoreList) {
                        JLabel nextHS = new JLabel(i + ". " + h.getUsername() + " " + h.getScore());
                        nextHS.setForeground(Color.WHITE);
                        nextHS.setAlignmentX(Component.CENTER_ALIGNMENT);
                        highScoreScreen.add(nextHS);
                        i++;
                    }
                }
                highScoreScreen.add(highScoresToGame);
                cl.show(contentPanel, "High Score Screen");
            }
        });
        control_panel.add(highScores);
        
        // Let's Play button
        final JButton letsPlay = new JButton("Let's Play!");
        letsPlay.setAlignmentX(Component.CENTER_ALIGNMENT);
        letsPlay.addActionListener(submitUsername);
        welcomeScreen.add(letsPlay);
        
        // Instructions button
        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GameCourt.playing = false;
                cl.show(contentPanel, "Instructions Screen");
            }
        });
        control_panel.add(instructions);

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
