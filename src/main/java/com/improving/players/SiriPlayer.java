package com.improving.players;

import com.improving.game.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

//@Component
public class SiriPlayer implements IPlayer {
        List<Card> myHand;
        int id;
        Colors mostCommonColor;
        String name = "Siri";

        public SiriPlayer(List<Card> myHand) {
            this.myHand = myHand;
        }


        @Override
        public Card draw(IGame game) {
            var temp = game.draw();
            myHand.add(temp);
            return temp;
        }

        @Override
        public int handSize() {
            return myHand.size();
        }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
        public void takeTurn(IGame game) {
            if(!this.myHand.isEmpty()) {
                int playBasedOnPlayer = playBasedOnPlayers(game.getPreviousPlayer().handSize(),game.getNextPlayer().handSize(), game.getNextNextPlayer().handSize());


                if(game.isPlayable(findBestSuggestedCard())) {
                    var card = findBestSuggestedCard();
                    //     System.out.println("Playing my best card!");
                    myHand.remove(card);
                    if (card.getColor() == Colors.Wild) {
                        game.playCard(card, Optional.of(Colors.Wild), this);
                        return;
                    } else {
                        game.playCard(card, Optional.ofNullable(null), this);
                        return;
                    }
                }
                if(playBasedOnPlayer == 1) {
                    for(Card e: myHand) {
                        if((e.getFace() == Faces.Reverse) || (e.getFace() == Faces.Draw_4) || (e.getFace() == Faces.Draw_2)) {
                            if(game.isPlayable(e)) {

                                if(e.getColor() == Colors.Wild) {
                                    myHand.remove(e);
                                    game.playCard(e, Optional.of(getMostCommonColor()), this);
                                    return;
                                } else {
                                    myHand.remove(e);
                                    game.playCard(e, Optional.ofNullable(null), this);
                                    return;
                                }
                            }
                        }
                    }
                }
                for(Card e: myHand) {
                    if(game.isPlayable(e)) {
                        myHand.remove(e);
                        if (e.getColor() == Colors.Wild) {
                            game.playCard(e, Optional.of(getMostCommonColor()), this);
                            return;
                        } else {
                            game.playCard(e, Optional.ofNullable(null), this);
                            return;
                        }
                    }
                }
                draw(game);
                //     System.out.println("" + name() + " Drawing!");
            }
            return;
        }

        @Override
        public void newHand(List<Card> cards) {
            this.myHand.clear();
            this.myHand.addAll(cards);
        }

    @Override
    public List<Card> getHand() {
        return myHand;
    }

    public int playBasedOnPlayers(int previous, int next, int nextnext) {
            if(next <=2) {
                //      System.out.println("Watch out, next player is almost going to win!");
                return 1;
            }
            return 0;
        }

        public void setMostCommonColor(Colors mostCommonColor) {
            this.mostCommonColor = mostCommonColor;
        }

        public Colors getMostCommonColor() {
            return mostCommonColor;
        }

        public Card findBestSuggestedCard() {

            mostCommonColor = myHand.stream()
                    .map(Card::getColor).filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet().stream().max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey).orElse(null);
            setMostCommonColor(mostCommonColor);

            Faces mostCommonFace = myHand.stream()
                    .map(Card::getFace).filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet().stream().max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey).orElse(null);

            int highestFaceCount = (int) myHand.stream().parallel()
                    .filter(f -> (f.getFace() == mostCommonFace)).count();
            int highestColorCount = (int) myHand.stream().parallel()
                    .filter(f -> (f.getColor() == mostCommonColor)).count();

            if(highestFaceCount < highestColorCount)
                return myHand.stream().parallel()
                        .filter(f -> (f.getColor() == mostCommonColor)).findFirst().get();
            else if(highestFaceCount > highestColorCount) {
                return myHand.stream().parallel()
                        .filter(f -> (f.getFace() == mostCommonFace)).findFirst().get();
            }
            else return myHand.stream().findFirst().get();
        }

        public void showMyCards() {
            System.out.println(myHand);
        }

        public Colors pickAColor() {
            Random rand = new Random();
            int randNum = rand.nextInt();
            if(randNum == 0) {
                return Colors.Blue;
            }
            if(randNum == 1) {
                return Colors.Red;
            }
            if(randNum == 2) {
                return Colors.Green;
            }if(randNum == 3) {
                return Colors.Yellow;
            }
            else return null;
        }
    }

