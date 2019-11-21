package com.improving.players;

import com.improving.Logger;
import com.improving.game.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class ShavonnePlayer implements IPlayer {
        private List<Card> hand = new ArrayList<>();
        private final Logger logger;

        public ShavonnePlayer(Logger logger, List<Card> handCards) {
            this.hand = handCards;
            this.logger = logger;
        }

        public List<Card> getHandCards() {
            return hand;
        }

        @Override
        public void takeTurn(IGame iGame) {
            for (var playerCard : getHandCards()) {
                if (iGame.isPlayable(playerCard)) {
                    if (attackAttack(playerCard, iGame)) {
                        playCard(playerCard, iGame);
                    } else playCard(playerCard, iGame);
                    return;
                }
            }
            var newCard = draw(iGame);
            if (iGame.isPlayable(newCard)) {
                playCard(newCard, iGame);
            }
        }

        @Override
        public String getName() {
            return "Shavonne";
        }

        @Override
        public int handSize() {
            return hand.size();
        }

        @Override
        public void newHand(List<Card> cards) {
            this.hand.clear();
            this.hand.addAll(cards);
        }

    @Override
    public List<Card> getHand() {
        return hand;
    }

    @Override
        public Card draw(IGame iGame) {
            var drawnCard = iGame.draw();
            hand.add(drawnCard);
            return drawnCard;
        }

        private void playCard(Card card, IGame iGame) {
            Colors declaredColor = declareColor(card, iGame);
            hand.remove(card);
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

        private boolean attackAttack(Card playerCard, IGame iGame) {
            if (iGame.getNextPlayer().handSize() == 1) {
                return playerCard.getFace().getValue() == 20 || playerCard.getFace().getValue() == 50;
            }
            if (iGame.getDeckInfo().getDiscardPile().get(iGame.getDeckInfo().getDiscardPile().size() - 1).getColor().equals(Colors.Wild)) {
                return playerCard.getFace().getValue() == 20 || playerCard.getFace().getValue() == 50;
            }
            if (iGame.getDeckInfo().getDiscardPile().get(iGame.getDeckInfo().getDiscardPile().size() - 1).getFace().getValue() == 20
                    || iGame.getDeckInfo().getDiscardPile().get(iGame.getDeckInfo().getDiscardPile().size() - 1).getFace().getValue() == 50) {
                return playerCard.getFace().getValue() == 20
                        || playerCard.getFace().getValue() == 50
                        || playerCard.getColor().equals(Colors.Wild);
            }
            if (iGame.getPreviousPlayer().handSize() == 1) {
                return !playerCard.getFace().equals(Faces.Reverse);
            }
            if (handSize() <= 3) {
                return playerCard.getFace().getValue() == 20 || playerCard.getFace().getValue() == 50;
            }
            if (iGame.getNextNextPlayer().handSize() <= 5) {
                return playerCard.getFace().getValue() != 20 || playerCard.getFace().getValue() != 50;
            }

            return false;
        }
    }
    
