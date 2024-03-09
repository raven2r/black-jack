package me.raven2r;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;

public class BlackJack extends JPanel implements KeyListener {
    private class Card {
        String value;
        String type;

        Card(String value, String type) {
            this.value = value;
            this.type = type;
        }

        public int getValue() {
            if("AJQK".contains(value)) {
                if(value.equals("A"))
                    return 11;
                else
                    return 10;
            }

            return Integer.parseInt(value);
        }

        public boolean isAce() {
            return value.equals("A");
        }

        @Override
        public String toString() {
            return value + "-" + type;
        }
    }

    ArrayList<Card> deck = new ArrayList<>();

    Card hiddenCard;
    ArrayList<Card> dealerHand = new ArrayList<>();
    int dealerSum;
    int dealerAceCount;

    ArrayList<Card> playerHand = new ArrayList<>();
    int playerSum;
    int playerAceCount;

    int boardWidth = 600;
    int boardHeight = boardWidth;

    int cardWidth = 110;
    int cardHeight = 154; // ratio 1/1.4
//    JFrame frame = new JFrame("Black Jack");

    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");


    BlackJack() {
        setLayout(new BorderLayout());
        this.setBackground(new Color(54, 121, 77));
        setPreferredSize(new Dimension(boardWidth, boardWidth));

        hitButton.addActionListener(e -> {
            var card = deck.remove(deck.size() - 1);
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);

            if(reducePlayerAce() > 21 || reduceDealerAce() > 21) {
                hitButton.setEnabled(false);
            }

            repaint();
        });

        stayButton.addActionListener(e -> {
            hitButton.setEnabled(false);
            stayButton.setEnabled(false);

            while(dealerSum < 17) {
                Card card = deck.remove(deck.size() - 1);
                dealerSum += card.getValue();
                dealerAceCount += card.isAce() ? 1 : 0;
                dealerHand.add(card);
            }

            repaint();
        });

        hitButton.setFocusable(false);
        stayButton.setFocusable(false);
        buttonPanel.add(hitButton);
        buttonPanel.add(stayButton);
        add(buttonPanel, BorderLayout.SOUTH);



        startGame();
    }

    public void startGame() {
        buildDeck();
        shuffleDeck();

        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size() - 1);
        dealerSum += hiddenCard.getValue();
        dealerAceCount = hiddenCard.isAce() ? 1 : 0;

        var card = deck.remove(deck.size() - 1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);


        playerSum = 0;
        playerAceCount = 0;

        for(int i = 1; i <= 2; i++) {
            card = deck.remove(deck.size() - 1);
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);
        }
    }

    public void buildDeck() {
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "H", "S"};

        for(int i = 0; i < values.length; i++)
            for(int j = 0; j < types.length; j++) {
                var card = new Card(values[i], types[j]);
                deck.add(card);
            }
    }

    public void shuffleDeck() {
        Collections.shuffle(deck);
    }

    public void printDeck() {
        System.out.println(deck);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            Image hiddenCardImg;
            // draw hidden card
            if(!stayButton.isEnabled())
                hiddenCardImg = new ImageIcon(getClass().getResource("/" + hiddenCard + ".png")).getImage();
            else
                hiddenCardImg = new ImageIcon(getClass().getResource("/BACK.png")).getImage();

            g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null);

            for(int i = 0; i < dealerHand.size(); i++) {
                Card card = dealerHand.get(i);
                var imgPath = "/" + card + ".png";
                Image cardImage = new ImageIcon(getClass().getResource(imgPath)).getImage();
                System.out.println("dealer write, " + imgPath);
                g.drawImage(cardImage, cardWidth + 25 + (cardWidth + 5) * i, 20 , cardWidth, cardHeight, null);
            }

            for(int i = 0; i < playerHand.size(); i++) {
                Card card = playerHand.get(i);
                var imgPath = "/" + card + ".png";
                Image cardImage = new ImageIcon(getClass().getResource(imgPath)).getImage();
                System.out.println("player draw card, " + imgPath);
                g.drawImage(cardImage, 20 + (cardWidth + 5) * i, 320 , cardWidth, cardHeight, null);
            }

            if(!stayButton.isEnabled()) {
                dealerSum = reduceDealerAce();
                playerSum = reducePlayerAce();

                System.out.println("STAY: ");
                System.out.println(dealerSum);
                System.out.println(playerSum);

                String message = "";

                if(playerSum > 21)
                    message = "You lose!";
                else if(dealerSum > 21)
                    message = "You win!";
                else if(playerSum == dealerSum)
                    message = "Tie!";
                else if(playerSum > dealerSum)
                    message = "You win!";
                else
                    message = "You lose!";

                g.setFont(new Font("Arial", Font.PLAIN, 30));
                g.setColor(Color.WHITE);
                g.drawString(message, 220, 250);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int reducePlayerAce() {
        while (playerSum > 21 && playerAceCount > 0) {
            playerSum -= 10;
            playerAceCount--;
        }

        return playerSum;
    }

    public int reduceDealerAce() {
        while (dealerSum > 21 && dealerAceCount > 0) {
            dealerSum -= 10;
            dealerAceCount--;
        }

        return dealerSum;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
