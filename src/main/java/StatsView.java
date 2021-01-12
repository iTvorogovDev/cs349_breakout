import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.*;

//StatsView displays relevant info about the game, such as score and messages.
public class StatsView extends JPanel implements IView {

    private Model model;
    private JLabel score;
    private JLabel message;
    private File file = new File("./stats.b");
    private long highScore;

    public StatsView(Model model) {

        this.model = model;
        score = new JLabel("Score: " + model.score);
        message = new JLabel();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(Box.createRigidArea(new Dimension(5, 10)));
        this.add(score);
        score.setFont(new Font("Nasalization", Font.PLAIN, 28));
        score.setForeground(Color.GREEN);
            message.setFont(new Font("Lucida Console", Font.PLAIN, 18));
        message.setForeground(Color.GREEN);
        this.add(Box.createRigidArea(new Dimension(30, 10)));
        this.add(message);
        this.setBackground(Color.BLACK);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //Source: CS349 Spring 2019 A1
        BufferedReader reader = null;
        try {

            reader = new BufferedReader(new FileReader(file));

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        }
        try {

            highScore = Integer.parseInt(reader.readLine());

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    //Source: 10.mvc/hellomvc3/IView.java
    public void updateView() {

        this.score.setText("Score: " + model.score + " High Score: " + highScore);

        if (model.curLevel == 0) {

            this.message.setText("All levels completed! Press Space to play again.");
            try {

                if (highScore < model.score && model.zenMode == false) {

                    highScore = model.score;
                    //Source: CS349 Spring 2019 A1
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    writer.write(Long.toString(model.score));
                    writer.close();

                }

            } catch (IOException e) {

                e.printStackTrace();

            }


        } else if (model.numBricks > 0 && model.isLost) {

            this.message.setText("Press Space to try again");

        } else if (model.numBricks <= 0 && model.curLevel <= 5){

            this.message.setText("Nice job! Press Space to go to Level " + model.curLevel);

        } else if (model.isStarted == false) {

            this.message.setText("Press Space to begin playing");

        } else if (model.numBricks > 0) {

            this.message.setText("Level: " + model.curLevel);

        }

    }

}
