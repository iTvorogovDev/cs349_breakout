import java.awt.*;
import java.util.ArrayList;

public class Model {

    ArrayList<IView> views = new ArrayList<IView>();
    //Variables related to the functionality of the game logic
    double maxX; //X-constraint for the playing field
    double maxY; //Y-constraint for the playing field
    int speedMult;
    Ball ball;
    Paddle paddle;
    Brick[][] bricks;
    int numBricks;
    long score;
    int multiplier;
    boolean isPaused;
    boolean isLost;
    boolean isStarted;
    boolean zenMode;
    int curLevel;

    public Model(int maxX, int maxY, boolean zenMode, int speedMult) {

        this.isPaused = true;
        this.score = 0;
        this.maxX = maxX;
        this.maxY = maxY;
        this.paddle = new Paddle(maxX, maxY);
        this.ball = new Ball(paddle.xPos + paddle.width / 2, paddle.yPos + paddle.height - 30);
        this.setupGame(1);
        this.zenMode = zenMode;
        this.speedMult = speedMult;

    }

    //Build a specific level
    public void setupGame(int curLevel) {

        multiplier = 1;
        this.curLevel = curLevel;
        this.isStarted = false;
        this.isPaused = false;
        paddle.xPos = maxX / 2 - paddle.width;
        Level l = new Level(curLevel, maxX, maxY);
        bricks = l.bricks;
        numBricks = l.numBricks;
        ball.xPos = paddle.xPos + ball.xSize;
        ball.yPos = paddle.yPos - ball.ySize;
        for (IView view : this.views) {

            view.updateView();

        }


    }

    public void addView(IView view) {

        views.add(view);

    }

    public void clearLevel() {

        ball.xSpeed = 0;
        ball.ySpeed = 0;
        paddle.speed = 0;
        //Notify observing views
        for (IView view : this.views) {

            view.updateView();

        }

    }

    public void updateState(double deltaTime) {

        if (isStarted) { //Only move the ball if the game is started

            //Check for collision with paddle
            if (new Rectangle((int) Math.floor(ball.xPos), (int) Math.floor(ball.yPos),
                    (int) Math.floor(ball.xSize), (int) Math.floor(ball.ySize)).intersects
                    (new Rectangle((int)Math.floor(paddle.xPos), (int) Math.floor(paddle.yPos),
                            (int) Math.floor(paddle.width), (int) Math.floor(paddle.height)))) {

                multiplier = 1;
                if (ball.y0 >= paddle.yPos || ball.y0 <= paddle.yPos) {

                    ball.ySpeed *= -1;
                    ball.yPos = ball.y0 - ball.ySize;

                } else if (ball.x0 >= paddle.xPos || ball.x0 <= paddle.xPos) {

                    ball.xSpeed *= -1;
                    ball.xPos = ball.x0 - ball.xSize;

                }

            }

            //Check and handle collision with walls
            //Some of the code used in wall collision checking is taken from 09.graphics - AnimationDemo.java
            else if (ball.xPos < 0 || ball.xPos > (maxX - ball.xSize)) {

                ball.xSpeed *= -1;
                //Prevent the ball from clipping into the wall
                if (ball.xSpeed < 0) {

                    ball.xPos += ball.xSpeed * deltaTime - ball.xSize;

                } else {

                    ball.xPos += ball.xSpeed * deltaTime + ball.xSize;

                }
                score += 1 * speedMult;

            } else if (ball.yPos < 0) {

                ball.ySpeed *= -1;
                //Prevent the ball from clipping into the wall
                if (ball.ySpeed < 0) {

                    ball.yPos += ball.xSpeed * deltaTime - ball.ySize;

                } else {

                    ball.yPos += ball.xSpeed * deltaTime + ball.ySize;

                }
                score += 1 * speedMult;

            } else if (ball.yPos > (maxY - ball.ySize)) {

                //Lose the game if hit the bottom while not in Zen mode
                if (zenMode == false) {

                    isLost = true;
                    ball.xSpeed = 0;
                    ball.ySpeed = 0;
                    paddle.speed = 0;

                } else {

                    ball.ySpeed *= -1;
                    //Prevent the ball from clipping into the wall
                    if (ball.ySpeed < 0) {

                        ball.yPos += ball.xSpeed * deltaTime - ball.ySize;

                    } else {

                        ball.yPos += ball.xSpeed * deltaTime + ball.ySize;

                    }

                }

            } else {

                //Check and handle collision with bricks
                for (int i = 0; i < bricks.length; ++i) {

                    for (int j = 0; j < bricks[i].length; ++j) {

                        if (bricks[i][j].visible &&
                                new Rectangle((int) Math.floor(ball.xPos), (int) Math.floor(ball.yPos),
                                        (int) Math.floor(ball.xSize), (int) Math.floor(ball.ySize)).intersects
                                        (new Rectangle((int) Math.floor(bricks[i][j].xPos),
                                                (int) Math.floor(bricks[i][j].yPos),
                                                (int) Math.floor(bricks[i][j].width),
                                                (int) Math.floor(bricks[i][j].height)))) {

                            bricks[i][j].visible = false;
                            ball.xPos -= ball.xSpeed * deltaTime;
                            ball.yPos -= ball.ySpeed * deltaTime;
                            score += 10 * multiplier * speedMult;
                            ++multiplier;
                            --numBricks;
                            //Determine if the brick was hit from a side or above/below
                            if (ball.y0 >= bricks[i][j].yPos + bricks[i][j].height
                                    || ball.y0 <= bricks[i][j].yPos) ball.ySpeed *= -1;
                            else if (ball.x0 >= bricks[i][j].xPos + bricks[i][j].width
                                    || ball.x0 <= bricks[i][j].xPos) ball.xSpeed *= -1;
                            j = bricks[i].length - 1;
                            i = bricks.length - 1;

                        }

                    }

                }

                //Check if the level has been cleared
                if (numBricks == 0) {

                    clearLevel();

                }
            }

        } else { //Let the user choose the starting position for the paddle

            ball.xPos = paddle.xPos + paddle.width / 2;
            ball.yPos = paddle.yPos - ball.ySize;

        }

        //Keep track of previous position of the ball's centre.
        //Useful for checking the side of collision
        ball.x0 = ball.xPos + ball.xSize / 2;
        ball.y0 = ball.yPos + ball.ySize / 2;

        //Move the ball
        ball.xPos += speedMult * ball.xSpeed * deltaTime;
        ball.yPos += speedMult * ball.ySpeed * deltaTime;

        //Move the paddle
        if (!isPaused) {
            if (paddle.state == Paddle.MOVE_LEFT && paddle.xPos > 0) {

                paddle.xPos -= paddle.speed * deltaTime;

            } else if (paddle.state == Paddle.MOVE_RIGHT && paddle.xPos < (this.maxX - paddle.width)) {

                paddle.xPos += paddle.speed * deltaTime;

            }
        }

        //Notify observing views
        for (IView view : this.views) {

            view.updateView();

        }

    }

    public void updateSize(int newWidth, int newHeight) {

        //Scale everything on screen resize
        double scaleX = newWidth / maxX;
        double scaleY = newHeight / maxY;
        this.maxX *= scaleX;
        this.maxY *= scaleY;
        ball.xSize *= scaleX;
        ball.ySize *= scaleY;
        ball.xSpeed *= scaleX;
        ball.ySpeed *= scaleY;
        paddle.xPos *= scaleX;
        paddle.yPos *= scaleY;
        paddle.width *= scaleX;
        paddle.height *= scaleY;
        paddle.speed *= scaleX;
        if (this.isStarted) {

            ball.xPos *= scaleX;
            ball.yPos *= scaleY;

        } else {

            ball.xPos = paddle.xPos + paddle.width / 2;
            ball.yPos = paddle.yPos - ball.ySize - 1;

        }

        for (int i = 0; i < bricks.length; ++i) {

            for (int j = 0; j < bricks[i].length; ++j) {


                bricks[i][j].xPos *= scaleX;
                bricks[i][j].yPos *= scaleY;
                bricks[i][j].width *= scaleX;
                bricks[i][j].height *= scaleY;

            }

        }

    }

    public class Brick {

        double xPos;
        double yPos;
        double width;
        double height;
        boolean visible;

        public Brick(double xPos, double yPos, double width, double height) {

            this.xPos = xPos;
            this.yPos = yPos;
            this.width = width;
            this.height = height;
            visible = true;

        }

    }

    //Level is a collection of Bricks used by the model
    public class Level {

        Brick bricks[][];
        int numBricks;

        Level(int level, double maxX, double maxY) {

            if (level == 1) {

                bricks = new Brick[6][6];
                double brickWidth = maxX / 6;
                double brickHeight = maxY / 20;

                for (int i = 0; i < bricks.length; ++i) {

                    for (int j = 0; j < bricks[i].length; ++j) {

                        bricks[i][j] = new Brick(0 + brickWidth * j,
                                0 + brickHeight * i, brickWidth, brickHeight);
                        ++numBricks;

                    }

                }

            } else if (level == 2) {

                bricks = new Brick[6][6];
                double brickWidth = maxX / 9;
                double brickHeight = maxY / 15;

                for (int i = 0; i < bricks.length; ++i) {

                    for (int j = 0; j < bricks[i].length; ++j) {


                        bricks[i][j] = new Brick(maxX / 2 + brickWidth * (j - 3),
                                maxY / 6 + brickHeight * i, brickWidth, brickHeight);
                        ++numBricks;

                    }

                }

            } else if (level == 3) {

                double brickWidth = maxX / 9;
                double brickHeight = maxY / 16;
                bricks = new Brick[12][];

                for (int i = 0; i < 6; ++i) {

                    bricks[i] = new Brick[i + 1];

                    for (int j = 0; j < bricks[i].length; ++j) {

                        bricks[i][j] = new Brick(maxX / 2 - (brickWidth / 2) * (i + 1) + brickWidth * j,
                                0 + brickHeight * i, brickWidth, brickHeight);
                        ++numBricks;

                    }

                }

                for (int i = 6; i < bricks.length; ++i) {

                    bricks[i] = new Brick[12 - i];

                    for (int j = 0; j < bricks[i].length; ++j) {

                        bricks[i][j] = new Brick(maxX / 2 - (brickWidth / 2) * (12 - i) + brickWidth * j,
                                0 + brickHeight * i, brickWidth, brickHeight);
                        ++numBricks;

                    }

                }

            } else if (level == 4) {

                double brickWidth = maxX / 12;
                double brickHeight = maxY / 25;
                bricks = new Brick[12][];

                for (int i = 0; i < 6; ++i) {

                    bricks[i] = new Brick[12 - i];

                    for (int j = 0; j < bricks[i].length; ++j) {

                        bricks[i][j] = new Brick(0 + brickWidth * j + (brickWidth / 2) * i,
                                0 + brickHeight * i, brickWidth, brickHeight);
                        ++numBricks;

                    }

                }

                for (int i = 6; i < bricks.length; ++i) {

                    bricks[i] = new Brick[i + 1];

                    for (int j = 0; j < bricks[i].length; ++j) {

                        bricks[i][j] = new Brick(maxX / 2 - (brickWidth / 2) * (i + 1) + brickWidth * j,
                                0 + brickHeight * i, brickWidth, brickHeight);
                        ++numBricks;

                    }

                }

            } else if (level == 5) {

                double brickWidth = maxX / 25;
                double brickHeight = maxY / 25;
                bricks = new Brick[6][25];

                for (int i = 0; i < bricks.length; ++i) {

                    for (int j = 0; j < bricks[i].length; ++j) {

                        bricks[i][j] = new Brick(0 + brickWidth * j, 0 + brickHeight * i + 50 * i,
                                brickWidth, brickHeight);
                        ++numBricks;

                    }

                }

            }

        }

    }

    public class Ball {

        double xSpeed;
        double ySpeed;
        double x0;
        double y0;
        double xPos;
        double yPos;
        double xSize;
        double ySize;

        public Ball(double xPos, double yPos) {

            this.xSize = 30;
            this.ySize = 30;
            this.xPos = xPos;
            this.yPos = yPos;
            this.xSpeed = 0;
            this.ySpeed = 0;

        }

    }

    public class Paddle {

        static final int MOVE_LEFT = 0;
        static final int MOVE_RIGHT = 1;
        static final int STAY = 2;

        double speed;
        double xPos;
        double yPos;
        double width;
        double height;
        double state;

        public Paddle(double maxX, double maxY) {

            this.width = 100;
            this.height = 10;
            this.xPos = maxX / 2 - width;
            this.yPos = maxY - 60;
            this.speed = 1000;
            this.state = this.STAY;

        }

    }

}


