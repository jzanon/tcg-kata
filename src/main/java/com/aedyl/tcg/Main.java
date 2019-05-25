package com.aedyl.tcg;

public class Main {

    public static void main(String... args) {
        Game game = new Game(new Player("Human"), new Player("CPU"));
        game.run();
    }
}
