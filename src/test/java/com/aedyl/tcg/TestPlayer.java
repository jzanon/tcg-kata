package com.aedyl.tcg;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPlayer {


    @Test
    public void player_should_start_with_name() {
        Player player = new Player("Player 1");
        assertEquals("Player 1", player.getName(), "Player name initialized at creation");
    }

    @Test
    public void player_should_start_with_health() {
        Player player = new Player("Player 1");
        assertEquals(20, player.getHealth(), "Player starts with 30 Health");
    }

    @Test
    public void player_should_start_without_manaslots() {
        Player player = new Player("Player 1");
        assertEquals(new ManaSlot(0, 0), player.getManaSlots(), "Player starts with 30 Mana slots");
    }

    @Test
    public void player_should_start_with_specific_list_of_cards() {
        Player player = new Player("Player 1");
        assertThat(player.getDeck())
                .hasSize(20)
                .containsExactlyInAnyOrder(0, 0, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 5, 5, 6, 6, 7, 8);
    }

    @Test
    public void player_first_hand_contains_3_random_cards_from_deck() {
        Player player = new Player("Player 1");
        player.setActive(true);

        assertThat(player.getHand())
                .hasSize(0);

        player.drawInitialHand();

        assertThat(player.getHand())
                .hasSize(3)
                .isSubsetOf(0, 0, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 5, 5, 6, 6, 7, 8);
    }

    @Test
    public void opponent_player_draw_1_cards_in_addition() {
        Player player = new Player("Player 1");
        player.setActive(false);

        assertThat(player.getHand())
                .hasSize(0);


        player.drawInitialHand();

        assertThat(player.getHand())
                .hasSize(4)
                .isSubsetOf(0, 0, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 5, 5, 6, 6, 7, 8);
    }


    @Test
    public void increase_mana_slot_max_10() {
        Player player = new Player("Player 1");
        for (int nbOfIncrement = 1; nbOfIncrement < 12; nbOfIncrement++) {
            player.increaseManaSlot();
            if (nbOfIncrement <= 10) {
                assertEquals(nbOfIncrement, player.getManaSlots().max);
            } else {
                assertEquals(10, player.getManaSlots().max);
            }
        }
    }

    @Test
    @DisplayName("The active player draws a random card from his deck.")
    public void draw_card() {
        Player player1 = new Player("Player 1");
        player1.setActive(true);
        player1.drawInitialHand();

        assertEquals(3, player1.getHand().size());

        player1.drawCard();

        assertEquals(4, player1.getHand().size());
    }

    @Test
    @DisplayName("A player drawing card in empty deck takes 1 damage")
    public void draw_card_in_empty_deck_deals_damage() {
        Player player1 = new Player("Player 1");
        while (player1.getDeck().size() > 0) {
            player1.drawCard();
        }
        assertEquals(20, player1.getHealth());
        player1.drawCard();
        assertEquals(19, player1.getHealth());
    }

    @Test
    public void mana_slot_is_refilled() {
        Player player1 = new Player("Player 1");

        for (int i = 0; i < 6; i++) {
            player1.increaseManaSlot();
        }

        assertEquals(new ManaSlot(0, 6), player1.getManaSlots());

        player1.refillManaSlot();

        assertEquals(new ManaSlot(6, 6), player1.getManaSlots());

    }

    @Test
    @DisplayName("The active player receives 1 Mana slot up to a maximum of 10 total slots")
    public void mana_is_slotted_up_on_game_loop() {
        Player player1 = new Player("Player 1");

        assertEquals(new ManaSlot(0, 0), player1.getManaSlots());

        player1.increaseManaSlot();

        assertEquals(new ManaSlot(0, 1), player1.getManaSlots());

    }

    @Test
    public void play_card() {
        Player player = new Player("Player 1");
        List<Integer> initialHand = Arrays.asList(1, 2, 3, 4, 5);
        player.setHand(new ArrayList<>(initialHand));
        player.setManaSlot(new ManaSlot(4, 4));

        List<Integer> playedCards = player.playCards();

        assertThat(playedCards)
                .isSubsetOf(initialHand);

        Integer consumedMana = playedCards.stream().reduce(0, Integer::sum);
        assertThat(consumedMana)
                .isLessThanOrEqualTo(4)
                .isEqualTo(player.getManaSlots().max - player.getManaSlots().current);
    }

}
