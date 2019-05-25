package com.aedyl.tcg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Player {
    private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);

    private final String name;
    private int health;
    private ManaSlot manaSlot;
    private Deque<Integer> deck;
    private ArrayList<Integer> hand;
    private boolean isActive;

    public Player(String name) {
        this.name = name;
        this.health = 20;
        this.manaSlot = new ManaSlot(0, 0);

        List<Integer> initialDeck = Arrays.asList(0, 0, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 5, 5, 6, 6, 7, 8);
        Collections.shuffle(initialDeck);
        this.deck = new ArrayDeque<>(initialDeck);

        this.hand = new ArrayList<>();
    }

    public Integer drawCard() {
        if (!deck.isEmpty()) {
            Integer drawnCard = deck.pop();
            hand.add(drawnCard);
            return drawnCard;
        } else {
            LOGGER.info("{} : deck is empty, 1 damage done", name);
            dealDamages(1);
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }


    public ManaSlot getManaSlots() {
        return manaSlot.copy();
    }


    public Deque<Integer> getDeck() {
        return deck;
    }


    public List<Integer> getHand() {
        return hand;
    }

    public void drawInitialHand() {
        int nbOfCardToDraw = 3;
        if (!isActive) {
            nbOfCardToDraw++;
        }
        for (int i = 0; i < nbOfCardToDraw; i++) {
            Integer drawCard = drawCard();
            LOGGER.info("{} draws : {}", name, drawCard);
        }
        LOGGER.info("{} hand : {}", name, hand);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", health=" + health +
                ", " + manaSlot +
                '}';
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public void increaseManaSlot() {
        this.manaSlot.increaseMax();
    }

    public void refillManaSlot() {
        this.manaSlot.refill();
    }

    public void dealDamages(int damages) {
        health -= damages;
    }

    public List<Integer> playCards() {

        List<Integer> selectedCards = new ArrayList<>();
        if (hand.isEmpty()) {
            LOGGER.info("{} does not have card in hand", name);
            return selectedCards;
        }

        hand.stream()
                .filter(card -> selectedCards.stream().reduce(0, Integer::sum) + card <= manaSlot.current)
                .forEach(selectedCards::add);
        manaSlot.current -= selectedCards.stream().reduce(0, Integer::sum);

        for (Integer card : selectedCards) {
            hand.remove(card);
        }

        if (selectedCards.isEmpty()) {
            LOGGER.info("{} does not have enough mana to play a card: {}", name, manaSlot);
        }

        return selectedCards;
    }

    public void setHand(ArrayList<Integer> hand) {
        this.hand = hand;
    }

    public void setManaSlot(ManaSlot manaSlot) {
        this.manaSlot = manaSlot;
    }

    public boolean isAlive() {
        return getHealth() > 0;
    }
}
