package com.improving.players;

import com.improving.game.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class EthanPlayer implements IPlayer {

    private String name;
    private final List<Card> hand;

    public EthanPlayer(List<Card> hand) {
        this.name = "Ethan";
        this.hand = hand;
    }

    @Override
    public void takeTurn(IGame game) {
        for (Card card : hand) {
            if (game.isPlayable(card)) {
                if (game.getNextPlayer().handSize() <=1 ) playDrawCard(game, card);
                else playMostCommonColor(game);
                return;
            }
        }

        Card cardDrawn = draw(game);
        if (game.isPlayable(cardDrawn)) {
            playCard(game, cardDrawn);
        }
    }

    public void playMostCommonColor(IGame game) {
        var bestColor = getMostCommonColor();
        var playableCards = getPlayableCards(game)
                .filter(c -> c.getColor() == bestColor)
                .collect(Collectors.toList());

        // If there are no playable cards with the best color, play the card with the lowest value (draw cards last)
        if (playableCards.isEmpty()) playCard(game, pickFirstPlayableCard(game));
        else playCard(game, playableCards.get(0));
    }

    public Colors getMostCommonColor() {
        Map<Colors, Integer> colorRank = new HashMap<>();
        for (Colors color : getRealColors()) {
            colorRank.put(color, countCardsByColor(color));
        }
        return Collections.max(colorRank.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }

    private Card pickFirstPlayableCard(IGame game) {
        for (var card : hand) {
            if (game.isPlayable(card)) {
                return card;
            }
        }
        return null;
    }

    private void playDrawCard(IGame game, Card card) {
        if (getPlayableCards(game).anyMatch(c -> c.getFace() == Faces.Draw_2)) {
            Card draw_2 = getPlayableCards(game).filter(c -> c.getFace() == Faces.Draw_2).findFirst().orElse(null);
            if (draw_2 != null) {
                playCard(game, draw_2);
                return;
            }
        } else if (getPlayableCards(game).anyMatch(c -> c.getFace() == Faces.Draw_4)) {
            Card draw_4 = getPlayableCards(game).filter(c -> c.getFace() == Faces.Draw_4).findFirst().orElse(null);
            if (draw_4 != null) {
                playCard(game, draw_4);
                return;
            }
        }
        playMostCommonColor(game);
    }

    public List<Colors> getRealColors() {
        return Arrays.stream(Colors.values()).filter(c -> c.ordinal() < 4)
                .collect(Collectors.toList());
    }

    public int countCardsByColor(Colors color) {
        var cards = new ArrayList<Card>();
        for (var card : hand) if (card.getColor() == color) cards.add(card);
        return cards.size();
    }

    //========================================================================//
    //=================================UNUSED=================================//
    //========================================================================//

    public Colors getMostCommonPlayableColor(IGame game) {
        // Create a map to keep track of how many of each color
        Map<Colors, Integer> colorRank = new HashMap<>();

        // If a card is a wild, all colors are playable
        var anyWild = hand.stream().anyMatch(c -> c.getFace().getValue() == 50);
        var playableColors = anyWild ? getRealColors() :

                // Otherwise filter out colors that have no playable card in the hand
                getPlayableCards(game)
                        .map(Card::getColor).distinct().collect(Collectors.toList());

        // For each playable color, count how many cards of that color are in hand
        for (Colors color : playableColors) {
            colorRank.put(color, countCardsByColor(color));
        }

        // Return the color with the maximum number of cards
        return Collections.max(colorRank.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }

    private Card pickHighValueCard(IGame game) {
        return getPlayableCards(game).filter(c -> c.getColor() == getMostCommonColor())
                .max(Comparator.comparing(c -> c.getFace().getValue())).orElse(null);
    }


    //========================================================================//
    //=========================STANDARDIZED METHODS===========================//
    //========================================================================//

    public void newHand(List<Card> cards) {
        this.hand.clear();
        this.getHand().addAll(cards);
    }

    public Stream<Card> getPlayableCards(IGame game) {
        return hand.stream().filter(game::isPlayable);
    }

    @Override
    public Card draw(IGame game) {
        var drawnCard = game.draw();
        if (drawnCard != null) hand.add(drawnCard);
        sortHandByPointValue();
        return drawnCard;
    }

    public void sortHandByPointValue() {
        Collections.sort(hand, Comparator.comparing(c -> c.getFace().getValue()));
    }

    private void playCard(IGame game, Card card) {
        Colors declaredColor = getMostCommonColor();
        if (!card.getColor().equals(Colors.Wild)) declaredColor = null;
        hand.remove(card);
        game.playCard(card, Optional.ofNullable(declaredColor), this);
    }

    @Override
    public int handSize() {
        return hand.size();
    }

    @Override
    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

}
