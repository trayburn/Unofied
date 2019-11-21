package com.improving.game;

import com.improving.Logger;
import com.improving.game.Card;
import com.improving.game.Colors;

public class TopCard {
    private Card card;
    private Colors declaredColor;
    private final Logger logger;

    public TopCard(Logger logger, Card card, Colors declaredColor) {
        this.logger = logger;
        this.card = card;
        this.declaredColor = declaredColor;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card, Colors declaredColor, IPlayerInfo player) {
        if (declaredColor != null) {
            logger.println(player.getName() + " has played a " + card.toString() + " calling " + declaredColor.getColorName());
        } else {
            logger.println(player.getName() + " has played a " + card.toString());
        }

        if (card.getColor() == Colors.Wild && declaredColor == null)
            throw new RuntimeException("Something is wrong here");
        else
            this.declaredColor = null;
        this.card = card;
        this.declaredColor = declaredColor;
    }

    public Colors getDeclaredColor() {
        return declaredColor;
    }

    @Override
    public String toString() {
        return this.getCard().toString() + (declaredColor != null ? ". The declared color is " + declaredColor.toString() : "");
    }
}
