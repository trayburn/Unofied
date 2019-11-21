package com.improving.game;

import com.improving.Logger;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Game implements IGame {
    private Deck deck;
    private final List<IPlayer> players = new ArrayList<>();
    private TopCard topCard = new TopCard();
    private int numPlayers;
    private int turnDirection = 1;
    private int turnEngine = 0;
    private boolean gameInProgress = true;
    private final Logger logger;
    private IPlayerInfo winningPlayer;

    public Game(Logger logger, List<IPlayer> players) {
        this.logger = logger;
        this.players.addAll(players);
    }

    public void play() {
        winningPlayer = null;
        gameInProgress = true;
        deck = new Deck();
        for(var p : players) {
            p.newHand(getStartingHand(deck));
        }
        numPlayers = players.size();

        arrangeStartingDeck(deck);
        logger.println("Top Card on Discard Pile: " + topCard.toString());

        if (hasAction(topCard.getCard())) {
            executeCardAction(topCard.getCard(), this, 0);
        }

        numPlayers = players.size();
        while (gameInProgress) {
            if (turnEngine < 0) {
                turnEngine = turnEngine + numPlayers;
            }
            int currentPlayer = turnEngine % numPlayers;
            var player = players.get(currentPlayer);

            player.takeTurn(this);

            if (player.handSize() == 0) {
                winningPlayer = player;
                logger.println(player.getName() + " has won the game!");
                gameInProgress = false;
            }
            turnEngine = turnEngine + turnDirection;
        }
    }

    @Override
    public Card draw() {
        return deck.draw();
    }

    private void arrangeStartingDeck(Deck deck) {
        this.deck = deck;
        deck.shuffle(deck.getDrawPile());
        Card firstCard = deck.draw();
        setTopCard(firstCard, firstCard.getColor());
        deck.getDiscardPile().add(topCard.getCard());
    }

    private List<Card> getStartingHand(Deck deck) {
        this.deck = deck;
        List<Card> hand = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            hand.add(this.draw());
        }
        return hand;
    }

    private void setTopCard(Card card, Colors declaredColor) {
        topCard.setCard(card);

        if (declaredColor.toString().equalsIgnoreCase("Wild")) {
            Random random = new Random();
            int number = random.nextInt(4);
            if (number == 0) {
                declaredColor = Colors.Red;
            } else if (number == 1) {
                declaredColor = Colors.Green;
            } else if (number == 2) {
                declaredColor = Colors.Blue;
            } else if (number == 3) {
                declaredColor = Colors.Yellow;
            }
        }
        topCard.setDeclaredColor(declaredColor);
    }

    @Override
    public boolean isPlayable(Card playerCard) {
        if (topCard.getCard().getColor().equals(playerCard.getColor())
                || topCard.getCard().getFace().equals(playerCard.getFace())
                || playerCard.getColor().equals(Colors.Wild)
                || topCard.getCard().getColor().equals(Colors.Wild)) {
            return true;
        }
        return false;
    }

    private Boolean hasAction(Card card) {
        return card.getFace().toString().equalsIgnoreCase("Draw_4") ||
                card.getFace().toString().equalsIgnoreCase("Draw_2") ||
                card.getFace().toString().equalsIgnoreCase("Skip") ||
                card.getFace().toString().equalsIgnoreCase("Reverse");
    }

    private void executeCardAction(Card card, Game game, int playerIndex) {
        var player = players.get(playerIndex);
        var topCardFace = topCard.getCard().getFace();

        if (topCardFace.equals(Faces.Draw_2)) {
            player.draw(game);
            player.draw(game);
            turnEngine = turnEngine + turnDirection;
        } else if (topCardFace.equals(Faces.Draw_4)) {
            player.draw(game);
            player.draw(game);
            player.draw(game);
            player.draw(game);
            turnEngine = turnEngine + turnDirection;
        } else if (topCardFace.equals(Faces.Skip)) {
            turnEngine = turnEngine + turnDirection;
        } else if (topCardFace.equals(Faces.Reverse)) {
            turnDirection = turnDirection * (-1);
        }

    }

    private int getNextPlayerIndex() {
        if (turnEngine <= 0) {
            turnEngine = turnEngine + numPlayers;
        }
        return (turnEngine + turnDirection) % numPlayers;
    }

    @Override
    public void playCard(Card card, Optional<Colors> declaredColor, IPlayerInfo player) {
        if (declaredColor.isPresent())
            logger.println(player.getName() + " has played a " + card.toString() + " calling " + declaredColor.get().getColorName());
        else
            logger.println(player.getName() + " has played a " + card.toString());
        deck.getDiscardPile().add(card);
        if (declaredColor.isPresent() == false) {
            if (card.getColor().ordinal() != 5) {
                topCard.setDeclaredColor(card.getColor());
            } else {
                topCard.setDeclaredColor(forcePickValidDeclaredColor());
                topCard.setCard(card);
            }
        } else if (declaredColor.isPresent()) {
            if (isValidDeclaredColor(declaredColor) == false) {
                declaredColor = Optional.ofNullable(forcePickValidDeclaredColor());
            }
            topCard.setCard(card);
            topCard.setDeclaredColor(declaredColor.orElseThrow());
        }
        if (hasAction(card)) {
            executeCardAction(card, this, getNextPlayerIndex());
        }
    }

    private boolean isValidDeclaredColor(Optional<Colors> declaredColor) {
        boolean isValid = false;
        Colors[] validColor = {Colors.Red, Colors.Green, Colors.Yellow, Colors.Blue};
        for (Colors color : validColor) {
            if (declaredColor.get().ordinal() == color.ordinal()) {
                isValid = true;
            }
        }
        return isValid;
    }

    private Colors forcePickValidDeclaredColor() {
        List<Colors> randomColors = new ArrayList<>();
        randomColors.add(Colors.Blue);
        randomColors.add(Colors.Red);
        randomColors.add(Colors.Green);
        randomColors.add(Colors.Yellow);
        Collections.shuffle(randomColors);
        logger.println("Invalid color declaration - random color chosen instead.");
        return randomColors.get(0);
    }
    @Override
    public IDeck getDeckInfo() {return this.deck;};


    public List<IPlayer> getPlayers() {
        return players;
    }

    @Override
    public List<IPlayerInfo> getPlayerInfo() {
        List<IPlayerInfo> playerInfo = new ArrayList<>();

        for (IPlayer player : players) {
            playerInfo.add(player);
        }
        return playerInfo;
    }

    @Override
    public IPlayer getNextPlayer() {
        if (turnEngine <= 0) {
            turnEngine = turnEngine + numPlayers;
        }
        var nextPlayer = (turnEngine + turnDirection) % numPlayers;
        return players.get(nextPlayer);
    }

    @Override
    public IPlayer getPreviousPlayer() {
        if (turnEngine <= 0) {
            turnEngine = turnEngine + numPlayers;
        }
        var previousPlayer = (turnEngine + turnDirection - 1) % numPlayers;
        return players.get(previousPlayer);
    }

    @Override
    public IPlayer getNextNextPlayer() {
        if (turnEngine <= 0) {
            turnEngine = turnEngine + numPlayers;
        }
        var nextNextPlayer = (turnEngine + turnDirection + 1) % numPlayers;
        return players.get(nextNextPlayer);
    }

    public IPlayerInfo getWinningPlayer() {
        return winningPlayer;
    }
}

