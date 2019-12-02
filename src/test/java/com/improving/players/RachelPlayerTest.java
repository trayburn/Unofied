package com.improving.players;

import com.improving.game.Card;
import com.improving.game.Colors;
import com.improving.game.Faces;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RachelPlayerTest {

    private RachelPlayer rPlayer = new RachelPlayer();

    @Test
    public void optimal_play_order_returns_corect_number_of_posible_play_orders(){
        //arrange
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card(Faces.Wild, Colors.Wild));
        hand.add(new Card(Faces.Two, Colors.Blue));
        hand.add(new Card(Faces.Nine, Colors.Red));
        //act
        HashMap<List<Card>, Long> optimalPlayOrder = rPlayer.findOptimalPlayCardOrder(hand);
        //assert
        assertEquals(6,optimalPlayOrder.size());
    }

    @Test
    public void play_orders_have_correct_depth(){
        //arrange
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card(Faces.Wild, Colors.Wild));
        hand.add(new Card(Faces.Two, Colors.Blue));
        hand.add(new Card(Faces.Nine, Colors.Red));
        ArrayList<Long> depths = new ArrayList<>();
        //act
        HashMap<List<Card>, Long> optimalPlayOrder = rPlayer.findOptimalPlayCardOrder(hand);
        Optional<Map.Entry<List<Card>, Long>> order = optimalPlayOrder.entrySet().stream().findFirst();
        for (List<Card> optimalPermutation: optimalPlayOrder.keySet()){
            for(Card card : optimalPermutation){
                System.out.println(card.toString());
            }
            depths.add(optimalPlayOrder.get(optimalPermutation));
            System.out.println(optimalPlayOrder.get(optimalPermutation));
        }
        //assert
        assertEquals(Long.valueOf(1),depths.get(0));
        assertEquals(Long.valueOf(1),depths.get(1));
        assertEquals(Long.valueOf(2),depths.get(2));
        assertEquals(Long.valueOf(2),depths.get(3));
        assertEquals(Long.valueOf(3),depths.get(4));
        assertEquals(Long.valueOf(3),depths.get(5));
    }


    @Test
    public void play_orders_can_rank_hands_with_Matching_Numbers_Correctly(){
        //arrange
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card(Faces.Nine, Colors.Green));
        hand.add(new Card(Faces.Nine, Colors.Blue));
        hand.add(new Card(Faces.Nine, Colors.Red));
        hand.add(new Card (Faces.Nine, Colors.Yellow));
        ArrayList<Long> depths = new ArrayList<>();
        //act
        HashMap<List<Card>, Long> optimalPlayOrder = rPlayer.findOptimalPlayCardOrder(hand);
        Optional<Map.Entry<List<Card>, Long>> order = optimalPlayOrder.entrySet().stream().findFirst();
        for (List<Card> optimalPermutation: optimalPlayOrder.keySet()){
            for(Card card : optimalPermutation){
                System.out.println(card.toString());
            }
            depths.add(optimalPlayOrder.get(optimalPermutation));
            System.out.println(optimalPlayOrder.get(optimalPermutation));
        }
        //assert
        assertEquals(24,optimalPlayOrder.size());
        assertEquals(Long.valueOf(4),depths.get(0));
        assertEquals(Long.valueOf(4),depths.get(2));
        assertEquals(Long.valueOf(4),depths.get(6));
        assertEquals(Long.valueOf(4),depths.get(10));
        assertEquals(Long.valueOf(4),depths.get(17));
        assertEquals(Long.valueOf(4),depths.get(20));
    }

    @Test
    public void play_orders_can_rank_hands_with_Matching_Colors_Correctly(){
        //arrange
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card(Faces.Nine, Colors.Green));
        hand.add(new Card(Faces.Three, Colors.Green));
        hand.add(new Card(Faces.Skip, Colors.Green));
        ArrayList<Long> depths = new ArrayList<>();
        //act
        HashMap<List<Card>, Long> optimalPlayOrder = rPlayer.findOptimalPlayCardOrder(hand);
        Optional<Map.Entry<List<Card>, Long>> order = optimalPlayOrder.entrySet().stream().findFirst();
        for (List<Card> optimalPermutation: optimalPlayOrder.keySet()){
            for(Card card : optimalPermutation){
                System.out.println(card.toString());
            }
            depths.add(optimalPlayOrder.get(optimalPermutation));
            System.out.println(optimalPlayOrder.get(optimalPermutation));
        }
        //assert
        assertEquals(6,optimalPlayOrder.size());
        assertEquals(Long.valueOf(3),depths.get(0));
        assertEquals(Long.valueOf(3),depths.get(2));
        assertEquals(Long.valueOf(3),depths.get(5));
    }


    @Test
    public void play_orders_can_find_both_ideal_orders(){
        //arrange
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card(Faces.Nine, Colors.Green));
        hand.add(new Card(Faces.Three, Colors.Green));
        hand.add(new Card(Faces.Three, Colors.Red));
        hand.add(new Card(Faces.Two, Colors.Red));
        hand.add(new Card(Faces.Two,Colors.Yellow));
        ArrayList<Long> depths = new ArrayList<>();
        //act
        HashMap<List<Card>, Long> optimalPlayOrder = rPlayer.findOptimalPlayCardOrder(hand);
        Optional<Map.Entry<List<Card>, Long>> order = optimalPlayOrder.entrySet().stream().findFirst();
        for (List<Card> optimalPermutation: optimalPlayOrder.keySet()){
            for(Card card : optimalPermutation){
                System.out.println(card.toString());
            }
            depths.add(optimalPlayOrder.get(optimalPermutation));
            System.out.println(optimalPlayOrder.get(optimalPermutation));
        }
        //assert
          assertEquals(120,optimalPlayOrder.size());
        assertEquals(Long.valueOf(1),depths.get(0));
        assertEquals(Long.valueOf(2),depths.get(100));
        assertEquals(Long.valueOf(3),depths.get(115));
        assertEquals(Long.valueOf(4),depths.get(117));
        assertEquals(Long.valueOf(5),depths.get(119));
    }

    @Test
    public void factorialTest (){

        RachelPlayer player = new RachelPlayer();
        assertEquals(Long.valueOf(1), player.factorial(1));
        assertEquals(Long.valueOf(2), player.factorial(2));
        assertEquals(Long.valueOf(6), player.factorial(3));
        assertEquals(Long.valueOf(24), player.factorial(4));
        assertEquals(Long.valueOf(120), player.factorial(5));
        assertEquals(Long.valueOf(720), player.factorial(6));
        assertEquals(Long.valueOf(5040), player.factorial(7));
        assertEquals(Long.valueOf(40320), player.factorial(8));
    }

}
