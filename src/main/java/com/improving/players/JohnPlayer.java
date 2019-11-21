package com.improving.players;
import com.improving.game.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JohnPlayer implements IPlayer {
    private String name;
    private List<Card> hand = new ArrayList<>();


    public JohnPlayer(List<Card> hand) {
        this.name = name;
    }


    public void takeTurn(IGame iGame) {
        Card playableCard = findPlayableCard(iGame, hand); //Will return null if there are no valid cards to play.
        System.out.println("" + getName() + "\'s Current Hand: " + hand);

        if (playableCard != null) { //If playableCard has a Card, it will then put that card down :)
            hand.remove(playableCard);
            if (playableCard.getColor() == Colors.Wild) {
                iGame.playCard(playableCard, Optional.of(declareOptional(playableCard)), this);
            } else {
                iGame.playCard(playableCard, Optional.ofNullable(null), this);
            }


        } else {
            System.out.println("No valid card found. Drawing...");
            draw(iGame);
            System.out.println("Seeing if new card can fit");
            playableCard = findPlayableCard(iGame, hand); //Will now draw a card and see if that card is valid. If it is, it will play the card.
            if (playableCard != null) {
                hand.remove(playableCard);
                if (playableCard.getColor() == Colors.Wild) {

                    iGame.playCard(playableCard, Optional.of(declareOptional(playableCard)), this);
                } else {
                    iGame.playCard(playableCard, Optional.ofNullable(null), this);
                }
            } else {
                System.out.println("New card can't fit. Turn ended.");

            }
        }
    }


    @Override
    public void newHand(List<Card> cards) {
        this.hand.clear();
        this.hand.addAll(cards);
    }
    private Card findPlayableCard(IGame iGame, List<Card> hand) {
        List<Card> playableCards = new ArrayList<>();
        Map<Colors, Integer> mapOfColors = new HashMap<>();
        Map<Faces, Integer> mapOfFaces = new HashMap<>();
        Map<Colors, Integer> reverseSortedMap= new HashMap<>();

        var red = 0;
        var blue = 0;
        var yellow = 0;
        var green = 0;
        var wild = 0;
        for (var card : hand) {
            if (iGame.isPlayable(card)) {

                playableCards.add(card);
            }
        }
        if (playableCards.size() != 0) {


            for (var card : hand) {
                switch (card.getColor()) { //Is getting every color and the amount of times that color is in your hand
                    case Wild:
                        mapOfColors.put(card.getColor(), ++wild);
                        break;
                    case Red:
                        mapOfColors.put(card.getColor(), ++red);
                        break;
                    case Green:
                        mapOfColors.put(card.getColor(), ++green);
                        break;
                    case Yellow:
                        mapOfColors.put(card.getColor(), ++yellow);
                        break;
                    case Blue:
                        mapOfColors.put(card.getColor(), ++blue);
                        break;
                }
            }
            mapOfColors.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

            Colors[] maxColorSet = reverseSortedMap.keySet().toArray(Colors[]::new); //Is an array given the colors in descending order by amount of colors
            for (var color : maxColorSet) {
                for (var card: playableCards) {
                    if (card.getColor().equals(color)) {
                        return card;
                    }
                }
            }




        }
            return null;
        }

        public Card draw (IGame iGame){
            hand.add(iGame.draw());
            System.out.println("Drew Card: " + hand.get(handSize() - 1));
            return hand.get(handSize() - 1);
        }

        public void drawFour (IGame iGame){
            draw(iGame);
            draw(iGame);
            draw(iGame);
            draw(iGame);
        }

        public void drawTwo (IGame iGame){
            draw(iGame);
            draw(iGame);
        }


        public String getName () {
            return "John";
        }


        public int handSize () {
            return hand.size();
        }


        private List<Card> getHand () {
            return hand;
        }

        public void setHand (List < Card > hand) {
            this.hand = hand;
        }



        public Colors declareOptional (Card card){
            { //If wild, will choose color from first in deck. Change later to be more strategized.
                Colors color;
                Map<Colors, Integer> mapOfColors = new HashMap<>();

                if(handSize() == 0) {
                    return Colors.Red;
                }



                var red = 0;
                var blue = 0;
                var yellow = 0;
                var green = 0;
                var wild = 0;

                for (var cardList : hand) {
                    switch (cardList.getColor()) { //Is getting every color and the amount of times that color is in your hand
                        case Red:
                            mapOfColors.put(cardList.getColor(), ++red);
                            break;
                        case Green:
                            mapOfColors.put(cardList.getColor(), ++green);
                            break;
                        case Yellow:
                            mapOfColors.put(cardList.getColor(), ++yellow);
                            break;
                        case Blue:
                            mapOfColors.put(cardList.getColor(), ++blue);
                            break;
                    }
                }
                var colorOptional = mapOfColors.entrySet().stream()
                        .max(Comparator.comparing(Map.Entry::getValue));
                if (colorOptional.isPresent()) {
                    color = colorOptional.get().getKey();
                }
                else color = Colors.Blue;

                System.out.println("Wild Card Chosen Color: " + color);
                return color;
            }
        }


    }
