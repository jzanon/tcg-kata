package com.aedyl.tcg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

    private final List<Player> players = new ArrayList<>();


    public Game(Player player1, Player player2) {
        players.add(player1);
        players.add(player2);
    }


    private Player chooseRandomPlayer() {
        Random random = new Random();
        return players.get(random.nextInt(2));
    }

    public Player getActivePlayer() {
        return players.stream().filter(Player::isActive).findFirst().orElse(null);
    }

    public Player getNotActivePlayer() {
        return players.stream().filter(player -> !player.isActive()).findFirst().orElse(null);
    }

    public void run() {
        initFirstPlayer();
        switchActivePlayer();
        boolean gameFinished = false;
        while (!gameFinished) {
            Player activePlayer = getActivePlayer();
            LOGGER.info("{} is active player", activePlayer.getName());
            beginTurn(activePlayer);
            LOGGER.info("{} hand is {} with {}", activePlayer.getName(), activePlayer.getHand(), activePlayer.getManaSlots());
            play(activePlayer);
            if (isWinner(activePlayer)) {
                gameFinished = true;
                LOGGER.info("{} has win the game", activePlayer.getName());
            } else {
                switchActivePlayer();
            }
        }
    }

    public void initFirstPlayer() {
        Player activePlayer = chooseRandomPlayer();
        activePlayer.setActive(true);
        LOGGER.info("{} will initFirstPlayer the game", activePlayer.getName());
        players.forEach(Player::drawInitialHand);

        activePlayer.increaseManaSlot();
        activePlayer.refillManaSlot();
        play(activePlayer);
    }

    private void beginTurn(Player activePlayer) {
        activePlayer.increaseManaSlot();
        activePlayer.refillManaSlot();
        activePlayer.drawCard();
    }

    public void switchActivePlayer() {
        Player notActivePlayer = getNotActivePlayer();
        Player activePlayer = getActivePlayer();
        notActivePlayer.setActive(true);
        activePlayer.setActive(false);

    }


    public void play(Player activePlayer) {
        List<Integer> playedCards = activePlayer.playCards();
        int damages = playedCards.stream().reduce(0, Integer::sum);
        Player notActivePlayer = getNotActivePlayer();
        notActivePlayer.dealDamages(damages);
        LOGGER.info("{} played: {}. Damages: {}, {} health: {}", activePlayer.getName(), playedCards, damages, notActivePlayer.getName(), notActivePlayer.getHealth());
    }

    public boolean isWinner(Player player) {
        boolean otherPlayersAreDead = players.stream()
                .filter(p -> !p.getName().equals(player.getName()))
                .noneMatch(Player::isAlive);
        return player.isAlive() && otherPlayersAreDead;
    }
}
