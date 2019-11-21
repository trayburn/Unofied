package com.improving.players;

import com.improving.game.Card;
import com.improving.game.Colors;
import com.improving.game.IGame;
import com.improving.game.IPlayer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class EthanPlayer implements IPlayer {
    private String name;
    protected LinkedList<Card> hand;

    public EthanPlayer(LinkedList<Card> hand) {
        this.name = "Ethan";
        this.hand = hand;
    }

    @Override
    public void takeTurn(IGame game) {
        var card = pickCard(game);
        card = card == null ? pickFirstPlayableCard(game) : card;


        // PickCard returns null if no card was playable
        if (card != null) {
            playCard(game, card);
            System.out.println(name + " has played " + card + " and finished turn.");
            return;
        }

        var newCard = draw(game);
        if (game.isPlayable(newCard)) {
            playCard(game, newCard);
        }

    }

    private Card pickFirstPlayableCard(IGame game) {
        for (var card : hand) {
            if (game.isPlayable(card)) {
                return card;
            }
        }
        return null;
    }

    private void playCard(IGame game, Card card) {
        Colors declaredColor = chooseColor(card);
        if (!card.getColor().equals(Colors.Wild)) declaredColor = null;
        hand.remove(card);
        game.playCard(card, Optional.ofNullable(declaredColor), this);
    }

    public Colors chooseColor(Card card) {
        var realColors = Arrays.stream(Colors.values()).filter(c -> c.ordinal() < 5)
                .collect(Collectors.toList());

        if (card.getColor() == Colors.Wild){
            Collections.shuffle(realColors);

            // Don't choose a color that's not in our hand
            for (Card c : hand) {
                if (card.getColor() == realColors.get(0)) {
                    return card.getColor();
                }
            }
        }
        return card.getColor();
    }

    @Override
    public int handSize() {
        return hand.size();
    }

    @Override
    public Card draw(IGame game) {
        var drawnCard = game.draw();
        hand.add(drawnCard);
        return drawnCard;
    }

    public String getName() {
        return name;
    }

    public int countCardsByColor(Colors color) {
        var cards = new ArrayList<Card>();
        for (var card : hand) if (card.getColor() == color) cards.add(card);
        return cards.size();
    }

    protected Card pickCard(IGame game) {
        Colors topPlayableColor = getBestColor(game);
        var playableCards = hand.stream().filter(game::isPlayable);
        return playableCards.filter(c -> c.getColor() == topPlayableColor)
                .max(Comparator.comparing(c -> c.getFace().getValue())).orElse(null);
    }

    private Colors getBestColor(IGame game) {
        Map<Colors, Integer> colorRank = new HashMap<>();
        var anyWild = hand.stream().anyMatch(c -> c.getFace().getValue() == 50);
        var playableColors = anyWild ? getRealColors() :
                hand.stream().filter(game::isPlayable)
                        .map(Card::getColor).distinct().collect(Collectors.toList());
        for (Colors color : playableColors) {
            colorRank.put(color, countCardsByColor(color));
        }

        // If NoSuchElement, return null
        if (colorRank.isEmpty()) return null;
        return Collections.max(colorRank.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }

    @Override
    public void newHand(List<Card> hand) {
        this.hand.clear();
        this.hand.addAll(hand);
    }

    public List<Colors> getRealColors() {
        return Arrays.stream(Colors.values()).filter(c -> c.ordinal() > 4)
                .collect(Collectors.toList());
    }

}
