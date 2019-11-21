package com.improving.players;

import com.improving.game.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ZachPlayer implements IPlayer {
    List<Card> hand;
    String name = "Zach";

    public ZachPlayer(List<Card> hand) {
        this.hand = hand;
    }

    public void takeTurn(IGame iGame) {
        var mostCommonColor = getMostCommonColor();
        List<Card> cardsWithMostCommonColor = hand.stream().filter(card -> card.getColor().equals(mostCommonColor)).collect(Collectors.toList());
        List<Card> otherCards = hand.stream().filter(card -> card.getColor() != mostCommonColor).collect(Collectors.toList());

        boolean ableToScrewOtherPlayer = false;
        if (iGame.getNextPlayer().handSize() < 3) {
            ableToScrewOtherPlayer = screwOtherPlayer(iGame);
        }

        //play normal if next player has at least 3 cards left
        if (ableToScrewOtherPlayer == false) {
            for (Card card : cardsWithMostCommonColor) {
                if (iGame.isPlayable(card)) {
                    prepPlayReport(card, iGame);
                    return;
                }
            }

            for (Card card : otherCards) {
                if (iGame.isPlayable(card)) {
                    prepPlayReport(card, iGame);
                    return;
                }
            }

            //if CANNOT play then draw...if card matches top card on discardPile, if so play
            var cardDrawn = this.draw(iGame);
            System.out.println(this.name + " drew a card");
            if (iGame.isPlayable(cardDrawn)) {
                prepPlayReport(cardDrawn, iGame);
                return;
            }
        }
    }

    private List<Card> getHand() {
        return hand;
    }


    public Card draw(IGame iGame) {
        var cardDrawn = iGame.draw();
        hand.add(cardDrawn);
        return cardDrawn;
    }

    public int handSize() {
        return hand.size();
    }

    private Colors chooseNewColorToDeclare() {
        Colors newColor;
        var mostCommonColor = getMostCommonColor();

        if (mostCommonColor != null) {
            newColor = mostCommonColor;
        } else {
            newColor = Colors.Red;
        }
        return newColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Colors getMostCommonColor() {
        Map<Colors, Integer> colorsMap = new HashMap<>();
        for (var card : hand) {
            if (colorsMap.containsKey(card.getColor())) {
                colorsMap.put(card.getColor(), colorsMap.get(card.getColor()) + 1);
            } else {
                colorsMap.put(card.getColor(), 1);
            }
        }
        Integer maxValue = colorsMap.entrySet().stream().map(color -> color.getValue()).max(Integer::compare).orElse(0);
        Colors mostCommonColor = colorsMap.entrySet().stream().filter(color -> color.getValue().equals(maxValue)).findFirst().map(pair -> pair.getKey()).orElse(Colors.Red);
        return mostCommonColor;
    }

    public List<Card> findAllPlayableCards(IGame iGame) {
        List<Card> playableCards = new ArrayList<>();
        for (var card : hand) {
            if (iGame.isPlayable(card)) {
                playableCards.add(card);
            }
        }
        return playableCards;
    }

    @Override
    public void newHand(List<Card> cards) {
        this.hand.clear();
        this.hand.addAll(cards);
    }

    public boolean screwOtherPlayer(IGame iGame) {
        Card draw4Card = hand.stream().filter(card -> card.getFace().equals(Faces.Draw_4)).findFirst().orElse(null);
        Card draw2Card = hand.stream().filter(card -> card.getFace().equals(Faces.Draw_2)).findFirst().orElse(null);
        Card skipCard = hand.stream().filter(card -> card.getFace().equals(Faces.Skip)).findFirst().orElse(null);
        Card reverseCard = hand.stream().filter(card -> card.getFace().equals(Faces.Reverse)).findFirst().orElse(null);

        if ((iGame.getPlayerInfo().size() > 2 && iGame.getNextNextPlayer().handSize() > 2) || (iGame.getPlayerInfo().size() <= 2)) {
            if (draw4Card != null) {
                prepPlayReport(draw4Card, iGame);
                return true;
            } else if (draw2Card != null && iGame.isPlayable(draw2Card)) {
                prepPlayReport(draw2Card, iGame);
                return true;
            } else if (skipCard != null && iGame.isPlayable(skipCard)) {
                prepPlayReport(skipCard, iGame);
                return true;
            } else if (reverseCard != null && iGame.isPlayable(reverseCard)) {
                prepPlayReport(reverseCard, iGame);
                return true;
            }
        } else {
            if (reverseCard != null && iGame.isPlayable(reverseCard)) {
                prepPlayReport(reverseCard, iGame);
                return true;
            } else if (draw4Card != null) {
                prepPlayReport(draw4Card, iGame);
                return true;
            } else if (draw2Card != null && iGame.isPlayable(draw2Card)) {
                prepPlayReport(draw2Card, iGame);
                return true;
            } else if (skipCard != null && iGame.isPlayable(skipCard)) {
                prepPlayReport(skipCard, iGame);
                return true;
            }
        }
        return false;
    }

    private void prepPlayReport(Card card, IGame iGame) {
        if (card.getColor().equals(Colors.Wild)) {
            var newColor = chooseNewColorToDeclare();
            hand.remove(card);
            iGame.playCard(card, Optional.of(newColor), this);

        } else {
            hand.remove(card);
            iGame.playCard(card, Optional.ofNullable(null), this);

        }
    }
}
