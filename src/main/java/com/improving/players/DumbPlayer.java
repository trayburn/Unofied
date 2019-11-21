package com.improving.players;

import com.improving.Logger;
import com.improving.game.Card;
import com.improving.game.Colors;
import com.improving.game.IGame;
import com.improving.game.IPlayer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class DumbPlayer implements IPlayer {
    private String name;
    private List<Card> handCards = new ArrayList<>();

    public DumbPlayer(List<Card> handCards) {
        this.handCards = handCards;
    }

    public List<Card> getHandCards() {
        return handCards;
    }

    @Override
    public void takeTurn(IGame iGame) {
        for (var playerCard : getHandCards()) {
            if (iGame.isPlayable(playerCard)) {
                playCard(playerCard, iGame);
                return;
            }
        }
        var newCard = draw(iGame);
        if (iGame.isPlayable(newCard)) {
            playCard(newCard, iGame);
        }
    }

    @Override
    public void newHand(List<Card> hand) {
        this.handCards.clear();
        this.handCards.addAll(hand);
    }

    @Override
    public List<Card> getHand() {
        return handCards;
    }

    @Override
    public int handSize() {
        return handCards.size();
    }

    @Override
    public String getName() {
        return this.toString().replace("com.improving.players.","");
    }

    @Override
    public Card draw(IGame iGame) {
        var drawnCard = iGame.draw();
        handCards.add(drawnCard);
        return drawnCard;
    }

    private void playCard(Card card, IGame iGame) {
        Colors declaredColor = declareColor(card, iGame);
        if (card.getColor().equals(Colors.Wild) == false)
            declaredColor = null;
        handCards.remove(card);
        iGame.playCard(card, Optional.ofNullable(declaredColor), this);
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
                    for (Card c : handCards) {
                        if (card.getColor().equals(randomColors.get(0))) {
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
}
