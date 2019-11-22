package com.improving.players;

import com.improving.Logger;
import com.improving.game.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class PalomaPlayer implements IPlayer {
    private List<Card> hand = new ArrayList<>();
    private Colors mostCommonColor;
    private int yellowCommon = 1;
    private int blueCommon = 1;
    private int redCommon = 1;
    private int greenCommon = 1;
    private Faces playableFace;

    public PalomaPlayer(List<Card> hand) {
        this.hand = hand;
    }

    @Override
    public int handSize() {
        return hand.size();
    }

    public void newHand(List<Card> cards) {
        this.hand.clear();
        this.hand.addAll(cards);
    }

    @Override
    public void takeTurn(IGame game) {
        for (Card card : getHandCards()) {
            checkTheColorAmountInHand(card);
            if (game.isPlayable(card)) {
                if (playActionCard(game, card)) {
                    playCard(card, game);
                } else playCard(card, game);
                return;
//            } else
//                getRidOfCommonColorCard(game);
            }
        }

        Card cardDrawn = draw(game);
        if (game.isPlayable(cardDrawn)) {
            playCard(cardDrawn, game);
        }
    }


    public Colors getMostCommonColor() {
        return mostCommonColor;
    }

    public void setMostCommonColor(Colors mostCommonColor) {
        this.mostCommonColor = mostCommonColor;
    }

    @Override
    public List<Card> getHand() {
        return hand;
    }

//    public void getRidOfCommonColorCard(IGame game) {
//        for (Card card : hand) {
//            if (yellowIsCommonColor() == true) {
//                mostCommonColor = Colors.Yellow;
//                game.playCard(card, Optional.of(mostCommonColor), this);
//            } else if (redIsCommonColor() == true) {
//                mostCommonColor = Colors.Red;
//                game.playCard(card, Optional.of(mostCommonColor), this);
//            } else if (greenIsCommonColor() == true) {
//                mostCommonColor = Colors.Green;
//                game.playCard(card, Optional.of(mostCommonColor), this);
//            } else if (blueIsCommonColor() == true) {
//                mostCommonColor = Colors.Blue;
//                game.playCard(card, Optional.of(mostCommonColor), this);
//            }
//            return;
//        }
//        return;
//    }

    private void playCard(Card card, IGame iGame) {
        Colors declaredColor = declareColor(card, iGame);
        hand.remove(card);
        if (card.getColor().equals(Colors.Wild)) {
            iGame.playCard(card, Optional.ofNullable(declaredColor), this);
        } else {
            iGame.playCard(card, java.util.Optional.ofNullable(null), this);
        }
    }

    private Colors declareColor(Card card, IGame iGame) {
        var declaredColor = card.getColor();

        if (card.getColor().toString().equals("Wild")) {
            List<Colors> randomColors = new ArrayList<>();
            randomColors.add(Colors.Red);
            randomColors.add(Colors.Blue);
            randomColors.add(Colors.Green);
            randomColors.add(Colors.Yellow);
            declaredColor = randomColors.get(0);
            boolean declaredColorinHand = false;
            int numWildColorCardsInHand = 0;

            if (card.getColor().equals(Colors.Wild)) {
                while (declaredColorinHand) {
                    Collections.shuffle(randomColors);
                    for (Card c : hand) {
                        if (card.getColor().equals(randomColors.get(0)) && card.getFace().getValue() == 20) {
                            declaredColorinHand = true;
                            declaredColor = card.getColor();
                            break;
                        }
                        if (card.getColor().equals(Colors.Wild)) {
                            numWildColorCardsInHand++;
                        }
                        if (numWildColorCardsInHand == handSize()) {
                            Collections.shuffle(randomColors);
                            declaredColorinHand = true;
                            declaredColor = randomColors.get(0);
                        }
                    }
                }
            }
        }
        return declaredColor;
    }

    private boolean playActionCard(IGame game, Card card) {
        if (game.getNextPlayer().handSize() <= 2
                && hand.contains(Faces.Draw_2) || hand.contains(Faces.Draw_4) || hand.contains(Faces.Reverse)) {
            return true;
        }
        return false;
    }

    public boolean yellowIsCommonColor() {
        if (yellowCommon >= redCommon && yellowCommon >= blueCommon && yellowCommon >= greenCommon) {
            return true;
        }
        return false;
    }

    public boolean redIsCommonColor() {
        if (redCommon >= yellowCommon && redCommon >= blueCommon && redCommon >= greenCommon) {
            return true;
        }
        return false;
    }

    public boolean greenIsCommonColor() {
        if (greenCommon >= yellowCommon && greenCommon >= blueCommon && greenCommon >= redCommon) {
            return true;
        }
        return false;
    }

    public boolean blueIsCommonColor() {
        if (blueCommon >= yellowCommon && blueCommon >= redCommon && blueCommon >= greenCommon) {
            return true;
        }
        return false;
    }

    private void checkTheColorAmountInHand(Card card) {
        if (card.getColor() == Colors.Yellow) {
            yellowCommon++;
        }
        if (card.getColor() == Colors.Red) {
            redCommon++;
        }
        if (card.getColor() == Colors.Blue) {
            blueCommon++;
        }
        if (card.getColor() == Colors.Yellow) {
            yellowCommon++;
        }
    }

    @Override
    public Card draw(IGame game) {
        Card card = game.draw();
        if (card != null) {
            hand.add(card);
        }
        return card;
    }

    public String getName() {
        return "Paloma";
    }

    public List<Card> getHandCards() {
        return hand;
    }
}

