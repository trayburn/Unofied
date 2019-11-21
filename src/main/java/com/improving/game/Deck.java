package com.improving.game;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.List;


@Component
public class Deck implements IDeck {
    private List<Card> drawPile = new ArrayList<>();
    private List<Card> discardPile = new ArrayList<>();

    public Deck() {
        Set<Colors> numColorSet = new HashSet<>();
        numColorSet.add(Colors.Blue);
        numColorSet.add(Colors.Green);
        numColorSet.add(Colors.Yellow);
        numColorSet.add(Colors.Red);
        Set<Faces> numFaceSet = new HashSet<>();
        numFaceSet.add(Faces.Zero);
        numFaceSet.add(Faces.One);
        numFaceSet.add(Faces.Two);
        numFaceSet.add(Faces.Three);
        numFaceSet.add(Faces.Four);
        numFaceSet.add(Faces.Five);
        numFaceSet.add(Faces.Six);
        numFaceSet.add(Faces.Seven);
        numFaceSet.add(Faces.Eight);
        numFaceSet.add(Faces.Nine);
        Set<Faces> actionFaceSet = new HashSet<>();
        actionFaceSet.add(Faces.Reverse);
        actionFaceSet.add(Faces.Skip);
        actionFaceSet.add(Faces.Draw_2);
        Set<Faces> wildFaceSet = new HashSet<>();
        wildFaceSet.add(Faces.Wild);
        wildFaceSet.add(Faces.Draw_4);
        Set<Colors> wildColorSet = new HashSet<>();
        wildColorSet.add(Colors.Wild);

        for (var color : numColorSet) {
            for (var face : numFaceSet) {
                drawPile.add(new Card(face, color));
                drawPile.add(new Card(face, color));
            }
        }

        for (var color : numColorSet) {
            for (var action : actionFaceSet) {
                drawPile.add(new Card(action, color));
                drawPile.add(new Card(action, color));
            }
        }

        for (var wildColor : wildColorSet) {
            for (var wildFace : wildFaceSet) {
                drawPile.add(new Card(wildFace, wildColor));
                drawPile.add(new Card(wildFace, wildColor));
                drawPile.add(new Card(wildFace, wildColor));
                drawPile.add(new Card(wildFace, wildColor));
            }
        }
    }

    public int getDrawPileSize() {
        return drawPile.size();
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }

    public List<Card> getDrawPile() {
        return drawPile;
    }

    public Card draw() {
        if (drawPile.size() == 0) {
            var topCard = discardPile.get(discardPile.size() - 1);
            drawPile.addAll(discardPile);
            drawPile.remove(topCard);
            discardPile.clear();
            discardPile.add(topCard);
        }
        shuffle();
        var newCard = drawPile.get(0);
        drawPile.remove(newCard);
        return newCard;

    }

    public void shuffle() {
        Collections.shuffle(drawPile);
    }
}
