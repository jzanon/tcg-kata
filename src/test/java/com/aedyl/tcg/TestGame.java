package com.aedyl.tcg;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class TestGame {


    @Test
    public void start_game_with_random_player() {
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        Game game = new Game(player1, player2);

        game.initFirstPlayer();

        Player firstPlayer = game.getActivePlayer();
        assertThat(firstPlayer)
                .isIn(player1, player2);

        Player notActivePlayer = game.getNotActivePlayer();
        assertThat(notActivePlayer.getHand())
                .hasSize(4);

        assertThat(firstPlayer).isNotEqualTo(notActivePlayer);

    }


    @Test
    @DisplayName("Played cards deal immediate damage to the opponent player equal to their Mana cost")
    public void deal_damage_to_other_player() {
        Player player1 = new Player("Player 1");
        player1.setManaSlot(new ManaSlot(20, 20));
        Player player2 = new Player("Player 2");
        player2.setManaSlot(new ManaSlot(20, 20));
        Game game = new Game(player1, player2);

        player1.setActive(true);
        player1.drawInitialHand();
        player2.drawInitialHand();
        Player activePlayer = game.getActivePlayer();
        Player notActivePlayer = game.getNotActivePlayer();

        assertEquals(20, activePlayer.getHealth());
        assertEquals(20, notActivePlayer.getHealth());

        game.play(activePlayer);


        assertEquals(20, activePlayer.getHealth());

        int manaUsedByActivePlayer = activePlayer.getManaSlots().max - activePlayer.getManaSlots().current;

        assertThat(notActivePlayer.getHealth())
                .isLessThan(20)
                .isEqualTo(20 - manaUsedByActivePlayer);

    }

    @Test
    @DisplayName("If the opponent player's Health drops to or below zero the active player wins the game.")
    public void player_loose_game_if_no_health() {
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        Game game = new Game(player1, player2);

        player1.dealDamages(19);

        boolean hasWin = game.isWinner(player1);

        assertFalse(hasWin);

        player1.dealDamages(2);
        hasWin = game.isWinner(player2);

        assertTrue(hasWin);

    }

    @Test
    public void switch_active_player() {
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        Game game = new Game(player1, player2);
        game.initFirstPlayer();

        Player activePlayerBeforeSwitch = game.getActivePlayer();
        Player notActivePlayerBeforeSwitch = game.getNotActivePlayer();

        assertNotEquals(activePlayerBeforeSwitch, notActivePlayerBeforeSwitch);

        game.switchActivePlayer();

        Player activePlayerAfterSwitch = game.getActivePlayer();
        Player notActivePlayerAfterSwitch = game.getNotActivePlayer();

        assertEquals(activePlayerBeforeSwitch, notActivePlayerAfterSwitch);
        assertEquals(notActivePlayerBeforeSwitch, activePlayerAfterSwitch);


    }


}
