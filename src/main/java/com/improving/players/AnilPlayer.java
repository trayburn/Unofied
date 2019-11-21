package com.improving.players;

import com.improving.game.*;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
@Component
public class AnilPlayer implements IPlayer {

        LinkedList<Card> hand;
        private String name;
        private java.util.List<Optional> chooseColor;


        public AnilPlayer(String name, Deck deck) {
            this.hand = new LinkedList<>();
            this.name = name;
            dealHand(deck);
        }

        public void dealHand(Deck deck) {
            for (int i = 0; i < 7; i++) {
                hand.add(deck.draw());
            }
        }

        public java.util.List<Card> getHand() {
            return hand;
        }

        @Override
        public void takeTurn(IGame game) {
            for (var card : hand) {
                if (game.isPlayable(card)) {
                    hand.remove(card);
                    game.playCard(card, Optional.of(Colors.Red), this);
                    System.out.println(card + " was played ");
                    return;
                }
            }
            hand.add(draw(game));
            System.out.println("Drew card");
        }

        public void playHighestValue(IGame game) {
            for (var cards : hand) {
                if(cards.getFace().getValue() > 10){
                }
            }
        }


        public String getName() {
            return name;
        }

        @Override
        public int handSize() {
            return hand.size();
        }

        @Override
        public Card draw(IGame game) {
            return game.draw();
        }

        @Override
        public void newHand(List<Card> cards){
            this.hand.clear();
            this.hand.addAll(cards);
        }
    }
    
