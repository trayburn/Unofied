package com.improving;

import com.improving.game.Game;
import com.improving.players.ZachPlayer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Random;

public class Application {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        var game = context.getBean(Game.class);
        var wins = new HashMap<String, Integer>();
        var random = new Random();

        while(game.getPlayers().size() > 4) {
            var rIndex = random.nextInt(game.getPlayers().size());
            if (!game.getPlayers().get(rIndex).getClass().equals(ZachPlayer.class))
                game.getPlayers().remove(rIndex);
        }

        for(var p : game.getPlayerInfo()) {
            wins.put(p.getName(), 0);
        }

        for (int i = 0; i < 10000; i++) {
            try {
                game.play();
            } catch (Exception e) {
                for (var p : game.getPlayers()) {
                    System.out.println(p.getName());
                    e.printStackTrace();
                    return;
                }
            }
            var name = game.getWinningPlayer().getName();
            wins.put(name, wins.get(name) + 1);
        }

        for (var key : wins.keySet()) {
            System.out.println(key + "\t\t\t" + wins.get(key));
        }
    }
}
