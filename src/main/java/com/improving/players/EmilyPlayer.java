package com.improving.players;

import com.improving.game.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmilyPlayer implements IPlayer {
    private String name;
    private List<Card> handCards = new ArrayList<>();

    public EmilyPlayer(List<Card> handCards) {
        this.handCards = handCards;
    }

    public List<Card> getHandCards() {
        return handCards;
    }


    @Override
    public Card draw(IGame iGame) {
        handCards.add(iGame.draw());
        return iGame.draw();
    }

    @Override
    public void takeTurn(IGame iGame) {
        for(var card: handCards) {
            filterCard(handCards);
            int handSize = iGame.getNextPlayer().handSize();
            if( handSize <= 2 && checkWildCard(handCards)) {
                var wildCard = handCards.stream().filter(c->c.getColor() == Colors.Wild).findFirst().get();
                playCard(wildCard,iGame);
                return;
            }

            if(iGame.isPlayable(card)) {
                if(handSize <=2 && checkSkipCard(handCards)){
                    var skipCard = handCards.stream().filter(c->c.getFace() == Faces.Skip).findFirst().get();
                    playCard(skipCard, iGame);
                    return;
                }

                if(handSize <=2 && checkReverseCard(handCards)){
                    var reverseCard = handCards.stream().filter(c->c.getFace() == Faces.Reverse).findFirst().get();
                    playCard(reverseCard, iGame);
                    return;
                }

                if(handSize <=2 && checkDrawTwoCard(handCards)){
                    var drawTwoCard = handCards.stream().filter(c->c.getFace() == Faces.Draw_2).findFirst().get();
                    playCard(drawTwoCard, iGame);
                    return;
                }

                playCard(card,iGame);
                return;
            }
        }

        var drawnCard = draw(iGame);
        if(iGame.isPlayable(drawnCard)) {
            playCard(drawnCard, iGame);
        }



    }

    @Override
    public void newHand(List<Card> hand) {

        this.handCards.clear();
        this.handCards.addAll(hand);

    }

    @Override
    public int handSize() {
        return handCards.size();
    }

    @Override
    public String getName() {
        return "Emily's smart player";
    }

    public void playCard(Card card, IGame game) {
        Colors declaredColor = declareColor(card,game);
        handCards.remove(card);

        //declared color can be null
        game.playCard(card, java.util.Optional.ofNullable(declaredColor), this);
    }

    public Colors declareColor(Card card, IGame game) {
        var declaredColor = card.getColor();
        int numWildColorCardsinHand = 0;
        ArrayList<Colors> randomColors = new ArrayList<>();
        randomColors.add(Colors.Red);
        randomColors.add(Colors.Blue);
        randomColors.add(Colors.Green);
        randomColors.add(Colors.Yellow);

        if(randomColors.contains(declaredColor)){
            declaredColor = card.getColor();
        }else if (declaredColor.equals(Colors.Wild)) {
            numWildColorCardsinHand++;
            Collections.shuffle(randomColors);
            for(Card c: handCards) {
                if (c.getColor() == randomColors.get(0)) {
                    declaredColor = c.getColor();
                    break;
                }
            }

            if(numWildColorCardsinHand == handCards.size()){
                Collections.shuffle(randomColors);
                declaredColor = randomColors.get(0);
            }

        }

        return declaredColor;
    }


        private List filterCard (List<Card> hand) {
            hand = hand.stream()
                    .sorted(Comparator.comparingInt(card -> card.getFace().getPointValue()))
                    .sorted(Comparator.comparing(card -> card.getColor().pointValue))
                    .collect(Collectors.toList());
            return hand;

        }

        private boolean checkWildCard(List<Card> cards) {
            return cards.contains(Colors.Wild);
        }

        private boolean checkSkipCard(List<Card> cards) {
            return cards.contains(Faces.Skip);
        }

        private boolean checkReverseCard(List<Card> cards) {
            return cards.contains(Faces.Reverse);
        }

        private boolean checkDrawTwoCard(List<Card> cards) {
            return cards.contains(Faces.Draw_2);
        }

}
