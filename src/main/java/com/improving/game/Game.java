package com.improving.game;

import com.improving.Logger;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Game implements IGame {
    private Deck deck;
    private final List<IPlayer> players = new ArrayList<>();
    private TopCard topCard;
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
        turnEngine = 0;
        turnDirection = 1;
        winningPlayer = null;
        gameInProgress = true;
        deck = new Deck();
        arrangeStartingDeck(deck);

        for (var p : players) {
            p.newHand(getStartingHand(deck));
        }

        logger.println("Top Card on Discard Pile: " + topCard.toString());

        if (hasAction(topCard.getCard())) {
            executeCardAction(topCard.getCard(), this, 0);
        }

        while (gameInProgress) {
            var player = players.get(playerIndex(0));
            logger.println("---- " + player.getName());

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
        deck.shuffle();
        Card firstCard = deck.draw();
        this.topCard = new TopCard(logger, firstCard, firstCard.getColor());
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

    @Override
    public boolean isPlayable(Card playerCard) {
        if (playerCard.getColor() == Colors.Wild)
            return true;
        if (topCard.getDeclaredColor() != null) {
            return playerCard.getColor().equals(topCard.getDeclaredColor());
        }
        if (topCard.getCard().getColor().equals(playerCard.getColor())
                || topCard.getCard().getFace().equals(playerCard.getFace())) {
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

    @Override
    public void playCard(Card card, Optional<Colors> declaredColor, IPlayerInfo player) {
        if (card == null)
            throw new RuntimeException(player.getName() + " NULL card was played");
        if (card == topCard.getCard())
            throw new RuntimeException(player.getName() + " WTF!  The same card?!");
        if (isPlayable(card) == false)
            throw new RuntimeException(player.getName() + " This card is not playable!");
        if ((card.getColor().equals(Colors.Wild) == false) && declaredColor.isPresent()) {
            logger.println(card.toString() + " // DECLARING " + declaredColor.get());
            throw new RuntimeException(player.getName() + " Declared a color without playing a Wild card!");
        }
        if (((IPlayer)player).getHand().contains(card))
            throw new RuntimeException(player.getName() + " Card is still in hand!");
        if (deck.getAllCards().contains(card) == false)
            throw new RuntimeException("This card was not originally in the deck!");

        if (getSumOfCards() != 111)
            throw new RuntimeException(getSumOfCards() + " A card has disappeared from the game.");

        deck.getDiscardPile().add(card);
        topCard.setCard(card, declaredColor.orElse(null), player);


        if (getSumOfCards() != 112)
            throw new RuntimeException(getSumOfCards() + " A card has disappeared from the game.");

        if (hasAction(card)) {
            executeCardAction(card, this, playerIndex(1));
        }

        if (getSumOfCards() != 112)
            throw new RuntimeException(getSumOfCards() + " A card has disappeared from the game.");

    }

    private int getSumOfCards() {
        return (deck.getDrawPile().size() + deck.getDiscardPile().size() + players.stream().mapToInt(IPlayerInfo::handSize).sum());
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

    @Override
    public IDeck getDeckInfo() {
        return this.deck;
    }

    ;


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

    public int playerIndex(int shift) {
        var index = (turnEngine + (shift * turnDirection)) % players.size();
        if (index < 0) index += players.size();
        return index;
    }

    @Override
    public IPlayer getNextPlayer() {
        return players.get(playerIndex(1));
    }

    @Override
    public IPlayer getPreviousPlayer() {
        return players.get(playerIndex(-1));
    }

    @Override
    public IPlayer getNextNextPlayer() {
        return players.get(playerIndex(2));
    }

    private int getNextPlayerIndex() {
        if (turnEngine <= 0) {
            turnEngine = turnEngine + players.size();
        }
        return (turnEngine + turnDirection) % players.size();
    }

    public IPlayerInfo getWinningPlayer() {
        return winningPlayer;
    }

    public void setTurnDirection(int direction) {
        turnDirection = direction;
    }
}

