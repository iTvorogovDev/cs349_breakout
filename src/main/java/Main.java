import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    static JFrame frame;
    static JPanel mainPanel;
    static Model model;
    static GameView gameView;
    static StatsView statsView;

    public static void main(String[] args) {

        if (Integer.parseInt(args[0]) > 120 || Integer.parseInt(args[0]) < 25) {

            //Source: CS349 Spring 2019 A1
            JOptionPane optionPane = new JOptionPane();
            optionPane.showMessageDialog(frame,
                    "Please use FPS between 25 and 120 inclusive",
                    "Error", JOptionPane.ERROR_MESSAGE);

        } else {

            frame = new JFrame("Breakout");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setMinimumSize(new Dimension(1024, 768));
            mainPanel = new JPanel(new BorderLayout());
            frame.setContentPane(mainPanel);
            mainPanel.add(new SplashScreen(args), BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);

        }

    }

    private static void startGame(boolean zenMode, SplashScreen ss, String[] args) {

        model = new Model(frame.getWidth(), frame.getHeight(), zenMode, Integer.parseInt(args[1]));
        gameView = new GameView(model, Integer.parseInt(args[0]), Boolean.parseBoolean(args[2]));
        statsView = new StatsView(model);
        model.addView(gameView);
        model.addView(statsView);
        mainPanel.remove(ss);
        mainPanel.add(gameView, BorderLayout.CENTER);
        mainPanel.add(statsView, BorderLayout.SOUTH);
        frame.pack();
        frame.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {

                gameView.updateSize();

            }

        });
        gameView.setupGame();

    }

    public static class SplashScreen extends JPanel implements ActionListener {

        JButton playButton = new JButton("Play!");
        JButton zenButton = new JButton("Zen mode");
        JButton resetHighScoreButton = new JButton("Reset High Score");
        JButton exitButton = new JButton("Exit");
        JLabel[] text = new JLabel[17];
        String[] args;

        public SplashScreen(String[] args) {

            this.args = args;
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            playButton.setAlignmentX(CENTER_ALIGNMENT);
            playButton.setFont(new Font("Alien Encounters", Font.BOLD, 22));
            playButton.setActionCommand("play");
            zenButton.setFont(new Font("Calligraphic", Font.BOLD, 22));
            zenButton.setAlignmentX(CENTER_ALIGNMENT);
            zenButton.setActionCommand("zen");
            resetHighScoreButton.setFont(new Font("Russo One", Font.PLAIN, 22));
            resetHighScoreButton.setAlignmentX(CENTER_ALIGNMENT);
            resetHighScoreButton.setActionCommand("resetHighScore");
            exitButton.setFont(new Font("Russo One", Font.PLAIN, 22));
            exitButton.setAlignmentX(CENTER_ALIGNMENT);
            exitButton.setActionCommand("exit");
            for (int i = 0; i < text.length; ++i) {

                text[i] = new JLabel();
                text[i].setFont(new Font("Russo One", Font.BOLD, 20));
                text[i].setAlignmentX(CENTER_ALIGNMENT);

            }
            text[0].setText("Welcome to a game of Breakout!");
            text[1].setText("A CS349 Project by Igor Tvorogov");
            text[2].setText("Bounce the ball using a paddle and don't let it fall down!");
            text[3].setText("If the ball falls down, you can press Space to try the level again");
            text[4].setText("However, you will lose all points");
            text[5].setText("Destroy all bricks to clear a level");
            text[6].setText("Hit bricks without touching the paddle to maximize the score");
            text[7].setText("Once you clear all 5 levels, you can press Space to return to title screen");
            text[8].setText("Left Arrow: move paddle left");
            text[9].setText("Right Arrow: move paddle right");
            text[10].setText("You can also move the paddle using the mouse");
            text[11].setText("P: take a break");
            text[12].setText("Press Play to test your skills");
            text[13].setText("or Zen Mode for a relaxing experience.");
            text[14].setText("(High score is not saved in Zen mode)");
            text[15].setText("You can press Q anytime to return to the title screen");
            text[16].setText("Have fun!");
            this.add(Box.createRigidArea(new Dimension(10, 20)));
            for (int i = 0; i < text.length; ++i) {

                this.add(text[i]);

            }
            //Source: https://docs.oracle.com/javase/tutorial/uiswing/layout/box.html#filler
            //for creating empty space with rigid areas
            this.add(Box.createRigidArea(new Dimension(5, 20)));
            this.add(playButton);
            this.add(Box.createRigidArea(new Dimension(5, 20)));
            this.add(zenButton);
            this.add(Box.createRigidArea(new Dimension(5, 20)));
            this.add(resetHighScoreButton);
            this.add(Box.createRigidArea(new Dimension(5, 20)));
            this.add(exitButton);
            playButton.addActionListener(this);
            zenButton.addActionListener(this);
            resetHighScoreButton.addActionListener(this);
            exitButton.addActionListener(this);

        }

        public void actionPerformed(ActionEvent e) {

            JButton source = (JButton) e.getSource();

            if (source.getActionCommand().equals("play")) {

                startGame(false, this, args);

            } else if (source.getActionCommand().equals("zen")) {

                startGame(true, this, args);

            } else if (source.getActionCommand().equals("resetHighScore")) {

                try {

                    File file = new File("./stats.b");
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    writer.write(Long.toString(0));
                    writer.close();

                } catch (IOException exception) {

                    exception.printStackTrace();

                }

            } else if (source.getActionCommand().equals("exit")) {

                System.exit(0);

            }

        }

    }

}
