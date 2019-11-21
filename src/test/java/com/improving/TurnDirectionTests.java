package com.improving;


import com.improving.game.Card;
import com.improving.game.Game;
import com.improving.game.IPlayer;
import com.improving.players.DumbPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TurnDirectionTests {
    private Logger logger;
    private List<IPlayer> players;
    private IPlayer p1;
    private IPlayer p2;
    private IPlayer p3;
    private IPlayer p4;
    private Game game;

    @BeforeEach
    public void Setup() {
        logger = new Logger();
        players = new ArrayList<IPlayer>();
        p1 = new DumbPlayer(new ArrayList<Card>());
        p2 = new DumbPlayer(new ArrayList<Card>());
        p3 = new DumbPlayer(new ArrayList<Card>());
        p4 = new DumbPlayer(new ArrayList<Card>());
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        game = new Game(logger, players);
    }

    @Test
    public void getNextPlayer_Turn0_Direction1() {
        // Arrange
        // Act
        var player = game.getNextPlayer();

        // Assert
        assertEquals(p2, player);
    }

    @Test
    public void getNextNextPlayer_Turn0_Direction1() {
        // Arrange
        // Act
        var player = game.getNextNextPlayer();

        // Assert
        assertEquals(p3, player);
    }

    @Test
    public void getPreviousPlayer_Turn0_Direction1() {
        // Arrange
        // Act
        var player = game.getPreviousPlayer();

        // Assert
        assertEquals(p4, player);
    }
    @Test
    public void getNextPlayer_Turn0_DirectionNeg1() {
        // Arrange
        game.setTurnDirection(-1);

        // Act
        var player = game.getNextPlayer();

        // Assert
        assertEquals(p4, player);
    }

    @Test
    public void getNextNextPlayer_Turn0_DirectionNeg1() {
        // Arrange
        game.setTurnDirection(-1);

        // Act
        var player = game.getNextNextPlayer();

        // Assert
        assertEquals(p3, player);
    }

    @Test
    public void getPreviousPlayer_Turn0_DirectionNeg1() {
        // Arrange
        game.setTurnDirection(-1);

        // Act
        var player = game.getPreviousPlayer();

        // Assert
        assertEquals(p2, player);
    }
}
