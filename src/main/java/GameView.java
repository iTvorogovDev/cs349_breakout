import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Timer;
import java.util.TimerTask;

import static javax.swing.BoxLayout.Y_AXIS;

//GameView serves as the principal view of the game state and the controller
public class GameView extends JPanel implements IView {

    private Model model;
    private int fps;
    private boolean devMode;
    private Timer repaintTimer;
    private TimerTask repaintTask;

    Action movePaddleLeft = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {

            model.paddle.state = Model.Paddle.MOVE_LEFT;

        }

    };

    Action movePaddleRight = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {

            model.paddle.state = Model.Paddle.MOVE_RIGHT;

        }

    };

    Action stopPaddle = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {

            model.paddle.state = Model.Paddle.STAY;

        }

    };

    Action startGame = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {

            GameView source = (GameView) e.getSource();

            //Return to title screen if all levels have been cleared
            if (model.curLevel == 0) {

                String[] args = {Integer.toString(source.fps), Integer.toString(source.model.speedMult),
                        Boolean.toString(devMode)};
                Main.frame.remove(Main.mainPanel.getComponent(0));
                Main.frame.remove(Main.mainPanel.getComponent(0));
                Main.frame.getContentPane().add(new Main.SplashScreen(args), BorderLayout.CENTER);
                Main.frame.pack();

            } else if (model.numBricks > 0 && !(model.isLost)) { //Start a level

                //Pause game on P key press
                source.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                        KeyStroke.getKeyStroke(KeyEvent.VK_P, 0, true), "pauseGame");
                source.getActionMap().put("pauseGame", pauseGame);
                model.isPaused = false;
                model.isStarted = true;
                model.ball.xSpeed = model.maxX / 5;
                model.ball.ySpeed = model.maxY / 5;
                model.paddle.speed = model.maxX * 1.5;
                source.getActionMap().remove("startGame");

            } else if (model.isLost || model.numBricks == -1) { //Reset a lost level

                setupGame();
                repaint();

            }

        }

    };

    Action pauseGame = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {

            if (model.isPaused) model.isPaused = false;
            else model.isPaused = true;

        }

    };

    Action clearLevel = new AbstractAction() { //Cheat, enabled via devMode

        public void actionPerformed(ActionEvent e) {

            model.numBricks = 0;

        }

    };

    Action quit = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {

            GameView source = (GameView) e.getSource();
            String[] args = {Integer.toString(source.fps), Integer.toString(source.model.speedMult),
                    Boolean.toString(devMode)};
            Main.frame.remove(Main.mainPanel.getComponent(0));
            Main.frame.remove(Main.mainPanel.getComponent(0));
            Main.frame.getContentPane().add(new Main.SplashScreen(args), BorderLayout.CENTER);
            Main.frame.pack();

        }

    };

    public GameView(Model model, int fps, boolean devMode) {

        this.fps = fps;
        this.devMode = devMode;
        this.setLayout(new BoxLayout(this, Y_AXIS));
        this.repaintTimer = new Timer();

        //Set up the model to be controlled and viewed
        this.model = model;
        this.setPreferredSize(new Dimension((int) model.maxX, (int) model.maxY));

        //Bind keys

        //Source: CS349 Spring 2019 A1
        //Move paddle left on left arrow press
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "paddleLeft");
        this.getActionMap().put("paddleLeft", movePaddleLeft);
        //Move paddle right on right arrow press
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "paddleRight");
        this.getActionMap().put("paddleRight", movePaddleRight);
        //Stop moving paddle on left or right arrow release
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "stopPaddle");
        this.getActionMap().put("stopPaddle", stopPaddle);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "stopPaddle");
        this.getActionMap().put("stopPaddle", stopPaddle);
        //Depending on the state, Space can either start or reset a level, or return to the title screen
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "startGame");
        this.getActionMap().put("startGame", startGame);
        //Return to title screen on Q press
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, true), "quit");
        this.getActionMap().put("quit", quit);

        if (devMode) {

            this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0, true), "clearLevel");
            this.getActionMap().put("clearLevel", clearLevel);

        }

        this.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseMoved(MouseEvent e) {

                int x = e.getX();
                if (x > 0 + model.paddle.width / 2 && !(model.isLost) && x < model.maxX - model.paddle.width / 2)
                    model.paddle.xPos = x - model.paddle.width / 2;

            }

        });

        //Source: 09.graphics - AnimationDemo.java
        repaintTimer = new Timer();
        repaintTask = new TimerTask()  {
            @Override
            public void run() {

                if (model.isPaused == false) {

                    model.updateState(1.0f / fps);

                }

            }
        };
        repaintTimer.schedule(repaintTask, 0, (1000 / fps));

    }

    public void updateSize() { //Called when the window is resized

        model.updateSize(this.getWidth(), this.getHeight());
        this.setPreferredSize(new Dimension((int) model.maxX, (int) model.maxY));

    }

    public void setupGame() { //Reset or start a new level

        model.isPaused = false;
        model.isLost = false;
        model.paddle.speed = 1000;
        model.setupGame(model.curLevel);
        repaint();

    }

    //Source: 10.mvc/hellomvc3/IView.java
    public void updateView() {

        if (model.numBricks == 0) {

            model.numBricks = -1;
            model.clearLevel();
            if (model.curLevel < 5) {

                model.curLevel++;

            } else {

                this.getActionMap().remove("clearLevel");
                model.curLevel = 0;

            }
            this.getActionMap().put("startGame", startGame);


        } else if (model.isLost) {

            this.getActionMap().put("startGame", startGame);
            model.score = 0;

        }
        repaint();

    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //Source: CS349 Spring 2019 A1
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.fillOval((int)model.ball.xPos, (int)model.ball.yPos, (int)model.ball.xSize, (int)model.ball.ySize);
        g2.fillRect((int)model.paddle.xPos, (int)model.paddle.yPos, (int)model.paddle.width, (int)model.paddle.height);

        for (int i = 0; i < model.bricks.length; ++i) {

            for (int j = 0; j < model.bricks[i].length; ++j) {

                if (model.bricks[i][j].visible) {

                    if (i % 6 == 0) g2.setColor(Color.RED);
                    else if (i % 6 == 1) g2.setColor(Color.ORANGE);
                    else if (i % 6 == 2) g2.setColor(Color.YELLOW);
                    else if (i % 6 == 3) g2.setColor(Color.GREEN);
                    else if (i % 6 == 4) g2.setColor(Color.CYAN);
                    else if (i % 6 == 5) g2.setColor(Color.MAGENTA);
                    else g2.setColor(Color.BLUE);
                    g2.fillRect((int) model.bricks[i][j].xPos, (int) model.bricks[i][j].yPos,
                            (int) model.bricks[i][j].width, (int) model.bricks[i][j].height);
                    g2.setColor(Color.BLACK);
                    g2.drawRect((int) model.bricks[i][j].xPos, (int) model.bricks[i][j].yPos,
                            (int) model.bricks[i][j].width, (int) model.bricks[i][j].height);

                }

            }

        }

        if (model.isLost) {

            String text = "Game Over";
            //Source: https://docs.oracle.com/javase/tutorial/2d/text/measuringtext.html
            FontMetrics metrics = g2.getFontMetrics(new Font("Terminator Two", Font.PLAIN, 48));
            int width = metrics.stringWidth(text);
            g2.setFont(new Font("Terminator Two", Font.PLAIN, 48));
            g2.drawString("Game Over", this.getWidth() / 2 - width / 2, this.getHeight() / 2);

        }

    }

}
