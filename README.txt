CS349 Assignment 2
A Breakout Game by Igor Tvorogov

*Must be built and run using Gradle. Alternatively, you can import the code to IntelliJ IDEA as a project and run it from there.

Features:
-Five distinct levels
-Zen mode - the game is not lost when the ball touches the bottom!
-Keeps track of high score

Controls:
-Left/Right arrow or mouse cursor - move the paddle
-Space - launch the ball/reset the level
-P - pause the game
-Q - return to title screen

The following arguments need to be included when running the game:
args[0]: FPS, numeric value. Accepted values are between 25 and 120.
args[1]: ball speed level, numeric value. The following levels are officially supported:
  1 - Slow. Easy to play, but about as fun as watching the paint dry.
  2 - Normal.
  3 - Fast, for those looking for a challenge.
  4 - Extreme, for those who enjoy suffering.
  Higher values won't break the game, but will probably be unreasonably hard.
  On the other hand, higher ball speeds increase the gained score!
args[2]: devMode, enter true or false only. If true, allows the player to advance to the next
  level by pressing F1, preserving the score. For graders' use only!

Example command line prompt: gradle run --args='100 4 true'
