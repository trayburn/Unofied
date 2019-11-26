package com.improving.players;

import com.improving.Logger;
import com.improving.game.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EthanPlayerTest {

    @Test
    void getRealColors_should_return_all_colors_except_wild() {
        // Arrange
        var hand = new ArrayList<>(Arrays.asList(
                new Card(Faces.Seven, Colors.Blue)
        ));
        var Ethan = new EthanPlayer(hand);

        // Act
        var result = Ethan.getRealColors();
        var expected = new ArrayList<>(Arrays.asList(
                Colors.Red,
                Colors.Green,
                Colors.Blue,
                Colors.Yellow
        ));

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void getMostCommonPlayableColor_should_return_color_with_most_cards_in_hand() {
        // Arrange

        var hand = new ArrayList<>(Arrays.asList(
                new Card(Faces.Seven, Colors.Blue),
                new Card(Faces.Five, Colors.Blue),
                new Card(Faces.Eight, Colors.Yellow),
                new Card(Faces.Seven, Colors.Green)
        ));
        var Ethan = new EthanPlayer(hand);
        var players = new ArrayList<IPlayer>(Arrays.asList(Ethan));
        var game = new Game(new Logger(), players);

        // Act
        var result = Ethan.getMostCommonColor();

        // Assert
        assertEquals(Colors.Blue, result);
    }

    @Test
    void sortHandByPointValue_should_sort_highest_value_cards_last() {
        // Arrange

        var hand = new ArrayList<>(Arrays.asList(
                new Card(Faces.Wild, Colors.Wild),
                new Card(Faces.Five, Colors.Blue),
                new Card(Faces.Draw_2, Colors.Yellow),
                new Card(Faces.Seven, Colors.Green)
        ));
        var Ethan = new EthanPlayer(hand);
        var players = new ArrayList<IPlayer>(Arrays.asList(Ethan));

        // Act
        Ethan.sortHandByPointValue();
        var result = Ethan.getHand();
        var expected = new ArrayList<>(Arrays.asList(
                new Card(Faces.Five, Colors.Blue),
                new Card(Faces.Seven, Colors.Green),
                new Card(Faces.Draw_2, Colors.Yellow),
                new Card(Faces.Wild, Colors.Wild)
        ));

        // Assert
        assertEquals(expected.toString(), result.toString());
    }
}