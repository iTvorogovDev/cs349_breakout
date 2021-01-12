# CS349 A2
Student: itvorogo
Marker: Blaine


Total: 20 / 100 (20.00%)

Code:
(CO: won’t compile, CR: crashes, FR: UI freezes/unresponsive, NS: not submitted)


Notes:  Your game failed to run and instead output an error: Exception in thread "AWT-EventQueue-0" java.lang.ArrayIndexOutOfBoundsException: 2 

## TECHNICAL REQUIREMENTS (30)

1. [5/5] Basic Requirements

2. [0/10] Handling Command-Line Parameters

-5 The program takes in the parameter for adjusting frames per second. Use 25, 40 as test cases. Adjusting this value changes the rate at which the screen draws, but does not affect ball speed

-5 The program takes in the parameter for adjusting the ball speed. Use 1, 2, 3, as test cases, without modifying the frame rate. There should be an obvious difference in speed across these values. Ball speed should be independent of frame rate adjustments.

3. [0/10] Window Behaviour

-2 The user should be able to resize the window, making it larger or smaller than the starting size.

-3 There should be a minimum and maximum size that is supported.

-5 When the window is resized, all of the graphical elements of the screen should be resized as well. It is acceptable to constrain the aspect ratio, or put borders to try and maintain the same aspect ratio.

4. [0/5] Controls.
-2 Mouse can be used as input to control the game. The controls should be responsive enough to support gameplay.
-3 and keyboard are used as input to control the game. The controls should be responsive enough to support gameplay.

## GAMEPLAY (40)

5. [5/5] The program opens with a splash screen, displaying the student's name and ID are displayed. Instructions for playing the game (key usage) are also displayed.

6. [0/10] There are at least 5 rows of coloured blocks arranged at the top of the screen, and a paddle at the bottom that can be moved left or right.

-2 There are at least 5 rows of blocks
-3 The blocks are coloured
-5 Paddle at the bottom can move left and right

7. [0/5] When the game starts, the ball starts to move across the screen.

8. [0/5] The ball bounces when struck by the paddle, or when striking a brick, roughly conforming to the reflection law.

9. [0/5] If the ball hits a block, the block disappears, and the ball bounces.

10. [0/5] The game ends when the ball touches the bottom of the screen.

11. [0/5] There is a score system rewarding bounces of the ball or hits of the block. The score is displayed and updated in real-time when the ball hits the paddle (optionally, it can score for other factors, like hitting bricks).

## MODEL-VIEW-CONTROLLER (10)

12. [5/5] A model class exists which manages the animation timer and updates game elements.

13. [5/5] A view class exists that handles drawing the bricks, paddle and ball.

## ADDITIONAL FEATURE (10)

14. [0/10] An additional feature at the student's choice is described and implemented. The feature must be a significant enhacement.

Examples:
- Adding extra levels that appear when the current level is cleared.
- Randomly selecting one of many levels, each with a different configuration, when the game is launched.
- Adding power-ups that can be earned (e.g. special block that makes the paddle wider for a short period of time).

## AESTHETICS (10)

15. [0/5] The interface design is aesthetically pleasing.
16. [0/5] The game is enjoyable to play (responsive paddle, ball speed that makes for exciting gameplay, no lag).
