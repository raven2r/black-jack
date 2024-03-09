package me.raven2r;

import javax.swing.*;
import java.awt.*;

public class App {
    public static int GAME_WIDTH = 600;
    public static int GAME_HEIGHT = 600;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Black Jack");
        frame.setSize(GAME_WIDTH, GAME_HEIGHT);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var game = new BlackJack();
        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}