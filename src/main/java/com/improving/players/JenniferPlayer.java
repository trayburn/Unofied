package com.improving.players;

import com.improving.game.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JenniferPlayer implements IPlayer {
    public static int takeTurnCount = 1;
    private final List<Card> handCards;
    private static int gameOverUno = 0;
    private String name;
    Colors mostCommonColor;
    int yellowCommon = 1;
    int blueCommon = 1;
    int redCommon = 1;
    int greenCommon = 1;


    public JenniferPlayer(List<Card> handCards) {
        this.handCards = handCards;
    }


    @Override
    public int handSize() {
        return handCards.size();
    }

    @Override
    public String getName() {
        return this.toString().replace("com.improving.players.","");
    }


    public void newHand(List<Card> cards) {
        this.handCards.clear();
        this.getHandCards().addAll(cards);
    }

    public static int getTakeTurnCount() {
        return takeTurnCount;
    }

    @Override
    public void takeTurn(IGame game) {
        takeTurnCount++;
        for (Card card : handCards) {
            checkTheColorAmountInHand(card);
            if (game.isPlayable(card)) {
                if (game.getNextPlayer().handSize() <=1 ){
                    smartMove_MakeNextPlayerDrawIfUno(game, card);
                }else
//                System.out.println(getName() + " played a " + card + " from hand.");
                playSmartColorCard(game);
                return;
            }
        }
        Card cardDrawn = draw(game);
//        System.out.println("Player-" + getName() + " drew a card ");
        handCards.add(cardDrawn);
        if (game.isPlayable(cardDrawn)) {
//            System.out.println("and decided to play it. --> " + cardDrawn);
            playSmartColorCard(game);
            return;
        }


    }

    public void setMostCommonColor(Colors mostCommonColor) {
        this.mostCommonColor = mostCommonColor;
    }

    public Colors choseWildColor(IGame game, Card card){
        if (yellowIsCommonColor() == true) {
//            System.out.println(handCards);
            mostCommonColor = Colors.Yellow;
            handCards.remove(card);
            game.playCard(card, Optional.of(mostCommonColor), this);
        } else if (redIsCommonColor() == true) {
            mostCommonColor = Colors.Red;
            handCards.remove(card);
            game.playCard(card, Optional.of(mostCommonColor), this);
        } else if (greenIsCommonColor() == true) {
            mostCommonColor = Colors.Green;
            handCards.remove(card);
            game.playCard(card, Optional.of(mostCommonColor), this);
        } else if (blueIsCommonColor() == true) {
            mostCommonColor = Colors.Blue;
            handCards.remove(card);
            game.playCard(card, Optional.of(mostCommonColor), this);
        }
        return mostCommonColor;
    }

    public Colors getMostCommonColor() {
        return mostCommonColor;
    }


    public void playSmartColorCard(IGame game) {
        for (Card card : handCards) {
            if (yellowIsCommonColor() == true) {
                mostCommonColor = Colors.Yellow;
                handCards.remove(card);
                game.playCard(card, Optional.of(mostCommonColor), this);
            } else if (redIsCommonColor() == true) {
                mostCommonColor = Colors.Red;
                handCards.remove(card);
                game.playCard(card, Optional.of(mostCommonColor), this);
            } else if (greenIsCommonColor() == true) {
                mostCommonColor = Colors.Green;
                handCards.remove(card);
                game.playCard(card, Optional.of(mostCommonColor), this);
            } else if (blueIsCommonColor() == true) {
                mostCommonColor = Colors.Blue;
                handCards.remove(card);
                game.playCard(card, Optional.of(mostCommonColor), this);
            }
            return;
        }
        return;
    }

    private void smartMove_MakeNextPlayerDrawIfUno(IGame game, Card card) {
        if (handCards.contains(Faces.Draw_2) || handCards.contains(Faces.Draw_4)) {
            handCards.remove(card);
            game.playCard(card, Optional.of(choseWildColor(game, card)), this);
        }
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
        return card;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getHandCards() {
        return handCards;
    }
}
