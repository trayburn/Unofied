package com.improving.players;

import com.improving.game.*;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class JenniferPlayer implements IPlayer {
    public static int takeTurnCount = 1;
    private final List<Card> handCards;
    private static int gameOverUno = 0;
    private String name = "Jennifer";
    Colors mostCommonColor;
    int yellowCommon = 1;
    int blueCommon = 1;
    int redCommon = 1;
    int greenCommon = 1;

    public JenniferPlayer(List<Card> handCards) {
        this.handCards = handCards;
    }

    @Override
    public void takeTurn(IGame iGame) {
        takeTurnCount++;
        for (Card card : handCards) {
            checkTheColorAmountInHand(card);
            findMostCommonColor(iGame);
            if (iGame.isPlayable(card)) {
                playCard(card, iGame);
                return;
            }
        }
        var newCard = draw(iGame);
        if (iGame.isPlayable(newCard)) {
            playCard(newCard, iGame);
        }
    }

    private void playCard(Card card, IGame iGame) {
        Colors declaredColor = declareColor(card, iGame);
        if (card.getColor().equals(Colors.Wild) == false) {
            declaredColor = null;
        }
        handCards.remove(card);
        iGame.playCard(card, Optional.ofNullable(declaredColor), this);
    }

    private Colors declareColor(Card card, IGame iGame) {
        var declaredColor = card.getColor();
        if (card.getColor().toString().equals("Wild")) {
            setMostCommonColor(mostCommonColor);
            boolean declaredColorinHand = false;
            int numWildColorCardsInHand = 0;

            if (card.getColor().equals(Colors.Wild)) {
                while (declaredColorinHand) {
                    for (Card c : handCards) {
                        if (card.getColor() == mostCommonColor){
                            declaredColorinHand = true;
                            declaredColor = card.getColor();
                            break;
                        }
                        if (card.getColor().equals(Colors.Wild)) {
                            numWildColorCardsInHand++;
                        }
                        if (numWildColorCardsInHand == handSize()) {
                            declaredColorinHand = true;
                        }
                    }
                }
            }
        }
        return declaredColor;
    }

    public void setMostCommonColor(Colors mostCommonColor) {
        this.mostCommonColor = mostCommonColor;
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
            handCards.add(card);
        }
        return card;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int handSize() {
        return handCards.size();
    }

    public String getName() {
        return name;
    }


    public void newHand(List<Card> cards) {
        this.handCards.clear();
        this.getHand().addAll(cards);
    }

    @Override
    public List<Card> getHand() {
        return handCards;
    }

    public static int getTakeTurnCount() {
        return takeTurnCount;
    }


    /////////////////////////
//    private void smartMove_MakeNextPlayerDrawIfUno(IGame game, Card card) {
//        if (handCards.contains(Faces.Draw_4)) {
//            handCards.remove(card);
//            game.playCard(card, Optional.of(choseWildColor(game, card)), this);
//        } else if (handCards.contains(Faces.Draw_2)) {
//            handCards.remove(card);
//            game.playCard(card, Optional.of(choseWildColor(game, card)), this);
//        } else if (handCards.contains(Faces.Skip)) {
//            handCards.remove(card);
//            game.playCard(card, Optional.of(choseWildColor(game, card)), this);
//        } else if (handCards.contains(Faces.Reverse)) {
//            handCards.remove(card);
//            game.playCard(card, Optional.of(choseWildColor(game, card)), this);
//        }
//    }


    public void findMostCommonColor(IGame game) {
        for (Card card : handCards) {
            if (yellowIsCommonColor() == true && card.getColor() == Colors.Yellow) {
                mostCommonColor = Colors.Yellow;
            } else if (redIsCommonColor() == true && card.getColor() == Colors.Red) {
                mostCommonColor = Colors.Red;
            } else if (greenIsCommonColor() == true && card.getColor() == Colors.Green) {
                mostCommonColor = Colors.Green;
            } else if (blueIsCommonColor() == true && card.getColor() == Colors.Blue) {
                mostCommonColor = Colors.Blue;
            }
            return;
        }
        return;
    }


//    public Colors choseWildColor(IGame game, Card card) {
//        if (card.getFace() == Faces.Wild || card.getFace() == Faces.Draw_4) {
//            if (yellowIsCommonColor() == true) {
////            System.out.println(handCards);
//                mostCommonColor = Colors.Yellow;
//                handCards.remove(card);
//                game.playCard(card, Optional.of(mostCommonColor), this);
//            } else if (redIsCommonColor() == true) {
//                mostCommonColor = Colors.Red;
//                handCards.remove(card);
//                game.playCard(card, Optional.of(mostCommonColor), this);
//            } else if (greenIsCommonColor() == true) {
//                mostCommonColor = Colors.Green;
//                handCards.remove(card);
//                game.playCard(card, Optional.of(mostCommonColor), this);
//            } else if (blueIsCommonColor() == true) {
//                mostCommonColor = Colors.Blue;
//                handCards.remove(card);
//                game.playCard(card, Optional.of(mostCommonColor), this);
//            }
//            return mostCommonColor;
//        }
//        return card.getColor();
//    }
//
//    public Colors getMostCommonColor() {
//        return mostCommonColor;
//    }


}
