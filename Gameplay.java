/**
 * This code follows the tutorial: https://www.youtube.com/watch?v=K9qMm3JbOH0
 *
 * Modifications to tutorial made by Ching Yee Lo - added levels and resolved sticky keys
 *
 * Version: 2018-06-28
 *
 */


package brickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener {


    private static final long serialVersionUID = 4102841515739706193L;
    private boolean play = false;
    private int score = 0;
    private int level = 1;

    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXDir = 3; //  ball velocity
    private int ballYDir = 4; //  ball velocity

    private MapGenerator map;

    private boolean[] keyDown = new boolean[2]; // boolean array to resolve sticky keys for the paddle

    public Gameplay() {

        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();

        keyDown[0] = false; // left
        keyDown[1] = false; // right

    }

    public void paint(Graphics g) {
        // background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // drawing map
        map.draw((Graphics2D) g);

        // border for Frame
        g.setColor(Color.black);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score: " + score, 560, 30);

        // Level
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Level " + level, 450, 30);

        // the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // the ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);

        // level up

        if (totalBricks <= 0 && level == 3) {
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 45));
            g.drawString("You won", 250, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to restart", 245, 350);

        }

        else if (totalBricks <= 0) {
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 45));
            g.drawString("Next Level", 260, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to start", 280, 350);

        }

        // if (totalBricks <= 0) {
        // play = false;
        // ballXDir = 0;
        // ballYDir = 0;
        // g.setColor(Color.red);
        // g.setFont(new Font("serif", Font.BOLD, 45));
        // g.drawString("You won", 250, 300);
        //
        // g.setFont(new Font("serif", Font.BOLD, 20));
        // g.drawString("Press Enter to restart", 245, 350);
        //
        // }

        // Display Game Over when the ball goes out of play
        if (ballposY > 570) { // when ball is > 570 in y position
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            level = 1;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 45));
            g.drawString("Game Over", 250, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to restart", 265, 350);

        }

        g.dispose();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {

            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYDir = -ballYDir;
            }

            // make the logic for the bricks to disappear - nested loop to go through all of
            // the bricks

            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;

                        // if ball intersects brick then call setBrickValue method to set it to 0 for it
                        // to disappear
                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                ballXDir = -ballXDir;
                            } else {
                                ballYDir = -ballYDir;

                            }

                            break A; // break from outer loop

                        }

                    }

                }

            }

            ballposX += ballXDir;
            ballposY += ballYDir;

            if (ballposX < 0) { // for the left border
                ballXDir = -ballXDir;
            }

            if (ballposY < 0) { // for the top
                ballYDir = -ballYDir;
            }

            if (ballposX > 670) {
                ballXDir = -ballXDir;

            }

        }

        repaint(); // for the paddle

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }

        if (key == KeyEvent.VK_LEFT) {
            if (playerX <= 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }

        if (key == KeyEvent.VK_ENTER) { // Restart when game over

            if (!play && totalBricks <= 0 && level == 3) {
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXDir = 3;
                ballYDir = 4;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);

                repaint();

                level = 1;

            } else if(!play && totalBricks <= 0) {
                level();

            } else if (!play) {
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXDir = 3;
                ballYDir = 4;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);

                repaint();

                level = 1;

            }

        }

        if (key == KeyEvent.VK_ESCAPE)
            System.exit(1);

    }

    public void moveRight() {
        play = true;
        {
            playerX += 75;
            keyDown[0] = true;
        }
        // playerX += 70; // move 20 to the right

    }

    public void moveLeft() {
        play = true;
        {
            playerX += -75;
            keyDown[1] = true;
        }
    }

    public void level() {

        if (level == 1) {

            play = true;
            ballposX = 120;
            ballposY = 350;
            ballXDir = 4;
            ballYDir = 5;
            playerX = 310;
//			score = 0;
            totalBricks = 28;
            level++;

            map = new MapGenerator(4, 7);

            repaint();

            // level++;
        }

        else if (level == 2) {

            play = true;
            ballposX = 120;
            ballposY = 350;
            ballXDir = 4;
            ballYDir = 5;
            playerX = 310;
//			score = 0;
            totalBricks = 42;
            map = new MapGenerator(6, 7);

            repaint();

            level++;

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_RIGHT)
            keyDown[0] = false;
        if (key == KeyEvent.VK_LEFT)
            keyDown[1] = false;

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }




}
