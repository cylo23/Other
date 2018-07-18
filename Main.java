/**
 * This code follows the tutorial: https://www.youtube.com/watch?v=K9qMm3JbOH0
 *
 * This class is for the frame of the game
 *
 * Version: 2018-06-28
 *
 */



package brickBreaker;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame();

        Gameplay gameplay = new Gameplay();

        frame.setBounds(10, 10, 700, 600);
        frame.setTitle("Breakout");
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gameplay);


    }

}
